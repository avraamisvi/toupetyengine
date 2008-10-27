package br.org.gamexis.plataforma;

import java.util.Date;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.neodatis.odb.core.Objects;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.criteria.CriteriaQuery;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.main.ODB;
import org.neodatis.odb.main.ODBFactory;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

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
import br.org.gamexis.plataforma.exception.GXException;
import br.org.gamexis.plataforma.motor.CarregadorConfiguracaoInicial;
import br.org.gamexis.plataforma.motor.ConfiguracaoGeral;
import br.org.gamexis.plataforma.motor.ConfiguracaoInicial;
import br.org.gamexis.plataforma.motor.EntradaPerifericos;
import br.org.gamexis.plataforma.motor.RecursosFactory;
import br.org.gamexis.plataforma.motor.Salvavel;
import br.org.gamexis.plataforma.motor.Transicao;
import br.org.gamexis.plataforma.motor.ConfiguracaoInicial.Cursor;
import br.org.gamexis.plataforma.motor.configuracao.Som;
import br.org.gamexis.plataforma.motor.configuracao.Video;
import br.org.gamexis.plataforma.motor.som.FMODUtil;
import br.org.gamexis.plataforma.motor.som.JMFUtil;
import br.org.gamexis.plataforma.script.ScriptComportamento;

public class Motor extends BasicGame {
	
	private static final String RECURSOS_IMAGENS_FADE = "recursos/imagens/fade.png";
	private Font fonteFalas;
	private int maximo_intervalo_texto = 10;
	
	// formato data base 01/08/2007
	// N° Anos. N° de meses . N° versões geradas no mes
	// versão 0.10.22b - introduzido o gerenciamento de recursos,
	// pra solucionar problemas relativos a otimização
	// 1.0 -> lua pra groovy
	// nova metodologia para versionamento 10/07/2008
	// M.N.C build B
	// M - grandes mudanças mudança de paradigmas
	// N - novas funcionalidades
	// C - correçoes e pequenas ajustes
	// B - builds
	private String versao = "1.21.0 build 175";
	
	private static final int INTERVALO_TRANSICAO = 150;
	private static final String ARQUIVO_SALVAR = "jogo.salvo";
	private static final String ARQUIVO_CONFIGURACAO = "jogo.config";
	private static Motor instancia;
	public static float gravidade = 9.8f;
	private boolean modoDebug = false;
	private boolean modoDesenvolvedor = true;
	private Transicao transicao;
	private Music musicaAmbiente;
	private boolean flagMudarCena;
	private boolean flagCarregarProximaCena;
	private String proximaCena;
	AppGameContainer appGameContainer;
	public static final String ARQUIVO_LOG = "toupety.log";
	public Entidade entidadeCursor;
	private boolean cursorCapturado = false;
	private boolean cursorCapturadoDebug = false;
	
	// Slot de jogo salvo - Zero por padrão
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
	 * Controle do salvo
	 */
	ScriptComportamento scriptMotor;
	
	long tInicial;
	long fps;
	
	private Cena cenaAtual;
	private ConfiguracaoInicial inicial;
	private ConfiguracaoGeral configGeral;
	
	private Console console;
	
	public static Motor obterInstancia() {
		if (instancia == null)
			instancia = new Motor();
		
		return instancia;
	}
	
