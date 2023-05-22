package br.ufrn.monitortemperatura.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Mensagem{
	
	private String mensagem;
	private String horario;
	private Integer temperatura;
	private String local;
	
	public Mensagem(Integer temperatura, String local) {
		this.temperatura = temperatura;
		this.local = local;
		
		Date data = new Date();
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String dataFormatada = formatador.format(data);
		
		this.horario = dataFormatada;
		
		if(temperatura != null) {
			this.mensagem = "Temperatura em " + local + ": " + temperatura + " (" + dataFormatada + ").";
		} else {
			this.mensagem = "Não foi possível consultar temperatura.";
		}
	}
	
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	

	public Integer getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(Integer temperatura) {
		this.temperatura = temperatura;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getHorario() {
		return horario;
	}

	public void setHorario(String horario) {
		this.horario = horario;
	}
	
	
	
	

}
