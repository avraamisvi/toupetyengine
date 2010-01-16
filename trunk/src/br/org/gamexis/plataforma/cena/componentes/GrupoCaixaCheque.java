package br.org.gamexis.plataforma.cena.componentes;

import java.util.HashMap;

/**
 * Grupo de caixas selecionaveis
 * @author abraao
 *
 */
public class GrupoCaixaCheque {
	HashMap<String, CaixaCheque> caixas = new HashMap<String, CaixaCheque>();
	
	public void notificarSelecao(CaixaCheque caixaSelecionada) {
		for(CaixaCheque c : caixas.values()) {
			if(c != caixaSelecionada)
				c.setSelecionado(false);
		}
	}
	
	public void adicionar(CaixaCheque caixa) {
		caixas.put(caixa.getNome(), caixa);
	}
}
