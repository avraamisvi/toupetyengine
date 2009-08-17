package br.org.gamexis.plataforma.cena;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.entidade.Entidade;
import br.org.gamexis.plataforma.exception.GXException;
import br.org.gamexis.plataforma.script.ScriptComportamento;
import br.org.gamexis.plataforma.script.ScriptComportamentoGroovy;

public abstract class CenaBase implements Cena {
	
	private String nome;
	private ScriptComportamento comportamento;// geralemente associado a controle
	private int comprimento;
	private int altura;
	private Color corFundo;
	
	private List<Desenho> excluidos = new ArrayList<Desenho>();

	private float cameraX;
	private float cameraY;
	private float cameraVelocidadeX;
	private float cameraVelocidadeY;

	private Tela fundo = new Tela();
	private Tela meio = new Tela();
	private Tela animados = new Tela();
	private Tela frente = new Tela();
	private List<Desenho> todos = new ArrayList<Desenho>();

	private boolean iniciada;
	// private GameContainer container;	
	
	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#estaIniciada()
	 */
	public boolean estaIniciada() {
		return iniciada;
	}
	
	
	public CenaBase() {
	}
	
	public void setEntidadeCursor(Entidade entidade) {		
	}
	
	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#iniciar(org.newdawn.slick.GameContainer)
	 */
	public void iniciar(GameContainer container) throws SlickException {
		iniciada = true;
		try {
			Motor.obterInstancia().capturarCursor();
			purgeExcluidos();
			if (comportamento != null) {
				comportamento.execute("iniciar", new Object[] { this, Motor.obterInstancia() });
			}
		} catch (Throwable cause) {
			tratarExcecao(cause);
		}		
	}
	
