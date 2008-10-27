package br.org.gamexis.plataforma.entidade;

import java.util.ArrayList;
import java.util.List;

import net.phys2d.math.Vector2f;

import org.lwjgl.Sys;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.cena.Animacao;
import br.org.gamexis.plataforma.cena.Animado;
import br.org.gamexis.plataforma.cena.Ator;
import br.org.gamexis.plataforma.cena.AtorFaceOrientacao;
import br.org.gamexis.plataforma.cena.CenarioIF;
import br.org.gamexis.plataforma.cena.Colisivel;
import br.org.gamexis.plataforma.cena.Efeito;
import br.org.gamexis.plataforma.cena.NivelCena;
import br.org.gamexis.plataforma.entidade.arma.ModoDisparo;
import br.org.gamexis.plataforma.entidade.logestado.Municao;
import br.org.gamexis.plataforma.eventos.Evento;
import br.org.gamexis.plataforma.exception.GXException;

/**
 * Representa um projetil.
 * 
 * @author abraao
 * 
 */
public class EntidadeArma extends Entidade implements EntidadeOrientavel {

	private Projetil projetil;
	private List<Projetil> projeteislAcumulado = new ArrayList<Projetil>();
	private int intervalo = 300;
	private int maximo = 3;
	private Animado animacaoBrilho;
	private EntidadeJogavel jogavel;
	private AtorFaceOrientacao face;

	private ModoDisparo modoDisparo = ModoDisparo.normal;

	private int maximoNivelAcumulador = 3;// ACUMUMLADOR
	private int acumuladoNivelAtual = 0;
	private int tempoAumentarNivel = 600;
	private long tempoInicialNivel = 0;

	private List<Efeito> efeitosCarregamento = new ArrayList<Efeito>();

	ConfiguracaoOrientacao direita = new ConfiguracaoOrientacao();
	ConfiguracaoOrientacao direitaAcima = new ConfiguracaoOrientacao();
	ConfiguracaoOrientacao direitaAbaixo = new ConfiguracaoOrientacao();

	ConfiguracaoOrientacao esquerda = new ConfiguracaoOrientacao();
	ConfiguracaoOrientacao esquerdaAcima = new ConfiguracaoOrientacao();
	ConfiguracaoOrientacao esquerdaAbaixo = new ConfiguracaoOrientacao();

	private long ultimoDisparo = 0;

	private List<Projetil> projeteis = new ArrayList<Projetil>();

	public EntidadeArma() {

	}

	public void setFaceOrientacao(AtorFaceOrientacao face) {
		this.face = face;
		((Ator)getAtor()).setFace(face);
		projetil.setFaceOrientacao(face);
	}

	public AtorFaceOrientacao getFaceOrientacao() {
		return face;
	}

	public void setJogavel(EntidadeJogavel jogavel) {
		this.jogavel = jogavel;
	}

	@Override
	public TipoEntidade getTipo() {
		return TipoEntidade.arma;
	}

