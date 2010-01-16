package br.org.gamexis.plataforma.cena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.CollisionEvent;
import net.phys2d.raw.CollisionListener;
import net.phys2d.raw.Joint;
import net.phys2d.raw.World;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.cena.componentes.PainelEstado;
import br.org.gamexis.plataforma.cena.componentes.ToupetyComponente;
import br.org.gamexis.plataforma.cena.evento.EventoColisaoCorpoColisao;
import br.org.gamexis.plataforma.cena.evento.Listerner;
import br.org.gamexis.plataforma.cena.evento.ListernerManager;
import br.org.gamexis.plataforma.cena.fisica.SuperCorpo;
import br.org.gamexis.plataforma.cena.util.DesenhadorCorpo;
import br.org.gamexis.plataforma.entidade.AreaAcao;
import br.org.gamexis.plataforma.entidade.Entidade;
import br.org.gamexis.plataforma.entidade.EntidadeAnimada;
import br.org.gamexis.plataforma.entidade.EntidadeJogavel;
import br.org.gamexis.plataforma.entidade.TipoEntidade;
import br.org.gamexis.plataforma.eventos.EventoColisao;
import br.org.gamexis.plataforma.exception.GXException;

/**
 * Represento um Cenário. 
 * 
 * @author abraao
 * 
 */
