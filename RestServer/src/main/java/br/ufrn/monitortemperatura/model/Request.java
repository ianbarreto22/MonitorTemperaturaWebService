package br.ufrn.monitortemperatura.model;

public class Request {
	private Operacao operacao;
	private int x;
	private int y;
	private boolean satisfeita;
	
	
	public Request() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Request(Operacao operacao, int x) {
		super();
		this.operacao = operacao; 
		this.x = x;
		this.satisfeita = false;
	}
	
	public Request(Operacao operacao, int x, int y) {
		super();
		this.operacao = operacao;
		this.x = x;
		this.y = y;
		this.satisfeita = false;
	}
	public Operacao getOperacao() {
		return operacao;
	}
	public void setOperacao(Operacao operacao) {
		this.operacao = operacao;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	
	public boolean getSatisfeita() {
		return this.satisfeita;
	}
	
	
	public void setSatisfeita(boolean satisfeita) {
		this.satisfeita = satisfeita;
	}
	
	
	
}
