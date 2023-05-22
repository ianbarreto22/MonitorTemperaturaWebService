package br.imd.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.imd.rest.expections.RestRequestException;

public class Client {

	public Client() {

	}

	public void consultarTemperatura(String local) throws RestRequestException {

		String uri = "http://localhost:8080/RestServer/monitortemperatura/" + local;
		Map<String, String> headerParams = new HashMap<String, String>();

		headerParams.put("accept", "application/json");

		String response = HttpUtils.httpGetRequest(uri, headerParams);
		
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode json = null;
		try {
			json = objectMapper.readTree(response);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		System.out.println(json.get("mensagem").asText());
	}

	public Integer adicionarRequest(String local, Request r) throws RestRequestException {

		String uri = "http://localhost:8080/RestServer/monitortemperatura/" + local;
		Map<String, String> headerParams = new HashMap<String, String>();

		headerParams.put("accept", "application/json");
		headerParams.put("content-type", "application/json");

		ObjectMapper objectMapper = new ObjectMapper();

		String body = null;
		try {
			body = objectMapper.writeValueAsString(r);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String response = HttpUtils.httpPostRequest(uri, headerParams, body, 200);

//		System.out.println(response);
		return Integer.parseInt(response);
	}	

	public static void main(String[] args) throws RestRequestException {
		
		Client restClient = new Client();	
		
		Map<Integer, String> cidadesSelect = new HashMap<Integer, String>();
		
		cidadesSelect.put(1, "natal");
        cidadesSelect.put(2, "londres");
        cidadesSelect.put(3, "novaiorque");
        cidadesSelect.put(4, "oslo");
        cidadesSelect.put(5, "cairo");
		
		Scanner scanner = new Scanner(System.in);
		
		String opcoesCidades = "\n 1 - Natal \n 2 - Londres \n 3 - Nova Iorque \n 4 - Oslo \n 5 - Cairo";
		
		for(;;){
			System.out.println("Digite a cidade desejada:");
			System.out.println(opcoesCidades);
			int cidadeNum = scanner.nextInt();
			Request request = new Request();
			if(cidadeNum >= 1 && cidadeNum <= 5) {
				System.out.println("Digite a operação a ser realizada: ");
				System.out.println("\n 1 - Consultar temperatura atual \n 2 - Adicionar monitor de temperatura\n");
				
				int operacaoNum = scanner.nextInt();
				int opcaoNum=0, x=0, y=0;
			    switch (operacaoNum){
					case 1:
					    restClient.consultarTemperatura(cidadesSelect.get(cidadeNum));
						break;
						
					case 2:						
						System.out.println("Digite a opção de monitoramento:");
						System.out.println("\n 1 - Menor \n 2 - Maior \n 3 - Igual \n 4 - Intervalo");
						opcaoNum = scanner.nextInt() - 1;
						
						if(opcaoNum == 3) {
							System.out.print("Digite o primeiro valor desejado desejado (Em celsius):");
							x = scanner.nextInt();
							System.out.print("Digite o segundo valor desejado desejado (Em celsius):");
							y = scanner.nextInt();
						} else {
							System.out.print("Digite o valor desejado desejado (Em celsius):");
							x = scanner.nextInt();
						}
						int index = restClient.adicionarRequest(cidadesSelect.get(cidadeNum), new Request(Operacao.values()[opcaoNum], x,y));
						VerificadorRequest vr = new VerificadorRequest(cidadesSelect.get(cidadeNum), index);
						vr.start();
						
						break;
					default:
						System.out.println("Opção inválida");
				 }
			}
		}
		
		
	}


}
