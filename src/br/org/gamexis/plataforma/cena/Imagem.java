package br.org.gamexis.plataforma.cena;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import br.org.gamexis.plataforma.motor.RecursosFactory;

/**
 * Representa uma imagem numa cena.
 * @author abraao
 *
 */
public class Imagem implements Desenho {
	private Color efeitoCor = Color.white;
	private Image imagem;
	float x,y;
	private boolean disposed = false;
	
	public Imagem(String ref) throws SlickException {
		imagem = RecursosFactory.getInstancia().getImage(ref);
	}
	
	public Imagem(String ref, boolean grande) throws SlickException {
		imagem = RecursosFactory.getInstancia().getImage(ref, grande);
	}
	
	@Override
	public void desenhar(Graphics g) {
		float x2 = getX() - (imagem.getWidth()/2);
		float y2 = getY() - (imagem.getHeight()/2);
		
		imagem.draw(x2, y2);
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
	public void setX(float x) {
		this.x = x;
	}

	@Override
	public void setY(float y) {
		this.y = y;
	}

	private boolean excluido = false;
	
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
		return imagem.getHeight();
	}

	@Override
	public float getComprimento() {
		return imagem.getWidth();
	}
	
	public boolean estaVisivel() {
		return true;
	}

	@Override
	public void setVisivel(boolean visivel) {
		
	}
	
	public void setEfeitoCor(Color efeitoCor) {
		this.efeitoCor = efeitoCor;
	}
	
	public Color getEfeitoCor() {
		return efeitoCor;
	}

	@Override
	public String getNome() {
		return imagem.getTexture().getTextureRef();
	}

	@Override
	public void dispose() {
		disposed = true;
		RecursosFactory.getInstancia().libertarRecurso(imagem.getReference());
	}

	@Override
	public boolean isDisposed() {
		return disposed;
	}

	@Override
	public void desenhar(Graphics g, float fatorX, float fatorY) {
		float w = imagem.getWidth() * fatorX;
		float h = imagem.getHeight() * fatorY;
		
		float x2 = getX() - (w/2);
		float y2 = getY() - (h/2);
		
		imagem.draw(x2, y2, w, h);
	}
}
