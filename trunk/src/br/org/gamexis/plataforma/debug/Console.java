package br.org.gamexis.plataforma.debug;

import groovy.lang.GroovyShell;
import groovy.lang.Script;

import org.codehaus.groovy.control.CompilationFailedException;
import org.newdawn.slick.Font;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.cena.componentes.CampoTexto;
import br.org.gamexis.plataforma.cena.componentes.Painel;
import br.org.gamexis.plataforma.motor.RecursosFactory;

/**
 * Console para debug
 * 
 * @author abraao
 * 
 */
public class Console extends Painel {

	GroovyShell shell = new GroovyShell();
	Script script;
	public Console(GUIContext container) {
		super(container);
		construir(container);
		//definirFuncoes();
	}

	private void construir(GUIContext container) {
		try {			
			adicionarComponente(new CampoTextoDebug(container, RecursosFactory
					.getInstancia().getFonte("toupetyFonte18"), 0, 0, 800, 40));
			setFundo( RecursosFactory.getInstancia().getCaixaShape(800, 40));
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public void definirFuncoes() {
		try {
			shell.setProperty("MOTOR", Motor.obterInstancia());
			
			if(Motor.obterInstancia().getJogavelCarregado() != null) {
				shell.setProperty("JOGAVEL", Motor.obterInstancia().getJogavelCarregado());
				shell.setProperty("ESTADO", Motor.obterInstancia().getJogavelCarregado().getPainelEstado());
			}
			
			shell.setProperty("FABRICA", RecursosFactory.getInstancia());
			shell.setProperty("DEBUG", new DebugMestre());
		} catch (CompilationFailedException e) {
			e.printStackTrace();
		}
	}

	public void executar(String texto) {
		try {
			shell.evaluate(texto);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class CampoTextoDebug extends CampoTexto {

		public CampoTextoDebug(GUIContext container, Font font, int x, int y,
				int width, int height) {
			super(container, font, x, y, width, height);
			setMaxLength(3000);
		}

		@Override
		public void keyPressed(int key, char c) {
			if (isAtivado()) {
				if (key == Input.KEY_RETURN) {
					executar(getText());
					notifyListeners();
				} else {
					super.keyPressed(key, c);
				}
			}
		}
	}

}
