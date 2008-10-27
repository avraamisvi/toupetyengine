package br.org.gamexis.plataforma.entidade;

import net.phys2d.math.Vector2f;

import org.lwjgl.Sys;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.cena.Ator;
import br.org.gamexis.plataforma.cena.AtorFaceOrientacao;
import br.org.gamexis.plataforma.cena.Cenario;
import br.org.gamexis.plataforma.cena.CenarioIF;
import br.org.gamexis.plataforma.cena.Colisivel;
import br.org.gamexis.plataforma.cena.Desenho;
import br.org.gamexis.plataforma.cena.Efeito;
import br.org.gamexis.plataforma.entidade.arma.GerenciadorArmas;
import br.org.gamexis.plataforma.entidade.logestado.BarraLogEstado;
import br.org.gamexis.plataforma.entidade.logestado.LogEstado;
import br.org.gamexis.plataforma.entidade.logestado.Municao;
import br.org.gamexis.plataforma.eventos.Evento;
import br.org.gamexis.plataforma.eventos.EventoColisao;
import br.org.gamexis.plataforma.exception.GXException;
import br.org.gamexis.plataforma.motor.EntradaPerifericos;
import br.org.gamexis.plataforma.motor.VariaveisAmbiente;
import br.org.gamexis.plataforma.motor.configuracao.Controles;

/**
 * Representa uma entidade.
 * 
 * @author abraao
 * 
 */
