package br.org.gamexis.plataforma.cena.componentes;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.GUIContext;

import br.org.gamexis.plataforma.motor.RecursosFactory;

public class Rotulo extends ToupetyComponente {
	
	private String texto;
	private Font fonte;
	
	public Rotulo(GUIContext container) {
		super(container);
		
		try {
			fonte = RecursosFactory.getInstancia().getFonte("toupetyFonte18"); 
//			new AngelCodeFont("recursos/fontes/toupetyFonte18.fnt",
//					"recursos/fontes/toupetyFonte18.png");
		} catch (Exception e) {
			e.printStackTrace();
		}		
		setFocus(false);
	}
	
	
	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	public String getTexto() {
		return texto;
	}
	
	@Override
	public void desenhar(Graphics g) {
		fonte.drawString(getX(), getY(), texto);
	}
	
	@Override
	public float getAltura() {
		return 0;
	}

	@Override
	public float getComprimento() {
		return 0;
	}

	@Override
	public Color getEfeitoCor() {
		return null;
	}

	@Override
	public void setEfeitoCor(Color cor) {
	}
	
	public void setFonte(Font fonte) {
		this.fonte = fonte;
	}
	
	public Font getFonte() {
		return fonte;
	}
	
	@Override
	public void desenhar(Graphics g, float fatorX, float fatorY) {
		// TODO ainda nao suportado
		desenhar(g);
	}
}
