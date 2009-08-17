package br.org.gamexis.plataforma.cena.evento;

import java.util.HashMap;

import org.newdawn.slick.Input;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.motor.EntradaPerifericos;

/**
 * Listerner para chaves pressionadas
 * @author abraao
 *
 */
public class KeyListerner implements Listerner {
	private int chave;
	private Comando comando;
	private boolean excluido;
	
	private HashMap<String, Object> variaveis = new HashMap<String, Object>();
	
	public KeyListerner(int chave, Comando comando) {
		this.comando = comando;
		this.chave = chave;
	}
	
	@Override
	public void atualizar(int delta) {
		
		if(EntradaPerifericos.foiPressionado(chave) 
				|| EntradaPerifericos.estaPressionado(chave)) {
			comando.execute();
			excluido = true;
		}
	}

	@Override
	public boolean excluido() {
		return excluido;
	}

	@Override
	public void setVariavel(String nome, Object valor) {
		variaveis.put(nome, valor);
	}
	
	public Object getValorVariavel(String nome){
		return variaveis.get(nome);
	}
	
}