public class EntidadeJogavel extends EntidadeAnimada implements
		EntidadeControlavel {
	public static int TEMPO_MAX_DANO_INVENCIVEL = 1000;
	private float atritoPadrao = 0;
	private int vidas;
	private int vidasMaxima;
	private boolean controlavel = true;
	private float alturaMaximoPuloIncrementar = 30;
	private float alturaIncialAntesPulo = 0;
	private float impulsoPulo = -1f;

	private boolean invencivel = false;
	private long tempoMaximoInvencivel = 0;
	private long tempoInicialInvencibilidade = 0;

	private boolean armaTravada = false;

	//private HashMap<String, EntidadeArma> armas = new HashMap<String, EntidadeArma>();// armas
	private GerenciadorArmas armas = new GerenciadorArmas();
	
	// para acessar sem ordem e diminuir o processamento
	// private List<GXEntidadeArma> listaArmas = new
	// ArrayList<GXEntidadeArma>();

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
		atritoPadrao = ator.getCorpo().getFriction();
		animacaoInicial = ((Ator)ator).getNomeAnimacao();
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

		if (dec < 0)
			energia.decrementeAtual(Math.abs(dec));
		else
			energia.incrementeAtual(Math.abs(dec));

	}

	public void incrementeEnergiaVital(int inc) {
		BarraLogEstado energia = painelEstado.getBarraEstado("energia");
		energia.incrementeAtual(inc);
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
//		arma.setJogavel(this);
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
		
		if (getAtor().getCorpo().getVelocity().getY() > 0f) {
			if(getAtor().getCorpo().getFriction() > 0f) {
				getAtor().getCorpo().setFriction(0f);
			}
		} 
//		else {
//			getAtor().getCorpo().setFriction(atritoPadrao);
//		}
		
		if (evento instanceof EventoColisao) {//evita travar no ar embaixo de um objeto
			EventoColisao coli = (EventoColisao)evento;
//			Ator jogavel = (Ator) getAtor();
			
			if(coli.getFonte().getAtor().getCorpo().getPosition().getY() 
					<= getAtor().getCorpo().getPosition().getY()) {				
				jogavel.setCaindo(true);
			}
		}
		
		//PARA REGULAR O ATRITO EVITANDO O BUG DE ESCORREGAR INFINITaMENTE
		 if(jogavel.getVelocidadeX() != 0) {
			if(jogavel.getNomeAnimacao().contains("PARADO")) {
				jogavel.getCorpo().setFriction(atritoPadrao);
			}
		}
		
		super.atualizar(evento);

		arma.atualizar(evento);
		escudo.atualizar(evento);
		if (evento instanceof EventoColisao) {
			foguete.descarregado = false;// TODO CRIAR TIPO ESPECIFICOS PARA
			// CHAO E ETC
			EventoColisao e = (EventoColisao)evento;

			if(atritoPadrao > 0) {
				if(e.getNormalY() <= -1f) {
					getAtor().getCorpo().setFriction(atritoPadrao);
				}
			}
		}

		if (getEnergiaVital() <= 0) {// MORTO?
			setControlavel(false);
			//((Ator)getAtor()).desabilitarColisao();
			
//			Ator jogavel = (Ator) getAtor();

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
	}

	@Override
	public void setFaceOrientacao(AtorFaceOrientacao face) {
		Ator jogavel = (Ator)getAtor();
		jogavel.setFace(face);
		
		super.setFaceOrientacao(face);
		arma.setFaceOrientacao(face);
		
		if(orientacaoInicial == null)
			orientacaoInicial = face;
	}

	public void atualizarControle(GameContainer container, CenarioIF cena,
			int delta) {

		if(getEnergiaVital() <= 0)
			return;
		
		if (!isControlavel())
			return;

		Ator jogavel = (Ator) getAtor();

//		Input entrada = container.getInput();
		
		// TELEPORTANDO
		if (teleporteHabilitado && teleporteDestravado) {
			if (EntradaPerifericos.estaPressionado(Controles.T_X)) {
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

		if (!jogavel.isCaindo() && !jogavel.isQuicando()
				&& !jogavel.isPulando() && !jogavel.isNoar()
				&& !jogavel.isVoando()) {

			if (EntradaPerifericos.direitaEstaPressionado()) {// ANDAR
				jogavel.setAndando(true);//
				if (EntradaPerifericos.estaPressionado(Controles.T_CTRL)) {
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

			} else if (EntradaPerifericos.esquerdaEstaPressionado()) {
				jogavel.setAndando(true);
				if (EntradaPerifericos.estaPressionado(Controles.T_CTRL)) {
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
		}

		if (EntradaPerifericos.cimaEstaPressionado()) {

			if ((getFaceOrientacao().equals(AtorFaceOrientacao.direita) || getFaceOrientacao().equals( AtorFaceOrientacao.direitaAbaixo))
					&& !getFaceOrientacao().equals(AtorFaceOrientacao.direitaAcima)) {
				
				setFaceOrientacao(AtorFaceOrientacao.direitaAcima);
			} else if ((getFaceOrientacao().equals(AtorFaceOrientacao.esquerda)
					|| getFaceOrientacao().equals(AtorFaceOrientacao.esquerdaAbaixo)) 
					&& !getFaceOrientacao().equals(AtorFaceOrientacao.esquerdaAcima)) {

				setFaceOrientacao(AtorFaceOrientacao.esquerdaAcima);
			}
		} else if (EntradaPerifericos.baixoEstaPressionado() && (jogavel.isNoar() || jogavel.isPulando()	|| jogavel.isCaindo())) {
			if ((getFaceOrientacao().equals(AtorFaceOrientacao.direita)
					|| getFaceOrientacao().equals(AtorFaceOrientacao.direitaAcima))
					&& !getFaceOrientacao().equals(AtorFaceOrientacao.direitaAbaixo)) {

				if (jogavel.isNoar() || jogavel.isPulando()	|| jogavel.isCaindo())
					setFaceOrientacao(AtorFaceOrientacao.direitaAbaixo);
				else
					setFaceOrientacao(AtorFaceOrientacao.direita);
			} else if ((getFaceOrientacao().equals(AtorFaceOrientacao.esquerda)
					|| getFaceOrientacao().equals(AtorFaceOrientacao.esquerdaAcima))
					&& !getFaceOrientacao().equals(AtorFaceOrientacao.esquerdaAbaixo)) {

				if (jogavel.isNoar() || jogavel.isPulando()	|| jogavel.isCaindo())
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
//			arma.normalizarArma();// NORMALIZA ARMA
		}

		if (EntradaPerifericos.foiPressionado(Controles.T_A)) {
			if (!jogavel.isPulando()) {/* FLAG PRA HABILITAR PULOS NO AR */
				if (!jogavel.isCaindo()) {
					if (!jogavel.isNoar()) {
						if (!jogavel.isVoando()) {
							if (jogavel.getFace().equals(
									AtorFaceOrientacao.direita)) {
								jogavel.configureAnimacao("PULANDO_DIREITA");
							} else {
								jogavel.configureAnimacao("PULANDO_ESQUERDA");
							}
							jogavel.setPulando(true);							
							getAtor().getCorpo().setFriction(0f);
							alturaIncialAntesPulo = jogavel.getY();
						}
					}
				}
			}

			if (Math.abs(alturaIncialAntesPulo - jogavel.getY()) <= alturaMaximoPuloIncrementar) {
				jogavel.getCorpo().adjustVelocity(
						new Vector2f(0, impulsoPulo));
			}
		} else if (EntradaPerifericos.estaPressionado(Controles.T_A)) {

			if (jogavel.isNoar() && !jogavel.isCaindo()) {
				if (Math.abs(alturaIncialAntesPulo - jogavel.getY()) <= alturaMaximoPuloIncrementar) {
					jogavel.getCorpo().adjustVelocity(new Vector2f(0, impulsoPulo));
				}				
			}

		}

		if (jogavel.isCaindo()) {
			if (EntradaPerifericos.direitaEstaPressionado()) {
				if (!jogavel.getNomeAnimacao().equals("CAINDO_DIREITA")) {
//					jogavel.configureAnimacao("CAINDO_DIREITA");
					jogavel.setFace(AtorFaceOrientacao.direita);
					setFaceOrientacao(AtorFaceOrientacao.direita);
					configureAnimacao("CAINDO");
				}

				if (jogavel.getCorpo().getVelocity().getX() <= 16f) {
					jogavel.getCorpo().adjustVelocity(new Vector2f(8f, 0));
				}

			} else if (EntradaPerifericos.esquerdaEstaPressionado()) {
				if (!jogavel.getNomeAnimacao().equals("CAINDO_ESQUERDA")) {
//					jogavel.configureAnimacao("CAINDO_ESQUERDA");
					jogavel.setFace(AtorFaceOrientacao.esquerda);
					setFaceOrientacao(AtorFaceOrientacao.esquerda);
					configureAnimacao("CAINDO");
				}

				if (jogavel.getCorpo().getVelocity().getX() >= -16f) {
					jogavel.getCorpo().adjustVelocity(new Vector2f(-8f, 0));
				}
			}
		}

		if (!armaTravada) {
			if (((EntidadeJogavel) jogavel.getEntidade()).getArma()
					.modoAcumulador()) {
				if (EntradaPerifericos.estaPressionado(Controles.T_S)) {// ACUMULAR
					((EntidadeJogavel) jogavel.getEntidade()).getArma().acumular();
				} else if (EntradaPerifericos.foiPressionado(Controles.T_S)) {// || 
					//((EntidadeJogavel) jogavel.getEntidade()).getArma().getAcumulado() > 0
					((EntidadeJogavel) jogavel.getEntidade()).getArma().disparar(cena);
				}
			} else if (((EntidadeJogavel) jogavel.getEntidade()).getArma()
					.modoNormal()) {
				if (EntradaPerifericos.foiPressionado(Controles.T_S)) {// ATIRA
					((EntidadeJogavel) jogavel.getEntidade()).getArma()
							.disparar(cena);
				}
			} else if (((EntidadeJogavel) jogavel.getEntidade()).getArma()
					.modoMetralhadora()) {
				//entrada.isKeyDown(Input.KEY_S)
				if (EntradaPerifericos.estaPressionado(Controles.T_S)) {// METRALHA
					((EntidadeJogavel) jogavel.getEntidade()).getArma()
							.disparar(cena);
				}
			}
		}

		//entrada.isKeyDown(Input.KEY_Z)
		if (EntradaPerifericos.estaPressionado(Controles.T_Z)) {// VOANDO
			BarraLogEstado comb = painelEstado.getBarraEstado("combustivel");

			if (!foguete.descarregado && foguete.habilitado) {
				if (comb.getAtual() > 5) {
					if (!jogavel.isNoar()) {
					
						if (!jogavel.isVoando()) {
							jogavel.getCorpo().getVelocity().getY();
							jogavel.getCorpo().adjustVelocity(new Vector2f(0, -jogavel.getCorpo().
								getVelocity().getY()));
						}
						
						if (jogavel.getFace()
								.equals(AtorFaceOrientacao.direita)) {
							jogavel.configureAnimacao("VOANDO_DIREITA");
						} else {
							jogavel.configureAnimacao("VOANDO_ESQUERDA");
						}

						jogavel.setVoando(true);						
					}

					if (jogavel.isVoando()) {
						getAtor().getCorpo().setFriction(0f);
						if (jogavel.getCorpo().getVelocity().getY() >= foguete.fogueteMaximaVelocidade) {
							jogavel.getCorpo().
							adjustVelocity(new Vector2f(0, foguete.inpulsoPuloFoguete));
						}
						comb.decrementeAtual(foguete.consumo);
					}
				} else {
					foguete.descarregado = true;
					jogavel.setCaindo(true);
				}
			}
		}
		// MUDANDO A DIREÇÂO NO VOO COM O FOGUETE PROPULSOR
		if (jogavel.isVoando() && !jogavel.isCaindo()) {
			//entrada.isKeyDown(Input.KEY_RIGHT)
			if (EntradaPerifericos.direitaEstaPressionado()) {
				if (jogavel.getCorpo().getVelocity().getX() <= foguete.fogueteMaximaVelocidadeX) {
					jogavel.getCorpo().adjustVelocity(new Vector2f(foguete.forcaX, 0));
					jogavel.setFace(AtorFaceOrientacao.direita);
					setFaceOrientacao(AtorFaceOrientacao.direita);
				}//entrada.isKeyDown(Input.KEY_LEFT)
			} else if (EntradaPerifericos.esquerdaEstaPressionado()) {
				if (jogavel.getCorpo().getVelocity().getX() >= -foguete.fogueteMaximaVelocidadeX) {
					jogavel.getCorpo().adjustVelocity(new Vector2f(-foguete.forcaX, 0));
					jogavel.setFace(AtorFaceOrientacao.esquerda);
					setFaceOrientacao(AtorFaceOrientacao.esquerda);
				}
			}

			if (jogavel.isVoando() && !jogavel.isCaindo()) {

				if (jogavel.getFace().equals(AtorFaceOrientacao.direita)) {
					jogavel.configureAnimacao("VOANDO_DIREITA");
				} else {
					jogavel.configureAnimacao("VOANDO_ESQUERDA");
				}
			}
		}

		//entrada.isKeyPressed(Input.KEY_C)
		if (EntradaPerifericos.foiPressionado(Controles.T_C)) {// ESCUDO
			
			if (escudo.estaHabilitado()) {
				escudo.desabilitar();
			} else {
				escudo.habilitar();
			}
		}

		if (EntradaPerifericos.foiPressionado(Controles.T_F)) {// ARMA PROXIMA
				Cenario cenaAtual = (Cenario) Motor.obterInstancia()
						.getCenaAtual();
				cenaAtual.forceExcluirDesenho((Desenho) arma.getAtor());

				arma = armas.proximo();				
				arma.setFaceOrientacao(getFaceOrientacao());

				cenaAtual.adicionarMeio((Desenho) arma.getAtor());

				painelEstado.setArma(arma.getNome());
		}

		if (EntradaPerifericos.foiPressionado(Controles.T_D)) {//ARMA ANTERIOR
				Cenario cenaAtual = (Cenario) Motor.obterInstancia()
						.getCenaAtual();
				cenaAtual.forceExcluirDesenho((Desenho) arma.getAtor());
				
				arma = armas.anterior();
				arma.setFaceOrientacao(getFaceOrientacao());
				cenaAtual.adicionarMeio((Desenho) arma.getAtor());

				painelEstado.setArma(arma.getNome());
		}

		if (container.getInput().isKeyPressed(Input.KEY_F10)) {// MODO
																// DEPURAÇÂO
			if (Motor.obterInstancia().isModoDebug()) {
				habilitaFoguetePropulsor(true);
				setTeleporteDestravado(true);
				
				Cenario cenaAtual = (Cenario) Motor.obterInstancia()
						.getCenaAtual();
				cenaAtual.getPainelEstado().getBarraEstado("combustivel")
						.setVisivel(true);

				getEscudo().destravar();
				cenaAtual.getPainelEstado().getBarraEstado("escudo")
						.setVisivel(true);

				setBazucahabilitada(true);
				setArmaFogoHabilitada(true);
				setArmaGeloHabilitada(true);
				getArma().configureModoAcumulador();
			}
		}

	}

	@Override
	public void atualizarFisica(int delta) {
		Ator jogavel = (Ator) getAtor();

		super.atualizarFisica(delta);

		if (!jogavel.isVoando()) {
			BarraLogEstado comb = painelEstado.getBarraEstado("combustivel");

			if (comb != null) {// TODO VERIFICAR BUG DE BARRA VIR NULO
				
				if (comb.getAtual() < comb.getMaximo()) {
					if(foguete.autoIncrementa || Motor.obterInstancia().isModoDebug())
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
		if(bazucahabilitada) {
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
		if(armaFogoHabilitada) {
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
		if(armaGeloHabilitada) {
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
		habilitaFoguetePropulsor(false);		
		setEnergiaVital(getEnergiaVitalMaxima());				
		arma = armas.get("EV");		
		//cenaAtual.forceExcluirDesenho((Desenho) arma.getAtor());			
		//cenaAtual.adicionarMeio((Desenho) arma.getAtor());		
		painelEstado.setArma(arma.getNome());				
		getEscudo().travar();		
		Motor.obterInstancia().getPainelEstado().getBarraEstado("combustivel").setVisivel(false);
		Motor.obterInstancia().getPainelEstado().getBarraEstado("escudo").setVisivel(false);
		setBazucahabilitada(false);
		setArmaFogoHabilitada(false);
		setArmaGeloHabilitada(false);
		setControlavel(true);
		setFaceOrientacao(orientacaoInicial);
		configureAnimacao("PARADO");
		arma.setFaceOrientacao(getFaceOrientacao());
		//
	}
}
