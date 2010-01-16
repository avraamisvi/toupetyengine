package br.org.gamexis.plataforma.cena.colisao;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.geom.Rectangle;

import br.org.gamexis.plataforma.cena.Ator;

public class CorpoColisao {
	private List<RetanguloColisao> caixasColisao = new ArrayList<RetanguloColisao>();
	private Ator dono;
	
	
	public Ator getDono() {
		return dono;
	}

	public void setDono(Ator dono) {
		this.dono = dono;
	}

	public void adicionarRectangulo(String nome, Rectangle rec) {
		RetanguloColisao ret = new RetanguloColisao();
		
		ret.setNome(nome);
		ret.setRectangle(rec);
		caixasColisao.add(ret);
	}
	
	public String colide(float comprimento, float altura, float x, float y) {
		for (RetanguloColisao rec : caixasColisao) {
			if(estaTocando(rec, comprimento, altura, x, y)) {
				return rec.getNome();
			}
		}
		
		return null;
	}
	
	public List<RetanguloColisao> getCaixasColisao() {
		return caixasColisao;
	}
	
	public boolean estaTocando(RetanguloColisao rec, float comprimento, float altura, float x, float y) {
		
		Rectangle rectb = new Rectangle(x,y,comprimento,altura);
		rectb.setX(x);
		rectb.setY(y);
		
		float posx = getDono().getX() + rec.getRectangle().getX();
		float posy = getDono().getY() + rec.getRectangle().getY();
		Rectangle recta = new Rectangle(posx, posy, rec.getRectangle().getWidth(),rec.getRectangle().getHeight());
		recta.setX(posx);
		recta.setY(posy);
		
		return recta.intersects(rectb);
		
//		float x1 = getDono().getCorpo().getPosition().getX();
//		float y1 = getDono().getCorpo().getPosition().getY();
//
//		float x2 = x;
//		float y2 = y;
//
//		
//		AABox ab1 = new AABox(rec.getRectangle().getWidth(), rec.getRectangle().getHeight());
//
//		AABox ab2 = new AABox(comprimento, altura);
//		if(ab1.touches(x1+rec.getRectangle().getX(), y1+rec.getRectangle().getY(), ab2, x2, y2)) {
//			return true;
//		}
//		
//		return false;
	}

}
