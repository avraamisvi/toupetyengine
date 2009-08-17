package br.org.gamexis.plataforma.cena;

import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import br.org.gamexis.plataforma.cena.evento.Listerner;
import br.org.gamexis.plataforma.entidade.Entidade;
import br.org.gamexis.plataforma.exception.GXException;
import br.org.gamexis.plataforma.script.ScriptComportamento;
import br.org.gamexis.plataforma.script.ScriptComportamentoGroovy;

public interface Cena {

	public abstract boolean estaIniciada();

	public abstract void setEntidadeCursor(Entidade entidade);

	public abstract void iniciar(GameContainer container) throws SlickException;

	/**
	 * Avalia colisões etc.
	 * 
	 * @throws GXException
	 */
	public abstract void atualizar(GameContainer container, int delta)
			throws GXException;

	public abstract void setComportamento(ScriptComportamento comportamento);

	public abstract ScriptComportamento getComportamento();

	/**
	 * Desenha a cena.
	 */
	public abstract void desenhar(GameContainer container, Graphics g);

	public abstract int getComprimento();

	public abstract void setComprimento(int comprimento);

	public abstract int getAltura();

	public abstract void setAltura(int altura);

	public abstract float getCameraX();

	public abstract void setCameraX(float cameraX);

	public abstract float getCameraY();

	public abstract void setCameraY(float cameraY);

	public abstract float getCameraVelocidadeX();

	public abstract void setCameraVelocidadeX(float cameraVelocidadeX);

	public abstract float getCameraVelocidadeY();

	public abstract void setCameraVelocidadeY(float cameraVelocidadeY);

	public abstract void adicionarFundo(Desenho desenho);

	public abstract void adicionarMeio(Desenho desenho);

	public abstract void adicionarAnimados(Desenho desenho);

	public abstract void adicionarFrente(Desenho desenho);

	public abstract void atualizarCamera(int velocidadeX, int velocidadeY);

	public abstract void tratarExcecao(Throwable cause);

	public abstract Tela getAnimados();

	public abstract Tela getFrente();

	public abstract Tela getFundo();

	public abstract Tela getMeio();

	public abstract void adicionarDesenho(Desenho desenho, int nivel);

	public abstract void excluirDesenho(Desenho desenho);

	/**
	 * Exclui um desenho a força
	 * 
	 * @param desenho
	 */
	public abstract void forceExcluirDesenho(Desenho desenho);

	public abstract void removaDesenho(Desenho desenho);

	public abstract List<Desenho> getExcluidos();

	public abstract void removaExclusao(Desenho desenho);

	/**
	 * Elimina toda e qualquer referencia dos objetos listados para serem
	 * removidos.
	 */
	public abstract void purgeExcluidos();

	public abstract void setNome(String nome);

	public abstract String getNome();

	public abstract void setCorFundo(float r, float g, float b);
	
	public void finalizar();

	public Listerner getListernerAtual();
}