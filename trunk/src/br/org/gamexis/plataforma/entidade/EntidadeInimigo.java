package br.org.gamexis.plataforma.entidade;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.cena.Ator;
import br.org.gamexis.plataforma.cena.Colisivel;
import br.org.gamexis.plataforma.cena.Efeito;
import br.org.gamexis.plataforma.eventos.Evento;
import br.org.gamexis.plataforma.exception.GXException;

public class EntidadeInimigo extends EntidadeAnimada {
	int energiaVital = 120;
	
	public EntidadeInimigo() {
	}

	@Override
	public void atualizar(Evento evento) throws GXException {
		try {
			super.atualizar(evento);				
		} catch(Throwable cause) {
			tratarExcecao(cause);
		}
	}

	@Override
	public void atualizarFisica(int delta) {
		super.atualizarFisica(delta);
	}
	
	@Override
	public TipoEntidade getTipo() {
		return TipoEntidade.inimigo;
	}
	
	public void sofrerDano(Efeito efeito, int dec) {
		sofrerDano(efeito, dec, 0, 30);
	}
	
	public void sofrerDano(Efeito efeito, int dec, float dx, float dy) {
		if(dec > energiaVital)
			energiaVital = 0;
		else
			energiaVital = energiaVital - Math.abs(dec);
		
		Motor.obterInstancia().exibirDanoPadrao(this, dec, dx, dy);
	}
	
	public int getEnergiaVital() {
		return energiaVital;
	}
	
	public void setEnergiaVital(int energiaVital) {
		this.energiaVital = energiaVital;
	}
	
	@Override
	public void setAtor(Colisivel ator) {
		super.setAtor(ator);
		
		((Ator)ator).getCorpo().setSecondExclusionKey(getTipo());
		((Ator)ator).getCorpo().addExclusionKey(getTipo());
	}

}
