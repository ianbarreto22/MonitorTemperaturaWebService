package br.imd.rest;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.imd.rest.expections.RestRequestException;

public class VerificadorRequest extends Thread {
	private String cidade;
	private Integer request;
	
	public VerificadorRequest(String cidade, Integer request) {
		this.cidade = cidade;
		this.request = request;
	}
	
	public Request verificarRequest(String local, int req) throws RestRequestException {
		String uri = "http://localhost:8080/RestServer/monitortemperatura/" + local + "/requests/" + req;
		Map<String, String> headerParams = new HashMap<String, String>();

		headerParams.put("accept", "application/json");
		
		String response = HttpUtils.httpGetRequest(uri, headerParams);

		ObjectMapper objectMapper = new ObjectMapper();
		Request r = null;
		try {
			r = objectMapper.readValue(response, Request.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return r;

	}

	public void run() {
		for (;;) {
			
			try {
				Request req = this.verificarRequest(cidade, request);
				if(req.getSatisfeita()) {
					String msg = "A temperatura em " + this.cidade;
					switch(req.getOperacao()) {
					case IGUAL:
						msg += " está igual a " + req.getX();
						break;
					case MAIOR:
						msg += " está maior que " + req.getX();
						break;
					case MENOR:
						msg += " está menor que " + req.getX();
						break;
					case INTERVALO:
						msg += " está entre " + req.getX() + " e " + req.getY();
						
						break;
					}
					System.out.println(msg + '\n');				}
			} catch (RestRequestException e) {
				e.printStackTrace();
			}
			
			try {
				Thread.sleep(15 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
}
