package br.org.gamexis.plataforma.eventos;

import org.newdawn.slick.GameContainer;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.cena.Cena;

public class EventoControle implements Evento {	
	
	private GameContainer container;
	private int delta;
	private Cena cena;
	private int chave;
	private boolean solto;
	
	@Override
	public Cena getCena() {
		return cena;
	}
	
	@Override
	public Motor getMotor() {
		return Motor.obterInstancia();
	}

	public int getDelta() {
		return delta;
	}

	public void setDelta(int delta) {
		this.delta = delta;
	}

	public int getChave() {
		return chave;
	}

	public void setChave(int chave) {
		this.chave = chave;
	}

	public boolean isSolto() {
		return solto;
	}

	public void setSolto(boolean solto) {
		this.solto = solto;
	}
	
	
}
