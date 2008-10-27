package br.org.gamexis.plataforma.cena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
import br.org.gamexis.plataforma.cena.evento.Listerner;
import br.org.gamexis.plataforma.cena.fisica.SuperCorpo;
import br.org.gamexis.plataforma.cena.util.DesenhadorCorpo;
import br.org.gamexis.plataforma.entidade.AreaAcao;
import br.org.gamexis.plataforma.entidade.EntidadeAnimada;
import br.org.gamexis.plataforma.entidade.EntidadeControlavel;
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
public class Cenario extends CenaBase implements CenarioIF, CollisionListener {

	/**
	 * Trava a camera para controle manual
	 */
	private boolean travaCamera = false;
	private boolean terremoto = false;
	private int terremotoCont = 0;
	
	private Ator jogavel = null;// jogador
	private Ator arma = null;// arma

	private World mundoFisico; // MUNDO FISICO
	private List<Listerner> listerners = new ArrayList<Listerner>();
	
	
	HashMap<String, ToupetyComponente> componentesNomes = new HashMap<String, ToupetyComponente>();
	private List<ToupetyComponente> componentes = new ArrayList<ToupetyComponente>();

	private List<Ator> atoresFundo = new ArrayList<Ator>();
	private List<Ator> atoresMeio = new ArrayList<Ator>();
	private List<Ator> atoresAnimado = new ArrayList<Ator>();
	private List<Ator> atoresFrente = new ArrayList<Ator>();

	/**
	 * Para incluir atores dinamicamente, e evitar concorrencia no loop de update
	 */
	private Queue<TemporarioInclusaoAtor> tempAtores = new LinkedList<TemporarioInclusaoAtor>();
	private boolean flagInserirTemporarios = false; //controla a inserção de atores temporarios
	
	private List<EmissorParticulas> emissores = new ArrayList<EmissorParticulas>();

	// ator
	private List<Ator> atores = new ArrayList<Ator>();// todos os atores
	private List<Animado> animados = new ArrayList<Animado>();// animados
	private List<Plataforma> plataformas = new ArrayList<Plataforma>();

	private List<Ator> todos = new ArrayList<Ator>();
	
	// NIVEL ADICIONAL DE OBJETOS FIXOS QUE SEGUEM A CAMERA
	private Tela fixosFrente = new Tela();
	private Tela fixosFundo = new Tela();

	private float comprimento = 800;
	private float altura = 600;

	private float cameraX = 0;
	private float cameraY = 0;
	private PainelEstado painelEstado;

	private boolean pausado = false;
	private boolean pausadoTexto = false;
	private boolean controlesTravados = false;

	// Configurações da camera
	private float cameraXFator = 2;
	private float cameraYFator = 2;
	private float fatorZY = 0;
	private float fatorZX = 0;	
	

	//Maximos e minimos da posição da camera
	private float cameraXMinimo = 0;
	private float cameraYMinimo = 0;
	private float cameraXMaximo = 0;
	private float cameraYMaximo = 0;
	
	//SINCRONIZAÇÃO DAS FREQUÊNCIAS
	int totalDelta;
	int stepSize = 5;
	
	/**
	 * Areas de ação.
	 */
	private List<AreaAcao> areasAcao = new ArrayList<AreaAcao>();

	public Cenario(float comprimento, float altura) {
		//mundoFisico = new World(new Vector2f(0.0f, 9.8f), 5);		
//		mundoFisico = new World(new Vector2f(0.0f, 10f), 5,
//				new QuadSpaceStrategy(20, 5));

		mundoFisico = new World(new Vector2f(0.0f, 10f), 20);
				
		mundoFisico.addListener(this);

		this.comprimento = comprimento;
		this.altura = altura;

	}

	public Cenario() {
		this(800, 600);
	}

	@Override
	public void iniciar(GameContainer container) throws SlickException {
		super.iniciar(container);
		painelEstado.getBarraEstado("energia").setVisivel(true);
		
		//configurando a posição do jogavel
		//dono.setCorFundo(0.235294118f,0.247058824f,0.266666667f)
		Entrada entrada = Motor.obterInstancia().popEntrada();
		if(entrada != null) {
			((SuperCorpo)jogavel.getCorpo()).moveAll(entrada.x, entrada.y);
		}

//		dono.setCameraY(-153)
//		dono.setCameraX(-182)
//
//		dono.setCameraXMinimo(-321)
//		dono.setCameraXMaximo(-213)
//
//		dono.setCameraYMinimo(-650)
//		dono.setCameraYMaximo(-150)
	}
	
