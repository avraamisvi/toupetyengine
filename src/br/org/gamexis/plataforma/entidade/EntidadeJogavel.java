package br.org.gamexis.plataforma.entidade;

import net.phys2d.math.Vector2f;

import org.lwjgl.Sys;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.InputListener;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.cena.Ator;
import br.org.gamexis.plataforma.cena.AtorFaceOrientacao;
import br.org.gamexis.plataforma.cena.Cena;
import br.org.gamexis.plataforma.cena.Cenario;
import br.org.gamexis.plataforma.cena.CenarioIF;
import br.org.gamexis.plataforma.cena.Colisivel;
import br.org.gamexis.plataforma.cena.Desenho;
import br.org.gamexis.plataforma.cena.Efeito;
import br.org.gamexis.plataforma.cena.efeitos.EfeitoAlfaCor;
import br.org.gamexis.plataforma.entidade.arma.GerenciadorArmas;
import br.org.gamexis.plataforma.entidade.logestado.BarraLogEstado;
import br.org.gamexis.plataforma.entidade.logestado.LogEstado;
import br.org.gamexis.plataforma.entidade.logestado.Municao;
import br.org.gamexis.plataforma.eventos.Evento;
import br.org.gamexis.plataforma.eventos.EventoAtualizar;
import br.org.gamexis.plataforma.eventos.EventoColisao;
import br.org.gamexis.plataforma.exception.GXException;
import br.org.gamexis.plataforma.motor.EntradaPerifericos;
import br.org.gamexis.plataforma.motor.RecursosFactory;
import br.org.gamexis.plataforma.motor.VariaveisAmbiente;
import br.org.gamexis.plataforma.motor.configuracao.Controles;

/**
 * Representa uma entidade.
 * 
 * @author abraao
 * 
 */
