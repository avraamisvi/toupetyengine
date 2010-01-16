package br.org.gamexis.plataforma.entidade;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.shapes.Circle;
import net.phys2d.raw.shapes.Polygon;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import br.org.gamexis.plataforma.cena.Colisivel;
import br.org.gamexis.plataforma.cena.Desenho;

/**
 * Areas que executam ações a partir de uma intercessão.
 * 
 * @author abraao
 * 
 */
public abstract class AreaAcao implements Desenho {
	private Rectangle retangulo;
	private boolean disposed = false;
	
	public void setRetangulo(Rectangle retangulo) {
		this.retangulo = retangulo;
	}
	
	public abstract void executar(Colisivel colisivel);

	public void verificarExecutar(Colisivel colisivel) {
		Shape ret = null;
		colisivel.getCorpo();
		Body corpo = colisivel.getCorpo();

		//TODO IMPLEMENTAR PARA OUTROS TIPOS DE CORPOS 
		if (corpo.getShape() instanceof Box) {

			Box box = (Box) corpo.getShape();
			Vector2f[] pts = box.getPoints(corpo.getPosition(), corpo
					.getRotation());

			Vector2f v1 = pts[0];
			Vector2f v2 = pts[1];
			Vector2f v3 = pts[2];
			Vector2f v4 = pts[3];

			float comprimento = v2.x - v1.x;
			float altura = v4.y - v1.y;

			ret = new Rectangle(v1.x, v1.y, comprimento, altura);
		} else if (corpo.getShape() instanceof Circle){
			Circle circ = (Circle) corpo.getShape();
			
			ret = new Rectangle(corpo.getPosition().getX(), corpo.getPosition().getY(), 
					circ.getBounds().getWidth(), circ.getBounds().getHeight());
			ret.setCenterX(corpo.getPosition().getX());
			ret.setCenterY(corpo.getPosition().getY());
			
//			Circle circ = (Circle) corpo.getShape();
//			float r = circ.getRadius();
//			
//			ret = new org.newdawn.slick.geom.Circle(corpo.getPosition().getX(), corpo.getPosition().getY(), r);
//			ret.setCenterX(corpo.getPosition().getX());
//			ret.setCenterY(corpo.getPosition().getY());
		} else if (corpo.getShape() instanceof Polygon){
			Polygon poly = (Polygon) corpo.getShape();
			
			ret = new Rectangle(corpo.getPosition().getX(), corpo.getPosition().getY(), 
					poly.getBounds().getWidth(), poly.getBounds().getHeight());
			ret.setCenterX(corpo.getPosition().getX());
			ret.setCenterY(corpo.getPosition().getY());
			
		}

			
		if (retangulo.intersects(ret)) {
			executar(colisivel);
		}
	}

	@Override
	public void desenhar(Graphics g) {
		g.setColor(Color.red);
		g.draw(retangulo);
	}

	@Override
	public void excluir() {
	}

	@Override
	public boolean excluido() {
		return false;
	}

	@Override
	public float getAltura() {
		return retangulo.getHeight();
	}

	@Override
	public float getComprimento() {
		return retangulo.getWidth();
	}

	@Override
	public float getX() {
		return retangulo.getX();
	}

	@Override
	public float getY() {
		return retangulo.getY();
	}

	@Override
	public void setX(float x) {
		retangulo.setX(x);
	}

	@Override
	public void setY(float y) {
		retangulo.setY(y);
	}
	
	@Override
	public boolean estaVisivel() {
		return false;
	}

	@Override
	public void setVisivel(boolean visivel) {
	}

	@Override
	public void dispose() {
		//nada pq n�o desenha imagens, nem usa recursos.
		disposed = true;
	}

	@Override
	public boolean isDisposed() {
		return disposed;
	}
	
	
}
