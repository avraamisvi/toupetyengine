package br.org.gamexis.plataforma.cena.componentes.util;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import br.org.gamexis.plataforma.cena.Mosaico;

/**
 * Caixa de um balao de texto baseado numa imagem, 
 * usando um mosaico.
 * 
 * @author abraao
 *
 */
public class CaixaMosaico implements Caixa {
	
	String nome;
	
	float x;
	float y;
	
	float comprimento;
	float altura;
	
	Mosaico fundo;
	
	float margem = 25;
	
	boolean excluido = false;
	boolean visivel = true;
	
	public CaixaMosaico() {
		try {
			
			construaQuadro("recursos/imagens/frameVerde.png", 40, 40, 3, 16);
			
//			fundo = new Mosaico("recursos/imagens/frameVerde.png", 40, 40, 10, 16);
//			
//			fundo.adicionarTile("a0", 0, 0, 0, 0, true);
//			for(int i = 1; i <= 14;i++)
//				fundo.adicionarTile("a"+i, 0, i, 0, 1, true);
//			fundo.adicionarTile("af", 0, 15, 0, 2, true);//TOPO
//			
//			fundo.adicionarTile("b0", 1, 0, 1, 0, true);//MEIO
//			for(int i = 1; i <= 14;i++)
//				fundo.adicionarTile("b"+i, 1, i, 1, 1, true);//MEIO
//			fundo.adicionarTile("bf", 1, 15, 1, 2, true);//MEIO
//			
//			fundo.adicionarTile("c0", 2, 0, 2, 0, true);//FUNDO
//			for(int i = 1; i <= 14;i++)
//				fundo.adicionarTile("c"+i, 2, i, 2, 1, true);//FUNDO
//			fundo.adicionarTile("cf", 2, 15, 2, 2, true);//FUNDO
			
			
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Constroi um novo quadro. Assume que a imagem esta dividida em 3x3.
	 * 
	 * @param imagem imagem base
	 * @param comprimento comprimento de cada pedaço do mosaico
	 * @param altura altura de cada pedaço do mosaico
	 * @param linhas quantidade de linhas a serem preenchidas 
	 * @param colunas quantidade de colunas a serem preenchidas 
	 * @throws SlickException
	 */
	public void construaQuadro(String imagem, int comprimento, 
			int altura, int linhas, int colunas) throws SlickException {
		int i = 0, j = 0;
		int colunasImagem = 3;
		int colunaCentral = 1;
		
		fundo = new Mosaico(imagem, comprimento, altura, linhas, colunas);
		
		for(i = 0; i < linhas; i++) {
			fundo.adicionarTile("a"+i+j, i, j, i, 0, true);
			for(j = 1; j < colunas-1; j++) {
				fundo.adicionarTile("a"+i+j, i, j, i, colunaCentral, true);
			}
			fundo.adicionarTile("a"+i+j, i, j, i, colunasImagem-1, true);
			j=0;
		}
	}
	
	@Override
	public float getMargemHorizontal() {
		return margem;
	}

	@Override
	public float getMargemVertical() {
		return margem;
	}
	
	public void setMargem(float margem) {
		this.margem = margem;
	}
	
	@Override
	public void desenhar(Graphics g) {
		fundo.setX(x);
		fundo.setY(y);
		fundo.desenhar(g);
	}

	@Override
	public boolean estaVisivel() {
		return visivel;
	}

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
		return fundo.getAltura();
	}

	@Override
	public float getComprimento() {
		return fundo.getComprimento();
	}

	@Override
	public Color getEfeitoCor() {
		return null;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	@Override
	public String getNome() {
		return nome;
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public void setEfeitoCor(Color cor) {
	}

	public Mosaico getFundo() {
		return fundo;
	}
	
	public void setFundo(Mosaico fundo) {
		this.fundo = fundo;
	}
	
	@Override
	public void setVisivel(boolean visivel) {
		this.visivel = visivel;
	}

	@Override
	public void setX(float x) {
		this.x = x;
	}

	@Override
	public void setY(float y) {
		this.y = y;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isDisposed() {
		// TODO Auto-generated method stub
		return false;
	}

}
