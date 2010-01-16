package br.org.gamexis.plataforma;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GLContext;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.renderer.Renderer;

import br.org.gamexis.plataforma.cena.Ator;
import br.org.gamexis.plataforma.cena.CarregadorCenario;
import br.org.gamexis.plataforma.cena.Cena;
import br.org.gamexis.plataforma.cena.Cenario;
import br.org.gamexis.plataforma.cena.Desenho;
import br.org.gamexis.plataforma.cena.Efeito;
import br.org.gamexis.plataforma.cena.Entrada;
import br.org.gamexis.plataforma.cena.NivelCena;
import br.org.gamexis.plataforma.cena.componentes.BalaoTexto;
import br.org.gamexis.plataforma.cena.componentes.PainelEstado;
import br.org.gamexis.plataforma.cena.componentes.util.CaixaImagem;
import br.org.gamexis.plataforma.debug.Console;
import br.org.gamexis.plataforma.entidade.CarregadorEntidade;
import br.org.gamexis.plataforma.entidade.Entidade;
import br.org.gamexis.plataforma.entidade.EntidadeJogavel;
import br.org.gamexis.plataforma.entidade.TipoEntidade;
import br.org.gamexis.plataforma.motor.EntradaPerifericos;
import br.org.gamexis.plataforma.motor.RecursosFactory;
import br.org.gamexis.plataforma.motor.Salvavel;
import br.org.gamexis.plataforma.motor.Transicao;
import br.org.gamexis.plataforma.motor.configuracao.CarregadorConfiguracaoInicial;
import br.org.gamexis.plataforma.motor.configuracao.ConfiguracaoGeral;
import br.org.gamexis.plataforma.motor.configuracao.ConfiguracaoInicial;
import br.org.gamexis.plataforma.motor.configuracao.GerenciadorConfiguracaoGeral;
import br.org.gamexis.plataforma.motor.configuracao.GerenciadorSalvos;
import br.org.gamexis.plataforma.motor.configuracao.ConfiguracaoInicial.Cursor;
import br.org.gamexis.plataforma.motor.configuracao.entrada.ComandoEntrada;
import br.org.gamexis.plataforma.motor.filesystem.FileSystemFactory;
import br.org.gamexis.plataforma.script.ScriptComportamento;

/**
 * Classe motora do jogo, onde o loop principal é executado. Se comporta tambem
 * como um facade das funcionalidades basicas do motor, como carregamento de
 * configuraçao, mudança de cenas, entre outros.
 * 
 * @author abraaoisvioliveirasouzadocarmo
 * 
 */
public class Motor extends BasicGame {

	private static final String SCRIPT_MOTOR = "motor.groovy";
	private Font fonteFalas;
	private int maximo_intervalo_texto = 10;

	/**
	 * 
	 * <b>Formato Versionamento:</b> M.N.C build b [Final | Beta]<br>
	 * <b>M</b> - grandes mudanças mudança de paradigmas <br>
	 * <b>N</b> - novas funcionalidades <br>
	 * <b>C</b> - correçoes e pequenas ajustes <br>
	 * <b>b</b> - builds <b>F</b> - Final <b>B</b> - Beta
	 */
	private String versao = "1.0.0.Beta build 250";

	private static Motor instancia;
	public static float gravidade = 9.8f;
	private boolean modoDebug = false;
	private boolean modoDesenvolvedor = false;
	private Transicao transicao;
	private HashMap<String, Music> musicas = new HashMap<String, Music>();
	private boolean flagMudarCena;
	private boolean flagCarregarProximaCena;
	private String proximaCena;
	private AppGameContainer appGameContainer;
	public static final String ARQUIVO_LOG = "toupety.log";
	public Entidade entidadeCursor;
	private boolean cursorCapturado = false;
	private boolean cursorCapturadoDebug = false;
	private boolean exibirLogEstadoJogavel = false;

	/**
	 * controle da posição de Toupety na entrada de cenas
	 */
	private Entrada entrada = null;

	/**
	 * Slot de jogo salvo - Zero por padrao
	 */
	private int slotSalvo = 0;

