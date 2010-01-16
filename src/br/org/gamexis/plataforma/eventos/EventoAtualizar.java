package br.org.gamexis.plataforma.eventos;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.cena.Cena;

public class EventoAtualizar implements Evento {
	
	private int delta;
	
	public EventoAtualizar(int delta) {
		this.delta = delta;
	}

	public int getDelta() {
		return delta;
	}

	@Override
	public Cena getCena() {
		return Motor.obterInstancia().getCenaAtual();
	}

	@Override
	public Motor getMotor() {
		return Motor.obterInstancia();
	}

}
