package br.org.gamexis.plataforma.cena;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Graphics;

/**
 * Representa uma tela.
 * @author abraao
 *
 */
public class Tela {
	
	private float visaoX;
	private float visaoY;
	
	private float oldx;
	private float oldy;
	
	private List<Desenho> desenhos; 
	private boolean visivel = true;
	private float deslocamentoX = 0;
	private float deslocamentoY = 0;
	
	public Tela() {
		visaoX = 0;
		visaoY = 0;
		desenhos = new ArrayList<Desenho>();
	}
	
	public void ajustePosicao(float x, float y) {
		if(oldx != x) {
			deslocamentoX = x - oldx;
		}

		if(oldy != y) {
			deslocamentoY = y - oldy;
		}
		
		oldx = x;
		oldy = y;
	}
	
	public void configurePosicao(float x, float y) {
		deslocamentoX = -x; 
		deslocamentoY = -y;
		
		visaoX = x;
		visaoY = y;
	}
	
	public void adicionarDesenho(Desenho desenho) {
		desenhos.add(desenho);
	}
	
	public boolean removerDesenho(Desenho desenho) {
		return desenhos.remove(desenho);
	}
	
	public void desenhe(Graphics g) {
		if(visivel) {			
			for (Desenho desenho : desenhos) {
				desenho.setX(desenho.getX() - deslocamentoX);
				desenho.setY(desenho.getY() - deslocamentoY);
				desenho.desenhar(g);
			}
			deslocamentoX = 0;
			deslocamentoY = 0;
		}
				
	}
	
	public boolean estaVisivel() {
		return visivel;
	}
	
	public void setVisivel(boolean visivel) {
		this.visivel = visivel;
	}
	
	public void dispose() {
		desenhos = null; 
	}
}
