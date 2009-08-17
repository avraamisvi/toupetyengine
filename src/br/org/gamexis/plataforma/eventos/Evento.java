package br.org.gamexis.plataforma.eventos;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.cena.Cena;

/**
 * Representa um evento.
 * @author abraao
 *
 */
public interface Evento {
	Cena getCena();
	Motor getMotor();
}
