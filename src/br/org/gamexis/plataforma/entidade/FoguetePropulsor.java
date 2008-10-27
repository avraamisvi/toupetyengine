package br.org.gamexis.plataforma.entidade;

import br.org.gamexis.plataforma.cena.Ator;
import br.org.gamexis.plataforma.eventos.Evento;
import br.org.gamexis.plataforma.exception.GXException;

public class FoguetePropulsor extends EntidadeNeutra {
	
	private EntidadeJogavel jogavel;
	
	public FoguetePropulsor(EntidadeJogavel jogavel) {	
		this.jogavel = jogavel;
	}
	
	@Override
	public TipoEntidade getTipo() {
		return TipoEntidade.propulsor;
	}
	
	@Override
	public void atualizar(Evento evento) throws GXException {
		
		Ator at = (Ator)getAtor();		
		
		at.setX(((Ator)jogavel.getAtor()).getX() - 10);
		at.setY(((Ator)jogavel.getAtor()).getY() - 10);		
	}	
	
		
}
