package br.ufrn.monitortemperatura.restserver;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import br.ufrn.monitortemperatura.model.Mensagem;
import br.ufrn.monitortemperatura.model.Request;

@Path("oslo")
public class OsloRestService {
	
	private static final String API_KEY = "5f2cabd73aed334a5d71ef400639bb8f";
	private static final String LAT = "59.9138";
	private static final String LON = "10.7387";
	private static final String NAME = "Oslo";
	
	private volatile ArrayList<Request> requests = new ArrayList<Request>();

	public OsloRestService() {
		new MonitorTemperatura().start();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultarTemperatura() {
		
		Double temp = null;
		JsonNode jsonNode = null;
		
		try {
			
			ObjectMapper objectMapper = new ObjectMapper();
			URL url = new URL("https://api.openweathermap.org/data/2.5/weather?lat="+LAT+"&lon="+LON+"&appid=" + API_KEY + "&units=metric");
			URLConnection connection = url.openConnection();
			InputStream inputStream = connection.getInputStream();
			jsonNode = objectMapper.readTree(inputStream);
			temp = jsonNode.get("main").get("temp").asDouble();
			
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		Mensagem msg = new Mensagem((int) Math.round(temp), NAME);


		return Response.ok(msg).build();
	}
	
	
	@GET
	@Path("requests/{request}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response verificarRequest(@PathParam("request") int request) {
		if(request < 0 || requests.get(request) == null) {
			return Response.serverError().build();
		}

		return Response.ok(requests.get(request)).build();
	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response monitorarTemperatura(Request request) {
		
		requests.add(request);

		return Response.ok(requests.indexOf(request)).build();
	}
	
	
private class MonitorTemperatura extends Thread{
		
		public void run() {
			
			for(;;) {
								
				if(requests.size() > 0) {
					
					
					Integer temp = null;
					
					try {
						
						ObjectMapper objectMapper = new ObjectMapper();
						URL url = new URL("https://api.openweathermap.org/data/2.5/weather?lat="+LAT+"&lon="+LON+"&appid=" + API_KEY + "&units=metric");
						URLConnection connection = url.openConnection();
						InputStream inputStream = connection.getInputStream();
						JsonNode jsonNode = objectMapper.readTree(inputStream);
						temp = (int) Math.round(jsonNode.get("main").get("temp").asDouble());
						
					} catch(IOException e) {
						e.printStackTrace();
					}
					
					if(temp != null) {
						for(Request r : requests) {
							switch(r.getOperacao()) {
							case IGUAL:
								if(r.getX() == temp) {
									r.setSatisfeita(true);
								} else {
									r.setSatisfeita(false);
								}
								break;
							case MAIOR:
								if(r.getX() < temp) {
									r.setSatisfeita(true);
								} else {
									r.setSatisfeita(false);
								}
								break;
							case MENOR:
								if(r.getX() > temp) {
									r.setSatisfeita(true);
								} else {
									r.setSatisfeita(false);
								}
								break;
							case INTERVALO:
								int maiorTemp = Math.max(r.getX(), r.getY());
								int menorTemp = Math.min(r.getX(), r.getY());
								if(maiorTemp > temp && menorTemp < temp) {
									r.setSatisfeita(true);
								} else {
									r.setSatisfeita(false);
								}
								break;
							}
						}
					}
					
					
					try {
						Thread.sleep(15 * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
			
		}
	}
	
	
	
}
