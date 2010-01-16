package br.org.gamexis.plataforma.cena.fisica;

import net.phys2d.raw.Body;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.shapes.Shape;

public class RetangularPlataformaCorpo extends Body {
	private Body direita;
	private Body esquerda;
	
	public RetangularPlataformaCorpo(Shape shape) {
		super(shape, Body.INFINITE_MASS);
		criarCorpos(shape);
	}

	public RetangularPlataformaCorpo(String name, Shape shape) {
		super(name, shape, Body.INFINITE_MASS);
		criarCorpos(shape);
	}

	private void criarCorpos(Shape shape) {
		direita = new StaticBody(new Box(2, ((Box)shape).getSize().getY()));
		direita.setFriction(0);
		esquerda = new StaticBody(new Box(2, ((Box)shape).getSize().getY()));
		esquerda.setFriction(0);
	}
	
	@Override
	public void setPosition(float x, float y) {
		direita.setPosition(x + (((Box)this.getShape()).getSize().getX()/2), y);
		esquerda.setPosition(x - (((Box)this.getShape()).getSize().getX()/2), y);
		
		super.setPosition(x, y);
	}
	
	
	public Body getDireita() {
		return direita;
	}

	public void setDireita(Body direita) {
		this.direita = direita;
	}

	public Body getEsquerda() {
		return esquerda;
	}

	public void setEsquerda(Body esquerda) {
		this.esquerda = esquerda;
	}

	/**
	 * @see net.phys2d.raw.Body#isRotatable()
	 */
	public boolean isRotatable() {
		return false;
	}
	
	/**
	 * @see net.phys2d.raw.Body#isMoveable()
	 */
	public boolean isMoveable() {
		return false;
	}
	
	/**
	 * Check if this body is static
	 * 
	 * @return True if this body is static
	 */
	public boolean isStatic() {
		return true;
	}
	
	/**
	 * @see net.phys2d.raw.Body#isResting()
	 */
	public boolean isResting() {
		return true;
	}	
}
