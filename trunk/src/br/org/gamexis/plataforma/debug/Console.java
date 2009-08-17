package br.org.gamexis.plataforma.debug;

import org.lwjgl.input.Mouse;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.cena.componentes.nifty.NiftyFactory;

/**
 * Console para debug
 * 
 * @author abraao
 * 
 */
public class Console {

	private boolean ativo = false;

	public void ativar() {
		ativo = true;
		NiftyFactory.carregarTela("console.xml");
	}

	public void desativar() {
		if (ativo) {
			ativo = false;
			NiftyFactory.getNifty().exit();
		}
	}

	public void desenhar() {
		if (NiftyFactory.iniciado()) {
			int mx = Motor.obterInstancia().getAppGameContainer().getInput()
					.getMouseX();
			int my = Motor.obterInstancia().getAppGameContainer().getInput()
					.getMouseY();

			NiftyFactory.getNifty()
					.render(false, mx, my, Mouse.isButtonDown(0));
		}
	}

	public boolean isAtivo() {
		return ativo;
	}

}
