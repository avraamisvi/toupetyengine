package br.org.gamexis.plataforma.testes;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.renderer.Renderer;

import br.org.gamexis.plataforma.Motor;

public class ParalaxeTeste {

	public static void main(String[] args) {
		try {

			Motor mot = Motor.obterInstancia();
			mot.setCenaInicialDebug("deserto_sala11");
			
			mot.resolvaComandosEntrada(args);
			Renderer.setRenderer(Renderer.VERTEX_ARRAY_RENDERER);
			Renderer
					.setLineStripRenderer(Renderer.QUAD_BASED_LINE_STRIP_RENDERER);

			AppGameContainer app = new AppGameContainer(mot);
			app.setShowFPS(false);

			mot.setAppGameContainer(app);
			app.start();
		} catch (SlickException e) {
			if (Motor.obterInstancia() != null) {
				Motor.obterInstancia().tratarExcecao(e);
			} else {
				e.printStackTrace();
			}

		}
	}
}
