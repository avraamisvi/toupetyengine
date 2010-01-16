package br.org.gamexis.plataforma.motor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

import net.phys2d.math.ROVector2f;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.cena.Animacao;
import br.org.gamexis.plataforma.cena.AnimacaoMista;
import br.org.gamexis.plataforma.cena.Ator;
import br.org.gamexis.plataforma.cena.AtorFaceOrientacao;
import br.org.gamexis.plataforma.cena.Cena;
import br.org.gamexis.plataforma.cena.Cenario;
import br.org.gamexis.plataforma.cena.Desenho;
import br.org.gamexis.plataforma.cena.Efeito;
import br.org.gamexis.plataforma.cena.EmissorParticulas;
import br.org.gamexis.plataforma.cena.Imagem;
import br.org.gamexis.plataforma.cena.Mosaico;
import br.org.gamexis.plataforma.cena.MosaicoAnimado;
import br.org.gamexis.plataforma.cena.MultImagem;
import br.org.gamexis.plataforma.cena.Tile;
import br.org.gamexis.plataforma.cena.componentes.BalaoTexto;
import br.org.gamexis.plataforma.cena.componentes.Botao;
import br.org.gamexis.plataforma.cena.componentes.CaixaCheque;
import br.org.gamexis.plataforma.cena.componentes.CaixaImagem;
import br.org.gamexis.plataforma.cena.componentes.GrupoCaixaCheque;
import br.org.gamexis.plataforma.cena.componentes.Painel;
import br.org.gamexis.plataforma.cena.componentes.Rotulo;
import br.org.gamexis.plataforma.cena.componentes.util.CaixaShape;
import br.org.gamexis.plataforma.cena.efeitos.EfeitoAlfaCor;
import br.org.gamexis.plataforma.cena.efeitos.EfeitoEmissor;
import br.org.gamexis.plataforma.cena.efeitos.FadeIn;
import br.org.gamexis.plataforma.cena.efeitos.FadeOut;
import br.org.gamexis.plataforma.cena.evento.Comando;
import br.org.gamexis.plataforma.cena.evento.KeyListerner;
import br.org.gamexis.plataforma.cena.evento.TemporizadorListerner;
import br.org.gamexis.plataforma.cena.materiais.CarregadorParalaxer;
import br.org.gamexis.plataforma.cena.materiais.Paralaxer;
import br.org.gamexis.plataforma.cena.util.CarregadorMultImagem;
import br.org.gamexis.plataforma.entidade.CarregadorEntidade;
import br.org.gamexis.plataforma.entidade.Entidade;
import br.org.gamexis.plataforma.entidade.proxy.CarregadorAtorProxy;
import br.org.gamexis.plataforma.eventos.EventoColisao;
import br.org.gamexis.plataforma.motor.filesystem.FileSystem;
import br.org.gamexis.plataforma.motor.filesystem.FileSystemFactory;
import br.org.gamexis.plataforma.motor.image.BigImage;
import br.org.gamexis.plataforma.motor.memoria.GerenciadorRecursosAlocados;
import br.org.gamexis.plataforma.motor.memoria.MemoriaValorCusto;
import br.org.gamexis.plataforma.motor.particulas.ToupetyEmitterFactory;
import br.org.gamexis.plataforma.motor.som.Som;
import br.org.gamexis.plataforma.motor.som.SomImpl;
import br.org.gamexis.plataforma.script.ScriptComportamento;
import br.org.gamexis.plataforma.script.ScriptComportamentoGroovy;
import br.org.gamexis.plataforma.script.ScriptComportamentoJavaScript;

public class RecursosFactory implements MemoriaValorCusto {

	public static final String _MOSAICO_ = "@#_MOSAICO_#@:";

	public static final String _MOSAICO_ANIM_ = "@#_MOSAICO_ANIM_#@:";

	GerenciadorRecursosAlocados gerenciador = new GerenciadorRecursosAlocados(
			"imagens");
	GerenciadorRecursosAlocados gerenciador_scripts = new GerenciadorRecursosAlocados(
			"scripts");
	GerenciadorRecursosAlocados gerenciador_som = new GerenciadorRecursosAlocados(
			"sons");

