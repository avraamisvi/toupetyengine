package br.org.gamexis.plataforma.cena.componentes.nifty;

import org.newdawn.slick.InputListener;
import org.newdawn.slick.util.InputAdapter;

import br.org.gamexis.plataforma.Motor;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.render.spi.lwjgl.RenderDeviceLwjgl;
import de.lessvoid.nifty.sound.SoundSystem;
import de.lessvoid.nifty.sound.slick.SlickSoundLoader;
import de.lessvoid.nifty.tools.TimeProvider;

public class NiftyFactory {

	private static Nifty nifty;

	public static boolean iniciado() {
		return nifty != null;
	}
	
	public static void iniciar() {

		if (nifty == null) {

			nifty = new Nifty(new RenderDeviceLwjgl() {
				@Override
				public void clear() {
					// PARA N�O APAGAR A TELA DO JOGO
				}
			}, new SoundSystem(new SlickSoundLoader()), new TimeProvider());

			// Captura os eventos de teclado para o GUI.
			InputListener listernIn = new InputAdapter() {
				@Override
				public void keyReleased(int key, char c) {
					nifty.keyEvent(key, c, true);
					super.keyReleased(key, c);
				}
			};
			
			Motor.obterInstancia().getAppGameContainer().getInput()
					.addListener(listernIn);
		}

	}

	public static void carregarTela(String screen) {
		getNifty().fromXml("console.xml", "start");
	}

	public static Nifty getNifty() {
		iniciar();
		return nifty;
	}
}
