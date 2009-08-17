package br.org.gamexis.plataforma.entidade;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.eventos.Evento;
import br.org.gamexis.plataforma.eventos.EventoAtualizar;
import br.org.gamexis.plataforma.eventos.EventoColisao;
import br.org.gamexis.plataforma.exception.GXException;

/**
 * Representa uma entidade neutra.
 * 
 * @author abraao
 * 
 */
public class EntidadeNeutra extends EntidadeAnimada {

	public EntidadeNeutra() {
	}

	@Override
	public TipoEntidade getTipo() {
		return TipoEntidade.neutro;
	}

	@Override
	public void atualizar(Evento evento) throws GXException {
		try {
			if (evento instanceof EventoColisao) {
				executarComportamento("colisao", new Object[] { this, evento });
			}

			if (evento instanceof EventoAtualizar) {
				executarComportamento("atualizar", new Object[] { this,
						Motor.obterInstancia() });
			}
		} catch (Throwable cause) {
			tratarExcecao(cause);
		}
	}

	public void atualizarFisica(int delta) {
	}
}