	public static String diretorioImagens = "imagens/";
	public static String diretorioSom = "sons/";
	public static String diretorioMusicas = "musica/";
	public static String diretorioFontes = "fontes/";
	public static String diretorioComportamentos = "comportamentos/";
	public static String diretorioFalas = "falas/";
	public static String diretorioMosaicos = "mosaicos/";
	public static String diretorioEmissores = "emissores/";

	private static RecursosFactory instancia = new RecursosFactory();

	private EfeitoAlfaCor efeitoAlfaCorPadrao;

	private RecursosFactory() {

	}

	/**
	 * Executa a limpeza do buffer para permitir o GC.
	 */
	public void limparBuffer() {// TODO e os outros recursos
		gerenciador.limpar(CUSTO_BAIXO);
	}

	/**
	 * Executa a limpeza do buffer para permitir o GC.
	 */
	public void libertarRecurso(String ref) {
		gerenciador.release(ref);
	}

	public InputStream getMusicaStream(String som) throws IOException {
		return FileSystemFactory.getFileSystem().abrirInputStream(
				diretorioMusicas + som);
	}

	public Music getMusica(String musica) throws IOException, SlickException {
		return new Music(musica, getMusicaStream(musica));
	}

	public InputStream getSomStream(String som) throws IOException {
		return FileSystemFactory.getFileSystem().abrirInputStream(
				diretorioSom + som);
	}

	public static RecursosFactory getInstancia() {
		return instancia;
	}

	public Som getSom(String snd) throws IOException, SlickException {

		SomImpl smd = (SomImpl) gerenciador_som.get(snd);

		if (smd == null) {
			smd = new SomImpl();
			smd.setSound(new Sound(snd, getSomStream(snd)));
			gerenciador_som.put(snd, smd, CUSTO_SONS);
		}

		return smd;
	}

	public Image getImage(String referencia) throws SlickException {
		Image img = (Image) gerenciador.get(referencia);
		FileSystem fs = FileSystemFactory.getFileSystem();

		if (img == null) {
			try {
				img = new Image(fs.abrirInputStream(diretorioImagens
						+ referencia), referencia, false, Image.FILTER_NEAREST);
			} catch (IOException e) {
				throw new SlickException(e.getMessage());
			}
			gerenciador.put(referencia, img, CUSTO_BAIXO);
		}

		return img;
	}

	public Image getImage(String referencia, boolean grande)
			throws SlickException {

		FileSystem fs = FileSystemFactory.getFileSystem();

		if (!grande) {
			return getImage(referencia);
		} else {
			BigImage img = (BigImage) gerenciador.get(referencia);

			if (img == null) {
				try {
					img = new BigImage(fs.abrirInputStream(diretorioImagens
							+ referencia), diretorioImagens + referencia,
							Image.FILTER_NEAREST);
				} catch (IOException e) {
					throw new SlickException(e.getMessage());
				}
				gerenciador.put(referencia, img, CUSTO_BAIXO);
			}
			return img;
		}
	}

	public Imagem getImagem(String referencia, boolean grande)
			throws SlickException {
		return new Imagem(referencia, grande);
	}

	@SuppressWarnings("unchecked")
	public MultImagem getMultiImagem(String referencia) throws SlickException {

		MultImagem multImg = new MultImagem();

		HashMap<String, Tile> img = (HashMap<String, Tile>) gerenciador
				.get(referencia);

		if (img == null) {
			img = new CarregadorMultImagem().carregarImagem(referencia);
			gerenciador.put(referencia, img, CUSTO_MAIS_ALTO);
		}

		multImg.setTiles(img);
		multImg.setReferencia(referencia);

		return multImg;
	}

