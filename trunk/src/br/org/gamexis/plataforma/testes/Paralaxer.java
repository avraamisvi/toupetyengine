package br.org.gamexis.plataforma.testes;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.SlickException;

import br.org.gamexis.plataforma.cena.Desenho;

public class Paralaxer {
	public Desenho d;
	public Graphics g;
	public Image sup;
	
	public void iniciar() {
		sup = new ImageBuffer(256,256).getImage();
	}
	
	public void desenhar(Graphics g) {
		 try {
			 Image sub;
			 this.g = sup.getGraphics();
			 
			Graphics.setCurrent(this.g);
//			this.g.clear();
			d.desenhar(this.g);
//			sub = sup.getSubImage(0, 0, 66, 128);
//			this.g.clear();
//			sub.draw();
			
			Graphics.setCurrent(g);
			sup.draw();
			
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