	@Override
	public void atualizar(Evento evento) throws GXException {
		super.atualizar(evento);

		Ator atorJogavel = (Ator) jogavel.getAtor();
		Ator arm = (Ator) getAtor();
		AtorFaceOrientacao faceOrientacao = getFaceOrientacao();
		ajustarAnimacaoArma(jogavel.getFaceOrientacao());

	}

	
	private void ajustarAnimacaoArma(AtorFaceOrientacao faceOrientacao) {
		if (faceOrientacao != null) {
			Ator arm = (Ator) getAtor();
			Ator atorJogavel = (Ator) jogavel.getAtor();
			
			if (getFaceOrientacao().equals(AtorFaceOrientacao.direita)) {
				if (!arm.getNomeAnimacao().equalsIgnoreCase("PARADO_DIREITA")) {
					// arm.limparEfeitos();
					arm.configureAnimacao("PARADO_DIREITA");

					if (acumuladoNivelAtual - 1 > 0) {
						Efeito ef = arm.obterEfeitoPorNome(efeitosCarregamento
								.get(acumuladoNivelAtual - 1).getNome());

						if (ef != null) {
							ef.setDeslocX(direita.deslocamentoCarregamentoX);
							ef.setDeslocY(direita.deslocamentoCarregamentoY);
						}
					}
				}

				arm.setX(atorJogavel.getX()
						+ (arm.getComprimento() - arm.getComprimento() / 6));
				arm.setY(atorJogavel.getY());
			} else if (getFaceOrientacao().equals(AtorFaceOrientacao.esquerda)) {
				if (!arm.getNomeAnimacao().equalsIgnoreCase("PARADO_ESQUERDA")) {
					// arm.limparEfeitos();
					arm.configureAnimacao("PARADO_ESQUERDA");

					if (acumuladoNivelAtual - 1 > 0) {
						Efeito ef = arm.obterEfeitoPorNome(efeitosCarregamento
								.get(acumuladoNivelAtual - 1).getNome());

						if (ef != null) {
							ef.setDeslocX(esquerda.deslocamentoCarregamentoX);
							ef.setDeslocY(esquerda.deslocamentoCarregamentoY);
						}
					}
				}

				arm.setX(atorJogavel.getX()
						- (arm.getComprimento() - arm.getComprimento() / 6));
				arm.setY(atorJogavel.getY());
			} else if (getFaceOrientacao().equals(
					AtorFaceOrientacao.direitaAcima)) {
				if (!arm.getNomeAnimacao().equalsIgnoreCase("ACIMA_DIREITA")) {
					// arm.limparEfeitos();
					arm.configureAnimacao("ACIMA_DIREITA");

					if (acumuladoNivelAtual - 1 > 0) {
						Efeito ef = arm.obterEfeitoPorNome(efeitosCarregamento
								.get(acumuladoNivelAtual - 1).getNome());

						if (ef != null) {
							ef.setDeslocX(direitaAcima.deslocamentoCarregamentoX);
							ef.setDeslocY(direitaAcima.deslocamentoCarregamentoY);
						}
					}
				}

				arm.setX(atorJogavel.getX()
						+ (arm.getComprimento() - arm.getComprimento() / 2));
				arm.setY(atorJogavel.getY() - arm.getAltura() / 3);
			} else if (getFaceOrientacao().equals(
					AtorFaceOrientacao.direitaAbaixo)) {
				if (!arm.getNomeAnimacao().equalsIgnoreCase("ABAIXO_DIREITA")) {
					// arm.limparEfeitos();
					arm.configureAnimacao("ABAIXO_DIREITA");

					if (acumuladoNivelAtual - 1 > 0) {
						Efeito ef = arm.obterEfeitoPorNome(efeitosCarregamento
								.get(acumuladoNivelAtual - 1).getNome());

						if (ef != null) {
							ef.setDeslocX(direitaAbaixo.deslocamentoCarregamentoX);
							ef.setDeslocY(direitaAbaixo.deslocamentoCarregamentoY);
						}
					}
				}

				arm.setX(atorJogavel.getX()
						+ (arm.getComprimento() - arm.getComprimento() / 3));
				arm.setY(atorJogavel.getY()
						+ (arm.getAltura() - arm.getAltura() / 2));
			} else if (getFaceOrientacao().equals(
					AtorFaceOrientacao.esquerdaAcima)) {
				if (!arm.getNomeAnimacao().equalsIgnoreCase("ACIMA_ESQUERDA")) {
					// arm.limparEfeitos();
					arm.configureAnimacao("ACIMA_ESQUERDA");

					if (acumuladoNivelAtual - 1 > 0) {
						Efeito ef = arm.obterEfeitoPorNome(efeitosCarregamento
								.get(acumuladoNivelAtual - 1).getNome());

						if (ef != null) {
							ef.setDeslocX(esquerdaAcima.deslocamentoCarregamentoX);
							ef.setDeslocY(esquerdaAcima.deslocamentoCarregamentoY);
						}
					}
				}

				arm.setX(atorJogavel.getX()
						- (arm.getComprimento() - arm.getComprimento() / 2));
				arm.setY(atorJogavel.getY() - (arm.getAltura() / 3));
			} else if (getFaceOrientacao().equals(
					AtorFaceOrientacao.esquerdaAbaixo)) {
				if (!arm.getNomeAnimacao().equalsIgnoreCase("ABAIXO_ESQUERDA")) {
					// arm.limparEfeitos();
					arm.configureAnimacao("ABAIXO_ESQUERDA");

					if (acumuladoNivelAtual - 1 > 0) {
						Efeito ef = arm.obterEfeitoPorNome(efeitosCarregamento
								.get(acumuladoNivelAtual - 1).getNome());

						if (ef != null) {
							ef.setDeslocX(esquerdaAbaixo.deslocamentoCarregamentoX);
							ef.setDeslocY(esquerdaAbaixo.deslocamentoCarregamentoY);
						}
					}
				}

				arm.setX(atorJogavel.getX()
						- (arm.getComprimento() - arm.getComprimento() / 3));
				arm.setY(atorJogavel.getY()
						+ (arm.getAltura() - arm.getAltura() / 2));
			}
		}		
	}
	