	public Tile getTileMultImagem(String referencia, String celnome,
			boolean grande, String ext, int linha, int quadros, int coluna)
			throws SlickException {

		Tile tile = new Tile();
		tile.setLinha(linha);
		tile.setColuna(coluna);
		tile.setNome(celnome);

		Image img = null;
		// Image img = (Image) gerenciador.get(referencia + celnome + coluna);
		FileSystem fs = FileSystemFactory.getFileSystem();

		if (!grande) {

			try {
				if (img == null) {
					// img = new Image(, false, Image.FILTER_NEAREST);
					img = new Image(fs.abrirInputStream(diretorioImagens
							+ referencia + "/" + celnome + coluna + ext),
							referencia + celnome + coluna, false,
							Image.FILTER_NEAREST);

					// gerenciador.put(referencia + celnome + coluna, img,
					// CUSTO_MAIS_ALTO);
				}

			} catch (Exception e) {
				throw new SlickException(e.getMessage());
			}
		} else {
			if (img == null) {
				try {
					img = new BigImage(fs.abrirInputStream(diretorioImagens
							+ referencia + "/" + celnome + coluna + ext),
							diretorioImagens + referencia + "/" + celnome
									+ coluna + ext, Image.FILTER_NEAREST);
				} catch (IOException e) {
					throw new SlickException(e.getMessage());
				}

				// gerenciador.put(referencia + celnome + coluna, img,
				// CUSTO_BAIXO);
			}
		}

		tile.setImagem(img);

		return tile;
	}

	public Font getFonte(String nome) throws SlickException {
		FileSystem fs = FileSystemFactory.getFileSystem();

		try {
			return new AngelCodeFont(diretorioFontes + nome + ".png", fs
					.abrirInputStream(diretorioFontes + nome + ".fnt"), fs
					.abrirInputStream(diretorioFontes + nome + ".png"));
		} catch (IOException e) {
			throw new SlickException(e.getMessage());
		}
	}

	public Color getCor(float r, float g, float b, float a) {
		return new Color(r, g, b, a);
	}

	public Rectangle getRetangulo(float x, float y, float c, float a) {
		return new Rectangle(x, y, c, a);
	}

	public ScriptComportamento getComportamento(String referencia)
			throws SlickException {
		return getComportamento(referencia, true);
	}

	/**
	 * Usado internamente, não indicado para uso externo, sujeito a mudanças.
	 * 
	 * @param referencia
	 * @return
	 * @throws Exception
	 */
	public InputStream getComportamentoInputStream(String referencia)
			throws Exception {
		String ref = diretorioComportamentos + referencia;
		FileSystem fs = FileSystemFactory.getFileSystem();

		return fs.abrirInputStream(ref);
	}

	public ScriptComportamento getComportamento(String referencia, boolean auto)
			throws SlickException {
		if (referencia == null)
			return null;
		try {
			FileSystem fs = FileSystemFactory.getFileSystem();

			String extencao = referencia
					.substring(referencia.lastIndexOf(".") + 1);

			if (extencao.equalsIgnoreCase("groovy")) {
				String ref = null;
				if (auto)
					ref = diretorioComportamentos + referencia;
				else
					ref = referencia;

				ScriptComportamentoGroovy script = new ScriptComportamentoGroovy(
						ref, fs.abrirInputStream(ref));

				try {
					script.compile();

				} catch (Exception e) {
					Motor.obterInstancia().tratarExcecao(e);
				}

				return script;
			} else if (extencao.equalsIgnoreCase("js")) {
				String ref = null;
				Object obj = gerenciador_scripts.get(referencia);
				ScriptComportamento script;

				if (auto)
					ref = diretorioComportamentos + referencia;
				else
					ref = referencia;

				if (obj == null) {

					script = new ScriptComportamentoJavaScript(ref, fs
							.abrirInputStream(ref));

					script.compile();

					gerenciador_scripts.put(referencia, script, CUSTO_SCRIPTS);

					return script;
				} else {
					script = (ScriptComportamento) obj;
					return new ScriptComportamentoJavaScript(
							(ScriptComportamentoJavaScript) script);
				}
			}
		} catch (Throwable e) {
			throw new SlickException(e.getMessage());
		}

		return null;
	}

