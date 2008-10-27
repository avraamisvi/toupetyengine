package br.org.gamexis.plataforma.motor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import net.phys2d.math.ROVector2f;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.BigImage;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.cena.Animacao;
import br.org.gamexis.plataforma.cena.Ator;
import br.org.gamexis.plataforma.cena.AtorFaceOrientacao;
import br.org.gamexis.plataforma.cena.Cena;
import br.org.gamexis.plataforma.cena.Cenario;
import br.org.gamexis.plataforma.cena.Desenho;
import br.org.gamexis.plataforma.cena.Efeito;
import br.org.gamexis.plataforma.cena.EmissorParticulas;
import br.org.gamexis.plataforma.cena.Imagem;
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
import br.org.gamexis.plataforma.cena.evento.Comando;
import br.org.gamexis.plataforma.cena.evento.KeyListerner;
import br.org.gamexis.plataforma.cena.evento.TemporizadorListerner;
import br.org.gamexis.plataforma.cena.util.CarregadorMultImagem;
import br.org.gamexis.plataforma.entidade.CarregadorEntidade;
import br.org.gamexis.plataforma.entidade.Entidade;
import br.org.gamexis.plataforma.eventos.EventoColisao;
import br.org.gamexis.plataforma.motor.memoria.GerenciadorRecursosAlocados;
import br.org.gamexis.plataforma.script.ScriptComportamento;
import br.org.gamexis.plataforma.script.ScriptComportamentoGroovy;
import br.org.gamexis.plataforma.script.ScriptComportamentoJavaScript;

public class RecursosFactory {

	GerenciadorRecursosAlocados gerenciador = new GerenciadorRecursosAlocados();
	
	public static String diretorioImagens = "recursos/imagens/";
	public static String diretorioSom = "recursos/sons/";
	public static String diretorioFontes = "recursos/fontes/";
	public static String diretorioComportamentos = "recursos/comportamentos/";
	public static String diretorioFalas = "recursos/falas/";
	public static String diretorioMosaicos = "recursos/mosaicos/";
	public static String diretorioEmissores = "recursos/emissores/";
	
	private static RecursosFactory instancia = new RecursosFactory();

	private EfeitoAlfaCor efeitoAlfaCorPadrao;

	public static final int PRIORIDADE_IMAGENS = 1;
	public static final int PRIORIDADE_MULTIIMAGENS = 10;
	public static final int PRIORIDADE_SCRIPTS = 8;
	
	private RecursosFactory() {

	}

	public static RecursosFactory getInstancia() {
		return instancia;
	}

	public Image getImage(String referencia) throws SlickException {
		Image img = (Image)gerenciador.get(referencia);
		
		if(img == null) {
			img = new Image(diretorioImagens + referencia, false, Image.FILTER_NEAREST);
			gerenciador.put(referencia, img, PRIORIDADE_IMAGENS);
		}
		
		return img;
	}

	public Image getImage(String referencia, boolean grande) throws SlickException {
		
		if(!grande) {
			return getImage(referencia);
		} else {
			BigImage img = (BigImage)gerenciador.get(referencia);
			
			if(img == null) {
				img = new BigImage(diretorioImagens + referencia, Image.FILTER_NEAREST);
				gerenciador.put(referencia, img, PRIORIDADE_IMAGENS);
			}
			return img;
		}
	}
	
