package br.org.gamexis.plataforma.cena;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.motor.RecursosFactory;

public class Animacao implements Animado {

	private Color efeitoCor = Color.white;
	private Image imagem;
	private SpriteSheet base;
	private String imgRef;
	private Animation anim;
	private float x;
	private float y;
	private boolean repetir;
	private String nome;
	
	private float deslocX;
	private float deslocY;
	private int tempoDecorrido;
	private int duracaoPrevista;
	public static int VELOCIDADE_BASICA = 80;
	private boolean visivel = true;

	public Animacao() {
	}

	public Animacao(String imgRef) throws SlickException {
		imagem = new Image(imgRef);
		this.imgRef = imgRef;
	}

	public Animacao(String imgRef, int comp, int alt, int linha, int quadros,
			boolean repetir) throws SlickException {
		this(imgRef, comp, alt, linha, 0, quadros, repetir, VELOCIDADE_BASICA);
	}

	public Animacao(String imgRef, int comp, int alt, int linha, int coluna,
			int quadros, boolean repetir) throws SlickException {

		this(imgRef, comp, alt, linha, coluna, quadros, repetir,
				VELOCIDADE_BASICA);
	}

	public Animacao(String imgRef, int comp, int alt, int linha, int coluna,
			int quadros, boolean repetir, int duracao) throws SlickException {

		Image img = RecursosFactory.getInstancia().getImage(imgRef);
		SpriteSheet spriteSheet = new SpriteSheet(img, comp, alt);
		construir(spriteSheet, comp, alt, linha, coluna, quadros, repetir, duracao);
	}
	
	public Animacao(String imgRef, int comp, int alt, int linha, int coluna,
			int quadros, boolean repetir, boolean grande, int duracao) throws SlickException {

		Image img = RecursosFactory.getInstancia().getImage(imgRef, grande);
		SpriteSheet spriteSheet = new SpriteSheet(img, comp, alt);
		construir(spriteSheet, comp, alt, linha, coluna, quadros, repetir, duracao);
	}

	public Animacao(SpriteSheet spriteSheet, int comp, int alt, int linha, int coluna,
			int quadros, boolean repetir, int duracao) throws SlickException {
		construir(spriteSheet, comp, alt, linha, coluna, quadros, repetir, duracao);
	}
	
	public Animacao(String imgRef, HashMap<String, Tile> imagens, int comp, int alt, int linha, int coluna,
			int quadros, boolean repetir, int duracao) throws SlickException {
		construir(imgRef, imagens, comp, alt, linha, coluna, quadros, repetir, duracao);
	}
	
	private void construir(SpriteSheet spriteSheet, int comp, int alt, int linha, int coluna,
			int quadros, boolean repetir, int duracao) throws SlickException {
		
		base = spriteSheet;
		anim = new Animation(false);
		anim.setLooping(repetir);
		this.repetir = repetir;

		for (int i = 0; i < quadros; i++) {
			anim.addFrame(base.getSprite(i + coluna, linha), duracao);
		}

		anim.start();
		duracaoPrevista = quadros * duracao;

		this.imgRef = imgRef;
	}
	
	private void construir(String imgRef, HashMap<String, Tile> imagens, int comp, int alt, int linha, int coluna,
			int quadros, boolean repetir, int duracao) throws SlickException {
		
		base = null;
		anim = new Animation(false);
		anim.setLooping(repetir);
		this.repetir = repetir;

		//imagens.put("l_"+tile.getLinha()+"c_"+tile.getColuna(), tile);
		
		for (int i = 0; i < quadros; i++) {	
						
			Image img = imagens.get("l_"+ linha + "c_" + (i + coluna)).getImagem();
			anim.addFrame(img, duracao);
		}

		anim.start();
		duracaoPrevista = quadros * duracao;

		this.imgRef = imgRef;
	}
	
	/**
	 * Metodo de clonagem.
	 * 
	 * @param fonte
	 */
	public Animacao(Animacao fonte) {

		anim = new Animation(false);
		anim.setLooping(fonte.repetir);

		for (int i = 0; i < fonte.anim.getFrameCount(); i++) {
			anim.addFrame(fonte.anim.getImage(i), fonte.anim.getDuration(i));
		}

		anim.start();
		duracaoPrevista = fonte.duracaoPrevista;
		this.imgRef = fonte.imgRef;
		this.nome = fonte.nome;
		deslocX = fonte.deslocX;
		deslocY	= fonte.deslocY;
	}

	public void parar() {
		anim.stop();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		
		return new Animacao(this);
	}

	public void reiniciar() {
		anim.setCurrentFrame(0);
		anim.stop();
		anim.restart();
		anim.stop();
		anim.start();
		tempoDecorrido = 0;
	}

	public int getQuadroAtual() {
		return anim.getFrame();
	}

	public void atualizar(int delta) {
		anim.update(delta);
		tempoDecorrido += delta;		
	}

	@Override
	public void desenhar(Graphics g) {
		if (visivel) {
			if (Motor.obterInstancia().isModoDebug()) {
				g.setColor(Color.red);
				g.drawString("X:" + (x - deslocX) + " Y:" + (y - deslocY), x, y- deslocY - 20);
			}
			
			anim.draw(x - deslocX, y - deslocY, efeitoCor);
		}
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

	public float getDeslocX() {
		return deslocX;
	}

	public void setDeslocX(float deslocX) {
		this.deslocX = deslocX;
	}

	public float getDeslocY() {
		return deslocY;
	}

	public void setDeslocY(float deslocY) {
		this.deslocY = deslocY;
	}

	public boolean ultimoQuadro() {// TODO REFAZER
		return (anim.getFrame() == (anim.getFrameCount() - 1));
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

	public float getComprimento() {
		return anim.getWidth();
	}

	public float getAltura() {
		return anim.getHeight();
	}

	public int getTempoDecorrido() {
		return tempoDecorrido;
	}

	public String getImagemReferencia() {
		return this.imgRef;
	}

	@Override
	public boolean estaVisivel() {
		return visivel;
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

	@Override
	public void dispose() {
		
	}

	@Override
	public boolean isDisposed() {
		return false;
	}
}
