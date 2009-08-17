package br.org.gamexis.plataforma.entidade.logestado;

import br.org.gamexis.plataforma.cena.Imagem;

public class ArmaSelecionada {
	private Municao municao;
	private String nome;
	private int poder;	
	private Imagem icone;
	private float dx;
	private float dy;
	
	public Municao getMunicao() {
		return municao;
	}
	public void setMunicao(Municao municao) {
		this.municao = municao;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public int getPoder() {
		return poder;
	}
	public void setPoder(int poder) {
		this.poder = poder;
	}
	public Imagem getIcone() {
		return icone;
	}
	public void setIcone(Imagem icone) {
		this.icone = icone;
	}
	
	public void setDeslocamentoX(float dx) {
		this.dx = dx;
	}
	
	public void setDeslocamentoY(float dy) {
		this.dy = dy;
	}
	
	public float getDeslocamentoX() {	
		return dx;
	}
	
	public float getDeslocamentoY() {
		return dy;
	}
}
