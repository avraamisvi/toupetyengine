package br.org.gamexis.plataforma.motor.configuracao;

/**
 * Representa a resolução do jogo
 * @author abraao
 *
 */
public class Video {
	private int comprimento;
	private int altura;
	private boolean telaCheia;
	
	public int getComprimento() {
		return comprimento;
	}
	public void setComprimento(int comprimento) {
		this.comprimento = comprimento;
	}
	public int getAltura() {
		return altura;
	}
	public void setAltura(int altura) {
		this.altura = altura;
	}
	public boolean isTelaCheia() {
		return telaCheia;
	}
	public void setTelaCheia(boolean telaCheia) {
		this.telaCheia = telaCheia;
	}
	
	
	
}