	public Botao getBotao(String imagem, String comportamento, String nome,
			float comprimento, float altura) throws SlickException {
		Botao botao = new Botao(Motor.obterInstancia().getAppGameContainer(),
				getImage(imagem), getRetangulo(0, 0, comprimento, altura));

		botao.setComportamento(getComportamento(comportamento));
		botao.setNome(nome);
		return botao;
	}

	public CaixaImagem getCaixaImagem(String nome, String imagem)
			throws SlickException {
		CaixaImagem caixa = new CaixaImagem(Motor.obterInstancia()
				.getAppGameContainer(), getImage(imagem));
		caixa.setNome(nome);

		return caixa;
	}

	public Painel getPainel(String nome, String fundo) throws SlickException {
		Painel painel = new Painel(Motor.obterInstancia().getAppGameContainer());
		painel.setNome(nome);
		painel.setFundo(getCaixaImagem(nome + "Fundo", fundo));

		return painel;
	}

	public CaixaShape getCaixaShape(float comprimento, float altura) {
		CaixaShape shape = new CaixaShape();
		shape.setAltura(altura);
		shape.setComprimento(comprimento);
		return shape;
	}

	public BalaoTexto getBalaoTexto(String nomeImagem, String comportamento,
			String arquivoTexto, String chave) throws SlickException,
			InvalidPropertiesFormatException, FileNotFoundException,
			IOException {
		BalaoTexto balao = new BalaoTexto(Motor.obterInstancia()
				.getAppGameContainer());
		balao
				.setCaixa(new br.org.gamexis.plataforma.cena.componentes.util.CaixaImagem(
						getImage(nomeImagem)));
		balao.setComportamento(getComportamento(comportamento));
		balao.setTexto(getTextoFala(arquivoTexto, chave));
		return balao;
	}

	public String getTextoFala(String arquivo, String chave)
			throws InvalidPropertiesFormatException, FileNotFoundException,
			IOException {
		String valor = getPropertieValue(arquivo, chave);
		return valor.replace('|', '\n');
	}

	public String getPropertieValue(String arquivo, String chave)
			throws InvalidPropertiesFormatException, FileNotFoundException,
			IOException {
		FileSystem fs = FileSystemFactory.getFileSystem();

		Properties prop = new Properties();
		prop.loadFromXML(fs.abrirInputStream(diretorioFalas + arquivo));

		return prop.getProperty(chave);
	}

	public CaixaCheque getCaixaCheque(String nome, String normal,
			String selecionada, float comprimento, float altura)
			throws SlickException {
		CaixaCheque caixaCheque = new CaixaCheque(Motor.obterInstancia()
				.getAppGameContainer(), getImage(normal),
				getImage(selecionada), getRetangulo(0, 0, comprimento, altura));
		caixaCheque.setNome(nome);
		return caixaCheque;
	}

	public GrupoCaixaCheque criarGrupoCaixaSelecionavel() {
		return new GrupoCaixaCheque();
	}

	public Rotulo getRotulo(String nome, String texto) throws SlickException {
		Rotulo rotulo = new Rotulo(Motor.obterInstancia().getAppGameContainer());
		rotulo.setTexto(texto);
		rotulo.setNome(nome);

		return rotulo;
	}

	public List<Paralaxer> getParalaxers(String referencia) {
		return new CarregadorParalaxer().carregarParalaxer(referencia);
	}

	public TemporizadorListerner getTemporizadorListerner(long max, Comando cmd) {
		return new TemporizadorListerner(max, cmd);
	}

	public KeyListerner getKeyListerner(int ch, Comando cmd) {
		return new KeyListerner(ch, cmd);
	}

	public AtorFaceOrientacao getFaceOrientacao(String nome) {
		return AtorFaceOrientacao.valueOf(nome);
	}

