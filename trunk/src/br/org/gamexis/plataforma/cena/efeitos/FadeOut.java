package br.org.gamexis.plataforma.cena.efeitos;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import br.org.gamexis.plataforma.cena.Efeito;

public class FadeOut extends Efeito {
	private float fator = -0.1f;
	private float opacidade = 1;
	private long intervalo = 50;
	private long passado = 0;
	
	@Override
	public boolean ultimoQuadro() {
		return (opacidade <= 0);
	}
	
	public void setFator(float fator) {
		
		this.fator = Math.abs(fator);
		
		if(this.fator > 1)
			this.fator = 0.99f;
		
		this.fator = (-1) * this.fator;
	}
	
	
	public float getFator() {
		return fator;
	}
	
	@Override
	public void atualizar(int delta) {
		passado = passado + delta;
		
		if(passado < intervalo)
			return;
		
		passado = 0;
		opacidade = opacidade + fator;
		
		if(opacidade < 0)
			opacidade = 0;
		
		getReferencia().setEfeitoCor(new Color(1, 1, 1, opacidade));
		
		if(getReferencia() != null && getReferencia().isDisposed()) {
			setReferencia(null);
		}		
	}
	
	@Override
	public void desenhar(Graphics g) {
		getReferencia().setEfeitoCor(new Color(1, 1, 1, opacidade));
	}
	
	@Override
	public void desenhar(Graphics g, float fatorX, float fatorY) {
		getReferencia().setEfeitoCor(new Color(1, 1, 1, opacidade));
	}
	
	public float getOpacidade() {
		return opacidade;
	}
	
	public void setOpacidade(float opacidade) {
		this.opacidade = opacidade;
	}
	
	public void setIntervalo(long intervalo) {
		this.intervalo = intervalo;
	}
	
	public long getIntervalo() {
		return intervalo;
	}
	
	@Override
	public String getNome() {
		return "FadeOut";
	}	
}
