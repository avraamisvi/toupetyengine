package br.org.gamexis.plataforma.cena.componentes.util;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Rectangle;

public class CaixaShape implements Caixa {

	String nome;
	float x;
	float y;
	
	float comprimento = 600;
	float altura = 200;
	
	float larguraBorda = 10;
	
	Color corBorda = Color.darkGray;
	Color corFundo1 = Color.green;
	Color corFundo2 = Color.black;
	
//	Color efeitoColor = Color.white;
	
	boolean excluido = false;
	boolean visivel = true;
	
	@Override
	public float getMargemHorizontal() {
		return larguraBorda;
	}

	@Override
	public float getMargemVertical() {
		return larguraBorda;
	}
	
	@Override
	public void desenhar(Graphics g) {
		Rectangle rect = new Rectangle(x+larguraBorda, y+larguraBorda, 
				comprimento-(2*larguraBorda), altura-(2*larguraBorda));
		Rectangle borda = new Rectangle(x, y, comprimento, altura);
		
		g.setColor(corBorda);
		g.fill(borda);
		
		GradientFill fill = new GradientFill(0, -75, corFundo1, 0, 75, corFundo2);
		g.fill(rect, fill);
				
	}

	public void setComprimento(float comprimento) {
		this.comprimento = comprimento;
	}
	
	public void setAltura(float altura) {
		this.altura = altura;
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
		return altura;
	}

	@Override
	public float getComprimento() {
		return comprimento;
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

	public float getLarguraBorda() {
		return larguraBorda;
	}

	public void setLarguraBorda(float larguraBorda) {
		this.larguraBorda = larguraBorda;
	}

	public Color getCorBorda() {
		return corBorda;
	}

	public void setCorBorda(Color corBorda) {
		this.corBorda = corBorda;
	}

	public Color getCorFundo1() {
		return corFundo1;
	}

	public void setCorFundo1(Color corFundo1) {
		this.corFundo1 = corFundo1;
	}

	public Color getCorFundo2() {
		return corFundo2;
	}

	public void setCorFundo2(Color corFundo2) {
		this.corFundo2 = corFundo2;
	}

	private boolean disposed = false;
	
	@Override
	public void dispose() {
		disposed = true;
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
