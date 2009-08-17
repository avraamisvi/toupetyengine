package br.org.gamexis.plataforma.entidade.logestado;

public class BarraLogEstado {
	private int maximo;
	private int atual;
	private String nome;
	
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public int getMaximo() {
		return maximo;
	}
	public void setMaximo(int maximo) {
		this.maximo = maximo;
		this.atual = maximo;
	}
	public int getAtual() {
		return atual;
	}
	
	public void incrementeAtual(int atual) {
		this.atual += atual;
		
		if(this.atual > maximo)
			this.atual = maximo;
	}
	
	public void decrementeAtual(int atual) {		
		this.atual -= atual;
		
		if(this.atual < 0)
			this.atual = 0;
	}
}
