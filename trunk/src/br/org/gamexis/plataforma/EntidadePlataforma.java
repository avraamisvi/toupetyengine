package br.org.gamexis.plataforma;

import br.org.gamexis.plataforma.entidade.EntidadeNeutra;
import br.org.gamexis.plataforma.entidade.TipoEntidade;

public class EntidadePlataforma extends EntidadeNeutra {
	@Override
	public TipoEntidade getTipo() {
		return TipoEntidade.plataforma;
	}
}
