package br.org.gamexis.plataforma.cena;

import org.newdawn.slick.SlickException;

public class Efeito extends Animacao {

	private Desenho referencia;
	private boolean naoExcluir = false;
	private String nome;
	
	public Efeito() {
	}
	
	public Efeito(Desenho referencia, String imgRef, int comp, int alt, int linha,
			int quadros, boolean repetir, int duracao) throws SlickException {
		super(imgRef, comp, alt, linha, 0,
				quadros, repetir, duracao);
		
		this.referencia = referencia;
		naoExcluir = repetir;
		nome = imgRef;
	}
	
	public Efeito(Desenho referencia, String imgRef, int comp, int alt, int linha, int coluna,
			int quadros, boolean repetir, int duracao) throws SlickException {		
		super(imgRef, comp, alt, linha, coluna, quadros, repetir, duracao);
		
		this.referencia = referencia;
		naoExcluir = repetir;
		nome = imgRef;
	}
	
	public void setNaoExcluir(boolean naoExcluir) {
		this.naoExcluir = naoExcluir;
	}
	
	public boolean naoExcluir() {
		return naoExcluir;
	}
	
	public Efeito(Efeito fonte) {
		super(fonte);
		referencia = fonte.referencia;
		naoExcluir = fonte.naoExcluir;
		nome = fonte.nome;
	}		
	
	public Efeito clone() {		
		return new Efeito(this);
	}
	
	@Override
	public void atualizar(int delta) {
		
		if(ultimoQuadro()) {
			if(!naoExcluir) {
				excluir();
			}
		}
		
		super.atualizar(delta);
		
		if(referencia != null && referencia.isDisposed()) {
			referencia = null;
		}
		
		if(referencia != null) {
			setX(referencia.getX());
			setY(referencia.getY());
		}
				
	}	
	
	public Desenho getReferencia() {
		return referencia;
	}
	
	public void setReferencia(Desenho referencia) {
		this.referencia = referencia;
	}
	
	@Override
	public String getNome() {
		return nome;
	}
}
