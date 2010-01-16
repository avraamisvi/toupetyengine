package br.org.gamexis.plataforma.cena.componentes;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.GUIContext;
/**
 * Caixa de imagem
 * @author abraao
 *
 */
public class CaixaImagem extends ToupetyComponente {
	
	private Image imagem;
	private Color corBorda;
	private float borda;
	
	public CaixaImagem(GUIContext container, Image imagem) {
		super(container);
		this.imagem = imagem;
	}

	@Override
	public void desenhar(Graphics g) {
		if(borda > 0) {
			g.setLineWidth(borda);
			g.drawRect(getX(), getY(), getComprimento(), getAltura());
		}
		
		imagem.draw(getX()+borda, getY()+borda);
		
		if(borda > 0)
			g.resetLineWidth();
	}

	@Override
	public float getAltura() {
		return imagem.getHeight();
	}

	@Override
	public float getComprimento() {
		return imagem.getWidth();
	}

	@Override
	public Color getEfeitoCor() {
		return null;
	}

	@Override
	public void setEfeitoCor(Color cor) {

	}

	@Override
	public void desenhar(Graphics g, float fatorX, float fatorY) {
		// TODO ainda nao suportado
		desenhar(g);
	}
}
