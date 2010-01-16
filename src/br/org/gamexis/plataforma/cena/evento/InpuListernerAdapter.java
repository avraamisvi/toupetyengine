package br.org.gamexis.plataforma.cena.evento;

import org.newdawn.slick.Input;
import org.newdawn.slick.InputListener;

import br.org.gamexis.plataforma.motor.EntradaPerifericos;
import br.org.gamexis.plataforma.motor.configuracao.Controles;

public abstract class InpuListernerAdapter implements InputListener {
	
	private boolean pressionado;
	
	public abstract int getAlvo();
	
	public boolean foiPressionado() {
		return pressionado;
	}
	
	@Override
	public void controllerButtonPressed(int controller, int button) {
	}

	@Override
	public void controllerButtonReleased(int controller, int button) {
		if(EntradaPerifericos.getInstancia().getControle().isTecladoSelecionado())
			return;
		
		int chave = EntradaPerifericos.getInstancia().getControle().traduzir(button);
		
		if(chave == getAlvo()) {
			pressionado = true;
		}
	}

	@Override
	public void controllerDownPressed(int controller) {
	}

	@Override
	public void controllerDownReleased(int controller) {
		if(EntradaPerifericos.getInstancia().getControle().isTecladoSelecionado())
			return;
		
		if(getAlvo() == Controles.BAIXO) {
			pressionado = true;
		}
	}

	@Override
	public void controllerLeftPressed(int controller) {
	}

	@Override
	public void controllerLeftReleased(int controller) {
		if(EntradaPerifericos.getInstancia().getControle().isTecladoSelecionado())
			return;
		
		if(getAlvo() == Controles.ESQUERDA) {
			pressionado = true;
		}
	}

	@Override
	public void controllerRightPressed(int controller) {
	}

	@Override
	public void controllerRightReleased(int controller) {
		if(EntradaPerifericos.getInstancia().getControle().isTecladoSelecionado())
			return;
		
		if(getAlvo() == Controles.DIREITA) {
			pressionado = true;
		}
	}

	@Override
	public void controllerUpPressed(int controller) {
	}

	@Override
	public void controllerUpReleased(int controller) {
		if(EntradaPerifericos.getInstancia().getControle().isTecladoSelecionado())
			return;
		
		if(getAlvo() == Controles.CIMA) {
			pressionado = true;
		}
	}

	@Override
	public void inputEnded() {
	}

	@Override
	public boolean isAcceptingInput() {
		return true;
	}

	@Override
	public void keyPressed(int key, char c) {
	}

	@Override
	public void keyReleased(int key, char c) {
		if(!EntradaPerifericos.getInstancia().getControle().isTecladoSelecionado())
			return;
		
		int chave = EntradaPerifericos.getInstancia().getControle().traduzir(key);
		
		if(getAlvo() == chave) {
			pressionado = true;
		}
	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
	}

	@Override
	public void mousePressed(int button, int x, int y) {
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
	}

	@Override
	public void mouseWheelMoved(int change) {
	}

	@Override
	public void setInput(Input input) {
	}

}
