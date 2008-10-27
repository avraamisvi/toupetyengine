package br.org.gamexis.plataforma.entidade;

import org.newdawn.slick.GameContainer;

import br.org.gamexis.plataforma.cena.CenarioIF;

public interface EntidadeControlavel {
	public void atualizarControle(GameContainer container, CenarioIF cena, int delta);
}
