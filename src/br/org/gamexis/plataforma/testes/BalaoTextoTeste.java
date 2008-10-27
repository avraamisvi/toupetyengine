package br.org.gamexis.plataforma.testes;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.GUIContext;

public class BalaoTextoTeste extends AbstractComponent {
	
	String saida = " TESTE ";
	
	public BalaoTextoTeste(GUIContext container) {
		super(container);
		setFocus(true);
	}

	@Override
	public void keyPressed(int key, char c) {
		saida += " teste ";
	}
	
	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public int getWidth() {
		return 0;
	}

	@Override
	public int getX() {
		return 50;
	}

	@Override
	public int getY() {
		return 50;
	}

	@Override
	public void render(GUIContext container, Graphics g) throws SlickException {
		
		Rectangle rt = new Rectangle(0, 0, 300, 300);
		g.setColor(Color.white);
		g.fill(rt);
		g.setColor(Color.black);
		g.drawString(saida, 0, 0);
	}

	@Override
	public void setLocation(int x, int y) {
		
	}

}