	/**
	 * Jogavel instanciado e comum a todas as cenas.
	 */
	private EntidadeJogavel jogavelCarregado;

	/**
	 * Painel de estado instanciado e comum a todas as cenas.
	 */
	private PainelEstado painelEstado;

	/**
	 * Variaveis globais
	 */
	HashMap<String, Object> variaveis = new HashMap<String, Object>();

	/**
	 * Temporarias
	 */
	Queue<Object> variaveisTemporarias = new LinkedList<Object>();

	/**
	 * Controle do salvo
	 */
	ScriptComportamento scriptMotor;

	long tInicial;
	long fps;

	private Cena cenaAtual;
	private ConfiguracaoInicial inicial;
	private ConfiguracaoGeral configGeral;

	private Console console;

	private String cenaInicialDebug = null;

	public static Motor obterInstancia() {
		if (instancia == null)
			instancia = new Motor();

		return instancia;
	}

	public Motor() {
		super("GAMEXIS");
	}

	public void setAppGameContainer(AppGameContainer appGameContainer) {
		this.appGameContainer = appGameContainer;
	}

	public AppGameContainer getAppGameContainer() {
		return appGameContainer;
	}

	public Cena getCenaAtual() {
		return cenaAtual;
	}

	public static void main(String[] args) {
		try {

			Motor mot = obterInstancia();

			mot.resolvaComandosEntrada(args);
			Renderer.setRenderer(Renderer.VERTEX_ARRAY_RENDERER);
			Renderer
					.setLineStripRenderer(Renderer.QUAD_BASED_LINE_STRIP_RENDERER);

			AppGameContainer app = new AppGameContainer(mot);
			app.setShowFPS(false);

			mot.setAppGameContainer(app);
			app.start();
		} catch (SlickException e) {
			if (Motor.obterInstancia() != null) {
				Motor.obterInstancia().tratarExcecao(e);
			} else {
				e.printStackTrace();
			}

		}
	}

	public void resolvaComandosEntrada(String[] args) {
		ComandoEntrada comando = null;

		if (args.length >= 2) {
			for (int i = 0; i < args.length; i++) {
				if (comando != null) {
					executeComandoEntrada(comando, args[i]);
					comando = null;
				} else {
					comando = ComandoEntrada.valueOf(args[i]);
				}
			}
		}
	}

	private void executeComandoEntrada(ComandoEntrada comando, String parametro) {
		switch (comando) {
		case FS:
			FileSystemFactory.setFileSystem(parametro);
			break;
		case CENA:
			setCenaInicialDebug(parametro);
			break;
		}
	}

	public ConfiguracaoGeral getConfigGeral() {
		return configGeral;
	}

	public void setConfigGeral(ConfiguracaoGeral configGeral) {
		this.configGeral = configGeral;
	}

	public void setCenaInicialDebug(String cenaInicialDebug) {
		this.cenaInicialDebug = cenaInicialDebug;
	}

	public String getCenaInicialDebug() {
		return cenaInicialDebug;
	}

	public void initPadrao(GameContainer container) throws SlickException {
		inicial = new CarregadorConfiguracaoInicial().carregarConfiguracao();
		try {
			Logger logger = Logger.getLogger(ARQUIVO_LOG);
			FileHandler fh = new FileHandler(ARQUIVO_LOG, true);
			logger.addHandler(fh);
			logger.setLevel(Level.ALL);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
		} catch (Exception e) {
			tratarExcecao(e);
		}

		container.setTargetFrameRate(inicial.getFramerate());
		container.setIcon(inicial.getIcone());

		modoDesenvolvedor = inicial.isDebug();
		appGameContainer.setTitle(inicial.getTitulo() + " VERSAO: " + versao);

		carregarConfiguracao();
		aplicarConfiguracaoGeral();

		EntradaPerifericos.setInput(container.getInput());
		EntradaPerifericos.setControle(configGeral.getControles());

		container.getGraphics().setBackground(new Color(0.5f, 0.5f, 0.5f));
		if (cenaInicialDebug != null) {
			cenaAtual = new CarregadorCenario()
					.carregarCena(getCenaInicialDebug());
		} else {
			cenaAtual = new CarregadorCenario().carregarCena(inicial
					.getCenaInicial());
		}

		transicao = new Transicao(configGeral.getResolucao().getComprimento(),
				configGeral.getResolucao().getAltura());

		// transicao.setImagemBase(RecursosFactory.getInstancia().getImage(
		// RECURSOS_IMAGENS_FADE), 100, 100, 4);
		transicao.iniciarVoltando();

		configureCursor(inicial.getCursor());

		console = new Console();
		console.desativar();

		scriptMotor = RecursosFactory.getInstancia().getComportamento(
				SCRIPT_MOTOR, false);

		if (modoDesenvolvedor) {
			Logger.getLogger(Motor.ARQUIVO_LOG).log(
					Level.INFO,
					"Joysticks Encontrados:"
							+ container.getInput().getControllerCount());
			
			Logger.getLogger(Motor.ARQUIVO_LOG).log(Level.INFO,	"OpenGL Superior ou Igual a 2.0:" + GLContext.getCapabilities().OpenGL20);
		}
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		initPadrao(container);
	}

