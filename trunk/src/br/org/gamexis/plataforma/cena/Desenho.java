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
	
	public void desenhar(Graphics g);
	
	public void setX(float x);	
	public void setY(float y);
	public float getX();
	public float getY();

	public float getComprimento();
	public float getAltura();
	
	public void excluir();
	public boolean excluido();
//	/**
//	 * Usado internamente para limpar o objeto, facilitando o GC
//	 */
//	public void dispose();
	
	public void setVisivel(boolean visivel);
	public boolean estaVisivel();
	public void setEfeitoCor(Color cor);
	public Color getEfeitoCor();
	public String getNome();
	public void dispose();
	public boolean isDisposed();
}
