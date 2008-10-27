package br.org.gamexis.plataforma.entidade;

import br.org.gamexis.plataforma.motor.Fisica;

/**
 * Representa um estado da entidade.
 * @author abraao
 *
 */
public class Estado {
	
	private boolean controle = true;
	private boolean inatingivel = false;
	private float velocidadeInicialY;
	private float velocidadeInicialX;
	private Fisica fisica;
	private String nome;
	
	public void setControle(boolean controle) {
		this.controle = controle;
	}
	
	public boolean isControle() {
		return controle;
	}
	
	public float getVelocidadeInicialY() {
		return velocidadeInicialY;
	}
	public void setVelocidadeInicialY(float velocidadeInicialY) {
		this.velocidadeInicialY = velocidadeInicialY;
	}
	public float getVelocidadeInicialX() {
		return velocidadeInicialX;
	}
	public void setVelocidadeInicialX(float velocidadeInicialX) {
		this.velocidadeInicialX = velocidadeInicialX;
	}
	public Fisica getFisica() {
		return fisica;
	}
	public void setFisica(Fisica fisica) {
		this.fisica = fisica;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	public boolean isInatingivel() {
		return inatingivel;
	}

	public void setInatingivel(boolean inatingivel) {
		this.inatingivel = inatingivel;
	}
	
}
