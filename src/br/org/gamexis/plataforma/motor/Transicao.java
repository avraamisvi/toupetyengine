package br.org.gamexis.plataforma.motor;

import org.lwjgl.Sys;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.cena.Desenho;

public class Transicao implements Desenho {

	private boolean voltando;
	private boolean terminado;

	private float comp;
	private float alt;
	
	private float alpha;
	
	private float fator = 0.03f;
	
	/**
	 * Construtor
	 * 
	 * @param comprimento
	 *            comprimento total
	 * @param altura
	 *            altura total
	 * @param intervalo
	 *            intervalo de cada quadro
	 */
	public Transicao(float comprimento, float altura) {
		comp = comprimento;
		alt = altura;
	}

	/**
	 * Sequencia com uma linha e N colunas.
	 * 
	 * @param imagem
	 *            base
	 * @param comp
	 *            comprimento da coluna
	 * @param alt
	 *            altura da coluna
	 * @param quadros
	 *            quantidade de colunas
	 */
	public void setImagemBase(Image imagem, int comp, int alt, int quadros) {

	}

	@Override
	public void desenhar(Graphics g) {
		
		Color old = g.getColor();
		g.setColor(new Color(0,0,0,alpha));
		g.fillRect(0, 0, comp, alt);
		g.setColor(old);
		atualizarPosicao();
	}

	public void atualizarPosicao() {
			
		if(voltando) {
			if(alpha <= 0)
				return;
			
			 alpha = alpha - fator;
			 
			 if(alpha <= 0) {
				 alpha = 0;
				 terminado = true;
			 }
			 
		} else {
			if(alpha >= 1)
				return;
			
			 alpha = alpha + fator;
			 
			 if(alpha > 1) {
				 alpha = 1;
				 terminado = true;
			 }
		}
	}

	@Override
	public boolean estaVisivel() {
		return false;
	}

	@Override
	public void excluir() {
	}

	@Override
	public boolean excluido() {
		return false;
	}

	public void setAltura(float altura) {
		alt = altura;
	}

	public void setComprimento(float comprimento) {
		comp = comprimento;
	}
	
	@Override
	public float getAltura() {
		return alt;
	}

	@Override
	public float getComprimento() {
		return comp;
	}

	@Override
	public Color getEfeitoCor() {
		return null;
	}

	@Override
	public String getNome() {
		return null;
	}

	@Override
	public float getX() {
		return 0;
	}

	@Override
	public float getY() {
		return 0;
	}

	@Override
	public void setEfeitoCor(Color cor) {

	}

	@Override
	public void setVisivel(boolean visivel) {

	}

	@Override
	public void setX(float x) {

	}

	@Override
	public void setY(float y) {
	}

	public void setTerminado(boolean terminado) {
		this.terminado = terminado;
	}
	
	public void setVoltando(boolean voltando) {
		this.voltando = voltando;
	}
	
	public boolean voltando() {
		return voltando;
	}
	
	public void parar() {
		terminado = true;
		voltando = true;
	}
	
	public void iniciarIndo() {
		terminado = false;
		voltando = false;
		alpha = 0;
	}
	
	public void iniciarVoltando() {
		terminado = false;
		voltando = true;
		alpha = 1;
	}	

	public boolean terminado() {
		return terminado;
	}

	@Override
	public void dispose() {}

	@Override
	public boolean isDisposed() {
		return false;
	}
	
	@Override
	public void desenhar(Graphics g, float fatorX, float fatorY) {
		desenhar(g);
	}
}
