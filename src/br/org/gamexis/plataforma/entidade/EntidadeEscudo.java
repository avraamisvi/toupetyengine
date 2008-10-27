package br.org.gamexis.plataforma.entidade;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.cena.Ator;
import br.org.gamexis.plataforma.cena.Cenario;
import br.org.gamexis.plataforma.cena.Colisivel;
import br.org.gamexis.plataforma.cena.NivelCena;
import br.org.gamexis.plataforma.cena.componentes.BarraEstado;
import br.org.gamexis.plataforma.entidade.logestado.BarraLogEstado;
import br.org.gamexis.plataforma.entidade.logestado.LogEstado;
import br.org.gamexis.plataforma.eventos.Evento;
import br.org.gamexis.plataforma.exception.GXException;

/**
 * Representa um escudo.
 * 
 * @author abraao
 * 
 */
public class EntidadeEscudo extends Entidade {

	private BarraEstado barraEstado;
	// private GXAnimado animacaoBrilho;
	private EntidadeJogavel jogavel;
	private EntidadeJogavel escudoCopia;
	private String NOME_BARRA_ESCUDO = "escudo";
	private int valorDecrescimo = 2;
	private int valorAcrescimo = 1;
	
	private boolean escudoDestravado = false;// DEFAULT FALSE

	public EntidadeEscudo() {
	}

	@Override
	public void setAtor(Colisivel ator) {
		Ator escudo = (Ator) ator;
		escudo.setVisivel(false);
		super.setAtor(ator);
	}
	
	public void setJogavel(EntidadeJogavel jogavel) {
		this.jogavel = jogavel;
		getAtor().getCorpo().addExcludedBody(jogavel.getAtor().getCorpo());// excluindo
																			// jogavel
	}

	@Override
	public TipoEntidade getTipo() {
		return TipoEntidade.escudo;
	}

	@Override
	public void atualizar(Evento evento) throws GXException {
		super.atualizar(evento);

		Ator atorJogavel = (Ator) jogavel.getAtor();
		Ator escudo = (Ator) getAtor();
		escudo.setX(atorJogavel.getX());
		escudo.setY(atorJogavel.getY());
		
		if(estaHabilitado()) {
			decrementeEnergia(valorDecrescimo);
		} else {
			incrementeEnergia(valorAcrescimo);
		}
		
		if(getEnergia() < 1) {
			desabilitar();
		}
	}

	public int getEnergia() {
		LogEstado painelEstado = jogavel.getPainelEstado();
		BarraLogEstado energia = painelEstado.getBarraEstado(NOME_BARRA_ESCUDO);
		return energia.getAtual();
	}

	
	public void incrementeEnergia(int inc) {
		LogEstado painelEstado = jogavel.getPainelEstado();
		BarraLogEstado energia = painelEstado.getBarraEstado(NOME_BARRA_ESCUDO);
		energia.incrementeAtual(inc);
	}

	public void decrementeEnergia(int dec) {
		LogEstado painelEstado = jogavel.getPainelEstado();
		BarraLogEstado energia = painelEstado.getBarraEstado(NOME_BARRA_ESCUDO);
			energia.decrementeAtual(dec);
	}

	public int getEnergiaMaxima() {
		LogEstado painelEstado = jogavel.getPainelEstado();
		BarraLogEstado energia = painelEstado.getBarraEstado(NOME_BARRA_ESCUDO);
		return energia.getMaximo();
	}

	public void setEnergiaMaxima(int energiaMaxima) {
		LogEstado painelEstado = jogavel.getPainelEstado();
		BarraLogEstado energia = painelEstado.getBarraEstado(NOME_BARRA_ESCUDO);
		energia.setMaximo(energiaMaxima);
	}
	
	public boolean estaHabilitado() {
		Ator escudo = (Ator) getAtor();
		return escudo.estaVisivel() && escudoDestravado;
	}
	
	public void habilitar() {
		if (escudoDestravado) {
			Ator escudo = (Ator) getAtor();
			escudo.setVisivel(true);
			
			Cenario cenario = (Cenario)Motor.obterInstancia().getCenaAtual();
			escudo.removaExclusao();
			configureAnimacaoAtor("ATIVADO", escudo);
//			cenario.removaExclusao(escudo);
			
			if(!cenario.contemAtor(escudo)) {
				cenario.adicionarAtor(escudo, NivelCena.frente, NivelCena.plataforma);
			} else {
				cenario.forceAdicionarMundoFisico(escudo, NivelCena.plataforma);
				escudo.setVisivel(true);
			}
		}
	}

	public void configureAnimacaoAtor(String nome, Ator escudo) {
		switch(escudo.getFace()) {
		case direita:
		case direitaAbaixo:
		case direitaAcima:
			escudo.configureAnimacao((nome+"_direita").toUpperCase());
			break;
		case esquerda:
		case esquerdaAbaixo:
		case esquerdaAcima:
			escudo.configureAnimacao((nome+"_esquerda").toUpperCase());
			break;
		}
	}
	
	
	public void desabilitar() {
		if (escudoDestravado) {
			Ator escudo = (Ator) getAtor();
			configureAnimacaoAtor("desativado", escudo);
						
			if(Motor.obterInstancia().getCenaAtual() instanceof Cenario) {
				Cenario cenario = (Cenario)Motor.obterInstancia().getCenaAtual();
				if(cenario != null)
					cenario.forceExcluirColisivelMundoFisico(escudo);
			}
			
			escudoDestravado = true;
			escudo.setVisivel(false);
		}
	}

	public boolean estaEscudoDestravado() {
		return escudoDestravado;
	}

	public void destravar() {
		escudoDestravado = true;
		Ator escudo = (Ator) getAtor();
		escudo.setVisivel(false);
	}

	public void travar() {
		escudoDestravado = false;
	}
	
	
	public void setValorAcrescimo(int valorAcrescimo) {
		this.valorAcrescimo = valorAcrescimo;
	}
	
	public void setValorDecrescimo(int valorDecrescimo) {
		this.valorDecrescimo = valorDecrescimo;
	}	
	
}