	public void finalizar() {
		
//		fundo.dispose();
//		meio.dispose();
//		animados.dispose();
//		frente.dispose();
		
		for(Desenho d : todos)
			d.dispose();
		
		excluidos = null;
		todos = null;
		fundo = null;
		meio = null;
		animados = null;
		frente = null;
		comportamento = null;
	}
	
	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#atualizar(org.newdawn.slick.GameContainer, int)
	 */
	public void atualizar(GameContainer container, int delta)
			throws GXException {

		for (Desenho desen : todos) {
			if (desen.excluido()) {
				removaDesenho(desen);
			}
		}
		try {
			purgeExcluidos();
			if (comportamento != null) {
				comportamento.execute("atualizar", new Object[] { this, Motor.obterInstancia() });
			}
		} catch (Throwable cause) {
			tratarExcecao(cause);
		}
	}

	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#setComportamento(br.org.gamexis.plataforma.script.ScriptComportamento)
	 */
	public void setComportamento(ScriptComportamento comportamento) {
		this.comportamento = comportamento;
	}

	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#getComportamento()
	 */
	public ScriptComportamento getComportamento() {
		return comportamento;
	}

	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#desenhar(org.newdawn.slick.GameContainer, org.newdawn.slick.Graphics)
	 */
	public void desenhar(GameContainer container, Graphics g) {
		
		if(corFundo != null)
			g.setBackground(corFundo);
		
		fundo.desenhe(g);
		meio.desenhe(g);
		animados.desenhe(g);
		frente.desenhe(g);
	}

	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#getComprimento()
	 */
	public int getComprimento() {
		return comprimento;
	}

	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#setComprimento(int)
	 */
	public void setComprimento(int comprimento) {
		this.comprimento = comprimento;
	}

	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#getAltura()
	 */
	public int getAltura() {
		return altura;
	}

	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#setAltura(int)
	 */
	public void setAltura(int altura) {
		this.altura = altura;
	}

	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#getCameraX()
	 */
	public float getCameraX() {
		return cameraX;
	}

	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#setCameraX(float)
	 */
	public void setCameraX(float cameraX) {
		this.cameraX = cameraX;
	}

	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#getCameraY()
	 */
	public float getCameraY() {
		return cameraY;
	}

	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#setCameraY(float)
	 */
	public void setCameraY(float cameraY) {
		this.cameraY = cameraY;
	}

	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#getCameraVelocidadeX()
	 */
	public float getCameraVelocidadeX() {
		return cameraVelocidadeX;
	}

	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#setCameraVelocidadeX(float)
	 */
	public void setCameraVelocidadeX(float cameraVelocidadeX) {
		this.cameraVelocidadeX = cameraVelocidadeX;
	}

	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#getCameraVelocidadeY()
	 */
	public float getCameraVelocidadeY() {
		return cameraVelocidadeY;
	}

	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#setCameraVelocidadeY(float)
	 */
	public void setCameraVelocidadeY(float cameraVelocidadeY) {
		this.cameraVelocidadeY = cameraVelocidadeY;
	}

	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#adicionarFundo(br.org.gamexis.plataforma.cena.Desenho)
	 */
	public void adicionarFundo(Desenho desenho) {
		fundo.adicionarDesenho(desenho);
	}

	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#adicionarMeio(br.org.gamexis.plataforma.cena.Desenho)
	 */
	public void adicionarMeio(Desenho desenho) {
		meio.adicionarDesenho(desenho);
	}

	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#adicionarAnimados(br.org.gamexis.plataforma.cena.Desenho)
	 */
	public void adicionarAnimados(Desenho desenho) {
		animados.adicionarDesenho(desenho);
	}

	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#adicionarFrente(br.org.gamexis.plataforma.cena.Desenho)
	 */
	public void adicionarFrente(Desenho desenho) {
		frente.adicionarDesenho(desenho);
	}

	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#atualizarCamera(int, int)
	 */
	public void atualizarCamera(int velocidadeX, int velocidadeY) {
	}

	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#tratarExcecao(java.lang.Throwable)
	 */
	public void tratarExcecao(Throwable cause) {
		cause.printStackTrace();
		Logger.getLogger(Motor.ARQUIVO_LOG).log(Level.SEVERE, "erro cenario:", cause);
	}

	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#getAnimados()
	 */
	public Tela getAnimados() {
		return animados;
	}

	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#getFrente()
	 */
	public Tela getFrente() {
		return frente;
	}

	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#getFundo()
	 */
	public Tela getFundo() {
		return fundo;
	}

	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#getMeio()
	 */
	public Tela getMeio() {
		return meio;
	}

	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#adicionarDesenho(br.org.gamexis.plataforma.cena.Desenho, int)
	 */
	public void adicionarDesenho(Desenho desenho, int nivel) {
		switch (nivel) {
		case NivelCena.fundo:
			adicionarFundo(desenho);
			break;
		case NivelCena.meio:
			adicionarMeio(desenho);
			break;
		case NivelCena.animados:
			adicionarAnimados(desenho);
			break;
		case NivelCena.frente:
			adicionarFrente(desenho);
			break;
		}

		todos.add(desenho);
	}

	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#excluirDesenho(br.org.gamexis.plataforma.cena.Desenho)
	 */
	public void excluirDesenho(Desenho desenho) {
		if (desenho.excluido()) {
			todos.remove(desenho);
			if (fundo.removerDesenho(desenho)) {
				return;
			} else if (meio.removerDesenho(desenho)) {
				return;
			} else if (animados.removerDesenho(desenho)) {
				return;
			} else if (frente.removerDesenho(desenho)) {
				return;
			}
		}
	}

	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#forceExcluirDesenho(br.org.gamexis.plataforma.cena.Desenho)
	 */
	public void forceExcluirDesenho(Desenho desenho) {

		todos.remove(desenho);
		if (fundo.removerDesenho(desenho)) {
			return;
		} else if (meio.removerDesenho(desenho)) {
			return;
		} else if (animados.removerDesenho(desenho)) {
			return;
		} else if (frente.removerDesenho(desenho)) {
			return;
		}
	}

	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#removaDesenho(br.org.gamexis.plataforma.cena.Desenho)
	 */
	public void removaDesenho(Desenho desenho) {
		excluidos.add(desenho);
	}

	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#getExcluidos()
	 */
	public List<Desenho> getExcluidos() {
		return excluidos;
	}

	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#removaExclusao(br.org.gamexis.plataforma.cena.Desenho)
	 */
	public void removaExclusao(Desenho desenho) {
		excluidos.remove(desenho);
	}

	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#purgeExcluidos()
	 */
	public void purgeExcluidos() {
		for (Desenho desen : excluidos) {
			excluirDesenho(desen);
		}
		excluidos = null;
		excluidos = new ArrayList<Desenho>();
		// System.gc();
	}
	
	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#setNome(java.lang.String)
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#getNome()
	 */
	public String getNome() {
		return nome;
	}
	
	/* (non-Javadoc)
	 * @see br.org.gamexis.plataforma.cena.Cena#setCorFundo(float, float, float)
	 */
	public void setCorFundo(float r, float g, float b) {
		corFundo = new Color(r, g, b);
	}
	
	public int getTotalObjetos() {
		return todos.size();
	}
	
}
