package br.org.gamexis.plataforma.cena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.motor.RecursosFactory;

public class MosaicoAnimado implements Desenho, Animado {
	
	private String nome;
	private Color efeitoCor = Color.white;
	private Parte mapa[][];
	
	private HashMap<String, Parte> mapaNomes = new HashMap<String, Parte>();
	private List<Animacao> animacoes = new ArrayList<Animacao>();
	
	SpriteSheet spriteSheetBase;
	HashMap<String, Tile> imagens;
	
	private float x;
	private float y;

	private int comprimentoTile;
	private int alturaTile;

	private boolean visivel = true;
	
	public MosaicoAnimado(MosaicoAnimado original) {
		comprimentoTile = original.comprimentoTile;
		alturaTile = original.alturaTile;
		
		spriteSheetBase = new SpriteSheet(original.spriteSheetBase.copy(), comprimentoTile, alturaTile);
		mapa = new Parte[original.mapa.length][original.mapa[0].length];
	}
	
	/**
	 * Construtor
	 * 
	 * @param imgRef - imagem base
	 * @param comprimento - comprimento de cada bloco
	 * @param altura - altura de cada bloco
	 * @param linhas - quantidade de linhas do mosaico
	 * @param colunas - quantidade de colunas do mosaico
	 * @throws SlickException
	 */	
	public MosaicoAnimado(String imgRef, int comprimento, int altura, int linhas,
			int colunas) throws SlickException {
		Image img = RecursosFactory.getInstancia().getImage(imgRef);
		spriteSheetBase = new SpriteSheet(img, comprimento, altura);
		mapa = new Parte[linhas][colunas];
		comprimentoTile = comprimento;
		alturaTile = altura;
	}

	/**
	 * Construtor
	 * 
	 * @param imgRef - imagem base
	 * @param comprimento - comprimento de cada bloco
	 * @param altura - altura de cada bloco
	 * @param linhas - quantidade de linhas do mosaico
	 * @param colunas - quantidade de colunas do mosaico
	 * @throws SlickException
	 */	
	public MosaicoAnimado(HashMap<String, Tile> tiles, int comprimento, int altura, int linhas,
			int colunas) throws SlickException {
		//spriteSheetBase = new SpriteSheet(imgRef, comprimento, altura);
		
		imagens = tiles;

		mapa = new Parte[linhas][colunas];
		comprimentoTile = comprimento;
		alturaTile = altura;
	}
	
	@Override
	public void desenhar(Graphics g) {
		for (int linha = 0; linha < mapa.length; linha++) {
			for (int coluna = 0; coluna < mapa[linha].length; coluna++) {
				Parte parte = mapa[linha][coluna];
				
				if (parte != null && parte.visivel) {
					
					Animacao ani = animacoes.get(parte.animacao);
					
					ani.setX(x + (comprimentoTile * coluna));
					ani.setY(y + (alturaTile * linha));					
					ani.desenhar(g);					
				} 
				
				if(Motor.obterInstancia().isModoDebug()) {
					g.drawRect(x + (comprimentoTile * coluna), y + (alturaTile * linha), 
							comprimentoTile, alturaTile);
				}
			}
		}
	}

	/**
	 * Adiciona um tile numa posição específica.
	 * 
	 * @param nome no me do tile
	 * @param linha posicao linha na grade
	 * @param coluna posicao coluna na grade
	 * @param animacao indice da animação
	 * @param visivel
	 */
	public void adicionarTile(String nome, int linha, int coluna, int animacao, boolean visivel) {
		Parte parte = new Parte();
		parte.animacao = animacao;
		parte.visivel = visivel;

		mapa[linha][coluna] = parte;
		mapaNomes.put(nome, parte);
	}

	/**
	 * 
	 * @param linha
	 * @param coluna
	 * @param quadros
	 * @param repetir
	 * @param duracao
	 * @throws SlickException
	 */
	public void adicionarAnimacao(String nome, int linha, int coluna, int quadros, boolean repetir, int duracao) 
		throws SlickException {
		Animacao anim = null;
		if(spriteSheetBase != null) {
			anim = new Animacao(spriteSheetBase, comprimentoTile, alturaTile, 
					linha, coluna, quadros, repetir, duracao);
			anim.setNome(nome);
		} else {
			anim = new Animacao(nome, imagens, comprimentoTile, alturaTile, 
					linha, coluna, quadros, repetir, duracao);
			anim.setNome(nome);			
		}
		animacoes.add(anim);
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
	public void setX(float x) {
		this.x = x;
	}

	@Override
	public void setY(float y) {
		this.y = y;
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
		return 0;
	}

	@Override
	public float getComprimento() {
		return 0;
	}

	@Override
	public boolean estaVisivel() {
		return true;
	}

	@Override
	public void setVisivel(boolean visivel) {
		this.visivel = visivel;
	}
	
	public void setEfeitoCor(Color efeitoCor) {
		this.efeitoCor = efeitoCor;
	}
	
	public Color getEfeitoCor() {
		return efeitoCor;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	class Parte {
		int animacao;
		String nome;
		boolean visivel;
	}

	@Override
	public void atualizar(int delta) {
		for(Animacao anim : animacoes) {
			anim.atualizar(delta);
		}
	}

	@Override
	public void reiniciar() {
		for(Animacao anim : animacoes) {
			anim.reiniciar();
		}		
	}

	@Override
	public boolean ultimoQuadro() {
		return false;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		
		return new MosaicoAnimado(this);
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
