package br.org.gamexis.plataforma.cena;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.motor.RecursosFactory;

public class Mosaico implements Desenho {
	
	private String nome;
	private Color efeitoCor = Color.white;
	private Parte mapa[][];
	HashMap<String, Parte> mapaNomes = new HashMap<String, Parte>();
	
	SpriteSheet recortes;
	HashMap<String, Tile> imagens;
	
	private float x;
	private float y;

	private int comprimentoTile;
	private int alturaTile;

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
	public Mosaico(String imgRef, int comprimento, int altura, int linhas,
			int colunas) throws SlickException {
				
		Image img = RecursosFactory.getInstancia().getImage(imgRef);
		recortes = new SpriteSheet(img, comprimento, altura);		
		mapa = new Parte[linhas][colunas];
		comprimentoTile = comprimento;
		alturaTile = altura;
		
		//criarDoSpriteSheet(new SpriteSheet(imgRef, comprimento, altura));
	}

	public Mosaico(HashMap<String, Tile> tiles, int comprimento, int altura, int linhas,
			int colunas) throws SlickException {
		
		imagens = tiles;
		mapa = new Parte[linhas][colunas];
		comprimentoTile = comprimento;
		alturaTile = altura;
	}
	
//	public void criarDoSpriteSheet(SpriteSheet s) {
//		imagens = new HashMap<String, Tile>();
//		for (int linha = 0; linha < s.getVerticalCount(); linha++) {
//			for (int coluna = 0; coluna < s.getHorizontalCount(); coluna++) {
//				Tile t = new Tile();
//				t.setImagem(s.getSprite(coluna, linha));
//				imagens.put("l_"+linha+"c_"+coluna, t);
//			}
//		}
//		
//	}
	
	@Override
	public void desenhar(Graphics g) {
		for (int linha = 0; linha < mapa.length; linha++) {
			for (int coluna = 0; coluna < mapa[linha].length; coluna++) {
				Parte parte = mapa[linha][coluna];
				if (parte != null && parte.visivel) {		
					getSprite(parte.linha, parte.coluna).draw(
							x + (comprimentoTile * coluna),
							y + (alturaTile * linha), efeitoCor);		
				}
				
				if(Motor.obterInstancia().isModoDebug()) {
					g.drawRect(x + (comprimentoTile * coluna), y + (alturaTile * linha), 
							comprimentoTile, alturaTile);
				}
			}
		}
	}

	public Image getSprite(int linha, int coluna) {
		if(recortes != null) {
			return recortes.getSprite(coluna, linha);	
		} else {
			Image img = imagens.get("l_"+linha+"c_"+coluna).getImagem();
			//img.
			return img;//.getSubImage(0, 0, comprimentoTile, alturaTile);
		}
	}
	
	/**
	 * Adiciona um tile numa posição específica.
	 * 
	 * @param nome no me do tile
	 * @param linha posicao linha na grade
	 * @param coluna posicao coluna na grade
	 * @param parteLinha pedaço linha do desenho
	 * @param parteColuna pedaço coluna do desenho
	 * @param visivel
	 */
	public void adicionarTile(String nome, int linha, int coluna, int parteLinha,
			int parteColuna, boolean visivel) {
		Parte parte = new Parte();
		parte.linha = parteLinha;
		parte.coluna = parteColuna;
		parte.visivel = visivel;

		mapa[linha][coluna] = parte;
		mapaNomes.put(nome, parte);
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
		int linha;
		int coluna;
		String nome;
		boolean visivel;
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