	public EfeitoAlfaCor getEfeitoAlfaCorPadrao() {
		if (efeitoAlfaCorPadrao == null) {
			efeitoAlfaCorPadrao = new EfeitoAlfaCor();
			Color corF = Color.red;
			corF.a = .3f;
			((EfeitoAlfaCor) efeitoAlfaCorPadrao).setCorPrimaria(corF);
			((EfeitoAlfaCor) efeitoAlfaCorPadrao).setCorSecundaria(Color.white);
			((EfeitoAlfaCor) efeitoAlfaCorPadrao).setDuracao(1000);
			((EfeitoAlfaCor) efeitoAlfaCorPadrao).setFrequencia(3);
			((EfeitoAlfaCor) efeitoAlfaCorPadrao).setDuracao(1000);
		}
		return (EfeitoAlfaCor) efeitoAlfaCorPadrao.clone();
	}

	public FadeIn getFadeIn(float fator, float opacidadeInicial, Desenho dono) {

		FadeIn fadeIn = new FadeIn();

		fadeIn.setFator(fator);
		fadeIn.setOpacidade(opacidadeInicial);
		fadeIn.setReferencia(dono);

		return fadeIn;
	}

	public FadeOut getFadeOut(float fator, float opacidadeInicial, Desenho dono) {

		FadeOut fadeOut = new FadeOut();

		fadeOut.setFator(fator);
		fadeOut.setOpacidade(opacidadeInicial);
		fadeOut.setReferencia(dono);

		return fadeOut;
	}

	public EfeitoAlfaCor getEfeitoAlfaCor(float r, float g, float b, float a,
			int duracao, int frequencia) {
		if (efeitoAlfaCorPadrao == null) {
			efeitoAlfaCorPadrao = new EfeitoAlfaCor();
			Color corF = new Color(r, g, b);
			corF.a = a;
			((EfeitoAlfaCor) efeitoAlfaCorPadrao).setCorPrimaria(corF);
			((EfeitoAlfaCor) efeitoAlfaCorPadrao).setCorSecundaria(Color.white);
			((EfeitoAlfaCor) efeitoAlfaCorPadrao).setDuracao(duracao);
			((EfeitoAlfaCor) efeitoAlfaCorPadrao).setFrequencia(frequencia);
			((EfeitoAlfaCor) efeitoAlfaCorPadrao).setDuracao(duracao);
		}
		return (EfeitoAlfaCor) efeitoAlfaCorPadrao.clone();
	}

	public Entidade getEntidade(String nome) {
		Entidade entidade = null;
		entidade = new CarregadorEntidade().carregarEntidade(nome);
		return entidade;
	}

	public Entidade inserirEntidadeCenario(String nome, String comportamento,
			float x, float y, float vx, float vy, int nivelCena, int excluidos)
			throws SlickException {
		Entidade ent = getEntidade(nome);
		((Ator) ent.getAtor()).setX(x);
		((Ator) ent.getAtor()).setY(y);
		((Ator) ent.getAtor()).setVelocidadeX(vx);
		((Ator) ent.getAtor()).setVelocidadeY(vy);

		if (comportamento != null)
			ent.setComportamento(getComportamento(comportamento));

		((Cenario) Motor.obterInstancia().getCenaAtual()).adicionarAtor(
				(Ator) ent.getAtor(), nivelCena, excluidos);
		return ent;
	}

	public Entidade inserirEntidadeCenario(String nome, float x, float y,
			float vx, float vy, int nivelCena, int excluidos)
			throws SlickException {
		return inserirEntidadeCenario(nome, null, x, y, vx, vy, nivelCena,
				excluidos);
	}

	public Entidade inserirEntidadeCenario(String nome, String comportamento,
			float x, float y, int nivelCena, int excluidos)
			throws SlickException {

		return inserirEntidadeCenario(nome, comportamento, x, y, 0, 0,
				nivelCena, excluidos);
	}

	public EmissorParticulas getEmissorParticulas(String nome)
			throws IOException, SlickException {
		// ParticleSystem p = ParticleEmittersFactory.create(nome);
		ParticleSystem p = null;
		if (p == null) {
			p = ParticleIO.loadConfiguredSystem(FileSystemFactory
					.getFileSystem().abrirInputStream(
							diretorioEmissores + nome + ".xml"),
					new ToupetyEmitterFactory());
		}

		EmissorParticulas em = new EmissorParticulas(p);
		em.setNome(nome);
		return em;
	}

