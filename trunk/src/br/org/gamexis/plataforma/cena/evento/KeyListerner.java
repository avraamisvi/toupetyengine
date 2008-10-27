package br.org.gamexis.plataforma.cena.evento;

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
	
}

