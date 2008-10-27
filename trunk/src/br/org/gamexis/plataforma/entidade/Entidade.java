package br.org.gamexis.plataforma.entidade;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.cena.Colisivel;
import br.org.gamexis.plataforma.eventos.Evento;
import br.org.gamexis.plataforma.eventos.EventoAtualizar;
import br.org.gamexis.plataforma.eventos.EventoColisao;
import br.org.gamexis.plataforma.exception.GXException;
import br.org.gamexis.plataforma.script.ScriptComportamento;

public abstract class Entidade {

	private String nome;
	private Colisivel ator;// fundamental para a entidade.
	private ScriptComportamento comportamento;
	private String identificador;
	
	public Entidade() {
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void atualizar(Evento evento) throws GXException {
		try {
			if (evento instanceof EventoColisao) {
				if (comportamento != null) {
					comportamento.execute("colisao", new Object[] { this, evento });
				}
			}
			
			if (evento instanceof EventoAtualizar) {
				if (comportamento != null) {
					comportamento.execute("atualizar", new Object[] { this, Motor.obterInstancia() });
				}
			}
		} catch (Throwable cause) {
			tratarExcecao(cause);
		}
	}

	/**
	 * Executa um comportamento
	 * 
	 * @param metodo
	 * @throws LuaException
	 */
	protected void executarComportamento(String metodo, Object[] parametros)
			throws Exception {
		if (comportamento != null) {
			comportamento.execute(metodo, parametros);
		}
	}

	public Colisivel getAtor() {
		return ator;
	}

	public void setAtor(Colisivel ator) {
		this.ator = ator;
	}

	public ScriptComportamento getComportamento() {
		return comportamento;
	}

	public void setComportamento(ScriptComportamento comportamento) {
		this.comportamento = comportamento;
	}

	public void tratarExcecao(Throwable cause) throws GXException {
		cause.printStackTrace();
	}
	
	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public abstract TipoEntidade getTipo();
	
	@Override
	public void finalize() throws Throwable {
		System.out.println("FINALIZADO(Entidade):" + getNome());
		super.finalize();
	}
	
	public void dispose() {
		try {
			if (comportamento != null) {
				comportamento.execute("finalizar");
			}
		} catch (Exception cause) {
			Motor.obterInstancia().tratarExcecao(cause);
		}
		
		ator.dispose();
		ator.setEntidade(null);
		ator = null;		
		comportamento = null;
	}
}
