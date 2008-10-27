package br.org.gamexis.plataforma.cena;

import net.phys2d.raw.Body;

import org.newdawn.slick.Color;

import br.org.gamexis.plataforma.entidade.Entidade;

/**
 * Elementos invisiveis no mundo fisico que servem apenas como guias para a
 * colisao. A qualidade de desenho serve apenas para geração de debug visual.
 * 
 * @author abraao
 * 
 */
public class Plataforma implements Colisivel {
	
	private Color efeitoCor = Color.white;
	private Body corpo;
	private boolean visivel = true;
	private Entidade entidade;
	private String nome;
	
	public Plataforma(Entidade entidade, Body corpo) {
		this.corpo = corpo;
		corpo.setPrivateData(this);
		this.entidade = entidade;
		this.entidade.setAtor(this);
	}

	public void setPosicao(float x, float y) {
		corpo.setPosition(x, y);
	}

	@Override
	public void atualizar(int delta) {
		// NOP
	}

	@Override
	public Body getCorpo() {
		return corpo;
	}

	public Entidade getEntidade() {
		return entidade;
	}

	private boolean excluido = false;

	@Override
	public void excluir() {
		excluido = true;
	}

	@Override
	public boolean excluido() {
		return excluido;
	}

	@Override
	public float getAltura() {
		return corpo.getShape().getBounds().getHeight();
	}

	@Override
	public float getComprimento() {
		return corpo.getShape().getBounds().getWidth();
	}

//	public boolean estaVisivel() {
//		return visivel;
//	}
//
//	@Override
//	public void setVisivel(boolean visivel) {
//		this.visivel = visivel;
//	}
	
	public void setEfeitoCor(Color efeitoCor) {
		this.efeitoCor = efeitoCor;
	}
	
	public Color getEfeitoCor() {
		return efeitoCor;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public String getNome() {
		return nome;
	}

	@Override
	public void setEntidade(Entidade entidade) {
		this.entidade = entidade;
	}

	@Override
	public void dispose() {
		corpo = null;
	}

}