	public EmissorParticulas getEmissorParticulas(String nome, long tempoMaximo)
			throws IOException, SlickException {
		if (tempoMaximo > 0) {

			// ParticleSystem p = ParticleEmittersFactory.create(nome);
			ParticleSystem p = null;
			if (p == null) {
				p = ParticleIO.loadConfiguredSystem(FileSystemFactory
						.getFileSystem().abrirInputStream(
								diretorioEmissores + nome + ".xml"),
						new ToupetyEmitterFactory());
			}

			EmissorParticulas em = new EmissorParticulas(p, tempoMaximo);

			em.setNome(nome);
			return em;
		} else {
			return getEmissorParticulas(nome);
		}
	}

	public EfeitoEmissor getEfeitoEmissor(String nome, Desenho dono,
			int tempoMaximo) throws IOException, SlickException {
		EfeitoEmissor em = new EfeitoEmissor(dono, getEmissorParticulas(nome,
				tempoMaximo));
		return em;
	}

	public AnimacaoMista getAnimacaoMista(String nome, String base,
			String emissor, int comp, int alt, int linha, int coluna,
			int quadros, boolean repetir, int duracao, float deslocx,
			float deslocy, float ex, float ey) throws IOException,
			SlickException {

		AnimacaoMista mista = new AnimacaoMista();

		Animacao anim = getAnimacao(base, comp, alt, linha, coluna, quadros,
				repetir, duracao);

		anim.setDeslocX(deslocx);
		anim.setDeslocY(deslocy);

		mista.setAnimacao(anim);

		EmissorParticulas pEmi = RecursosFactory.getInstancia()
				.getEmissorParticulas(emissor);

		mista.setEmissor(pEmi);
		mista.setEX(ex);
		mista.setEY(ey);

		return mista;
	}

	public AnimacaoMista getAnimacaoMistaMultiImagem(String nome, String base,
			String emissor, int comp, int alt, int linha, int coluna,
			int quadros, boolean repetir, int duracao, float deslocx,
			float deslocy, float ex, float ey) throws IOException,
			SlickException {

		AnimacaoMista mista = new AnimacaoMista();

		Animacao anim = getAnimacaoMultImagem(nome, comp, alt, linha, coluna,
				quadros, repetir, duracao);

		anim.setDeslocX(deslocx);
		anim.setDeslocY(deslocy);

		mista.setAnimacao(anim);

		EmissorParticulas pEmi = RecursosFactory.getInstancia()
				.getEmissorParticulas(emissor);

		mista.setEmissor(pEmi);
		mista.setEX(ex);
		mista.setEY(ey);

		return mista;
	}

	public Animacao getAnimacao(String imgRef, int comp, int alt, int linha,
			int coluna, int quadros, boolean repetir, int duracao)
			throws IOException, SlickException {
		return new Animacao(imgRef, comp, alt, linha, coluna, quadros, repetir,
				duracao);
	}

	public Animacao getAnimacaoMultImagem(String imgRef, int comp, int alt,
			int linha, int coluna, int quadros, boolean repetir, int duracao)
			throws IOException, SlickException {
		return new Animacao(imgRef, getMultiImagem(imgRef), comp, alt, linha,
				coluna, quadros, repetir, duracao);
	}

	/**
	 * Carrega uma animação flipped em x ou y
	 * 
	 * @param imgRef
	 * @param comp
	 * @param alt
	 * @param linha
	 * @param coluna
	 * @param quadros
	 * @param repetir
	 * @param duracao
	 * @param flipHorizontal
	 * @param flipVertical
	 * @return
	 * @throws IOException
	 * @throws SlickException
	 */
	public Animacao getAnimacao(String imgRef, int comp, int alt, int linha,
			int coluna, int quadros, boolean repetir, int duracao,
			boolean flipHorizontal, boolean flipVertical) throws IOException,
			SlickException {
		return new Animacao(imgRef, comp, alt, linha, coluna, quadros, repetir,
				duracao, flipHorizontal, flipVertical);
	}

