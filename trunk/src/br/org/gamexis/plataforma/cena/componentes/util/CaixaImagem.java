package br.org.gamexis.plataforma.cena.componentes.util;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import br.org.gamexis.plataforma.motor.RecursosFactory;

/**
 * Caixa de um balao de texto baseado numa imagem, 
 * usando um mosaico.
 * 
 * @author abraao
 *
 */
public class CaixaImagem implements Caixa {
	
	String nome;
	
	float x;
	float y;
	
	float comprimento;
	float altura;
	
	Image fundo;
	
	float margemHorizontal = 25;
	float margemVertical = 25;
	
	boolean excluido = false;
	boolean visivel = true;
	
	private boolean disposed = false;
	
	public CaixaImagem(Image fundo) {
		this.fundo = fundo;
	}	
	
	@Override
	public float getMargemHorizontal() {
		return margemHorizontal;
	}

	@Override
	public float getMargemVertical() {
		return margemVertical;
	}
	
	public void setMargem(float margem) {
		this.margemVertical = margem;
		this.margemHorizontal = margem;
	}
	
	public void setMargemHorizontal(float margemHorizontal) {
		this.margemHorizontal = margemHorizontal;
	}
	
	public void setMargemVertical(float margemVertical) {
		this.margemVertical = margemVertical;
	}
	
	@Override
	public void desenhar(Graphics g) {
		fundo.draw(x, y);
	}

	@Override
	public boolean estaVisivel() {
		return visivel;
	}

	@Override
	public void excluir() {
		excluido = true;
	}

	@Override
	public boolean excluido() {
		return excluido;
	}

	@Override
	public float getAltura() {
		return fundo.getHeight();
	}

	@Override
	public float getComprimento() {
		return fundo.getWidth();
	}

	@Override
	public Color getEfeitoCor() {
		return null;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public String getNome() {
		return nome;
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public void setEfeitoCor(Color cor) {
	}

	public Image getFundo() {
		return fundo;
	}
	
	public void setFundo(Image fundo) {
		this.fundo = fundo;
	}
	
	@Override
	public void setVisivel(boolean visivel) {
		this.visivel = visivel;
	}

	@Override
	public void setX(float x) {
		this.x = x;
	}

	@Override
	public void setY(float y) {
		this.y = y;
	}

	@Override
	public void dispose() {
		disposed = true;
		RecursosFactory.getInstancia().libertarRecurso(fundo.getReference());
	}

	@Override
	public boolean isDisposed() {
		return disposed;
	}
	@Override
	public void desenhar(Graphics g, float fatorX, float fatorY) {
		// TODO ainda nao suportado
		desenhar(g);
	}
}