	public void exibirConsole() {

		if (!console.isAtivo()) {
			console.ativar();
			liberarCursor();

		}

	}

	public void esconderConsole() {
		capturarCursor();
		console.desativar();
	}

	public void desenharConsole(Graphics g) {
		console.desenhar();
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {

		if (console.isAtivo() && cursorCapturado) {
			liberarCursor();
		}

		if (entidadeCursor != null) {
			entidadeCursor.getAtor().atualizar(delta);
		}

		if (flagCarregarProximaCena) {
			flagCarregarProximaCena = false;
			if (cenaAtual != null) {
				cenaAtual.finalizar();
				cenaAtual = null;
			}

			RecursosFactory.getInstancia().limparBuffer();// LIMPAR BUFFER
			System.gc();
			cenaAtual = new CarregadorCenario().carregarCena(proximaCena);
		}

		if (container.getInput().isKeyPressed(Input.KEY_3)) {// SHOW FPS
			container.setShowFPS(!container.isShowingFPS());
		} else if (container.getInput().isKeyPressed(Input.KEY_0)) {

			if (modoDesenvolvedor) {
				modoDebug = !modoDebug;
				container.setShowFPS(!container.isShowingFPS());

				if (!modoDebug) {
					if (cursorCapturadoDebug) {
						capturarCursor();
					}
				} else {
					cursorCapturadoDebug = cursorCapturado;
					liberarCursor();
				}
			}

		} else if (container.getInput().isKeyPressed(Input.KEY_HOME)) {

			mudarFullScreenDisplay();

		} else if (container.getInput().isKeyPressed(Input.KEY_END)) {

			exibirConsole();
		} else if (container.getInput().isKeyPressed(Input.KEY_SLASH)) {
			if (modoDesenvolvedor)
				mudarCena(getCenaAtual().getNome());
		} else if (container.getInput().isKeyPressed(Input.KEY_2)) {
			if (modoDesenvolvedor)
				liberarCursorDebug();
		} else if (container.getInput().isKeyPressed(Input.KEY_8)) {
			if (modoDesenvolvedor) {
				if (!exibirLogEstadoJogavel)
					exibirLogEstadoJogavel();
				else
					esconderLogEstadoJogavel();
			}
		} else if (container.getInput().isKeyPressed(Input.KEY_4)) {
			if (modoDesenvolvedor) {
				jogavelCarregado.incrementeEnergiaVital(15);
			}
		} else if (container.getInput().isKeyPressed(Input.KEY_F5)) {
			if (modoDesenvolvedor) {
				jogavelCarregado.setEnergiaVitalMaxima(jogavelCarregado
						.getEnergiaVitalMaxima() + 15);
				jogavelCarregado.setFogueteCargaMaxima(jogavelCarregado
						.getFogueteCargaMaxima() + 100);
			}
		}

		if (transicao != null) {
			if (transicao.terminado()) {
				atualizarCenaAtual(container, delta);
			} else {
				if (!cenaAtual.estaIniciada()) {
					atualizarCenaAtual(container, delta);
				}
			}

		} else {
			atualizarCenaAtual(container, delta);
		}

		verificarCapturarMouse();
	}

	public void mudarFullScreenDisplay() throws SlickException {

		GameContainer container = getAppGameContainer();

		configGeral.getResolucao().setTelaCheia(
				!configGeral.getResolucao().isTelaCheia());

		Color corFundo = container.getGraphics().getBackground();

		aplicarConfiguracaoVideo();

		container.getGraphics().setBackground(corFundo);
	}

	public void verificarCapturarMouse() {
		if (modoDesenvolvedor) {
			int x = getAppGameContainer().getInput().getMouseX();
			int y = getAppGameContainer().getInput().getMouseY();

			if ((x <= 5 || x >= 795) || (y <= 2 || y >= 590)) {
				if (Mouse.isGrabbed()) {
					this.appGameContainer.setMouseGrabbed(false);

					Mouse.setCursorPosition(x, y);
				}
			} else if ((x >= 10 && x <= 790) || (y > 5 && y < 585)) {
				if (!Mouse.isGrabbed()) {
					this.appGameContainer.setMouseGrabbed(true);
				}
			}
		}
	}

	/**
	 * Atualiza a cena atual.
	 * 
	 * @param container
	 * @param delta
	 * @throws SlickException
	 */
	private void atualizarCenaAtual(GameContainer container, int delta)
			throws SlickException {
		try {
			if (!cenaAtual.estaIniciada())
				cenaAtual.iniciar(container);

			cenaAtual.atualizar(container, delta);

		} catch (Throwable e) {
			tratarExcecao(e);
		}
	}

	// TODO AJUSTAR EXCEÇÔES
	public void tratarExcecao(Throwable cause) {
		cause.printStackTrace();
		Logger.getLogger(Motor.ARQUIVO_LOG).log(Level.SEVERE, "erro motor:",
				cause.getStackTrace());
	}

	public void mudarCena(String cenaRef) {
		try {
			flagMudarCena = true;
			proximaCena = cenaRef;
			transicao.iniciarIndo();
		} catch (Exception e) {
			tratarExcecao(e);
		}
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {

		if(!GLContext.getCapabilities().OpenGL20)
			cenaAtual.limparFundo(g);
		
		cenaAtual.desenhar(container, g);
		desenharConsole(g);
		desenharEntidadeCursor(g);

		if (modoDebug) {
			g.drawString("MODO DEBUG", container.getWidth() / 2, 20);
			g.drawString("VERSAO " + versao, container.getWidth() / 2,
					container.getScreenHeight() - 50);

			if (getJogavelCarregado() != null) {
				Ator atorJog = (Ator) getJogavelCarregado().getAtor();

				Color old = g.getColor();
				g.setColor(Color.cyan);
				g.drawString("JOGAVEL(X,Y)" + atorJog.getX() + ", "
						+ atorJog.getY(), container.getWidth() / 2, 100);
				g.setColor(old);
			}
		}

		if (transicao != null) {
			if (!transicao.terminado()) {// TRANSIÇÃO
				transicao.desenhar(g);
			} else {
				if (!transicao.voltando() && transicao.terminado()) {
					if (flagMudarCena) {
						flagMudarCena = false;
						flagCarregarProximaCena = true;
					}
					transicao.iniciarVoltando();
					transicao.desenhar(g);
				}
			}
		}

		if (exibirLogEstadoJogavel) {
			desenharLogEstadoJogavel(g);
		}

	}

	private void desenharEntidadeCursor(Graphics g) {
		if (entidadeCursor != null) {
			g.resetTransform();
			((Ator) entidadeCursor.getAtor()).desenhar(g);
		}
	}

	public Integer getSlotSalvo() {
		return slotSalvo;
	}

	public void setSlotSalvo(int slotSalvo) {
		this.slotSalvo = slotSalvo;
	}

	/**
	 * Salva o jogo com a data atual como descrição.
	 */
	public void salvarJogo() {
		String descricao = getCenaAtual().getDescricao();
		salvarJogo(getSlotSalvo(), descricao);
	}

	/**
	 * Salva um jogo pela posição, chama o metodo do script pra fazer o
	 * salvamento
	 * 
	 * @param posicao
	 * @param descricao
	 */
	public void salvarJogo(Integer posicao, String descricao) {

		if (scriptMotor != null) {
			try {
				Salvavel salvo = new Salvavel();
				salvo.setIdentificador(posicao);

				if (salvo == null) {
					salvo = new Salvavel();
					salvo.setIdentificador(posicao);
				}

				salvo.setCena(getCenaAtual().getNome());
				salvo.setVariaveis(variaveis);
				float x = getJogavelCarregado().getAtor().getCorpo()
						.getPosition().getX();
				float y = getJogavelCarregado().getAtor().getCorpo()
						.getPosition().getY();
				salvo.setJogavelX(x);
				salvo.setJogavelY(y);
				salvo.setData(new Date());
				salvo.setDescricao(descricao);

				scriptMotor.execute("aoSalvar", new Object[] { this, salvo });
				new GerenciadorSalvos().salvar(salvo);


			} catch (Exception e) {
				tratarExcecao(e);
			}
		}
	}

	/**
	 * Carrega um jogo pela posição, chama o metodo do script pra fazer o
	 * carregaento
	 * 
	 * @param posicao
	 */
	public void carregarJogo(Integer posicao) {
		if (scriptMotor != null) {
			try {
				Salvavel salvo = obterPosicaoSalvo(posicao);// carregar(posicao);

				// DEPRECATED VERSÕES ANTIGAS
				setVariaveis(salvo.getVariaveis());
				setVariavel("posicaoJogavelX", String.valueOf(salvo
						.getJogavelX().floatValue()));
				setVariavel("posicaoJogavelY", String.valueOf(salvo
						.getJogavelY().floatValue()));
				// DEPRECATED

				float xj = salvo.getJogavelX().floatValue();
				float yj = salvo.getJogavelY().floatValue();
				putEntrada(xj, yj);

				mudarCena(salvo.getCena());

				scriptMotor.execute("aoCarregar", new Object[] { this, salvo });
			} catch (Exception e) {
				tratarExcecao(e);
			}
		}
	}

	public void salvarConfiguracao(ConfiguracaoGeral config) {
		try {
			new GerenciadorConfiguracaoGeral().salvarConfiguracao(config);
			setConfigGeral(config);
			aplicarConfiguracaoGeral();
		} catch (Exception e) {
			tratarExcecao(e);
		}
	}

	public ConfiguracaoGeral carregarConfiguracao() {
		ConfiguracaoGeral config = null;
		try {
			config = new GerenciadorConfiguracaoGeral().carregarConfiguracao();
			setConfigGeral(config);
		} catch (Exception e) {
			tratarExcecao(e);
		}

		return config;
	}

	public ConfiguracaoInicial getConfiguracaoInicial() {
		return inicial;
	}

	public void aplicarConfiguracaoGeral() {
		aplicarConfiguracaoControles();
		aplicarConfiguracaoVideo();
		aplicarConfiguracaoSom();
	}

	public void aplicarConfiguracaoControles() {
		EntradaPerifericos.setControle(this.configGeral.getControles());
	}

	@SuppressWarnings("unchecked")
	public void aplicarConfiguracaoVideo() {
		try {
			// GUARDA OS LISTERNERS PQ O SLICK APAGA TODOS LOL
			getAppGameContainer().getInput().setLockRemoveListerners(true);

			appGameContainer.setDisplayMode(configGeral.getResolucao()
					.getComprimento(), configGeral.getResolucao().getAltura(),
					configGeral.getResolucao().isTelaCheia());

			getAppGameContainer().getInput().setLockRemoveListerners(false);

		} catch (SlickException e) {
			tratarExcecao(e);
		}
	}

	public void aplicarConfiguracaoSom() {
		if (configGeral.getSom().getAlturaMusica() <= 0) {
			pausarMusica();
		} else {
			reTocarMusica();
		}
	}

	public void reTocarMusica() {
		for (String m : musicas.keySet())
			musicas.get(m).loop();
	}

	public void reTocarMusica(String m) {
		musicas.get(m).loop();
	}

	/**
	 * Para todas as musicas.
	 */
	public void pararMusica() {
		for (String m : musicas.keySet())
			musicas.remove(m).stop();
	}

	public void pararMusica(String musica) {
		musicas.remove(musica).stop();
	}

	/**
	 * Pausa todas as musicas.
	 */
	public void pausarMusica() {
		for (String m : musicas.keySet())
			musicas.get(m).pause();
	}

	public void pausarMusica(String musica) {
		musicas.get(musica).pause();
	}

	public void tocarMusica(String musica) {
		try {
			Music music = RecursosFactory.getInstancia().getMusica(musica);
			musicas.put(musica, music);
			if (configGeral.getSom().getAlturaMusica() > 0) {
				music.loop();
			}
		} catch (Exception e) {
			tratarExcecao(e);
		}
	}

	public void tocarMusica(String musica, float volume) {
		try {
			Music music = RecursosFactory.getInstancia().getMusica(musica);
			musicas.put(musica, music);
			if (configGeral.getSom().getAlturaMusica() > 0) {
				music.setVolume(volume);
				music.loop();
			}
		} catch (Exception e) {
			tratarExcecao(e);
		}
	}

	public boolean isTocandoMusica() {
		try {
			return !musicas.isEmpty();
		} catch (Exception e) {
			tratarExcecao(e);
		}

		return false;
	}

	public String getNomeMusica() {
		try {
			return "";
		} catch (Exception e) {
			tratarExcecao(e);
		}

		return null;
	}

	public br.org.gamexis.plataforma.motor.som.Som tocarEfeitoSom(String som,
			float x, float y, float z) {
		try {
			if (configGeral.getSom().getAlturaEfeitos() > 0) {
				Ator atorJog = ((Ator) jogavelCarregado.getAtor());
				if (Math.abs(atorJog.getX() - x) <= configGeral.getResolucao()
						.getComprimento()) {
					if (Math.abs(atorJog.getY() - y) <= configGeral
							.getResolucao().getAltura()) {
						if (configGeral.getSom().getAlturaEfeitos() > 0)
							return RecursosFactory.getInstancia().getSom(som);
					}
				}
			}
		} catch (Exception e) {
			tratarExcecao(e);
		}

		return null;
	}

	public br.org.gamexis.plataforma.motor.som.Som tocarEfeitoSom(String som) {
		try {
			if (configGeral.getSom().getAlturaEfeitos() > 0) {
				br.org.gamexis.plataforma.motor.som.Som s = RecursosFactory
						.getInstancia().getSom(som);
				s.tocar();

				return s;
			}
		} catch (Exception e) {
			tratarExcecao(e);
		}

		return null;
	}

	/**
	 * carrega um salvo pelo id
	 * 
	 * @param id
	 * @return
	 */
	public Salvavel obterPosicaoSalvo(Integer id) {
		Salvavel salvo = null;

		try {
			salvo = new GerenciadorSalvos().carregar(id.intValue());
		} catch (Exception e) {
			tratarExcecao(e);
		}
		return salvo;
	}

	/**
	 * carrega uum conjunto de variaveis globais, substituindo as anteriores
	 * 
	 * @param variaveis
	 */
	public void setVariaveis(HashMap<String, Object> variaveis) {
		this.variaveis = variaveis;
	}

	/**
	 * Seta o valor de uma variavel
	 * 
	 * @param nome
	 * @param valor
	 */
	public void setVariavel(String nome, Object valor) {
		variaveis.put(nome, valor);
	}

	/**
	 * Obtem o valor de uma variavel
	 * 
	 * @param nome
	 * @return
	 */
	public Object getValorVariavel(String nome) {
		return variaveis.get(nome);
	}

	/**
	 * Obtem o valor de uma variavel temporaria, removendo-a em seguida
	 * 
	 * @return
	 */
	public Object popValor() {
		return variaveisTemporarias.poll();
	}

	/**
	 * Adiciona uma variavel temporaria
	 * 
	 * @param valor
	 */
	public void putValor(Object valor) {
		variaveisTemporarias.add(valor);
	}

	/**
	 * Remove a variavel
	 * 
	 * @param nome
	 */
	public void removerVariavel(String nome) {
		variaveis.remove(nome);
	}

	public void fechar() {
		System.exit(0);
	}

	public boolean isModoDebug() {
		return modoDebug;
	}

	public void setJogavelCarregado(EntidadeJogavel jogavelCarregado) {
		this.jogavelCarregado = jogavelCarregado;
	}

	public EntidadeJogavel getJogavelCarregado() {
		return jogavelCarregado;
	}

	public String getVersao() {
		return versao;
	}

	public PainelEstado getPainelEstado() {
		return painelEstado;
	}

	public void setPainelEstado(PainelEstado painelEstado) {
		this.painelEstado = painelEstado;
	}

	public boolean cursorCapturado() {
		return cursorCapturado;
	}

	public void capturarCursor() {
		if (cursorCapturado)
			return;

		if (entidadeCursor != null)
			((Ator) entidadeCursor.getAtor()).setVisivel(false);

		cursorCapturado = true;
	}

	public void liberarCursor() {
		if (!cursorCapturado)
			return;

		cursorCapturado = false;

		if (entidadeCursor == null) {
			this.appGameContainer.setMouseGrabbed(false);
			return;
		}

		((Ator) entidadeCursor.getAtor()).setVisivel(true);
		// this.appGameContainer.setMouseGrabbed(true);
	}

	public void liberarCursorDebug() {
		this.appGameContainer.setMouseGrabbed(false);
	}

	public void configureCursor(Cursor cursor) throws SlickException {

		if (cursor.referencia != null) {
			this.appGameContainer.setMouseCursor(
					RecursosFactory.diretorioImagens + cursor.referencia, 0, 0);
		} else if (cursor.entidade != null) {
			this.setEntidadeCursor(new CarregadorEntidade()
					.carregarEntidade(cursor.entidade));
		}
	}

	/**
	 * Zera as variaveis globais
	 */
	public void limparVariaveis() {
		variaveis = new HashMap<String, Object>();
	}

	public void setEntidadeCursor(Entidade entidadeCursor) {
		this.entidadeCursor = entidadeCursor;
		this.appGameContainer.setMouseGrabbed(true);
	}

	public Entidade getEntidadeCursor() {
		return entidadeCursor;
	}

	public boolean tipoProjetil(Entidade entidade) {
		return (entidade.getTipo() == TipoEntidade.projetil);
	}

	public boolean tipoJogavel(Entidade entidade) {
		return (entidade.getTipo() == TipoEntidade.jogavel);
	}

	public boolean tipoInimigo(Entidade entidade) {
		return (entidade.getTipo() == TipoEntidade.inimigo);
	}

	public boolean tipoNeutro(Entidade entidade) {
		return (entidade.getTipo() == TipoEntidade.neutro);
	}

	public boolean tipoEscudo(Entidade entidade) {
		return (entidade.getTipo() == TipoEntidade.escudo);
	}

	public boolean tipoPlataforma(Entidade entidade) {
		return (entidade.getTipo() == TipoEntidade.plataforma);
	}

	public void exibirDanoPadrao(Entidade fonte, int dano) {
		exibirDanoPadrao(fonte, dano, 0, 30);
	}

	public void exibirDanoPadrao(Entidade fonte, int dano, float xd, float yd) {
		Efeito anima;
		try {
			anima = RecursosFactory.getInstancia().getEfeitoAnimacao(
					(Desenho) fonte.getAtor(), "perde_" + dano + "_anim.png",
					32, 64, 0, 0, 8, false, 100);
			anima.setDeslocY(yd);
			anima.setDeslocX(xd);
			((Cenario) getCenaAtual()).adicionarAnimacao(anima,
					NivelCena.frente);
		} catch (Exception e) {
			tratarExcecao(e);
		}
	}

	public void exibirGanhoPadrao(Entidade fonte, int ganho) {
		Efeito anima;
		try {
			anima = RecursosFactory.getInstancia().getEfeitoAnimacao(
					(Desenho) fonte.getAtor(), "ganha_" + ganho + "_anim.png",
					32, 64, 0, 0, 8, false, 100);
			anima.setDeslocY(30);
			((Cenario) getCenaAtual()).adicionarAnimacao(anima,
					NivelCena.frente);
		} catch (Exception e) {
			tratarExcecao(e);
		}
	}

	public void exibirDanoMaximo(Entidade fonte) {
		Efeito anima;
		try {
			anima = RecursosFactory.getInstancia().getEfeitoAnimacao(
					(Desenho) fonte.getAtor(), "perde_maximo_anim.png", 64, 64,
					0, 0, 8, false, 100);
			anima.setDeslocY(30);
			((Cenario) getCenaAtual()).adicionarAnimacao(anima,
					NivelCena.frente);
		} catch (Exception e) {
			tratarExcecao(e);
		}
	}

	/**
	 * Apresenta um balao de fala.
	 * 
	 * @param nomeImagem
	 * @param comportamento
	 * @param arquivoTexto
	 * @param chave
	 * @throws Exception
	 */
	public void falar(String nomeImagem, String comportamento,
			String arquivoTexto, String chave, float x, float y, float margemx,
			float margemy) throws Exception { // TODO Retirar daki

		BalaoTexto balao = RecursosFactory.getInstancia().getBalaoTexto(
				nomeImagem, comportamento, arquivoTexto, chave);

		((CaixaImagem) balao.getCaixa()).setMargemHorizontal(margemx);
		((CaixaImagem) balao.getCaixa()).setMargemVertical(margemy);

		balao.setMaximoIntervalo(getMaximoIntervaloTexto());
		balao.setX(x);
		balao.setY(y);

		if (fonteFalas == null) {
			setFontFalas("toupetyFonte18");
		}

		balao.setFonte(fonteFalas);
		((Cenario) getCenaAtual()).adicionarComponente(balao);
	}

	public void setFontFalas(String nomeFonte) throws SlickException {
		fonteFalas = RecursosFactory.getInstancia().getFonte(nomeFonte);
	}

	private void exibirLogEstadoJogavel() {
		exibirLogEstadoJogavel = true;
	}

	private void esconderLogEstadoJogavel() {
		exibirLogEstadoJogavel = false;
	}

	public void desenharLogEstadoJogavel(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(200, 200, 400, 200);
		g.setColor(Color.white);
		g.drawString("JOGAVEL: " + jogavelCarregado.getNome(), 230, 230);
		g.drawString("ANIMACAO: "
				+ ((Ator) (jogavelCarregado.getAtor())).getNomeAnimacao(), 230,
				260);

		String estado = "nop";
		Ator ator = (Ator) (jogavelCarregado.getAtor());

		if (ator.isAndando())
			estado = "isAndando ";
		if (ator.isCaindo())
			estado = "isCaindo ";
		if (ator.isEscorregando())
			estado = "isEscorregando ";
		if (ator.isNoar())
			estado = "isNoar ";
		if (ator.isParado())
			estado = "isParado ";
		if (ator.isPulando())
			estado = "isPulando ";
		if (ator.isQuicando())
			estado = "isQuicando ";
		if (ator.isVoando())
			estado = "isVoando ";

		g.drawString("ESTADO: " + estado, 230, 290);
		g.drawString("VX: " + ator.getVelocidadeX(), 230, 310);
		g.drawString("VY: " + ator.getVelocidadeY(), 230, 340);
		g.drawString("atrito: " + ator.getCorpo().getFriction(), 230, 370);
	}

	/* USADO NO DEBUG */

	public long getTempoMiliSegundos() {
		return System.currentTimeMillis();
	}

	public Entrada popEntrada() {
		Entrada tmp = entrada;
		entrada = null;
		return tmp;
	}

	public int getMaximoIntervaloTexto() {
		return maximo_intervalo_texto;
	}

	public int setMaximoIntervaloTexto(int intervalo) {
		return maximo_intervalo_texto = intervalo;
	}

	public void putEntrada(float x, float y) {
		entrada = new Entrada(x, y);
	}

	/**
	 * Retorna a instancia do controle dos perifericos de entrada.
	 * 
	 * @return
	 */
	public EntradaPerifericos getControlePerifericos() {
		return EntradaPerifericos.getInstancia();
	}
	
	
}
