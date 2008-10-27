package br.org.gamexis.plataforma.testes;

import org.fenggui.Button;
import org.fenggui.render.lwjgl.LWJGLBinding;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class TestesFengGUI extends BasicGame {

	public TestesFengGUI() {
		super("teste");
	}

	public static void main(String[] args) {
		try {
			TestesFengGUI mot = new TestesFengGUI();
			AppGameContainer app = new AppGameContainer(mot);
			app.setShowFPS(false);			
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}	
	
	org.fenggui.Display desk;
	@Override
	public void init(GameContainer container) throws SlickException {
		// init. the LWJGL Binding
		LWJGLBinding binding = new LWJGLBinding();
		
		// init the root Widget, that spans the whole
		// screen (i.e. the OpenGL context within the
		// Microsoft XP Window)
		desk = new org.fenggui.Display(binding);			
		Button b = new Button("This is a simple button");
		b.setX(50);
		b.setY(30);		
		b.setSizeToMinSize();
		
		desk.addWidget(b);
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {		
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		desk.display();
	}

}
