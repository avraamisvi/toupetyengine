package br.org.gamexis.plataforma.motor;

import org.lwjgl.Sys;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import br.org.gamexis.plataforma.cena.Desenho;

public class Transicao implements Desenho {

	private Image sequencia[];// fazer um mosaico com as imagens
	private int posicao;
	private long intervalo;
	private long intervaloAtual;
	private long inicio;
	private boolean voltando;
	private boolean terminado;

	private float comp;
	private float alt;

	private float x;
	private float y;

	/**
	 * Construtor
	 * 
	 * @param comprimento
	 *            comprimento total
	 * @param altura
	 *            altura total
	 * @param intervalo
	 *            intervalo de cada quadro
	 */
	public Transicao(float comprimento, float altura, long intervalo) {
		comp = comprimento;
		alt = altura;
		this.intervalo = intervalo;
	}

	/**
	 * Sequencia com uma linha e N colunas.
	 * 
	 * @param imagem
	 *            base
	 * @param comp
	 *            comprimento da coluna
	 * @param alt
	 *            altura da coluna
	 * @param quadros
	 *            quantidade de colunas
	 */
	public void setImagemBase(Image imagem, int comp, int alt, int quadros) {
		SpriteSheet sp = new SpriteSheet(imagem, comp, alt);
		sequencia = new Image[quadros];

		for (int i = 0; i < quadros; i++) {
			sequencia[i] = sp.getSprite(i, 0);
		}
	}

	@Override
	public void desenhar(Graphics g) {
		Image img = sequencia[posicao];
		float i = 0, j = 0;

		while (i < getComprimento()) {
			while (j < getAltura()) {
				img.draw(i, j);
				j += img.getHeight();
			}
			i += img.getWidth();
			j = 0;
		}

		atualizarPosicao();
	}

	public void atualizarPosicao() {
		if (!terminado) {
			if(Sys.getTime() - inicio > intervalo) {					
				inicio = Sys.getTime();
			} else {
				return;
			}
			
			if (voltando) {
				posicao--;				
				
				if (posicao < 0) {
					terminado = true;
					posicao = 0;
				}
			} else {
				posicao++;
				if (posicao >= sequencia.length) {
					posicao = sequencia.length - 1;
					terminado = true;
				}
			}
		}
	}

	@Override
	public boolean estaVisivel() {
		return false;
	}

	@Override
	public void excluir() {
	}

	@Override
	public boolean excluido() {
		return false;
	}

	public void setAltura(float altura) {
		alt = altura;
	}

	public void setComprimento(float comprimento) {
		comp = comprimento;
	}
	
	@Override
	public float getAltura() {
		return alt;
	}

	@Override
	public float getComprimento() {
		return comp;
	}

	@Override
	public Color getEfeitoCor() {
		return null;
	}

	@Override
	public String getNome() {
		return null;
	}

	@Override
	public float getX() {
		return 0;
	}

	@Override
	public float getY() {
		return 0;
	}

	@Override
	public void setEfeitoCor(Color cor) {

	}

	@Override
	public void setVisivel(boolean visivel) {

	}

	@Override
	public void setX(float x) {

	}

	@Override
	public void setY(float y) {
	}

	public void setTerminado(boolean terminado) {
		this.terminado = terminado;
	}
	
	public void setVoltando(boolean voltando) {
		this.voltando = voltando;
	}
	
	public boolean voltando() {
		return voltando;
	}
	
	public void parar() {
		terminado = true;
		voltando = true;
	}
	
	public void iniciarIndo() {
		terminado = false;
		voltando = false;
		posicao = 0;
		inicio = 0;
	}
	
	public void iniciarVoltando() {
		terminado = false;
		voltando = true;
		posicao = sequencia.length-1;
		inicio = 0;
	}	

	public boolean terminado() {
		return terminado;
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