public class Cenario extends CenaBase implements CenarioIF, CollisionListener,
		ListernerManager {

	/**
	 * Trava a camera para controle manual
	 */
	private boolean travaCamera = false;
	private boolean terremoto = false;
	private int terremotoCont = 0;

	/**
	 * Jogador
	 */
	private Ator jogavel = null;
	
	/**
	 * Arma
	 */
	private Ator arma = null;

	/**
	 * Mundo Fisico
	 */
	private World mundoFisico; 
	private List<Listerner> listerners = new ArrayList<Listerner>();
	private Listerner listernerAtual = null;

	HashMap<String, ToupetyComponente> componentesNomes = new HashMap<String, ToupetyComponente>();
	private List<ToupetyComponente> componentes = new ArrayList<ToupetyComponente>();

	private List<Ator> atoresFundo = new ArrayList<Ator>();
	private List<Ator> atoresMeio = new ArrayList<Ator>();
	private List<Ator> atoresAnimado = new ArrayList<Ator>();
	private List<Ator> atoresFrente = new ArrayList<Ator>();

	private Map<String, EmissorParticulas> emissores = new HashMap<String, EmissorParticulas>();

	/**
	 *  todos os atores
	 */
	private HashMap<String, Ator> atores = new HashMap<String, Ator>();

	/**
	 *  Animações
	 */
	private List<Animado> animados = new ArrayList<Animado>();
	
	/**
	 * Plataformas
	 */
	private List<Plataforma> plataformas = new ArrayList<Plataforma>();

	/**
	 *  NIVEL ADICIONAL DE OBJETOS FIXOS QUE SEGUEM A CAMERA
	 */
	private Tela fixosFrente = new Tela();
	private Tela fixosFundo = new Tela();
	private Tela paralaxeFundo = new Tela();

	private float cameraX = 0;
	private float cameraY = 0;
	private PainelEstado painelEstado;

	private boolean pausado = false;
	private boolean pausadoTexto = false;
	private boolean controlesTravados = false;

	/**
	 *  Configurações da camera
	 */
	private float cameraXFator = 2;
	private float cameraYFator = 2;
	private float fatorZY = 0;
	private float fatorZX = 0;

	/** 
	 * Maximos e minimos da posição da camera
	 */
	private float cameraXMinimo = 0;
	private float cameraYMinimo = 0;
	private float cameraXMaximo = 0;
	private float cameraYMaximo = 0;

	/**
	 *  SINCRONIZAÇÃO DAS FREQUÊNCIAS
	 */
	private int totalDelta;
	private int stepSize = 5;

	/**
	 * Areas de ação.
	 */
	private List<AreaAcao> areasAcao = new ArrayList<AreaAcao>();

	/**
	 * Contador de atores inseridos ao longo da existencia da cena
	 */
	private int atoresInseridos = 0;

	/**
	 * Contador de atores inseridos ao longo da existencia da cena
	 */
	private int emissioresInseridos = 0;

	public Cenario(float comprimento, float altura) {

		mundoFisico = new World(new Vector2f(0.0f, 10f), 5);

		mundoFisico.addListener(this);

	}

	public Cenario() {
		this(800, 600);
	}

	@Override
	public void iniciar(GameContainer container) throws SlickException {
		super.iniciar(container);
		painelEstado.getBarraEstado("energia").setVisivel(true);

		// configurando a posição do jogavel
		Entrada entrada = Motor.obterInstancia().popEntrada();
		if (entrada != null) {
			((SuperCorpo) jogavel.getCorpo()).moveAll(entrada.x, entrada.y);
		}
		container.getInput().clearKeyPressedRecord();
		container.getInput().clearControlPressedRecord();
		container.getInput().clearMousePressedRecord();

		container.getInput().addListener(
				Motor.obterInstancia().getJogavelCarregado());
	}

	public void verificaExcluirAtor(Ator ator) {
		if (ator.excluido()) {
			mundoFisico.remove(ator.getCorpo());
			excluirDesenho(ator);

			if (atoresFundo.remove(ator)) {
				return;
			} else if (atoresMeio.remove(ator)) {
				return;
			} else if (atoresAnimado.remove(ator)) {
				return;
			} else if (atoresFrente.remove(ator)) {
				return;
			}

		}
	}


	public void excluirDesenho(Desenho desen) {

		if (desen instanceof ToupetyComponente) {
			if (componentes.remove(desen)) {
				componentesNomes.remove(desen.getNome());
			}
		}
		else
		if (desen instanceof EmissorParticulas) {
			emissores.values().remove(desen);
		}
		else
		if (desen instanceof Animado) {
			animados.remove(desen);
		}
		
		
		if (desen instanceof Ator) {
			if (atores.remove(((Ator) desen).getEntidade().getIdentificador()) != null) {
				((Ator) desen).getEntidade().dispose();
			}
		} else {
			desen.dispose();
		}

		super.excluirDesenho(desen);
	}

	public void verificaExcluirAnimado(Animado anim) {
		if (anim.excluido()) {
			excluirDesenho(anim);
		}
	}

	// FIXME ESSE METODO N ESTA EXCLUINDO UM COMPONENTE CORRETAMENTE
	public void verificaExcluirComponente(ToupetyComponente componente) {
		if (componente.excluido()) {
			excluirDesenho(componente);
		}
	}


	@Override
	public void atualizar(GameContainer container, int d) throws GXException {

		int delta = stepSize;
		// TODO PAUSE GERAL NO MOTOR
		if (container.getInput().isKeyPressed(Input.KEY_P)
				&& !controlesTravados) {
			setPausado(!estaPausado());
		}

		if (pausado && pausadoTexto) {
			return;
		}

		totalDelta += d;
		if (d > 500) {
			totalDelta = stepSize * 5;
			System.out.println("ESTRANHO D > 500");
		}

		long cont = 0;
		while (totalDelta > stepSize) {
			cont++;

			try {
				if (isTerremoto()) {
					terremotoCont--;
					if (terremotoCont <= 0)
						terremoto = false;
				}

				if (!pausado) {

					mundoFisico.step(stepSize * 0.01f);
				}
				totalDelta -= stepSize;

				if (!pausado) {

					Ator atoresArray[] = atores.values().toArray(new Ator[0]);
					for (Ator ator : atoresArray) {
						atualizaFisica(ator, delta);
						ator.atualizar(delta);
						verificarAreasAcao(ator);
						verificaExcluirAtor(ator);
					}

					Animado animadosArray[] = animados.toArray(new Animado[0]);
					for (Animado animado : animadosArray) {
						animado.atualizar(delta);
						verificaExcluirAnimado(animado);
					}

					try {
						Ator arma = (Ator) ((EntidadeJogavel) jogavel
								.getEntidade()).getArma().getAtor();
						arma.atualizar(delta);
					} catch (Exception e) {
						tratarExcecao(e);
					}

					for (EmissorParticulas emi : emissores.values()) {
						emi.atualizar(delta);
					}
				}

				ToupetyComponente[] componentesArray = componentes
						.toArray(new ToupetyComponente[0]);
				for (ToupetyComponente componente : componentesArray) {
					verificaExcluirComponente(componente);
				}

				Object itl[] = listerners.toArray();
				for (Object lis : itl) {
					Listerner l = (Listerner) lis;
					listernerAtual = l;
					l.atualizar(delta);
					listernerAtual = null;
					if (l.excluido()) {
						listerners.remove(l);
					}
				}

				// Atualiza o painel de estado.
				painelEstado.atualizar();
				super.atualizar(container, delta);

			} catch (Throwable cause) {
				tratarExcecao(cause);
			}
		}

	}

	public void setFatorZX(float fatorZX) {
		this.fatorZX = fatorZX;
	}

	public void setFatorZY(float fatorZY) {
		this.fatorZY = fatorZY;
	}

	public void setCameraXFator(float cameraXFator) {
		this.cameraXFator = cameraXFator;
	}

	public void setCameraYFator(float cameraYFator) {
		this.cameraYFator = cameraYFator;
	}

	public void setPausado(boolean pausado) {
		this.pausado = pausado;
		this.pausadoTexto = pausado;
	}

	public void setPausado(boolean pausado, boolean mostrarTexto) {
		this.pausado = pausado;
		this.pausadoTexto = (pausado && mostrarTexto);
	}

	public boolean estaPausado() {
		return pausado;
	}

	public void atualizarCamera() {

	}

	int variacaox = 2, variacaoy = 2;

	@Override
	public void desenhar(GameContainer container, Graphics g) {

		paralaxeFundo.desenhe(g);

		if (isTerremoto()) {
			g.translate((getCameraX() + variacaox), (getCameraY() + variacaoy));
			variacaox = (-variacaox);
			variacaoy = (-variacaoy);
		} else {
			g.translate(getCameraX(), getCameraY());
		}

		float x = -jogavel.getCorpo().getPosition().getX()
				+ (container.getWidth() / cameraXFator);
		float y = -jogavel.getCorpo().getPosition().getY()
				+ (container.getHeight() / cameraYFator);

		fixosFundo.desenhe(g);
		float fx = fatorZX * getCameraX();
		float fy = fatorZY * getCameraY();
		fixosFundo.ajustePosicao(fx, fy);

		super.desenhar(container, g);

		if (Motor.obterInstancia().isModoDebug()) {
			desenhaAreasAcao(g);
			desenharCorpos(g);
		}

		cameraX = x;
		cameraY = y;

		if (cameraYMaximo != 0 && cameraYMinimo != 0) {
			if (cameraY > cameraYMaximo) {
				cameraY = cameraYMaximo;
			} else if (cameraY < cameraYMinimo) {
				cameraY = cameraYMinimo;
			}
		}

		if (cameraXMaximo != 0 && cameraXMinimo != 0) {
			if (cameraX > cameraXMaximo) {
				cameraX = cameraXMaximo;
			} else if (cameraX < cameraXMinimo) {
				cameraX = cameraXMinimo;
			}
		}

		if (!travaCamera) {
			setCameraX(cameraX);
			setCameraY(cameraY);
		}

		// APOS O RESET TRANSFORM GARANTE QUE SERÃO FIXOS NA TELA
		g.resetTransform();

		desenharComponentes(g);

		fixosFrente.desenhe(g);

		painelEstado.desenhar(g);
		if (Motor.obterInstancia().isModoDebug())
			g.drawString("CAMERA: X: " + (-getCameraX()) + " Y:"
					+ (-getCameraY()), container.getWidth() / 2, 50);

		if (pausado && pausadoTexto)
			g.drawString("PAUSADO", container.getWidth() / 2, container
					.getHeight() / 2);
	}

	public void desenharCorpos(Graphics g) {

		DesenhadorCorpo desenhador = new DesenhadorCorpo();

		for (int i = 0; i < mundoFisico.getBodies().size(); i++) {
			Body body = mundoFisico.getBodies().get(i);
			desenhador.desenhar(body, g);

		}
	}

	/**
	 * Desenha os componentes
	 */
	private void desenharComponentes(Graphics g) {
		for (ToupetyComponente componente : componentes) {
			componente.desenhar(g);
		}
	}

	int contesf = 0;

	public void atualizaFisica(Ator ator, int delta) {
		if (ator.getEntidade().getTipo() == TipoEntidade.escudo)
			return;

		((EntidadeAnimada) ator.getEntidade()).atualizarFisica(delta);
	}

	/**
	 * Verifica se um ator colidiu em alguma caixa de colisao de outro.
	 * Informando o colidido com o evento de colisao.
	 * 
	 * @param alvo
	 * @return
	 */
	public EventoColisaoCorpoColisao verificaColisao(Ator alvo) {
		EventoColisaoCorpoColisao ret = null;
		String caixaColisao;

		for (Ator ator : atores.values()) {
			if (ator != alvo) {
				caixaColisao = alvo.colideCaixaColisao(ator);

				if (caixaColisao != null) {
					ret = new EventoColisaoCorpoColisao(caixaColisao, ator);

					EventoColisao col = new EventoColisao(ator.getEntidade(),
							this);
					col.setNormalX(0);
					col.setNormalY(0);
					col.setPontoColisao(ator.getCorpo().getPosition());

					if (ator.getEntidade() != null)
						try {
							ator.getEntidade().atualizar(col);
						} catch (GXException e) {
							tratarExcecao(e);
						}

				}
			}
		}

		return ret;
	}

	/**
	 * Verifica colisão nas areas de ação
	 * 
	 * @param colisivel
	 */
	public void verificarAreasAcao(Colisivel colisivel) {
		for (AreaAcao area : areasAcao) {
			area.verificarExecutar(colisivel);
		}
	}

	/**
	 * Desenha o contorno das ares de ação para debug.
	 * 
	 * @param g
	 */
	public void desenhaAreasAcao(Graphics g) {
		for (AreaAcao area : areasAcao) {
			area.desenhar(g);
		}
	}

	public void adicionarAreaAcao(AreaAcao area) {
		areasAcao.add(area);
	}

	public void adicionarJuncao(Joint joint) {
		mundoFisico.add(joint);
	}

	public void setJogavel(Ator jogavel) {
		if (this.jogavel == null) {
			((EntidadeJogavel) jogavel.getEntidade())
					.setPainelEstado(painelEstado.getLogEstado());

			this.jogavel = jogavel;
			this.arma = (Ator) ((EntidadeJogavel) jogavel.getEntidade())
					.getArma().getAtor();
			this.arma.setFace(((EntidadeJogavel) jogavel.getEntidade())
					.getArma().getFaceOrientacao());

			if (arma != null)
				adicionarAnimados(arma);

			adicionarAtor(jogavel, NivelCena.animados, 0);
		}
	}

	public Ator getJogavel() {
		return jogavel;
	}

	public void adicionarPlataforma(Plataforma plat) {
		plat.getCorpo().setExclusionKey(NivelCena.plataforma);
		plat.getCorpo().addExclusionKey(NivelCena.plataforma);
		plataformas.add(plat);
		mundoFisico.add(plat.getCorpo());

	}

	private void excluirColisao(Colisivel ator, int nivel) {

		ator.getCorpo().addExclusionKey(new Integer(nivel));
	}

	/**
	 * Remove do mundo colisivel
	 */
	public void forceExcluirColisivelMundoFisico(Colisivel colisivel) {
		mundoFisico.remove(colisivel.getCorpo());
	}

	public void forceAdicionarMundoFisico(Colisivel colisivel, int excluidos) {

		if ((excluidos & NivelCena.animados) > 0) {
			excluirColisao(colisivel, NivelCena.animados);
		}
		if ((excluidos & NivelCena.frente) > 0) {
			excluirColisao(colisivel, NivelCena.frente);
		}
		if ((excluidos & NivelCena.meio) > 0) {
			excluirColisao(colisivel, NivelCena.meio);
		}
		if ((excluidos & NivelCena.fundo) > 0) {
			excluirColisao(colisivel, NivelCena.fundo);
		}
		if ((excluidos & NivelCena.plataforma) > 0) {
			excluirColisao(colisivel, NivelCena.plataforma);
		}

		mundoFisico.add(colisivel.getCorpo());// era remove
	}

	/**
	 * Força a exclusão de um ator.
	 * 
	 * @param ator
	 */
	public void forceExclusaoAtor(Ator ator) {
		forceExcluirColisivelMundoFisico(ator);
		forceExcluirDesenho(ator);
		atores.remove(ator);
		atoresFundo.remove(ator);
		atoresMeio.remove(ator);
		atoresAnimado.remove(ator);
		atoresFrente.remove(ator);
	}

	@Override
	public void forceExcluirDesenho(Desenho desenho) {
		animados.remove(desenho);
		super.forceExcluirDesenho(desenho);
	}

	/**
	 * Força a exclusão de uma plataforma.
	 * 
	 * @param plataforma
	 */
	public void forceExcluirPlataforma(Plataforma plataforma) {
		plataformas.remove(plataforma);
		mundoFisico.remove(plataforma.getCorpo());
	}

	public void adicionarEmissores(EmissorParticulas anim, int nivel) {

		String identificador = anim.getIdentificacao();
		if (identificador == null) {
			identificador = anim.getNome() + "_" + emissioresInseridos;
		}

		emissores.put(identificador, anim);
		adicionarDesenho(anim, nivel);
		emissioresInseridos++;
	}

	public void adicionarAnimacao(Animado anim, int nivel) {

		animados.add(anim);
		adicionarDesenho(anim, nivel);
	}

	public void adicionarDesenho(Desenho desenho, int nivel) {
		switch (nivel) {
		case NivelCena.fixosFrente:
			fixosFrente.adicionarDesenho(desenho);
			break;
		case NivelCena.fixosFundo:
			fixosFundo.adicionarDesenho(desenho);
			break;
		}
		super.adicionarDesenho(desenho, nivel);
	}

	public void adicionarParalaxeFundo(Desenho desenho) {
		paralaxeFundo.adicionarDesenho(desenho);
	}

	/**
	 * Adiciona um componente.
	 * 
	 * @param componente
	 */
	public void adicionarComponente(ToupetyComponente componente) {
		componentes.add(componente);
		componentesNomes.put(componente.getNome(), componente);
	}

	/**
	 * Verifica a existencia de um componente.
	 * 
	 * @param nome
	 * @return
	 */
	public boolean possuiComponente(String nome) {
		return componentesNomes.containsKey(nome);
	}

	public void adicionarAtor(Ator ator, int nivel, int excluidos) {
		adicionarAtor(ator, nivel, excluidos, false);
	}

	public void adicionarAtor(Ator ator, int nivel, int excluidos,
			boolean semCorpo) {

		ator.setMundoFisico(mundoFisico);

		String identificador = ator.getEntidade().getIdentificador();

		if (identificador == null) {
			identificador = ator.getEntidade().getNome() + "_"
					+ atoresInseridos;
			ator.getEntidade().setIdentificador(identificador);
		}

		if (atores.containsKey(identificador)) {
			identificador = identificador + "_" + atoresInseridos;
			ator.getEntidade().setIdentificador(identificador);
		}

		atores.put(identificador, ator);

		atoresInseridos++;

		ator.getCorpo().setExclusionKey(new Integer(nivel));

		if ((excluidos & NivelCena.animados) > 0) {
			excluirColisao(ator, NivelCena.animados);
		}
		if ((excluidos & NivelCena.frente) > 0) {
			excluirColisao(ator, NivelCena.frente);
		}
		if ((excluidos & NivelCena.meio) > 0) {
			excluirColisao(ator, NivelCena.meio);
		}
		if ((excluidos & NivelCena.fundo) > 0) {
			excluirColisao(ator, NivelCena.fundo);
		}
		if ((excluidos & NivelCena.plataforma) > 0) {
			excluirColisao(ator, NivelCena.plataforma);
		}
		if ((excluidos & NivelCena.fixosFrente) > 0) {
			excluirColisao(ator, NivelCena.fixosFrente);
		}
		if ((excluidos & NivelCena.fixosFundo) > 0) {
			excluirColisao(ator, NivelCena.fixosFundo);
		}

		if (!semCorpo) {
			mundoFisico.add(ator.getCorpo());

			if (ator.getCorpo() instanceof SuperCorpo) {
				SuperCorpo sc = (SuperCorpo) ator.getCorpo();

				for (Body b : sc.getCorpos())
					mundoFisico.add(b);

				for (Joint j : sc.getJuncoes())
					mundoFisico.add(j);
			}
		}

		switch (nivel) {
		case NivelCena.animados:
			adicionarAnimados(ator);
			atoresAnimado.add(ator);
			break;
		case NivelCena.frente:
			adicionarFrente(ator);
			atoresFrente.add(ator);
			break;
		case NivelCena.fundo:
			adicionarFundo(ator);
			atoresFundo.add(ator);
			break;
		case NivelCena.meio:
			adicionarMeio(ator);
			atoresMeio.add(ator);
			break;
		case NivelCena.fixosFrente:
			fixosFrente.adicionarDesenho(ator);
			break;
		case NivelCena.fixosFundo:
			fixosFundo.adicionarDesenho(ator);
			break;
		default:
			break;
		}

	}

	@Override
	public void collisionOccured(CollisionEvent event) {
		try {
			Colisivel reg = (Colisivel) event.getBodyA().getPrivateData();
			Colisivel reg2 = (Colisivel) event.getBodyB().getPrivateData();

			if (reg == null || reg2 == null)
				return;

			EventoColisao col = new EventoColisao(reg2.getEntidade(), this);
			col.setNormalX(event.getNormal().getX());
			col.setNormalY(event.getNormal().getY());
			col.setPontoColisao(event.getPoint());

			if (reg.getEntidade() != null)
				reg.getEntidade().atualizar(col);

			col = new EventoColisao(reg.getEntidade(), this);
			col.setNormalX(event.getNormal().getX());
			col.setNormalY(event.getNormal().getY());
			col.setPontoColisao(event.getPoint());

			if (reg2.getEntidade() != null)
				reg2.getEntidade().atualizar(col);

		} catch (Throwable cause) {
			tratarExcecao(cause);
		}

	}

	public PainelEstado getPainelEstado() {
		return painelEstado;
	}

	public void setPainelEstado(PainelEstado painelEstado) {
		this.painelEstado = painelEstado;
	}

	/**
	 * Verifica se um ator esta contido na cena.
	 * 
	 * @param ator
	 * @return
	 */
	public boolean contemAtor(Ator ator) {
		return (atoresFundo.contains(ator)) || (atoresMeio.contains(ator))
				|| (atoresAnimado.contains(ator))
				|| (atoresFrente.contains(ator));
	}

	public void setTravaCamera(boolean travaCamera) {
		this.travaCamera = travaCamera;
	}

	public boolean isTravaCamera() {
		return travaCamera;
	}

	public void setControlesTravados(boolean controlesTravados) {
		this.controlesTravados = controlesTravados;
	}

	public boolean controlesTravados() {
		return controlesTravados;
	}

	public void escondaEntidadeJogavel() {
		painelEstado.setVisivel(false);
		jogavel.setVisivel(false);
		((Ator) ((EntidadeJogavel) jogavel.getEntidade()).getArma().getAtor())
				.setVisivel(false);
	}

	public void exibaEntidadeJogavel() {
		painelEstado.setVisivel(true);
		jogavel.setVisivel(true);
		((Ator) ((EntidadeJogavel) jogavel.getEntidade()).getArma().getAtor())
				.setVisivel(true);
	}

	public Ator getAtor(String identificador) {
		return atores.get(identificador);
	}

	public float getCameraXMinimo() {
		return cameraXMinimo;
	}

	public void setCameraXMinimo(float cameraXMinimo) {
		this.cameraXMinimo = cameraXMinimo;
	}

	public float getCameraYMinimo() {
		return cameraYMinimo;
	}

	public void setCameraYMinimo(float cameraYMinimo) {
		this.cameraYMinimo = cameraYMinimo;
	}

	public float getCameraXMaximo() {
		return cameraXMaximo;
	}

	public void setCameraXMaximo(float cameraXMaximo) {
		this.cameraXMaximo = cameraXMaximo;
	}

	public float getCameraYMaximo() {
		return cameraYMaximo;
	}

	public void setCameraYMaximo(float cameraYMaximo) {
		this.cameraYMaximo = cameraYMaximo;
	}

	public void adicionarListerner(Listerner listerner) {
		listerners.add(listerner);
		listerner.aoAdicionar();
	}

	public void excluirListerner(Listerner listerner) {
		listerners.remove(listerner);
	}

	public void iniciarTerremoto() {
		terremoto = true;
		terremotoCont = 600;
	}

	public boolean isTerremoto() {
		return terremoto;
	}

	public void finalizar() {

		super.finalizar();

		for (ToupetyComponente comp : componentes) {
			comp.desativar();
			comp.excluir();
		}

		for (Ator a : atores.values()) {
			EntidadeJogavel entiJogavel = Motor.obterInstancia()
					.getJogavelCarregado();
			Entidade ae = a.getEntidade();

			if (ae != entiJogavel && ae != entiJogavel.getArma()
					&& ae != entiJogavel.getEscudo())
				a.getEntidade().dispose();
		}

		listerners = null;
		componentesNomes = null;
		componentes = null;
		atoresFundo = null;
		atoresMeio = null;
		atoresAnimado = null;
		atoresFrente = null;
		emissores = null;
		atores = null;
		animados = null;
		plataformas = null;
		areasAcao = null;
		jogavel = null;
		arma = null;
		mundoFisico = null;
	}

	// TODO todos devem possuir um identificador
	public Desenho getDesenho(String nome) {

		Desenho d = emissores.get(nome);
		if (d == null)
			d = atores.get(nome);

		return d;
	}

	@Override
	public Listerner getListernerAtual() {
		return listernerAtual;
	}
}
