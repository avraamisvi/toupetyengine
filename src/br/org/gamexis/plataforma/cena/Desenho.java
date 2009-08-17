package br.org.gamexis.plataforma.cena;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * Menor unidade desenhavel.
 * 
 * @author abraao
 *
 */
public interface Desenho {
	
	void desenhar(Graphics g);
	void desenhar(Graphics g, float fatorX, float fatorY);
	
	void setX(float x);	
	void setY(float y);
	float getX();
	float getY();

	float getComprimento();
	float getAltura();
	
	void excluir();
	boolean excluido();
//	/**
//	 * Usado internamente para limpar o objeto, facilitando o GC
//	 */
//	public void dispose();
	
	void setVisivel(boolean visivel);
	boolean estaVisivel();
	void setEfeitoCor(Color cor);
	Color getEfeitoCor();
	String getNome();
	void dispose();
	boolean isDisposed();
}
