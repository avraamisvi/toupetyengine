package br.org.gamexis.plataforma.cena;

import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class Tile {
	private String nome;
	private int linha;
	private int coluna;
	private Image imagem;
	//private SpriteSheet teste;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public int getLinha() {
		return linha;
	}
	public void setLinha(int linha) {
		this.linha = linha;
	}
	public int getColuna() {
		return coluna;
	}
	public void setColuna(int coluna) {
		this.coluna = coluna;
	}
	public Image getImagem() {
		return imagem;
	}
	public void setImagem(Image imagem) {
		//teste = new SpriteSheet(imagem, imagem.getWidth(), imagem.getHeight());
		this.imagem = imagem;
	}

	
}
