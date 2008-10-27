package br.org.gamexis.plataforma.cena.evento;

import org.lwjgl.Sys;

public class TemporizadorListerner implements Listerner {

	private long tempoInicial = 0;
	private long tempoMaximo = 200;
	private long passado = 0;
	private Comando comando = null;
	private boolean excluido = false;
	
	public TemporizadorListerner(long tempoMaximo,
			Comando comando) {
		
		this.tempoInicial = System.currentTimeMillis();
		this.tempoMaximo = tempoMaximo;
		this.comando = comando;
	}

	@Override
	public void atualizar(int delta) {
		passado += delta;
		if((tempoMaximo - passado) <= 0) {
			excluido = true;
			comando.execute();
		}
	}

	@Override
	public boolean excluido() { 
		return excluido;
	}

}
