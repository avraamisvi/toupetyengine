package br.org.gamexis.plataforma.entidade.logestado;

/**
 * Representação lógica de munição.
 * @author abraao
 *
 */
public class Municao {
	public static final int INFINITO = -1;
	
	private int municao;
	private int maximo;
	private String nome;
	
	public int getMunicao() {
		return municao;
	}
	
	public void incrementeMunicao(int municao) {		
		if(maximo == INFINITO)
			return;
		
		this.municao += Math.abs(municao);
		
		if(this.municao > maximo)
			this.municao = maximo;
	}
	
	public void decrementeMunicao(int municao) {
		if(maximo == INFINITO)
			return;
		
		this.municao -= Math.abs(municao);
		
		if(this.municao < 0)
			this.municao = 0;
	}
	
	public int getMaximo() {
		return maximo;
	}
	
	public void incrementeMaximo(int maximo) {
		if(this.maximo != INFINITO)
		this.maximo += maximo;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public void setMaximo(int maximo) {
		this.maximo = maximo;
		this.municao = maximo;
	}
	
}