	public Imagem getImagem(String referencia, boolean grande) throws SlickException {
		return new Imagem(referencia, grande);
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, Tile> getMultiImagem(String referencia) throws SlickException {		
		HashMap<String, Tile> img = (HashMap<String, Tile>)gerenciador.get(referencia);
		
		if(img == null) {			
			img = new CarregadorMultImagem().carregarImagem(referencia);
			gerenciador.put(referencia, img, PRIORIDADE_MULTIIMAGENS);
		}
		
		return img;
	}	
	
	public Tile getTileMultImagem(String referencia, String celnome, boolean grande, String ext, int linha, 
			int quadros, int coluna) throws SlickException {
		
		Tile tile = new Tile();
		tile.setLinha(linha);
		tile.setColuna(coluna);					
		tile.setNome(celnome);
		
		Image img = (Image)gerenciador.get(referencia+celnome+coluna);
		
		if(!grande) {
			if(img == null) {
				img = new Image(diretorioImagens + referencia + "/"	
						+ celnome + coluna + ext, false, Image.FILTER_NEAREST);
				gerenciador.put(referencia+celnome+coluna, img, PRIORIDADE_IMAGENS);
			}
		} else {
			if(img == null) {
				img = new BigImage(diretorioImagens + referencia + "/"	
						+ celnome + coluna + ext, Image.FILTER_NEAREST);
				gerenciador.put(referencia+celnome+coluna, img, PRIORIDADE_IMAGENS);
			}			
		}
		
		tile.setImagem(img);
		
		return tile;		
	}
	
	public Font getFonte(String nome) throws SlickException {
		return new AngelCodeFont(diretorioFontes + nome + ".fnt",
				diretorioFontes + nome + ".png");
	}

	public Color getCor(float r, float g, float b, float a) {
		return new Color(r, g, b, a);
	}

	public Rectangle getRetangulo(float x, float y, float c, float a) {
		return new Rectangle(x, y, c, a);
	}

	public ScriptComportamento getComportamento(String referencia) {
		return getComportamento(referencia, true);
	}
	
	public ScriptComportamento getComportamento(String referencia, boolean auto) {
		if(referencia == null)
			return null;
		
		String extencao = referencia.substring(referencia.lastIndexOf(".")+1);
		
		if(extencao.equalsIgnoreCase("groovy")) {
			String ref = null;
			if(auto)
				ref = diretorioComportamentos + referencia;
			else
				ref = referencia;
			
			ScriptComportamentoGroovy script = new ScriptComportamentoGroovy(ref);
			
			try {
				script.compile();
				
			} catch (Exception e) {
				Motor.obterInstancia().tratarExcecao(e);
			}
			
			return script;
		} else if(extencao.equalsIgnoreCase("js")) {
			String ref = null;
			ScriptComportamento script = (ScriptComportamento)gerenciador.get(referencia);
			
			if(auto)
				ref = diretorioComportamentos + referencia;
			else
				ref = referencia;
				
			if( script == null) {
				script = new ScriptComportamentoJavaScript(ref);
				
				try {				
					script.compile();
				} catch(Exception e) {
					e.printStackTrace();
				}
				
				gerenciador.put(referencia, script, PRIORIDADE_SCRIPTS);
				
				return script;
			} else {
				return new ScriptComportamentoJavaScript((ScriptComportamentoJavaScript)script);
			}
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
		Properties prop = new Properties();
		prop.loadFromXML(new FileInputStream(diretorioFalas + arquivo));
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

	public EfeitoAlfaCor getEfeitoAlfaCor(float r, float g, float b, float a, int duracao, int frequencia) {
		if (efeitoAlfaCorPadrao == null) {
			efeitoAlfaCorPadrao = new EfeitoAlfaCor();
			Color corF = new Color(r,g,b);
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
		
	public Entidade inserirEntidadeCenario(String nome, String comportamento, float x, float y, float vx, float vy, int nivelCena, int excluidos) {		
		Entidade ent = getEntidade(nome);
		((Ator)ent.getAtor()).setX(x);
		((Ator)ent.getAtor()).setY(y);
		((Ator)ent.getAtor()).setVelocidadeX(vx);
		((Ator)ent.getAtor()).setVelocidadeY(vy);
		
		if(comportamento != null)
			ent.setComportamento(getComportamento(comportamento));
		
		((Cenario)Motor.obterInstancia().getCenaAtual()).adicionarAtor((Ator)ent.getAtor(), nivelCena, excluidos);
		return ent;
	}
	
	public Entidade inserirEntidadeCenario(String nome, float x, float y, float vx, float vy, int nivelCena, int excluidos) {
		return inserirEntidadeCenario(nome, null, x, y, vx, vy, nivelCena, excluidos);
	}
	
	public Entidade inserirEntidadeCenario(String nome, String comportamento, float x, float y, int nivelCena, int excluidos) {
		
		return inserirEntidadeCenario(nome, comportamento, x, y, 0, 0, nivelCena, excluidos);
	}

	public EmissorParticulas getEmissorParticulas(String nome) throws IOException, SlickException {			
		ParticleSystem p = ParticleIO.loadConfiguredSystem(diretorioEmissores+nome+".xml");
		return new EmissorParticulas(p);
	}	
	
	public EmissorParticulas getEmissorParticulas(String nome, int tempoMaximo) throws IOException, SlickException {			
		ParticleSystem p = ParticleIO.loadConfiguredSystem(diretorioEmissores+nome+".xml");
		EmissorParticulas em = new EmissorParticulas(p, tempoMaximo);
		em.setNome(nome);
		return em;
	}
	
	public EfeitoEmissor getEfeitoEmissor(String nome, Desenho dono, int tempoMaximo) throws IOException, SlickException {
		EfeitoEmissor em = new EfeitoEmissor(dono, getEmissorParticulas(nome, tempoMaximo));
		return em;
	}
	
	public Animacao getAnimacao(String imgRef, int comp, int alt, int linha, int coluna,
			int quadros, boolean repetir, int duracao) throws IOException, SlickException {
		return new Animacao(imgRef, comp, alt, linha, coluna, quadros, repetir, duracao);
	}
	
	public Efeito getEfeitoAnimacao(Desenho dono, String imgRef, int comp, int alt, int linha, int coluna,
			int quadros, boolean repetir, int duracao) throws IOException, SlickException {
		return new Efeito(dono, imgRef, comp, alt, linha, coluna, quadros, repetir, duracao);
	}
	
	public EventoColisao getEventoColisao(Entidade entidade, Cena cena, float nx, float ny, ROVector2f ponto) {
		EventoColisao col = new EventoColisao(entidade, cena);
		
		col.setNormalX(nx);
		col.setNormalY(ny);
		col.setPontoColisao(ponto);
		
		return col;
	}
}