	public void normalizarArma() {
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

	public int getAcumulado() {
		return acumuladoNivelAtual;
	}

	public void acumular() {
		if(!modoAcumulador())
			return;
		
		if (tempoInicialNivel <= 0) {
			tempoInicialNivel = Sys.getTime();
		}

		if (acumuladoNivelAtual < maximoNivelAcumulador) {
			if (Sys.getTime() - tempoInicialNivel > tempoAumentarNivel) {

				if (acumuladoNivelAtual > 0) {
					((Ator) getAtor()).excluirEfeitoPorNome(efeitosCarregamento
							.get(acumuladoNivelAtual - 1).getNome());
				}

				acumuladoNivelAtual++;
					
				Efeito efeito = efeitosCarregamento
						.get(acumuladoNivelAtual - 1).clone();
				efeito.setReferencia(((Ator) getAtor()));

				((Ator) getAtor()).adcionarEfeitoPrioritario(efeito);

				switch (getFaceOrientacao()) {
				case direita:
					efeito.setDeslocX(direita.deslocamentoCarregamentoX);
					efeito.setDeslocY(direita.deslocamentoCarregamentoY);
					break;
				case direitaAbaixo:
					efeito.setDeslocX(direitaAbaixo.deslocamentoCarregamentoX);
					efeito.setDeslocY(direitaAbaixo.deslocamentoCarregamentoY);
					break;
				case direitaAcima:
					efeito.setDeslocX(direitaAcima.deslocamentoCarregamentoX);
					efeito.setDeslocY(direitaAcima.deslocamentoCarregamentoY);
					break;
				case esquerda:
					efeito.setDeslocX(esquerda.deslocamentoCarregamentoX);
					efeito.setDeslocY(esquerda.deslocamentoCarregamentoY);
					break;
				case esquerdaAbaixo:
					efeito.setDeslocX(esquerdaAbaixo.deslocamentoCarregamentoX);
					efeito.setDeslocY(esquerdaAbaixo.deslocamentoCarregamentoY);
					break;
				case esquerdaAcima:
					efeito.setDeslocX(esquerdaAcima.deslocamentoCarregamentoX);
					efeito.setDeslocY(esquerdaAcima.deslocamentoCarregamentoY);
					break;
				}

				tempoInicialNivel = Sys.getTime();
			}
		}

	}

	public boolean atingiuLimite() {
		if (acumuladoNivelAtual >= maximoNivelAcumulador) {
			return true;
		}

		return false;
	}

	public void disparar(CenarioIF cenario) {
		long atual = Sys.getTime();
		Municao muni = jogavel.getPainelEstado().getArma().getMunicao();

		if (muni.getMunicao() == 0)// ACABOU A MUNIÇÃO
			return;

		muni.decrementeMunicao(1);
		if (projeteis.size() < maximo) {
			if ((atual - ultimoDisparo) >= (intervalo)) {				
				ultimoDisparo = atual;

				Ator ator = (Ator) getAtor();

				Projetil projt = null;

				if (modoAcumulador()) {

					if (acumuladoNivelAtual > 0) {
						projt = this.projeteislAcumulado.get(acumuladoNivelAtual - 1).clone();
					} else {
						projt = this.projetil.clone();
					}
				} else {
					projt = this.projetil.clone();
				}
				
				projt.getAtor().getCorpo().addExcludedBody(
						jogavel.getAtor().getCorpo());
				projt.getAtor().getCorpo().addExcludedBody(
						jogavel.getEscudo().getAtor().getCorpo());

				if (modoAcumulador() && acumuladoNivelAtual > 0) {
					((Ator) getAtor()).excluirEfeitoPorNome(efeitosCarregamento
							.get(acumuladoNivelAtual - 1).getNome());
					acumuladoNivelAtual = 0;// ZERA O CAUMULADOR
				}

				if (animacaoBrilho != null) {
					animacaoBrilho = ((Efeito) animacaoBrilho).clone();
					cenario.adicionarAnimacao((Efeito) animacaoBrilho,
							NivelCena.frente);
				}

				if (getFaceOrientacao().equals(AtorFaceOrientacao.direitaAcima)) {
					projt.getAtor().getCorpo().setPosition(
							ator.getX() + direitaAcima.deslocamentoTiroX,
							ator.getY() + direitaAcima.deslocamentoTiroY);
					projt.getAtor().getCorpo().adjustVelocity(
							new Vector2f(0, -projetil// projetil.getVelocidade()
									.getVelocidade()));
					projt.setFaceOrientacao(getFaceOrientacao());

					if (animacaoBrilho != null) {
						((Animacao) animacaoBrilho)
								.setDeslocX(direitaAcima.deslocamentoBrilhoX);
						((Animacao) animacaoBrilho)
								.setDeslocY(direitaAcima.deslocamentoBrilhoY);
					}
				} else if (getFaceOrientacao().equals(
						AtorFaceOrientacao.direita)) {

					projt.getAtor().getCorpo().setPosition(
							ator.getX() + direita.deslocamentoTiroX,
							ator.getY() + direita.deslocamentoTiroY);
					projt.getAtor().getCorpo().adjustVelocity(
							new Vector2f(projetil.getVelocidade(), 0));
					projt.setFaceOrientacao(getFaceOrientacao());

					if (animacaoBrilho != null) {
						((Animacao) animacaoBrilho)
								.setDeslocX(direita.deslocamentoBrilhoX);
						((Animacao) animacaoBrilho)
								.setDeslocY(direita.deslocamentoBrilhoY);
					}

				} else if (getFaceOrientacao().equals(
						AtorFaceOrientacao.direitaAbaixo)) {

					projt.getAtor().getCorpo().setPosition(
							ator.getX() + direitaAbaixo.deslocamentoTiroX,
							ator.getY() + direitaAbaixo.deslocamentoTiroY);

					projt.getAtor().getCorpo().adjustVelocity(
							new Vector2f(0, projetil// projetil.getVelocidade()
									.getVelocidade()));
					projt.setFaceOrientacao(getFaceOrientacao());

					if (animacaoBrilho != null) {
						((Animacao) animacaoBrilho)
								.setDeslocX(direitaAbaixo.deslocamentoBrilhoX);
						((Animacao) animacaoBrilho)
								.setDeslocY(direitaAbaixo.deslocamentoBrilhoY);
					}

				} else if (getFaceOrientacao().equals(
						AtorFaceOrientacao.esquerda)) {

					projt.getAtor().getCorpo().setPosition(
							ator.getX() + esquerda.deslocamentoTiroX,
							ator.getY() + esquerda.deslocamentoTiroY);
					projt.getAtor().getCorpo().adjustVelocity(
							new Vector2f(-projetil.getVelocidade(), 0));
					projt.setFaceOrientacao(getFaceOrientacao());

					if (animacaoBrilho != null) {
						((Animacao) animacaoBrilho)
								.setDeslocX(esquerda.deslocamentoBrilhoX);
						((Animacao) animacaoBrilho)
								.setDeslocY(esquerda.deslocamentoBrilhoY);
					}
				} else if (getFaceOrientacao().equals(
						AtorFaceOrientacao.esquerdaAcima)) {

					projt.getAtor().getCorpo().setPosition(
							ator.getX() + esquerdaAcima.deslocamentoTiroX,
							ator.getY() + esquerdaAcima.deslocamentoTiroY);
					projt.getAtor().getCorpo().adjustVelocity(
							new Vector2f(0, -projetil// -projetil.getVelocidade()
									.getVelocidade()));
					projt.setFaceOrientacao(getFaceOrientacao());

					if (animacaoBrilho != null) {
						((Animacao) animacaoBrilho)
								.setDeslocX(esquerdaAcima.deslocamentoBrilhoX);
						((Animacao) animacaoBrilho)
								.setDeslocY(esquerdaAcima.deslocamentoBrilhoY);
					}
				} else if (getFaceOrientacao().equals(AtorFaceOrientacao.esquerdaAbaixo)) {

					projt.getAtor().getCorpo().setPosition(
							ator.getX() + esquerdaAbaixo.deslocamentoTiroX,
							ator.getY() + esquerdaAbaixo.deslocamentoTiroY);
					projt.getAtor().getCorpo().adjustVelocity(
							new Vector2f(0, projetil.getVelocidade()));
					projt.setFaceOrientacao(getFaceOrientacao());

					if (animacaoBrilho != null) {
						((Animacao) animacaoBrilho)
								.setDeslocX(esquerdaAbaixo.deslocamentoBrilhoX);
						((Animacao) animacaoBrilho)
								.setDeslocY(esquerdaAbaixo.deslocamentoBrilhoY);
					}
				}

				cenario.adicionarAtor((Ator) projt.getAtor(), NivelCena.meio, 0);
				projt.setArma(this);
				projeteis.add(projt);				
				try {
					executarComportamento("aoDisparar", new Object[] { this,
							Motor.obterInstancia() });
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}
		}
		
		if(acumuladoNivelAtual > 0) {
			((Ator) getAtor()).excluirEfeitoPorNome(efeitosCarregamento.get(acumuladoNivelAtual - 1).getNome());
		}
		acumuladoNivelAtual = 0;
	}

	public void setAnimacaoBrilho(Animado an) {
		an.setX(getAtor().getCorpo().getPosition().getX());
		an.setY(getAtor().getCorpo().getPosition().getY());

		animacaoBrilho = an;
	}

	@Override
	public void setAtor(Colisivel ator) {
		super.setAtor(ator);

		((Ator) ator).setEntidade(this);
	}

	public void removaProjetil(Projetil projetil) {
		projeteis.remove(projetil);
	}

	public Projetil getProjetil() {
		return projetil;
	}

	public void setProjetil(Projetil projetil) {
		this.projetil = projetil;
	}

	class ConfiguracaoOrientacao {
		float deslocamentoBrilhoX;
		float deslocamentoBrilhoY;
		float deslocamentoTiroX;
		float deslocamentoTiroY;
		float deslocamentoCarregamentoX;
		float deslocamentoCarregamentoY;
	}

	public void configureOrientacaoDireita(float deslocamentoBrilhoX,
			float deslocamentoBrilhoY, float deslocamentoTiroX,
			float deslocamentoTiroY, float deslocamentoCarregamentoX,
			float deslocamentoCarregamentoY) {

		direita = new ConfiguracaoOrientacao();

		direita.deslocamentoBrilhoX = deslocamentoBrilhoX;
		direita.deslocamentoBrilhoY = deslocamentoBrilhoY;
		direita.deslocamentoTiroX = deslocamentoTiroX;
		direita.deslocamentoTiroY = deslocamentoTiroY;
		direita.deslocamentoCarregamentoX = deslocamentoCarregamentoX;
		direita.deslocamentoCarregamentoY = deslocamentoCarregamentoY;
	}

	public void configureOrientacaoDireitaAcima(float deslocamentoBrilhoX,
			float deslocamentoBrilhoY, float deslocamentoTiroX,
			float deslocamentoTiroY, float deslocamentoCarregamentoX,
			float deslocamentoCarregamentoY) {

		direitaAcima = new ConfiguracaoOrientacao();

		direitaAcima.deslocamentoBrilhoX = deslocamentoBrilhoX;
		direitaAcima.deslocamentoBrilhoY = deslocamentoBrilhoY;
		direitaAcima.deslocamentoTiroX = deslocamentoTiroX;
		direitaAcima.deslocamentoTiroY = deslocamentoTiroY;
		direitaAcima.deslocamentoCarregamentoX = deslocamentoCarregamentoX;
		direitaAcima.deslocamentoCarregamentoY = deslocamentoCarregamentoY;
	}

	public void configureOrientacaoDireitaAbaixo(float deslocamentoBrilhoX,
			float deslocamentoBrilhoY, float deslocamentoTiroX,
			float deslocamentoTiroY, float deslocamentoCarregamentoX,
			float deslocamentoCarregamentoY) {

		direitaAbaixo = new ConfiguracaoOrientacao();

		direitaAbaixo.deslocamentoBrilhoX = deslocamentoBrilhoX;
		direitaAbaixo.deslocamentoBrilhoY = deslocamentoBrilhoY;
		direitaAbaixo.deslocamentoTiroX = deslocamentoTiroX;
		direitaAbaixo.deslocamentoTiroY = deslocamentoTiroY;
		direitaAbaixo.deslocamentoCarregamentoX = deslocamentoCarregamentoX;
		direitaAbaixo.deslocamentoCarregamentoY = deslocamentoCarregamentoY;
	}

	public void configureOrientacaoEsquerda(float deslocamentoBrilhoX,
			float deslocamentoBrilhoY, float deslocamentoTiroX,
			float deslocamentoTiroY, float deslocamentoCarregamentoX,
			float deslocamentoCarregamentoY) {

		esquerda = new ConfiguracaoOrientacao();

		esquerda.deslocamentoBrilhoX = deslocamentoBrilhoX;
		esquerda.deslocamentoBrilhoY = deslocamentoBrilhoY;
		esquerda.deslocamentoTiroX = deslocamentoTiroX;
		esquerda.deslocamentoTiroY = deslocamentoTiroY;
		esquerda.deslocamentoCarregamentoX = deslocamentoCarregamentoX;
		esquerda.deslocamentoCarregamentoY = deslocamentoCarregamentoY;
	}

	public void configureOrientacaoEsquerdaAcima(float deslocamentoBrilhoX,
			float deslocamentoBrilhoY, float deslocamentoTiroX,
			float deslocamentoTiroY, float deslocamentoCarregamentoX,
			float deslocamentoCarregamentoY) {

		esquerdaAcima = new ConfiguracaoOrientacao();

		esquerdaAcima.deslocamentoBrilhoX = deslocamentoBrilhoX;
		esquerdaAcima.deslocamentoBrilhoY = deslocamentoBrilhoY;
		esquerdaAcima.deslocamentoTiroX = deslocamentoTiroX;
		esquerdaAcima.deslocamentoTiroY = deslocamentoTiroY;
		esquerdaAcima.deslocamentoCarregamentoX = deslocamentoCarregamentoX;
		esquerdaAcima.deslocamentoCarregamentoY = deslocamentoCarregamentoY;
	}

	public void configureOrientacaoEsquerdaAbaixo(float deslocamentoBrilhoX,
			float deslocamentoBrilhoY, float deslocamentoTiroX,
			float deslocamentoTiroY, float deslocamentoCarregamentoX,
			float deslocamentoCarregamentoY) {

		esquerdaAbaixo = new ConfiguracaoOrientacao();

		esquerdaAbaixo.deslocamentoBrilhoX = deslocamentoBrilhoX;
		esquerdaAbaixo.deslocamentoBrilhoY = deslocamentoBrilhoY;
		esquerdaAbaixo.deslocamentoTiroX = deslocamentoTiroX;
		esquerdaAbaixo.deslocamentoTiroY = deslocamentoTiroY;
		esquerdaAbaixo.deslocamentoCarregamentoX = deslocamentoCarregamentoX;
		esquerdaAbaixo.deslocamentoCarregamentoY = deslocamentoCarregamentoY;
	}

	public boolean modoMetralhadora() {
		return modoDisparo.equals(ModoDisparo.metralhadora);
	}

	public boolean modoNormal() {
		return modoDisparo.equals(ModoDisparo.normal);
	}

	public boolean modoAcumulador() {
		return modoDisparo.equals(ModoDisparo.acumulador);
	}

	public void configureModoMetralhadora() {
		modoDisparo = ModoDisparo.metralhadora;
	}

	public void configureModoNormal() {
		modoDisparo = ModoDisparo.normal;
	}

	public void configureModoAcumulador() {
		if(projeteislAcumulado != null && projeteislAcumulado.size() > 0)
			modoDisparo = ModoDisparo.acumulador;
	}

	public void adicionarEfeitoCarregamento(Efeito efeito) {
		efeitosCarregamento.add(efeito);
	}

	public void adicionarProjeteisAcumulado(Projetil projetilAcumulado) {
		this.projeteislAcumulado.add(projetilAcumulado);
	}

	public int getMaximoNivelAcumulador() {
		return maximoNivelAcumulador;
	}

	public void setMaximoNivelAcumulador(int maximoNivelAcumulador) {
		this.maximoNivelAcumulador = maximoNivelAcumulador;
	}

	public int getTempoAumentarNivel() {
		return tempoAumentarNivel;
	}

	public void setTempoAumentarNivel(int tempoAumentarNivel) {
		this.tempoAumentarNivel = tempoAumentarNivel;
	}

}