public class EntidadeJogavel extends EntidadeAnimada implements
		EntidadeControlavel, InputListener {
	public static int TEMPO_MAX_DANO_INVENCIVEL = 1000;
	public float desacelerador = 1;

	private int vidas;
	private int vidasMaxima;
	private boolean controlavel = true;
	private float alturaMaximoPuloIncrementar = 30;
	private float alturaIncialAntesPulo = 0;
	private float impulsoPulo = -0.3f;

	private boolean invencivel = false;
	private long tempoMaximoInvencivel = 0;
	private long tempoInicialInvencibilidade = 0;

	private boolean armaTravada = false;

	private GerenciadorArmas armas = new GerenciadorArmas();

	private EntidadeArma arma;
	private FoguetePropulsor foguete = new FoguetePropulsor();
	private LogEstado painelEstado = new LogEstado();
	private boolean sofrendoDano = false;
	private boolean teleporteHabilitado = false;
	private boolean teleporteDestravado = false;

	// ESCUDO
	private EntidadeEscudo escudo;
	private boolean ultimoEstadoEscudoVisivel = false;// usado no teleporte

	private boolean bazucahabilitada = false;
	private boolean armaFogoHabilitada = false;
	private boolean armaGeloHabilitada = false;

	private String animacaoInicial;
	private AtorFaceOrientacao orientacaoInicial;

	
	//ACOES
	private boolean pularApertado;
	private boolean pularHabilitado = true;
	private boolean dispararApertado;
	private boolean direitaApertado;
	private boolean cimaApertado;
	private boolean baixoApertado;
	private boolean esquerdaApertado;
	private boolean correrApertado;
	private boolean voarApertado;
	private boolean teleportarApertado;
	private boolean dispararArmaAposTeleporte = false;
	
	public EntidadeJogavel() {
	}

	public int getVidas() {
		return vidas;
	}

	public void setVidas(int vidas) {
		this.vidas = vidas;
	}

	@Override
	public void setAtor(Colisivel ator) {
		super.setAtor(ator);
		animacaoInicial = ((Ator) ator).getNomeAnimacao();
	}

	public int getVidasMaxima() {
		return vidasMaxima;
	}

	public void setVidasMaxima(int vidasMaxima) {
		this.vidasMaxima = vidasMaxima;
	}

	public int getEnergiaVital() {
		BarraLogEstado energia = painelEstado.getBarraEstado("energia");
		return energia.getAtual();
	}

	public void setEnergiaVital(int energiaVital) {

		BarraLogEstado energia = painelEstado.getBarraEstado("energia");
		int dec = energiaVital - energia.getAtual();

		if (dec < 0) {
			energia.decrementeAtual(Math.abs(dec));
		} else {
			energia.incrementeAtual(Math.abs(dec));
		}

	}

	public void incrementeEnergiaVital(int inc) {

		inc = Math.abs(inc);
		BarraLogEstado energia = painelEstado.getBarraEstado("energia");
		energia.incrementeAtual(inc);

		Motor.obterInstancia().exibirGanhoPadrao(this, inc);
	}

	public void decrementeEnergiaVital(int dec) {
		if (!invencivel && !escudo.estaHabilitado()) {
			BarraLogEstado energia = painelEstado.getBarraEstado("energia");
			energia.decrementeAtual(dec);
		} else {
			escudo.decrementeEnergia(dec);
		}
	}

	public int getEnergiaVitalMaxima() {
		BarraLogEstado energia = painelEstado.getBarraEstado("energia");
		return energia.getMaximo();
	}

	public void setEnergiaVitalMaxima(int energiaVitalMaxima) {
		BarraLogEstado energia = painelEstado.getBarraEstado("energia");
		energia.setMaximo(energiaVitalMaxima);
	}

	public EntidadeArma getArma() {
		return arma;
	}

	public void setArma(EntidadeArma arma) {
		this.arma = armas.selecionar(arma.getNome());
	}

	public void setControlavel(boolean controlavel) {
		this.controlavel = controlavel;
	}

	public boolean isControlavel() {
		return controlavel;
	}

	@Override
	public TipoEntidade getTipo() {
		return TipoEntidade.jogavel;
	}

	@Override
	public void atualizar(Evento evento) throws GXException {
		Ator jogavel = (Ator) getAtor();

		
		if (Motor.obterInstancia().getAppGameContainer().getInput().isKeyPressed(Input.KEY_9)) {
			cheat();
		}
		
		// diminui travar no ar embaixo de um objeto
		if (evento instanceof EventoColisao) {
			EventoColisao coli = (EventoColisao) evento;

			if (coli.getFonte().getAtor().getCorpo().getPosition().getY() <= getAtor()
					.getCorpo().getPosition().getY()) {
				jogavel.setCaindo(true);
			} else if (coli.getFonte().getAtor().getCorpo().getPosition()
					.getY() > getAtor().getCorpo().getPosition().getY()) {
				foguete.autoIncrementa = true;
				foguete.descarregado = false;
				jogavel.setQuicando(true);
				configureAnimacaoPorNome("QUICANDO");
				jogavel.zerarVelocidadeY();
			}
		}
		
		//ATUALIZANDO CONTROLES
		acaoDirecionar();
		atualizarControle();
		
		super.atualizar(evento);

		arma.atualizar(evento);
		escudo.atualizar(evento);

		if (jogavel.getNomeAnimacao().contains("VOANDO")) {
			if (painelEstado.getBarraEstado("combustivel").getAtual() <= 0) {
				foguete.descarregado = true;
				foguete.autoIncrementa = false;
				jogavel.setCaindo(true);
				configureAnimacaoPorNome("CAINDO");
			}
		}

		if (getEnergiaVital() <= 0) {// MORTO?
			setControlavel(false);

			if (getFaceOrientacao().equals(AtorFaceOrientacao.direita)
					|| getFaceOrientacao().equals(
							AtorFaceOrientacao.direitaAcima)
					|| getFaceOrientacao().equals(
							AtorFaceOrientacao.direitaAbaixo)) {

				jogavel.configureAnimacao("MORRENDO_DIREITA");
			} else if (getFaceOrientacao().equals(AtorFaceOrientacao.esquerda)
					|| getFaceOrientacao().equals(
							AtorFaceOrientacao.esquerdaAcima)
					|| getFaceOrientacao().equals(
							AtorFaceOrientacao.esquerdaAbaixo)) {

				jogavel.configureAnimacao("MORRENDO_ESQUERDA");
			}
		}

		if (invencivel) {// controle a invencibilidade
			long tempoInv = Sys.getTime();

			if (tempoInv - tempoInicialInvencibilidade >= tempoMaximoInvencivel) {
				tempoMaximoInvencivel = 0;
				tempoInicialInvencibilidade = 0;

				invencivel = false;
			}
		}
		
		setTeleporteHabilitado(false);
	}

	@Override
	public void setFaceOrientacao(AtorFaceOrientacao face) {
		Ator jogavel = (Ator) getAtor();
		jogavel.setFace(face);

		super.setFaceOrientacao(face);
		if (arma != null) {
			arma.setFaceOrientacao(face);
		}

		if (orientacaoInicial == null)
			orientacaoInicial = face;
	}

	// TODO A titulo de teste, empurra jogavel para frente no ar
	public void aplicarDirecionalNoAr(Ator jogavel) {

		if (direitaApertado) {
			jogavel.setFace(AtorFaceOrientacao.direita);
			setFaceOrientacao(AtorFaceOrientacao.direita);

			if (jogavel.getCorpo().getVelocity().getX() <= 16f) {
				jogavel.getCorpo().adjustVelocity(new Vector2f(8f, 0));
			}

		} else if (esquerdaApertado) {
			jogavel.setFace(AtorFaceOrientacao.esquerda);
			setFaceOrientacao(AtorFaceOrientacao.esquerda);

			if (jogavel.getCorpo().getVelocity().getX() >= -16f) {
				jogavel.getCorpo().adjustVelocity(new Vector2f(-8f, 0));
			}
		}

		if (jogavel.getNomeAnimacao().contains("SUBINDO")) {
			configureAnimacaoPorNome("SUBINDO");
		} else if (jogavel.getNomeAnimacao().contains("CAINDO")) {
			configureAnimacaoPorNome("CAINDO");
		}
	}

	public void cheat() {
		// DEPURAÇÂO
		if (Motor.obterInstancia().isModoDebug()) {
			habilitaFoguetePropulsor(true);
			setTeleporteDestravado(true);

			Cenario cenaAtual = (Cenario) Motor.obterInstancia().getCenaAtual();
			cenaAtual.getPainelEstado().getBarraEstado("combustivel")
					.setVisivel(true);

			getEscudo().destravar();
			cenaAtual.getPainelEstado().getBarraEstado("escudo").setVisivel(
					true);

			setBazucahabilitada(true);
			setArmaFogoHabilitada(true);
			setArmaGeloHabilitada(true);
			getArma().configureModoAcumulador();
		}
	}
	
	private void acaoEscudo() {
		Ator jogavel = (Ator) getAtor();

		if (escudo.estaHabilitado()) {
			escudo.desabilitar();
		} else {
			escudo.habilitar();
		}
	}
	
	private void acaoProximaArma() {
		Ator jogavel = (Ator) getAtor();

			Cenario cenaAtual = (Cenario) Motor.obterInstancia().getCenaAtual();
			cenaAtual.forceExcluirDesenho((Desenho) arma.getAtor());

			arma = armas.proximo();
			arma.setFaceOrientacao(getFaceOrientacao());

			cenaAtual.adicionarMeio((Desenho) arma.getAtor());

			painelEstado.setArma(arma.getNome());
	}
	
	private void acaoArmaAnterior() {
		Ator jogavel = (Ator) getAtor();

			Cenario cenaAtual = (Cenario) Motor.obterInstancia().getCenaAtual();
			cenaAtual.forceExcluirDesenho((Desenho) arma.getAtor());

			arma = armas.anterior();
			arma.setFaceOrientacao(getFaceOrientacao());
			cenaAtual.adicionarMeio((Desenho) arma.getAtor());

			painelEstado.setArma(arma.getNome());
	}
	
	public void atualizarControle() {

		if (getEnergiaVital() <= 0)
			return;

		if (!isControlavel()) {
			return; 
		}

		Ator jogavel = (Ator) getAtor();
		
		if(pularApertado) {
			acaoPular();
		} 
		
		if(dispararApertado && getArma().modoAcumulador()) {
			acaoDisparar(false);
		}
		
		if(voarApertado) {
			acaoVoar();
		}
		
		if(teleportarApertado) {
			acaoTeleporte();
		}
		
		if(dispararArmaAposTeleporte) {
			dispararArmaAposTeleporte = false;
			acaoDisparar(true);
		}
	}

	private void acaoVoar() {
		Ator jogavel = (Ator) getAtor();


		if (voarApertado) {// VOANDO
			BarraLogEstado comb = painelEstado.getBarraEstado("combustivel");

			if (!foguete.descarregado && foguete.habilitado
					&& !jogavel.isQuicando()) {
				if (comb.getAtual() > 0) {
					if (!jogavel.isNoar()) {

						if (!jogavel.isVoando()) {
							jogavel.getCorpo().getVelocity().getY();
							jogavel.getCorpo().adjustVelocity(
									new Vector2f(0, -jogavel.getCorpo()
											.getVelocity().getY()));
						}

						if (isFaceDireita()) {
							jogavel.configureAnimacao("VOANDO_DIREITA");
						} else {
							jogavel.configureAnimacao("VOANDO_ESQUERDA");
						}

						jogavel.setVoando(true);
					}

					if (jogavel.isVoando()) {
						if (jogavel.getCorpo().getVelocity().getY() >= foguete.fogueteMaximaVelocidade) {
							jogavel
									.getCorpo()
									.adjustVelocity(
											new Vector2f(0,
													foguete.inpulsoPuloFoguete));
						}
						comb.decrementeAtual(foguete.consumo);
					}
				} else {
					foguete.descarregado = true;
					foguete.autoIncrementa = false;
					jogavel.setCaindo(true);
				}
			}
		} else if (jogavel.isVoando()) {
			foguete.autoIncrementa = false;
			jogavel.setCaindo(true);
			configureAnimacaoPorNome("CAINDO");
		}		
	}
	
	private void acaoDisparar(boolean solto) {
		Ator jogavel = (Ator) getAtor();

		if (!armaTravada) {
			if (((EntidadeJogavel) jogavel.getEntidade()).getArma()
					.modoAcumulador()) {
				if (!solto) {// ACUMULAR
					((EntidadeJogavel) jogavel.getEntidade()).getArma().acumular();
				} else {
					((EntidadeJogavel) jogavel.getEntidade()).getArma()
							.disparar((CenarioIF) Motor.obterInstancia().getCenaAtual());
				}
			} else if (((EntidadeJogavel) jogavel.getEntidade()).getArma()
					.modoNormal()) {
				if (!solto) {
					((EntidadeJogavel) jogavel.getEntidade()).getArma()
							.disparar((CenarioIF) Motor.obterInstancia().getCenaAtual());
				}
			} else if (((EntidadeJogavel) jogavel.getEntidade()).getArma()
					.modoMetralhadora()) {
					((EntidadeJogavel) jogavel.getEntidade()).getArma()
							.disparar((CenarioIF) Motor.obterInstancia().getCenaAtual());
			}
		}		
	}
	
//	public boolean travarPular = false;
	
	private void acaoPular() {
		Ator jogavel = (Ator) getAtor();

			if (!jogavel.isPulando()) {/* FLAG PRA HABILITAR PULOS NO AR */
				if (!jogavel.isCaindo()) {
					if (!jogavel.isNoar()) {
						if (!jogavel.isVoando()) {
							if (jogavel.getFace().equals(AtorFaceOrientacao.direita)) {
								jogavel.configureAnimacao("PULANDO_DIREITA");
							} else {
								jogavel.configureAnimacao("PULANDO_ESQUERDA");
							}
							alturaIncialAntesPulo = jogavel.getY();
							
							jogavel.setPulando(true);
							jogavel.getCorpo().adjustVelocity(new Vector2f(0, impulsoPulo));
						}
					}
				}
			} 
			
			if (jogavel.isNoar() && !jogavel.isCaindo()) {
				if (Math.abs(alturaIncialAntesPulo - jogavel.getY()) <= alturaMaximoPuloIncrementar) {
					jogavel.getCorpo().adjustVelocity(new Vector2f(0, impulsoPulo));
				}
			}

	}
	
	private void acaoTeleporte() {
		
		Ator jogavel = (Ator) getAtor();
		
		// TELEPORTANDO
		if (teleporteHabilitado && teleporteDestravado) {
				boolean teleportando = false;

				if (EntradaPerifericos.direitaEstaPressionado()) {

					teleportando = true;
					jogavel.configureAnimacao("TELEPORTE_DIREITA");
					VariaveisAmbiente.inserirVariavel("destino", "DIREITA");
				} else if (EntradaPerifericos.esquerdaEstaPressionado()) {

					teleportando = true;
					jogavel.configureAnimacao("TELEPORTE_ESQUERDA");
					VariaveisAmbiente.inserirVariavel("destino", "ESQUERDA");
				} else if (EntradaPerifericos.baixoEstaPressionado()) {

					teleportando = true;
					jogavel.configureAnimacao("TELEPORTE_ABAIXO");
					VariaveisAmbiente.inserirVariavel("destino", "ABAIXO");
				} else if (EntradaPerifericos.cimaEstaPressionado()) {

					teleportando = true;
					jogavel.configureAnimacao("TELEPORTE_ACIMA");
					VariaveisAmbiente.inserirVariavel("destino", "ACIMA");
				}

				if (teleportando) {
					setTrancarAnimacao(true);
					setControlavel(false);
					return;
				}
		}
	}
	
	//RESETA OS FLAGS DE CONTROLE DE COMANDO
	public void limparTeleportando() {
		pularApertado = false;
		pularHabilitado = true;
		direitaApertado = false;
		cimaApertado = false;
		baixoApertado = false;
		esquerdaApertado = false;
		correrApertado = false;
		voarApertado = false;
		teleportarApertado = false;
		dispararArmaAposTeleporte = dispararApertado;
		dispararApertado = false;
	}
	
	private void acaoDirecionar() {
		
		Ator jogavel = (Ator) getAtor();
		
		if(teleportarApertado && teleporteHabilitado && teleporteDestravado)
			return;
		
		if (!jogavel.isCaindo() && !jogavel.isQuicando()
				&& !jogavel.isPulando() && !jogavel.isNoar()
				&& !jogavel.isVoando()) {

			if (direitaApertado) {// ANDAR
				jogavel.setAndando(true);//
				if (correrApertado) {
					if (!jogavel.getNomeAnimacao().equals("CORRENDO_DIREITA")) {
						jogavel.configureAnimacao("CORRENDO_DIREITA");
						jogavel.setFace(AtorFaceOrientacao.direita);
						setFaceOrientacao(AtorFaceOrientacao.direita);
					}

					if (jogavel.getCorpo().getVelocity().getX() <= 32f) {
						jogavel.getCorpo().adjustVelocity(new Vector2f(8f, 0));
					}
				} else {
					if (!jogavel.getNomeAnimacao().equals("ANDANDO_DIREITA")) {
						jogavel.configureAnimacao("ANDANDO_DIREITA");
						jogavel.setFace(AtorFaceOrientacao.direita);
						setFaceOrientacao(AtorFaceOrientacao.direita);
					}

					if (jogavel.getCorpo().getVelocity().getX() <= 16f) {
						jogavel.getCorpo().adjustVelocity(new Vector2f(8f, 0));
					}
				}

			} else if (esquerdaApertado) {
				jogavel.setAndando(true);
				if (correrApertado) {
					if (!jogavel.getNomeAnimacao().equals("CORRENDO_ESQUERDA")) {
						jogavel.configureAnimacao("CORRENDO_ESQUERDA");
						jogavel.setFace(AtorFaceOrientacao.esquerda);
						setFaceOrientacao(AtorFaceOrientacao.esquerda);
					}

					if (jogavel.getCorpo().getVelocity().getX() >= -32f) {
						jogavel.getCorpo().adjustVelocity(new Vector2f(-8f, 0));
					}
				} else {
					if (!jogavel.getNomeAnimacao().equals("ANDANDO_ESQUERDA")) {
						jogavel.configureAnimacao("ANDANDO_ESQUERDA");
						jogavel.setFace(AtorFaceOrientacao.esquerda);
						setFaceOrientacao(AtorFaceOrientacao.esquerda);
					}

					if (jogavel.getCorpo().getVelocity().getX() >= -16f) {
						jogavel.getCorpo().adjustVelocity(new Vector2f(-8f, 0));
					}
				}
			} else {
				jogavel.setParado(true);
			}
		} else if (!jogavel.isCaindo() && !jogavel.isQuicando()
				&& !jogavel.isVoando()) {
			aplicarDirecionalNoAr(jogavel);
		}

		if (cimaApertado) {

			if ((getFaceOrientacao().equals(AtorFaceOrientacao.direita) || getFaceOrientacao()
					.equals(AtorFaceOrientacao.direitaAbaixo))
					&& !getFaceOrientacao().equals(
							AtorFaceOrientacao.direitaAcima)) {

				setFaceOrientacao(AtorFaceOrientacao.direitaAcima);
			} else if ((getFaceOrientacao().equals(AtorFaceOrientacao.esquerda) || getFaceOrientacao()
					.equals(AtorFaceOrientacao.esquerdaAbaixo))
					&& !getFaceOrientacao().equals(
							AtorFaceOrientacao.esquerdaAcima)) {

				setFaceOrientacao(AtorFaceOrientacao.esquerdaAcima);
			}
		} else if (baixoApertado && (jogavel.isNoar() || jogavel.isPulando() || jogavel.isCaindo())) {
			if ((getFaceOrientacao().equals(AtorFaceOrientacao.direita) || getFaceOrientacao()
					.equals(AtorFaceOrientacao.direitaAcima))
					&& !getFaceOrientacao().equals(
							AtorFaceOrientacao.direitaAbaixo)) {

				if (jogavel.isNoar() || jogavel.isPulando()
						|| jogavel.isCaindo())
					setFaceOrientacao(AtorFaceOrientacao.direitaAbaixo);
				else
					setFaceOrientacao(AtorFaceOrientacao.direita);
			} else if ((getFaceOrientacao().equals(AtorFaceOrientacao.esquerda) || getFaceOrientacao()
					.equals(AtorFaceOrientacao.esquerdaAcima))
					&& !getFaceOrientacao().equals(
							AtorFaceOrientacao.esquerdaAbaixo)) {

				if (jogavel.isNoar() || jogavel.isPulando()
						|| jogavel.isCaindo())
					setFaceOrientacao(AtorFaceOrientacao.esquerdaAbaixo);
				else
					setFaceOrientacao(AtorFaceOrientacao.esquerda);
			}
		} else {
			switch (getFaceOrientacao()) {
			case esquerdaAbaixo:
			case esquerdaAcima:
				setFaceOrientacao(AtorFaceOrientacao.esquerda);
				break;

			case direitaAbaixo:
			case direitaAcima:
				setFaceOrientacao(AtorFaceOrientacao.direita);
				break;
			}
		}
		
		if (jogavel.isCaindo()) {
			if (direitaApertado) {
				if (!jogavel.getNomeAnimacao().equals("CAINDO_DIREITA")) {
					jogavel.setFace(AtorFaceOrientacao.direita);
					setFaceOrientacao(AtorFaceOrientacao.direita);
					configureAnimacao("CAINDO");
				}

				if (jogavel.getCorpo().getVelocity().getX() <= 16f) {
					jogavel.getCorpo().adjustVelocity(new Vector2f(8f, 0));
				}

			} else if (esquerdaApertado) {
				if (!jogavel.getNomeAnimacao().equals("CAINDO_ESQUERDA")) {
					jogavel.setFace(AtorFaceOrientacao.esquerda);
					setFaceOrientacao(AtorFaceOrientacao.esquerda);
					configureAnimacao("CAINDO");
				}

				if (jogavel.getCorpo().getVelocity().getX() >= -16f) {
					jogavel.getCorpo().adjustVelocity(new Vector2f(-8f, 0));
				}
			}
		}
		
		// MUDANDO A DIREÇÂO NO VOO COM O FOGUETE PROPULSOR
		if (jogavel.isVoando() && !jogavel.isCaindo()) {
			if (direitaApertado) {
				if (jogavel.getCorpo().getVelocity().getX() <= foguete.fogueteMaximaVelocidadeX) {
					jogavel.getCorpo().adjustVelocity(
							new Vector2f(foguete.forcaX, 0));
					jogavel.setFace(AtorFaceOrientacao.direita);
					setFaceOrientacao(AtorFaceOrientacao.direita);
				}
			} else if (esquerdaApertado) {
				if (jogavel.getCorpo().getVelocity().getX() >= -foguete.fogueteMaximaVelocidadeX) {
					jogavel.getCorpo().adjustVelocity(
							new Vector2f(-foguete.forcaX, 0));
					jogavel.setFace(AtorFaceOrientacao.esquerda);
					setFaceOrientacao(AtorFaceOrientacao.esquerda);
				}
			}

			if (jogavel.isVoando() && !jogavel.isCaindo()) {

				if (isFaceDireita()) {
					jogavel.configureAnimacao("VOANDO_DIREITA");
				} else {
					jogavel.configureAnimacao("VOANDO_ESQUERDA");
				}
			}
		}		
	}
	
	@Override
	public void atualizarFisica(int delta) {
		Ator jogavel = (Ator) getAtor();

		super.atualizarFisica(delta);

		habilitarPulo();
		
		if (!jogavel.isVoando()) {
			BarraLogEstado comb = painelEstado.getBarraEstado("combustivel");

			if (comb != null) {// TODO VERIFICAR BUG DE BARRA VIR NULO

				if (comb.getAtual() < comb.getMaximo()) {
					if (foguete.autoIncrementa
							|| Motor.obterInstancia().isModoDebug())
						comb.incrementeAtual(foguete.recarga);
				} else {
					foguete.descarregado = false;
				}
			}
		}

		if (isSofrendoDano()) {
			if (jogavel.getAnimacaoAtual().ultimoQuadro()) {
				setSofrendoDano(false);
				setControlavel(true);
				setTrancarAnimacao(false);
			}
		}

		if (invencivel) {
			if (Sys.getTime() - tempoInicialInvencibilidade > tempoMaximoInvencivel) {
				setInvencivel(false, 0);
			}
		}

		if (!jogavel.isNoar() && !jogavel.isPulando() && !jogavel.isAndando()
				&& !jogavel.isCaindo() && !jogavel.isVoando()) {

			if (jogavel.getVelocidadeX() < 0) {
				jogavel.setVelocidadeX(desacelerador);
				if (jogavel.getVelocidadeX() > 0) {
					jogavel.zerarVelocidadeX();
				}
			} else if (jogavel.getVelocidadeX() > 0) {
				jogavel.setVelocidadeX(-desacelerador);
				if (jogavel.getVelocidadeX() < 0) {
					jogavel.zerarVelocidadeX();
				}
			} else {
				jogavel.zerarVelocidadeX();
			}
		}
	}

	/**
	 * Para configurar uma animação pelo nome.
	 * 
	 * @param animNome
	 * @see EntidadeAnimada.configureAnimacaoPorNome
	 */
	@Deprecated
	public void configureAnimacao(String animNome) {
		Ator jogavel = (Ator) getAtor();

		switch (getFaceOrientacao()) {
		case direita:
		case direitaAbaixo:
		case direitaAcima:
			jogavel.configureAnimacao(animNome + "_" + "DIREITA");
			break;
		case esquerda:
		case esquerdaAbaixo:
		case esquerdaAcima:
			jogavel.configureAnimacao(animNome + "_" + "ESQUERDA");
			break;
		}
	}

	public void setImpulsoPulo(float impulsoPulo) {
		this.impulsoPulo = impulsoPulo;
	}

	public void setAlturaMaximoPuloIncrementar(long alturaMaximoPuloIncrementar) {
		this.alturaMaximoPuloIncrementar = alturaMaximoPuloIncrementar;
	}

	class FoguetePropulsor {
		public float inpulsoPuloFoguete = -5f;
		public float forcaX = 1f;
		public int consumo = 10;
		public int recarga = 1;
		public int fogueteMaximaVelocidade = -30;
		public int fogueteMaximaVelocidadeX = 16;
		public boolean habilitado = false;
		public boolean autoIncrementa = true;
		public boolean descarregado = false;
	}

	public int getFogueteCargaMaxima() {
		BarraLogEstado energia = painelEstado.getBarraEstado("combustivel");
		return energia.getMaximo();
	}

	public void setFogueteCargaMaxima(int combustivel) {
		BarraLogEstado energia = painelEstado.getBarraEstado("combustivel");
		energia.setMaximo(combustivel);
	}

	public void setFoguetevelocidadeRecarga(int recarga) {
		foguete.recarga = recarga;
	}

	public void setFogueteAutoIncrementa(boolean auto) {
		foguete.autoIncrementa = auto;
	}

	public boolean isFoguetePropulsorHabilitado() {
		return foguete.habilitado;
	}

	public LogEstado getPainelEstado() {
		return painelEstado;
	}

	public void setInvencivel(boolean invencivel, long tempoMax) {
		this.invencivel = invencivel;
		tempoMaximoInvencivel = tempoMax;

		if (tempoMax > 0)
			tempoInicialInvencibilidade = Sys.getTime();
		else
			invencivel = false;
	}

	public boolean isInvencivel() {
		return invencivel;
	}

	public void setSofrendoDano(boolean sofrendoDano) {
		this.sofrendoDano = sofrendoDano;
	}

	public boolean isSofrendoDano() {
		return sofrendoDano;
	}

	public boolean isTeleporteHabilitado() {
		return teleporteHabilitado;
	}

	public void setTeleporteHabilitado(boolean teleporteHabilitado) {
		this.teleporteHabilitado = teleporteHabilitado;
	}

	public void setArmaVisivel(boolean visivel) {
		((Desenho) arma.getAtor()).setVisivel(visivel);
	}

	public void setEscudo(EntidadeEscudo escudo) {
		this.escudo = escudo;
		escudo.desabilitar();
	}

	public EntidadeEscudo getEscudo() {
		return escudo;
	}

	public boolean escudoHabilitado() {
		return escudo.estaHabilitado();
	}

	public boolean isUltimoEstadoEscudoVisivel() {
		return ultimoEstadoEscudoVisivel;
	}

	public void setUltimoEstadoEscudoVisivel(boolean ultimoEstadoEscudoVisivel) {
		this.ultimoEstadoEscudoVisivel = ultimoEstadoEscudoVisivel;
	}

	public void setEscudoVisivel(boolean visivel) {
		ultimoEstadoEscudoVisivel = escudo.estaHabilitado();

		if (visivel)
			escudo.habilitar();
		else
			escudo.desabilitar();
	}

	public void adicionarArma(EntidadeArma arma) {
		arma.setJogavel(this);
		armas.adicionar(arma.getNome(), arma);
	}

	public void setPainelEstado(LogEstado painelEstado) {
		this.painelEstado = painelEstado;
	}

	public void habilitaFoguetePropulsor(boolean habilitar) {
		foguete.habilitado = habilitar;
	}

	public boolean isBazucahabilitada() {
		return bazucahabilitada;
	}

	public void setBazucahabilitada(boolean bazucahabilitada) {
		this.bazucahabilitada = bazucahabilitada;
		if (bazucahabilitada) {
			armas.habilitar("BAZUCA");
		} else {
			armas.desabilitar("BAZUCA");
		}
	}

	public boolean isArmaFogoHabilitada() {
		return armaFogoHabilitada;
	}

	public void setArmaFogoHabilitada(boolean armaFogoHabilitada) {
		this.armaFogoHabilitada = armaFogoHabilitada;
		if (armaFogoHabilitada) {
			armas.habilitar("EV_FOGO");
		} else {
			armas.desabilitar("EV_FOGO");
		}

	}

	public boolean isArmaGeloHabilitada() {
		return armaGeloHabilitada;
	}

	public void setArmaGeloHabilitada(boolean armaGeloHabilitada) {
		this.armaGeloHabilitada = armaGeloHabilitada;
		if (armaGeloHabilitada) {
			armas.habilitar("EV_GELO");
		} else {
			armas.desabilitar("EV_GELO");
		}
	}

	/**
	 * Sofre dano e aplica efeito.
	 * 
	 * @param dec
	 * @param efeitoAplicado
	 * @param tempoMax
	 * @param vx
	 * @param vy
	 */
	public void sofrerDano(int dec, Efeito efeitoAplicado, int tempoMax,
			int vx, int vy) {
		if (!isInvencivel()) {
			Ator ator = (Ator) getAtor();
			ator.adcionarEfeito(efeitoAplicado);
			decrementeEnergiaVital(dec);

			float vvX = -ator.getVelocidadeX();
			float vvY = -ator.getVelocidadeY();

			if (vvX < 0)
				vx = -vx;

			if (vvY < 0)
				vy = -vy;

			ator.setVelocidadeX(vvX + vx);
			ator.setVelocidadeY(vvY + vy);
			setInvencivel(true, tempoMax);

			Motor.obterInstancia().exibirDanoPadrao(this, dec);
		}

	}

	public void sofrerDano(int dec) {
		EfeitoAlfaCor ef = RecursosFactory.getInstancia()
				.getEfeitoAlfaCorPadrao();
		ef.setDono((Desenho) getAtor());
		sofrerDano(ef, TEMPO_MAX_DANO_INVENCIVEL, dec);
	}

	public void sofrerDano(Efeito efeito, int dec) {
		sofrerDano(efeito, TEMPO_MAX_DANO_INVENCIVEL, dec);
	}

	public void sofrerDano(Efeito efeito, int tempoMax, int dec) {
		if (!isInvencivel()) {
			Ator ator = (Ator) getAtor();
			ator.adcionarEfeito(efeito);
			decrementeEnergiaVital(dec);

			setInvencivel(true, tempoMax);

			Motor.obterInstancia().exibirDanoPadrao(this, dec);
		}
	}

	public void sofrerDano(int dec, int tempoMax, int vx, int vy) {
		if (!isInvencivel()) {
			Ator ator = (Ator) getAtor();
			decrementeEnergiaVital(dec);

			float vvX = -ator.getVelocidadeX();
			float vvY = -ator.getVelocidadeY();

			if (vvX < 0)
				vx = -vx;

			if (vvY < 0)
				vy = -vy;

			ator.setVelocidadeX(vvX + vx);
			ator.setVelocidadeY(vvY + vy);
			setInvencivel(true, tempoMax);

			Motor.obterInstancia().exibirDanoPadrao(this, dec);
		}
	}

	/**
	 * @deprecated
	 * @param maximo
	 */
	public void incrementarMaximoMunicao(int maximo) {
		getPainelEstado().getArma().getMunicao().incrementeMaximo(maximo);
	}

	public Municao getMunicaoArma(String arma) {
		return getPainelEstado().getArma(arma).getMunicao();
	}

	public void setArmaTravada(boolean armaTravada) {
		this.armaTravada = armaTravada;
	}

	public boolean armaTravada() {
		return armaTravada;
	}

	public boolean isTeleporteDestravado() {
		return teleporteDestravado;
	}

	public void setTeleporteDestravado(boolean teleporteDestravado) {
		this.teleporteDestravado = teleporteDestravado;
	}

	/**
	 * Reinicia a entidade para as configurações iniciais
	 */
	public void reiniciar() {
		// TODO verificar se escudo habilitado e usando
		habilitaFoguetePropulsor(false);
		setEnergiaVital(getEnergiaVitalMaxima());
		arma = armas.get("EV");
		painelEstado.setArma(arma.getNome());
		getEscudo().travar();
		Motor.obterInstancia().getPainelEstado().getBarraEstado("combustivel")
				.setVisivel(false);
		Motor.obterInstancia().getPainelEstado().getBarraEstado("escudo")
				.setVisivel(false);
		setBazucahabilitada(false);
		setArmaFogoHabilitada(false);
		setArmaGeloHabilitada(false);
		setControlavel(true);
		setFaceOrientacao(orientacaoInicial);
		configureAnimacao("PARADO");
		arma.setFaceOrientacao(getFaceOrientacao());
	}

	@Override
	public String getIdentificador() {
		return "JOGAVEL";
	}

	@Override
	public void controllerButtonPressed(int controller, int button) {
		int chave = EntradaPerifericos.getInstancia().getControle().traduzir(button);
		
		controlePressionado(chave);
	}

	@Override
	public void controllerButtonReleased(int controller, int button) {
		
		int chave = EntradaPerifericos.getInstancia().getControle().traduzir(button);
		
		controleSolto(chave);
		
	}

	private  void habilitarPulo() {
		if (!((Ator) getAtor()).isNoar() && !((Ator) getAtor()).isPulando() && !pularApertado) {
			pularHabilitado = true;
		}
	}
	
	@Override
	public void controllerDownPressed(int controller) {
		baixoApertado = true;
	}

	@Override
	public void controllerDownReleased(int controller) {
		baixoApertado = false;
	}

	@Override
	public void controllerLeftPressed(int controller) {
		esquerdaApertado = true;
	}

	@Override
	public void controllerLeftReleased(int controller) {
		esquerdaApertado = false;
	}

	@Override
	public void controllerRightPressed(int controller) {
		direitaApertado = true;
	}

	@Override
	public void controllerRightReleased(int controller) {
		direitaApertado = false;
	}

	@Override
	public void controllerUpPressed(int controller) {
		cimaApertado = true;
	}

	@Override
	public void controllerUpReleased(int controller) {
		cimaApertado = false;
	}

	@Override
	public void inputEnded() {
	}

	@Override
	public boolean isAcceptingInput() {
		return isControlavel();
	}

	@Override
	public void keyPressed(int key, char c) {
		int chave = EntradaPerifericos.getInstancia().getControle().traduzir(key);
		
		if(chave == Controles.CIMA) {
			cimaApertado = true;
		} else if(chave == Controles.BAIXO) {
			baixoApertado = true;
		} else if(chave == Controles.DIREITA) {
			direitaApertado = true;
		} else if(chave == Controles.ESQUERDA) {
			esquerdaApertado = true;
		}
		
		controlePressionado(chave);	
	}

	private void controlePressionado(int chave) {
		if(chave == Controles.T_S) {
			acaoDisparar(false);
			dispararApertado = true;
		}else if(chave == Controles.T_A) {
			if(!pularApertado && pularHabilitado) {
				acaoPular();
				pularApertado = true;
				pularHabilitado = false;
			}
		} else if(chave == Controles.T_CTRL) {
			correrApertado = true;
		} else if(chave == Controles.T_Z) {
			voarApertado = true;
		} else if(chave == Controles.T_C) {
			acaoEscudo();
		} else if(chave == Controles.T_F) {
			acaoProximaArma();
		} else if(chave == Controles.T_D) {
			acaoArmaAnterior();
		}else if(chave == Controles.T_X) {
			teleportarApertado = true;
		}
	}

	@Override
	public void keyReleased(int key, char c) {
		int chave = EntradaPerifericos.getInstancia().getControle().traduzir(key);
		
		if(chave == Controles.CIMA) {
			cimaApertado = false;
		} else if(chave == Controles.BAIXO) {
			baixoApertado = false;
		} else if(chave == Controles.DIREITA) {
			direitaApertado = false;
		} else if(chave == Controles.ESQUERDA) {
			esquerdaApertado = false;
		}
		
		controleSolto(chave);
	}

	private void controleSolto(int chave) {
		if(chave == Controles.T_S) {
			acaoDisparar(true);
			dispararApertado = false;
		} else if(chave == Controles.T_A) {
			pularApertado = false;
			habilitarPulo();
		} else if(chave == Controles.T_CTRL) {
			correrApertado = false;
		} else if(chave == Controles.T_Z) {
			voarApertado = false;
		} else if(chave == Controles.T_X) {
			teleportarApertado = false;
		}
	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		
	}

	@Override
	public void mouseWheelMoved(int change) {
		
	}

	@Override
	public void setInput(Input input) {
		
	}
}
