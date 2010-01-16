package br.org.gamexis.plataforma.cena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.InputListener;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.cena.componentes.ToupetyComponente;
import br.org.gamexis.plataforma.cena.evento.Listerner;
import br.org.gamexis.plataforma.cena.evento.ListernerManager;
import br.org.gamexis.plataforma.exception.GXException;
import br.org.gamexis.plataforma.script.ScriptComportamento;

public class CenaEstatica extends CenaBase implements ListernerManager,
		InputListener {

	private List<Listerner> listerners = new ArrayList<Listerner>();
	private Listerner atual = null;
	private HashMap<String, ToupetyComponente> componentesNomes = new HashMap<String, ToupetyComponente>();
	private List<ToupetyComponente> componentes = new ArrayList<ToupetyComponente>();
	private boolean enableKeyListenning;

	@Override
	public void desenhar(GameContainer container, Graphics g) {
		super.desenhar(container, g);

		// APOS O RESET TRANSFORM GARANTE QUE SERÃO FIXOS NA TELA
		g.resetTransform();

		desenharComponentes(g);
	}

	@Override
	public void atualizar(GameContainer container, int delta)
			throws GXException {
		super.atualizar(container, delta);

		Iterator<Listerner> itl = listerners.iterator();
		while (itl.hasNext()) {
			Listerner l = itl.next();
			atual = l;// listerner atual em execução
			l.atualizar(delta);
			atual = null;
			if (l.excluido()) {
				itl.remove();
			}
		}

		verificarExcluirComponente();
	}

	private void verificarExcluirComponente() {
		
		ToupetyComponente[] componentesArray =  componentes.toArray(new ToupetyComponente[0]);
		for (ToupetyComponente componente : componentesArray) {
			if (componente.excluido()) {
				componente.desativar();
				componentes.remove(componente);
			}
		}
	}

	public void adicionarListerner(Listerner listerner) {
		listerners.add(listerner);
		listerner.aoAdicionar();
	}

	@Override
	public Listerner getListernerAtual() {
		return atual;
	}

	@Override
	public void excluirListerner(Listerner listerner) {
		listerners.remove(listerner);
	}

	/**
	 * Adiciona um componente.
	 * 
	 * @param componente
	 */
	public void adicionarComponente(ToupetyComponente componente) {
		componentes.add(componente);
		componentesNomes.put(componente.getNome(), componente);
	}

	/**
	 * Desenha os componentes
	 */
	private void desenharComponentes(Graphics g) {
		for (ToupetyComponente componente : componentes) {
			componente.desenhar(g);
		}
	}

	public void finalizar() {

		super.finalizar();

		for (ToupetyComponente comp : componentes) {
			comp.desativar();
			comp.excluir();
		}

		if (isEnableKeyListenning())
			Motor.obterInstancia().getAppGameContainer().getInput()
					.removeListener(this);
	}

	
	@Override
	public void controllerButtonPressed(int controller, int button) {
		try {
			ScriptComportamento comportamento =  getComportamento();
			if (comportamento != null) {
				comportamento.execute("botaoPressionado", controller, button);
			}
		} catch (Throwable cause) {
			tratarExcecao(cause);
		}
	}

	@Override
	public void controllerButtonReleased(int controller, int button) {
		try {
			ScriptComportamento comportamento =  getComportamento();
			if (comportamento != null) {
				comportamento.execute("botaoSolto", controller, button);
			}
		} catch (Throwable cause) {
			tratarExcecao(cause);
		}

	}

	@Override
	public void controllerDownPressed(int controller) {
		try {
			ScriptComportamento comportamento =  getComportamento();
			if (comportamento != null) {
				comportamento.execute("baixoPressionado", controller);
			}
		} catch (Throwable cause) {
			tratarExcecao(cause);
		}

	}

	@Override
	public void controllerDownReleased(int controller) {
		try {
			ScriptComportamento comportamento =  getComportamento();
			if (comportamento != null) {
				comportamento.execute("baixoSolto", controller);
			}
		} catch (Throwable cause) {
			tratarExcecao(cause);
		}
	}

	@Override
	public void controllerLeftPressed(int controller) {
		try {
			ScriptComportamento comportamento =  getComportamento();
			if (comportamento != null) {
				comportamento.execute("esquerdaPressionado", controller);
			}
		} catch (Throwable cause) {
			tratarExcecao(cause);
		}
	}

	@Override
	public void controllerLeftReleased(int controller) {
		try {
			ScriptComportamento comportamento =  getComportamento();
			if (comportamento != null) {
				comportamento.execute("esquerdaSolto", controller);
			}
		} catch (Throwable cause) {
			tratarExcecao(cause);
		}

	}

	@Override
	public void controllerRightPressed(int controller) {
		try {
			ScriptComportamento comportamento =  getComportamento();
			if (comportamento != null) {
				comportamento.execute("direitaPressionado", controller);
			}
		} catch (Throwable cause) {
			tratarExcecao(cause);
		}
	}

	@Override
	public void controllerRightReleased(int controller) {
		try {
			ScriptComportamento comportamento =  getComportamento();
			if (comportamento != null) {
				comportamento.execute("direitaSolto", controller);
			}
		} catch (Throwable cause) {
			tratarExcecao(cause);
		}
	}

	@Override
	public void controllerUpPressed(int controller) {
		try {
			ScriptComportamento comportamento =  getComportamento();
			if (comportamento != null) {
				comportamento.execute("cimaPressionado", controller);
			}
		} catch (Throwable cause) {
			tratarExcecao(cause);
		}
	}

	@Override
	public void controllerUpReleased(int controller) {
		try {
			ScriptComportamento comportamento =  getComportamento();
			if (comportamento != null) {
				comportamento.execute("cimaSolto", controller);
			}
		} catch (Throwable cause) {
			tratarExcecao(cause);
		}
	}

	@Override
	public void inputEnded() {

	}

	@Override
	public boolean isAcceptingInput() {
		return isEnableKeyListenning();
	}

	@Override
	public void keyPressed(int key, char c) {
		try {
			ScriptComportamento comportamento =  getComportamento();
			if (comportamento != null) {
				comportamento.execute("chavePressionada", key, Input.getKeyName(key));
			}
		} catch (Throwable cause) {
			tratarExcecao(cause);
		}
	}

	@Override
	public void keyReleased(int key, char c) {
		try {
			ScriptComportamento comportamento =  getComportamento();
			if (comportamento != null) {
				comportamento.execute("chaveSolta", key, c);
			}
		} catch (Throwable cause) {
			tratarExcecao(cause);
		}
	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		try {
			ScriptComportamento comportamento =  getComportamento();
			if (comportamento != null) {
				comportamento.execute("mouseClicado", button, x, y, clickCount);
			}
		} catch (Throwable cause) {
			tratarExcecao(cause);
		}
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		try {
			ScriptComportamento comportamento =  getComportamento();
			if (comportamento != null) {
				comportamento.execute("mouseMoveu", oldx, oldy, newx, newy);
			}
		} catch (Throwable cause) {
			tratarExcecao(cause);
		}
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		try {
			ScriptComportamento comportamento =  getComportamento();
			if (comportamento != null) {
				comportamento.execute("mousePressionado", button, x, y);
			}
		} catch (Throwable cause) {
			tratarExcecao(cause);
		}
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		try {
			ScriptComportamento comportamento =  getComportamento();
			if (comportamento != null) {
				comportamento.execute("mouseSolto", button, x, y);
			}
		} catch (Throwable cause) {
			tratarExcecao(cause);
		}
	}

	@Override
	public void mouseWheelMoved(int change) {
		try {
			ScriptComportamento comportamento =  getComportamento();
			if (comportamento != null) {
				comportamento.execute("mouseMoveuRoda", change);
			}
		} catch (Throwable cause) {
			tratarExcecao(cause);
		}
	}

	@Override
	public void setInput(Input input) {

	}

	public boolean isEnableKeyListenning() {
		return enableKeyListenning;
	}

	public void setEnableKeyListenning(boolean enableKeyListenning) {
		this.enableKeyListenning = enableKeyListenning;
		if (enableKeyListenning) {
			Motor.obterInstancia().getAppGameContainer().getInput()
					.addListener(this);
		} else {
			Motor.obterInstancia().getAppGameContainer().getInput()
					.removeListener(this);
		}
	}

}