	private Motor() {
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
			AppGameContainer app = new AppGameContainer(mot);
			// app.setClearEachFrame(false);
			app.setShowFPS(false);
			
			mot.setAppGameContainer(app);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void init(GameContainer container) throws SlickException {
		inicial = new CarregadorConfiguracaoInicial().carregarConfiguracao();
		try {
			Logger logger = Logger.getLogger(ARQUIVO_LOG);
			FileHandler fh = new FileHandler(ARQUIVO_LOG, true);
			logger.addHandler(fh);
			logger.setLevel(Level.ALL);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		container.setTargetFrameRate(inicial.getFramerate());
		// container.setMaximumLogicUpdateInterval(inicial.getLogicaMaxima());
		// container.setMinimumLogicUpdateInterval(inicial.getLogicaMinima());
		// container.setTargetFrameRate(100);
		// container.setVSync(true); sem isso fica melhor
		container.setIcon(inicial.getIcone());
		
		modoDebug = inicial.isDebug();
		appGameContainer.setTitle(inicial.getTitulo() + " VERSAO: " + versao);
		
		configGeral = carregarConfiguracao();
		aplicarConfiguracaoGeral();
		EntradaPerifericos.setInput(container.getInput());
		EntradaPerifericos.setControle(configGeral.getControles());
		
		container.getGraphics().setBackground(new Color(0.5f, 0.5f, 0.5f));
		cenaAtual = new CarregadorCenario().carregarCena(inicial.getCenaInicial());
		
		transicao = new Transicao(configGeral.getResolucao().getComprimento(), configGeral
				.getResolucao().getAltura(), INTERVALO_TRANSICAO);
		transicao.setImagemBase(new Image(RECURSOS_IMAGENS_FADE), 100, 100, 4);
		transicao.iniciarVoltando();
		
		configureCursor(inicial.getCursor());
		
		console = new Console(container);
		console.desativar();
		console.setVisivel(false);
		
		scriptMotor = RecursosFactory.getInstancia().getComportamento("recursos/motor.groovy",
				false);
	}
	
	public void exibirConsole() {
		if (!console.isAtivado()) {
			console.ativar();
			console.setVisivel(true);
			console.definirFuncoes();
			liberarCursor();
		} else {
			console.desativar();
			console.setVisivel(false);
			capturarCursor();
		}
	}
	
	public void desenharConsole(Graphics g) {
		console.desenhar(g);
	}
	
	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		
		long t = System.currentTimeMillis();
		
		if (entidadeCursor != null) {
			entidadeCursor.getAtor().atualizar(delta);
		}
		
		if (flagCarregarProximaCena) {
			flagCarregarProximaCena = false;
			if (cenaAtual != null) {
				cenaAtual.finalizar();
				cenaAtual = null;
			}
			
			cenaAtual = new CarregadorCenario().carregarCena(proximaCena);
		}
		
		if (container.getInput().isKeyPressed(Input.KEY_F3)) {// SHOW FPS
			container.setShowFPS(!container.isShowingFPS());
		} else if (container.getInput().isKeyPressed(Input.KEY_F12)) {
			
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
			
		} else if (container.getInput().isKeyPressed(Input.KEY_F11)) {
			
			configGeral.getResolucao().setTelaCheia(!configGeral.getResolucao().isTelaCheia());
			
			Color corFundo = container.getGraphics().getBackground();
			appGameContainer.setFullscreen(configGeral.getResolucao().isTelaCheia());
			container.getGraphics().setBackground(corFundo);
			
		} else if (container.getInput().isKeyPressed(Input.KEY_F1)) {
			
			exibirConsole();
		} else if (container.getInput().isKeyPressed(Input.KEY_F2)) {
			if (modoDesenvolvedor)
				liberarCursorDebug();
		} else if (container.getInput().isKeyPressed(Input.KEY_F8)) {
			if (modoDesenvolvedor) {
				if (!exibirLogEstadoJogavel)
					exibirLogEstadoJogavel();
				else
					esconderLogEstadoJogavel();
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
		
		// System.out.println("TOTAL UPDATE:" + (System.currentTimeMillis() -
		// t));
	}
	
	/**
	 * Atualiza a cena atual.
	 * 
	 * @param container
	 * @param delta
	 * @throws SlickException
	 */
	private void atualizarCenaAtual(GameContainer container, int delta) throws SlickException {
		try {
			if (!cenaAtual.estaIniciada())
				cenaAtual.iniciar(container);
			
			cenaAtual.atualizar(container, delta);
			
		} catch (GXException e) {
			Logger.getLogger("toupety_log").log(Level.SEVERE, "erro:", e);
			e.printStackTrace();
		}
	}
	
	public void tratarExcecao(Exception cause) {
		cause.printStackTrace();
		Logger.getLogger(Motor.ARQUIVO_LOG).log(Level.SEVERE, "erro motor:", cause);
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
	public void render(GameContainer container, Graphics g) throws SlickException {
		long t = System.currentTimeMillis();
		// g.setAntiAlias(true);
		cenaAtual.desenhar(container, g);
		desenharConsole(g);
		desenharEntidadeCursor(g);
		
		if (modoDebug) {
			g.drawString("MODO DEBUG", container.getWidth() / 2, 20);
			g.drawString("VERSAO " + versao, container.getWidth() / 2,
					container.getScreenHeight() - 50);
			
			Ator atorJog = (Ator) getJogavelCarregado().getAtor();
			
			Color old = g.getColor();
			g.setColor(Color.cyan);
			g.drawString("JOGAVEL(X,Y)" + atorJog.getX() + ", " + atorJog.getY(), container
					.getWidth() / 2, 100);
			g.setColor(old);
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
		
		// System.out.println("TOTAL RENDER:" + (System.currentTimeMillis() -
		// t));
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
		salvarJogo(getSlotSalvo(), getCenaAtual().getNome());
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
				
				ODB odb = ODBFactory.open(ARQUIVO_SALVAR);
				IQuery query = new CriteriaQuery(Salvavel.class, Where.equal("identificador", salvo
						.getIdentificador()));
				
				Objects objs = odb.getObjects(query);
				if (objs.hasNext())
					salvo = (Salvavel) objs.getFirst();
				
				if (salvo == null) {
					salvo = new Salvavel();
					salvo.setIdentificador(posicao);
				}
				
				salvo.setCena(getCenaAtual().getNome());
				salvo.setVariaveis(variaveis);
				float x = getJogavelCarregado().getAtor().getCorpo().getPosition().getX();
				float y = getJogavelCarregado().getAtor().getCorpo().getPosition().getY();
				salvo.setJogavelX(x);
				salvo.setJogavelY(y);
				salvo.setData(new Date());
				salvo.setDescricao(descricao);
				
				// odb.close();
				scriptMotor.execute("aoSalvar", new Object[] { this, salvo });
				// salvar(salvo);
				odb.store(salvo);
				odb.close();
				
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
				Salvavel salvo = carregar(posicao);
				// DEPRECATED VERSÕES ANTIGAS
				setVariaveis(salvo.getVariaveis());
				setVariavel("posicaoJogavelX", String.valueOf(salvo.getJogavelX().floatValue()));
				setVariavel("posicaoJogavelY", String.valueOf(salvo.getJogavelY().floatValue()));
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
			ODB odb = ODBFactory.open(ARQUIVO_CONFIGURACAO);
			
			IQuery query = new CriteriaQuery(ConfiguracaoGeral.class);
			Objects objs = odb.getObjects(query);
			
			ConfiguracaoGeral configTemp = null;
			if (objs.hasNext()) {
				configTemp = (ConfiguracaoGeral) objs.getFirst();
				
				// config.getControles().padrao();
				configTemp.setControles(config.getControles());
				configTemp.setResolucao(config.getResolucao());
				configTemp.setSom(config.getSom());
				config = configTemp;
			}
			
			odb.store(config);
			odb.close();
		} catch (Exception e) {
			tratarExcecao(e);
		}
	}
	
	public ConfiguracaoGeral carregarConfiguracao() {
		ConfiguracaoGeral conf = null;
		try {
			ODB odb = ODBFactory.open(ARQUIVO_CONFIGURACAO);
			IQuery query = new CriteriaQuery(ConfiguracaoGeral.class);
			Objects confs = odb.getObjects(query);
			conf = (ConfiguracaoGeral) confs.getFirst();
			odb.close();
		} catch (Exception e) {
			tratarExcecao(e);
		}
		
		// conf.setControles(new Controles());
		return conf;
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
	}
	
	public void aplicarConfiguracaoVideo() {
		try {
			// appGameContainer.setVSync(true);
			appGameContainer.setDisplayMode(configGeral.getResolucao().getComprimento(),
					configGeral.getResolucao().getAltura(), configGeral.getResolucao()
							.isTelaCheia());
		} catch (SlickException e) {
			tratarExcecao(e);
		}
	}
	
	public void aplicarConfiguracaoSom() {
		FMODUtil.obterInstancia().setMusicaHabilitada(configGeral.getSom().getAlturaMusica() > 0);
		FMODUtil.obterInstancia().setEfeitosHabilitado(configGeral.getSom().getAlturaEfeitos() > 0);
	}
	
	public void reTocarMusica() {
		JMFUtil.getInstancia().tocarMesmaMusica(true);
	}
	
	public void pararMusica() {
		JMFUtil.getInstancia().pararMusica();
	}
	
	public void tocarMusica(String musica) {
		try {
			JMFUtil.getInstancia().tocarMusica(musica, true);
		} catch (Exception e) {
			tratarExcecao(e);
		}
	}
	
	public boolean isTocandoMusica() {
		try {
			return JMFUtil.getInstancia().isTocandoMusica();
		} catch (Exception e) {
			tratarExcecao(e);
		}
		
		return false;
	}
	
	public String getNomeMusica() {
		try {
			return JMFUtil.getInstancia().getNomeMusica();
		} catch (Exception e) {
			tratarExcecao(e);
		}
		
		return null;
	}
	
	public br.org.gamexis.plataforma.motor.som.Som tocarEfeitoSom(String som, float x, float y,
			float z) {
		try {
			Ator atorJog = ((Ator) jogavelCarregado.getAtor());
			if (Math.abs(atorJog.getX() - x) <= configGeral.getResolucao().getComprimento()) {
				if (Math.abs(atorJog.getY() - y) <= configGeral.getResolucao().getAltura()) {
					if (configGeral.getSom().getAlturaEfeitos() > 0)
						return JMFUtil.getInstancia().tocarSom(som);
				}
			}
			
		} catch (Exception e) {
			tratarExcecao(e);
		}
		
		return null;
	}
	
	public br.org.gamexis.plataforma.motor.som.Som tocarEfeitoSom(String som) {
		try {
			if (configGeral.getSom().getAlturaEfeitos() > 0)
				return JMFUtil.getInstancia().tocarSom(som);
		} catch (Exception e) {
			tratarExcecao(e);
		}
		
		return null;
	}
	
	@Deprecated
	public void gerarConfiguracaoPadrao() {
		ConfiguracaoGeral geral = new ConfiguracaoGeral();
		
		Video video = new Video();
		video.setAltura(800);
		video.setComprimento(600);
		video.setTelaCheia(false);
		
		Som som = new Som();
		som.setAlturaEfeitos(1);
		som.setAlturaMusica(1);
		
		geral.setResolucao(video);
		geral.setSom(som);
		
		salvarConfiguracao(geral);
	}
	
	/**
	 * carrega um salvo pelo id
	 * 
	 * @param id
	 * @return
	 */
	public Salvavel carregar(Integer id) {
		Salvavel salvo = null;
		try {
			ODB odb = ODBFactory.open(ARQUIVO_SALVAR);
			IQuery query = new CriteriaQuery(Salvavel.class, Where.equal("identificador", id));
			Objects salvos = odb.getObjects(query);
			
			if (!salvos.isEmpty())
				salvo = (Salvavel) salvos.getFirst();
			
			odb.close();
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
		this.appGameContainer.setMouseGrabbed(true);
		
		if (entidadeCursor != null)
			((Ator) entidadeCursor.getAtor()).setVisivel(false);
		
		cursorCapturado = true;
	}
	
	public void liberarCursor() {
		cursorCapturado = false;
		
		if (entidadeCursor == null) {
			this.appGameContainer.setMouseGrabbed(false);
			return;
		}
		
		((Ator) entidadeCursor.getAtor()).setVisivel(true);
		this.appGameContainer.setMouseGrabbed(true);
	}
	
	public void liberarCursorDebug() {
		this.appGameContainer.setMouseGrabbed(false);
	}
	
	public void configureCursor(Cursor cursor) throws SlickException {
		
		if (cursor.referencia != null) {
			this.appGameContainer.setMouseCursor(RecursosFactory.diretorioImagens
					+ cursor.referencia, 0, 0);
		} else if (cursor.entidade != null) {
			this.setEntidadeCursor(new CarregadorEntidade().carregarEntidade(cursor.entidade));
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
			anima = RecursosFactory.getInstancia().getEfeitoAnimacao((Desenho) fonte.getAtor(),
					"perde_" + dano + "_anim.png", 32, 64, 0, 0, 8, false, 100);
			anima.setDeslocY(yd);
			anima.setDeslocX(xd);
			((Cenario) getCenaAtual()).adicionarAnimacao(anima, NivelCena.frente);
		} catch (Exception e) {
			tratarExcecao(e);
			e.printStackTrace();
		}
	}
	
	public void exibirGanhoPadrao(Entidade fonte, int ganho) {
		Efeito anima;
		try {
			anima = RecursosFactory.getInstancia().getEfeitoAnimacao((Desenho) fonte.getAtor(),
					"ganha_" + ganho + "_anim.png", 32, 64, 0, 0, 8, false, 100);
			anima.setDeslocY(30);
			((Cenario) getCenaAtual()).adicionarAnimacao(anima, NivelCena.frente);
		} catch (Exception e) {
			tratarExcecao(e);
			e.printStackTrace();
		}
	}
	
	public void exibirDanoMaximo(Entidade fonte) {
		Efeito anima;
		try {
			anima = RecursosFactory.getInstancia().getEfeitoAnimacao((Desenho) fonte.getAtor(),
					"perde_maximo_anim.png", 64, 64, 0, 0, 8, false, 100);
			anima.setDeslocY(30);
			((Cenario) getCenaAtual()).adicionarAnimacao(anima, NivelCena.frente);
		} catch (Exception e) {
			tratarExcecao(e);
			e.printStackTrace();
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
	public void falar(String nomeImagem, String comportamento, String arquivoTexto, String chave,
			float x, float y, float margemx, float margemy) throws Exception {
		
		BalaoTexto balao = RecursosFactory.getInstancia().getBalaoTexto(nomeImagem, comportamento,
				arquivoTexto, chave);
		
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
	
	/* USADO NO DEBUG */
	private boolean exibirLogEstadoJogavel = false;
	
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
		g.drawString("ANIMACAO: " + ((Ator) (jogavelCarregado.getAtor())).getNomeAnimacao(), 230,
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
	
	// controle da posição de Toupety na entrada de cenas
	private Entrada entrada = null;
	
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
