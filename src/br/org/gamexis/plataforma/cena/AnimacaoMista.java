package br.org.gamexis.plataforma.cena;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * Representa uma animação mista.
 * @author abraao
 *
 */
public class AnimacaoMista implements Animado {
	
	private Color efeitoCor = Color.white;
	private float x;
	private float y;
	
	private float eX;//offset do emissor	
	private float eY;	
	
	private EmissorParticulas emissor;
	private Animacao animacao;	
	
	public AnimacaoMista() {
	}
	
	public AnimacaoMista(AnimacaoMista original) {
		try {
			animacao = (Animacao)original.animacao.clone();
			emissor = (EmissorParticulas)original.emissor.clone();
			eX = original.eX;	
			eY = original.eY;
			
			setX(original.x);
			setY(original.y);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void setEmissor(EmissorParticulas emissor) {
		this.emissor = emissor;
	}
	
	public void setAnimacao(Animacao animacao) {
		this.animacao = animacao;
	}
	
	@Override
	public void atualizar(int delta) {
		emissor.atualizar(delta);
		animacao.atualizar(delta);
	}

	@Override
	public void reiniciar() {
//		emissor.reiniciar();
		animacao.reiniciar();
	}

	@Override
	public boolean ultimoQuadro() {
		return animacao.ultimoQuadro();
	}

	@Override
	public void desenhar(Graphics g) {
		emissor.desenhar(g);
		animacao.setEfeitoCor(efeitoCor);		
		animacao.desenhar(g);		
	}

	@Override
	public void excluir() {
		emissor.excluir();
		animacao.excluir();
	}

	@Override
	public boolean excluido() {
		return emissor.excluido();
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
		emissor.setX(x+eX);
		animacao.setX(x);
	}

	@Override
	public void setY(float y) {
		this.y = y;
		emissor.setY(y+eY);
		animacao.setY(y);
	}

	public float getEX() {
		return eX;
	}

	public void setEX(float ex) {
		eX = ex;
	}

	public float getEY() {
		return eY;
	}

	public void setEY(float ey) {
		this.eY = ey;
	}

	public EmissorParticulas getEmissor() {
		return emissor;
	}

	public Animacao getAnimacao() {
		return animacao;
	}

	@Override
	public float getAltura() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getComprimento() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean estaVisivel() {
		return true;
	}

	@Override
	public void setVisivel(boolean visivel) {
	}
	
	@Override
	public Color getEfeitoCor() {
		return efeitoCor;
	}

	@Override
	public void setEfeitoCor(Color cor) {
		this.efeitoCor = cor;
	}

	@Override
	public String getNome() {
		return null;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return new AnimacaoMista(this);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isDisposed() {
		// TODO Auto-generated method stub
		return false;
	}	
}
