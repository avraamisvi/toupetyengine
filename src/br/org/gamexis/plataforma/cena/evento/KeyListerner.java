package br.org.gamexis.plataforma.cena.evento;

import java.util.HashMap;

import br.org.gamexis.plataforma.Motor;

/**
 * Listerner para chaves pressionadas
 * @author abraao
 *
 */
public class KeyListerner extends InpuListernerAdapter implements Listerner {
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
		
		if(foiPressionado() && !excluido) {
			comando.execute();
			excluido = true;
			Motor.obterInstancia().getAppGameContainer().getInput().removeListener(this);
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

	@Override
	public void aoAdicionar() {
		Motor.obterInstancia().getAppGameContainer().getInput().addListener(this);
	}

	@Override
	public int getAlvo() {
		return chave;
	}
	
}