	public void verificaExcluirAtor(Ator ator) {
		if (ator.excluido()) {
			removaDesenho(ator);
			mundoFisico.remove(ator.getCorpo());

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
	
	int contadorGC = 500;//para testes
	public void purgeExcluidos() {
		for (Desenho desen : getExcluidos()) {
			
			if (componentes.remove(desen))
				componentesNomes.remove(desen.getNome());
			
			animados.remove(desen);
						
			if(atores.remove(desen))
				((Ator)desen).getEntidade().dispose();
		}

		super.purgeExcluidos();
//		if(contadorGC < 1) {
//			System.gc();
//			contadorGC = 500;
//		} else {
//			contadorGC--;
//		}
	}

	public void verificaExcluirAnimado(Animado anim) {
		if (anim.excluido()) {
			removaDesenho(anim);
		}
	}

	public void verificaExcluirComponente(ToupetyComponente componente) {
		if (componente.excluido()) {
			removaDesenho(componente);
		}
	}

	/**
	 * Insere os atores temporarios.
	 * NOTA: foi criado para evitar execeção ce concorrência
	 */
	private void inserirTemporarios() {
		TemporarioInclusaoAtor tAtor;
		flagInserirTemporarios = true;
		
		Iterator<TemporarioInclusaoAtor> it = null;
//		synchronized (tempAtores) {
			while(!tempAtores.isEmpty()) {
				tAtor = tempAtores.poll();
				adicionarAtor(tAtor.ator, tAtor.nivel, tAtor.excluidos, tAtor.semCorpo);
				//it.remove();
			}
//		}
		flagInserirTemporarios = false;
	}
	
	@Override
	public void atualizar(GameContainer container, int d)
			throws GXException {
		
		long t = System.currentTimeMillis();
		
		boolean first = true;
		int delta = stepSize;
		
		if (container.getInput().isKeyPressed(Input.KEY_F9)
				&& !controlesTravados) {			
			setPausado(!estaPausado());
		}
		
		if (pausado && pausadoTexto) {
			return;
		}
			
		totalDelta += d;
		if(d > 500)	
			System.out.println("ESTRANHO D > 500");
		
//		if(d > 30) {
//			try {
//				totalDelta = 0;
//				Thread.sleep(15);
//				return;
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
		
		long cont = 0;
		while (totalDelta > stepSize) {
			cont++;
		
			//System.out.println("Contado:" + cont + " totalDelta:" + totalDelta + " d:" + d);
//			world.step(stepSize * 0.01f);
//			totalDelta -= stepSize;
			
			try {
					if(isTerremoto()) {
						terremotoCont--;
						if(terremotoCont <= 0)
							terremoto = false;
					}
					
					if (!pausado) {
						mundoFisico.step(stepSize * 0.01f);				
						atualizarControle(container, delta, jogavel);
					}
					totalDelta -= stepSize;
		
					if (!pausado) {
						long t2 = System.currentTimeMillis();
						for (Ator ator : atores) {
								atualizaFisica(ator, delta);
								ator.atualizar(delta);
								verificaExcluirAtor(ator);
								verificarAreasAcao(ator);
//								ScriptComportamento.conta++;
						}						
						//System.out.println("ATORES " + (System.currentTimeMillis() - t2));
						
//						if(ScriptComportamento.conta > 1) {
//							//System.out.println("CONTA " + ScriptComportamento.conta);
//						}
						
						for (Animado animado : animados) {
							animado.atualizar(delta);
							verificaExcluirAnimado(animado);
						}
			
						try {
							Ator arma = (Ator) ((EntidadeJogavel) jogavel.getEntidade())
									.getArma().getAtor();
							arma.atualizar(delta);
						} catch (Exception e) {
						}
			
						for (EmissorParticulas emi : emissores) {
							emi.atualizar(delta);
						}
					}
					
				for (ToupetyComponente componente : componentes) {
					verificaExcluirComponente(componente);
				}			
				
				Iterator<Listerner> itl = listerners.iterator();
				while(itl.hasNext()){
					Listerner l = itl.next();			
					l.atualizar(delta);				
					if(l.excluido()) {
						itl.remove();
					}
				}
				
				// Atualiza o painel de estado.
				painelEstado.atualizar();
				super.atualizar(container, delta);
				
				inserirTemporarios();
			} catch (Throwable cause) {
				tratarExcecao(cause);
			}
		}

		//System.out.println("TOTAL: " + (System.currentTimeMillis() - t) + "sobrou: " + totalDelta);
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
		
		if(isTerremoto()) {
			g.translate(getCameraX() + variacaox, getCameraY() + variacaoy);
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
		float fx = fatorZX * getCameraX();// (fatorZX*jogavel.getCorpo().getPosition().getX());
		float fy = fatorZY * getCameraY();// (fatorZY*jogavel.getCorpo().getPosition().getY());
		fixosFundo.ajustePosicao(fx, fy);

		super.desenhar(container, g);

		if (Motor.obterInstancia().isModoDebug()) {
			desenhaAreasAcao(g);
			desenharCorpos(g);
		}

		
//		if (((jogavel.getX() + comprimento / 2) <= comprimento)
//				&& !((jogavel.getX() + comprimento / 2) <= 0)) {
			cameraX = x;
//		} else {
//			if(((jogavel.getX() + comprimento / 2) > comprimento)) {
//				cameraX = comprimento;
//			} else if((jogavel.getX() + comprimento / 2) < 0) {
//				cameraX = getCameraX();
//			}
//		}

//		if (((jogavel.getY() + altura / 2) <= altura)
//				&& !((jogavel.getY() + altura / 2) <= 0)) {			
			cameraY = y;			
//		} else {
//			if(((jogavel.getY() + altura / 2) > altura)) {
//				cameraY = altura;
//			} else if((jogavel.getY() + altura / 2) < 0) {
//				cameraY = getCameraY();
//			}
//		}

		if(cameraYMaximo != 0 && cameraYMinimo != 0) {
			if(cameraY > cameraYMaximo) {
				cameraY = cameraYMaximo;
			} else if(cameraY < cameraYMinimo) {
				cameraY = cameraYMinimo;
			}
		}
		
		if(cameraXMaximo != 0 && cameraXMinimo != 0) {
			if(cameraX > cameraXMaximo) {
				cameraX = cameraXMaximo;
			} else if(cameraX < cameraXMinimo) {
				cameraX = cameraXMinimo;
			}		
		}
		
		// g.translate(cameraX, cameraY);
		if (!travaCamera) {
			setCameraX(cameraX);
			setCameraY(cameraY);
		}

		g.resetTransform(); // APOS O RESET TRANSFORM GARANTE QUE SERÃO FIXOS NA
		// TELA

		desenharComponentes(g);

		fixosFrente.desenhe(g);

		painelEstado.desenhar(g);
		if (Motor.obterInstancia().isModoDebug())
			g.drawString("CAMERA: X: " + (-getCameraX()) + " Y:" + (-getCameraY()),
					container.getWidth() / 2, 50);

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

	public void atualizarControle(GameContainer container, int delta, Ator ator)
			throws Exception {

		if (ator.getEntidade() instanceof EntidadeControlavel) {
			((EntidadeControlavel) ator.getEntidade()).atualizarControle(
					container, this, delta);
		}
	}

	int contesf = 0;

	public void atualizaFisica(Ator ator, int delta) {
		if(ator.getEntidade().getTipo() == TipoEntidade.escudo)
			return;
		
		((EntidadeAnimada) ator.getEntidade()).atualizarFisica(delta);
	}

	public void verificaColisao(int delta) {
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
		// super.forceExcluirDesenho(plataforma);
		mundoFisico.remove(plataforma.getCorpo());
	}

	public void adicionarEmissores(EmissorParticulas anim, int nivel) {

		emissores.add(anim);
		adicionarDesenho(anim, nivel);
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
		
		
		if(estaIniciada() && !flagInserirTemporarios ) {
//			synchronized (tempAtores) {
			TemporarioInclusaoAtor tAtor = new TemporarioInclusaoAtor(ator, nivel, excluidos, semCorpo);
			tempAtores.add(tAtor);
//			}
			return;
		}
			
		atores.add(ator);

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
				// ator.getCorpo().setPosition(ator.getX(), ator.getY());
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
	
	public void getAtor(String nome) {
//		atores.
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
	}
	
	public void iniciarTerremoto() {
		terremoto = true;
		terremotoCont = 600;
		//TODO IMPLEMENTAR TERREMOTO
	}
	
	public boolean isTerremoto() {
		return terremoto;
	}
	
	class TemporarioInclusaoAtor {
		Ator ator;
		int nivel; 
		int excluidos;
		boolean semCorpo;
		
		public TemporarioInclusaoAtor(Ator ator, int nivel, int excluidos,
				boolean semCorpo) {
			this.ator = ator;
			this.nivel = nivel;
			this.excluidos = excluidos;
			this.semCorpo = semCorpo;
		}
		
	}
	
	public void finalizar() {
		for(ToupetyComponente comp : componentes) {
			comp.desativar();
			comp.excluir();
		}
		
		listerners = null;
		componentesNomes = null;
		componentes = null;
		atoresFundo = null;
		atoresMeio = null;
		atoresAnimado = null;
		atoresFrente = null;
		tempAtores = null;
		emissores = null;
		atores = null;
		animados = null;
		plataformas = null;
		areasAcao = null;
		jogavel = null;
		arma = null;
		mundoFisico = null;
	}
}
