package br.org.gamexis.plataforma.eventos;

import org.newdawn.slick.GameContainer;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.cena.Cena;

public class EventoControle implements Evento {	
	
	private GameContainer container;
	private int delta;
	private Cena cena;
	
	public void GXEventoControle() {
		
	}

	@Override
	public Cena getCena() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Motor getMotor() {
		return Motor.obterInstancia();
	}
}
