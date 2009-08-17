package br.org.gamexis.plataforma.cena.util;

import net.phys2d.math.ROVector2f;
import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.shapes.Circle;
import net.phys2d.raw.shapes.Line;
import net.phys2d.raw.shapes.Polygon;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

/**
 * Desenha um corpo
 * @author abraao
 *
 */
public class DesenhadorCorpo {
	
	public void desenhar(Body corpo, Graphics g) {
		if(corpo.getShape() instanceof Box) {
			desenharCaixa(corpo, g);
		} else if(corpo.getShape() instanceof Circle) {
			desenharCirculo(g, corpo);			
		} else if(corpo.getShape() instanceof Line) {
			desenharLinha(g, corpo);
		} else if(corpo.getShape() instanceof Polygon) {
			desenharPoligono(g, corpo);
		}  
	}
	
	private void desenharPoligono(Graphics g, Body corpo) {
		Polygon poligono = (Polygon)corpo.getShape();
		
		org.newdawn.slick.geom.Polygon poly = new org.newdawn.slick.geom.Polygon();
		g.setColor(Color.black);
		ROVector2f[] verts = poligono.getVertices(corpo.getPosition(), corpo.getRotation());
		for (int i = 0, j = verts.length - 1; i < verts.length; j = i, i++) {
			poly.addPoint(verts[i].getX(), verts[i].getY());
		}
		g.setColor(new Color(1.0f,1.0f,1.0f,0.5f));
		g.fill(poly);
	}
	
	
	private void desenharLinha(Graphics g, Body corpo) {
		Line line = (Line)corpo.getShape();
		g.setColor(Color.black);

		Vector2f[] verts = line.getVertices(corpo.getPosition(), corpo.getRotation());
		g.drawLine(
				(int) verts[0].getX(),
				(int) verts[0].getY(), 
				(int) verts[1].getX(),
				(int) verts[1].getY());
	}
	
	
	private void desenharCaixa(Body corpo, Graphics g) {		

		Box box = (Box)corpo.getShape();
		Vector2f[] pts = box.getPoints(corpo.getPosition(),
				corpo.getRotation());

		Vector2f v1 = pts[0];
		Vector2f v2 = pts[1];
		Vector2f v3 = pts[2];
		Vector2f v4 = pts[3];

		g.setLineWidth(1);
		g.setColor(Color.black);
		g.drawLine((int) v1.x, (int) v1.y, (int) v2.x, (int) v2.y);
		g.drawLine((int) v2.x, (int) v2.y, (int) v3.x, (int) v3.y);
		g.drawLine((int) v3.x, (int) v3.y, (int) v4.x, (int) v4.y);
		g.drawLine((int) v4.x, (int) v4.y, (int) v1.x, (int) v1.y);		
	}
	
	private void desenharCirculo(Graphics g, Body corpo) {
		Circle circle = (Circle)corpo.getShape();
		g.setColor(Color.black);
		float x = corpo.getPosition().getX();
		float y = corpo.getPosition().getY();
		float r = circle.getRadius();
		float rot = corpo.getRotation();
		float xo = (float) (Math.cos(rot) * r);
		float yo = (float) (Math.sin(rot) * r);
		
		g.drawOval((int) (x-r),(int) (y-r),(int) (r*2),(int) (r*2));
		g.drawLine((int) x,(int) y,(int) (x+xo),(int) (y+yo));
		
//		Rectangle ret = new Rectangle(corpo.getPosition().getX(), corpo.getPosition().getY(), 
//				circle.getBounds().getWidth(), circle.getBounds().getHeight());
//		ret.setCenterX(corpo.getPosition().getX());
//		ret.setCenterY(corpo.getPosition().getY());
//		
//		g.draw(ret);
	}	
}