	/**
	 * Carrega uma animação flipped em x ou y
	 * 
	 * @param imgRef
	 * @param comp
	 * @param alt
	 * @param linha
	 * @param coluna
	 * @param quadros
	 * @param repetir
	 * @param duracao
	 * @param flipHorizontal
	 * @param flipVertical
	 * @return
	 * @throws IOException
	 * @throws SlickException
	 */
	public Animacao getAnimacaoMultImagem(String imgRef, int comp, int alt,
			int linha, int coluna, int quadros, boolean repetir, int duracao,
			boolean flipHorizontal, boolean flipVertical) throws IOException,
			SlickException {
		return new Animacao(imgRef, getMultiImagem(imgRef), comp, alt, linha,
				coluna, quadros, repetir, duracao, flipHorizontal, flipVertical);
	}

	public Efeito getEfeitoAnimacao(Desenho dono, String imgRef, int comp,
			int alt, int linha, int coluna, int quadros, boolean repetir,
			int duracao) throws IOException, SlickException {
		return new Efeito(dono, imgRef, comp, alt, linha, coluna, quadros,
				repetir, duracao);
	}

	public Mosaico getMosaico(String nome, String base, int comprimentoCelulas,
			int alturaCelulas, int linhas, int colunas) throws SlickException {
		Mosaico mosaico = (Mosaico) gerenciador.get(_MOSAICO_ + nome);

		if (mosaico == null) {
			mosaico = new Mosaico(base, comprimentoCelulas, alturaCelulas,
					linhas, colunas);
			gerenciador.put(_MOSAICO_ + base, mosaico, CUSTO_MOSAICOS);
		}
		return mosaico;
	}

	public Mosaico getMultiImagemMosaico(String nome, String base,
			int comprimentoCelulas, int alturaCelulas, int linhas, int colunas)
			throws SlickException {
		Mosaico mosaico = (Mosaico) gerenciador.get(_MOSAICO_ + nome);
		if (mosaico == null) {
			mosaico = new Mosaico(RecursosFactory.getInstancia()
					.getMultiImagem(base), comprimentoCelulas, alturaCelulas,
					linhas, colunas);
			gerenciador.put(_MOSAICO_ + base, mosaico, CUSTO_MOSAICOS);
		}
		return mosaico;
	}

	public MosaicoAnimado getMosaicoAnimado(String nome, String base,
			int comprimentoCelulas, int alturaCelulas, int linhas, int colunas)
			throws SlickException {
		MosaicoAnimado mosaico = (MosaicoAnimado) gerenciador
				.get(_MOSAICO_ANIM_ + nome);
		if (mosaico == null) {
			mosaico = new MosaicoAnimado(base, comprimentoCelulas,
					alturaCelulas, linhas, colunas);
			gerenciador.put(_MOSAICO_ANIM_ + base, mosaico, CUSTO_MOSAICOS);
		}
		return mosaico;
	}

	public MosaicoAnimado getMultiImagemMosaicoAnimado(String nome,
			String base, int comprimentoCelulas, int alturaCelulas, int linhas,
			int colunas) throws SlickException {
		MosaicoAnimado mosaico = (MosaicoAnimado) gerenciador
				.get(_MOSAICO_ANIM_ + nome);

		if (mosaico == null) {
			mosaico = new MosaicoAnimado(RecursosFactory.getInstancia()
					.getMultiImagem(base), comprimentoCelulas, alturaCelulas,
					linhas, colunas);
			gerenciador.put(_MOSAICO_ANIM_ + base, mosaico, CUSTO_MOSAICOS);
		}
		return mosaico;
	}

	public Ator getAtorProxy(String nome) {
		return new CarregadorAtorProxy().carregarAtor(nome);
	}

	public EventoColisao getEventoColisao(Entidade entidade, Cena cena,
			float nx, float ny, ROVector2f ponto) {
		EventoColisao col = new EventoColisao(entidade, cena);

		col.setNormalX(nx);
		col.setNormalY(ny);
		col.setPontoColisao(ponto);

		return col;
	}
}
