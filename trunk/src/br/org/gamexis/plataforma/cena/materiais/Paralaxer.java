package br.org.gamexis.plataforma.cena.materiais;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.cena.Ator;
import br.org.gamexis.plataforma.cena.Atualizavel;
import br.org.gamexis.plataforma.cena.Desenho;
import br.org.gamexis.plataforma.motor.RecursosFactory;


public class Paralaxer implements Desenho {

	private float fatorXPadrao = 1;
	private float fatorYPadrao = 1;
	private float velocidadeDeslocY = 0;
	private float velocidadeDeslocX = 0;
	private float deslocY = 0;
	private float deslocX = 0;
	private float velocidadeX = 0;
	private float velocidadeY = 0;

	private float x = 0;
	private float y = 0;

	private float oldjCamX = 0;
	private float oldjCamY = 0;

	private float comprimento = 0;
	private float altura = 0;

	private float maxY = 0;
	private float maxX = 0;

	private float minY = 0;
	private float minX = 0;

	private boolean completarX = true;
	private boolean completarY = false;

	private String nome;
	
	private List<Image> imagens = new ArrayList<Image>();
	private boolean disposed;

	public Paralaxer(String nome) {
		this.nome = nome;
	}
	
	public void adicionarImagen(Image image) {
		comprimento += image.getWidth();
		imagens.add(image);
	}

	@Override
	public void desenhar(Graphics g) {
		desenhar(g, fatorXPadrao, fatorYPadrao);
	}

	@Override
	public void desenhar(Graphics g, float fatorX, float fatorY) {
		float compAnt = 0;
		float comprimentoTotal = 0;
		
		BigDecimal dividendo = new BigDecimal(deslocX);
		BigDecimal divisor = new BigDecimal(comprimento*fatorX);
		long quocienteDeslocX = (long)(dividendo.divideToIntegralValue(divisor).longValue());
		
		float deslocRealX = deslocX - (quocienteDeslocX*(comprimento*fatorX));

		float xd = x - deslocRealX;
		while(comprimentoTotal < Motor.obterInstancia().getConfigGeral().getResolucao().getComprimento()) {
			for (Image img : imagens) {	 
					img.draw(xd + compAnt, y - deslocY, img.getWidth()
						* fatorX, img.getHeight() * fatorY);
				
				compAnt += img.getWidth() * fatorX;
				comprimentoTotal += (xd + compAnt);
			}
			if(!completarX)
				break;
		}
		
		calcularDeslocamento(
				Motor.obterInstancia().getCenaAtual().getCameraX(), Motor
						.obterInstancia().getCenaAtual().getCameraY());
	}

	
	public void calcularDeslocamento(float camX, float camY) {
		float deslcTemp;

		deslocX += (camX - oldjCamX) * velocidadeDeslocX;
		deslocX += velocidadeX;
		
		oldjCamX = camX;

		deslcTemp = (camY - oldjCamY) * velocidadeDeslocY;

		if ((deslocY + deslcTemp) > minY && (deslocY + deslcTemp) < maxY) {
			deslocY += deslcTemp;
		}

		oldjCamY = camY;
	}

	@Override
	public void dispose() {
		for (Image img : imagens) {	 
			RecursosFactory.getInstancia().libertarRecurso(img.getReference().substring(img.getReference().lastIndexOf(File.separator)));
		}
		disposed = true;
	}

	@Override
	public boolean estaVisivel() {
		return true;
	}

	@Override
	public boolean excluido() {
		return false;
	}

	@Override
	public void excluir() {
	}

	@Override
	public float getAltura() {
		return altura;
	}

	@Override
	public float getComprimento() {
		return comprimento;
	}

	@Override
	public Color getEfeitoCor() {
		return Color.white;
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
	public boolean isDisposed() {
		return disposed;
	}

	@Override
	public void setEfeitoCor(Color cor) {
	}

	@Override
	public void setVisivel(boolean visivel) {
	}

	@Override
	public void setX(float x) {
		this.x = x;
	}

	@Override
	public void setY(float y) {
		this.y = y;
	}

	public void setFatorTamanho(float fator) {
		fatorXPadrao = fator;
		fatorYPadrao = fator;
	}
	
	public float getFatorX() {
		return fatorXPadrao;
	}

	public void setFatorX(float fatorX) {
		this.fatorXPadrao = fatorX;
	}

	public float getFatorY() {
		return fatorYPadrao;
	}

	public void setFatorY(float fatorY) {
		this.fatorYPadrao = fatorY;
	}

	public float getVelocidadeDeslocY() {
		return velocidadeDeslocY;
	}

	public void setVelocidadeDeslocY(float velocidadeDeslocY) {
		this.velocidadeDeslocY = velocidadeDeslocY;
	}

	public float getVelocidadeDeslocX() {
		return velocidadeDeslocX;
	}

	public void setVelocidadeDeslocX(float velocidadeDeslocX) {
		this.velocidadeDeslocX = velocidadeDeslocX;
	}

	public float getDeslocY() {
		return deslocY;
	}

	public void setDeslocY(float deslocY) {
		this.deslocY = deslocY;
	}

	public float getDeslocX() {
		return deslocX;
	}

	public void setDeslocX(float deslocX) {
		this.deslocX = deslocX;
	}

	public float getVelocidadeX() {
		return velocidadeX;
	}

	public void setVelocidadeX(float velocidadeX) {
		this.velocidadeX = velocidadeX;
	}

	public float getVelocidadeY() {
		return velocidadeY;
	}

	public void setVelocidadeY(float velocidadeY) {
		this.velocidadeY = velocidadeY;
	}

	public List<Image> getImagens() {
		return imagens;
	}

	public void setImagens(List<Image> imagens) {
		this.imagens = imagens;
	}

	public void setComprimento(float comprimento) {
		this.comprimento = comprimento;
	}

	public void setAltura(float altura) {
		this.altura = altura;
	}

	public void setCompletarX(boolean completarX) {
		this.completarX = completarX;
	}

	public void setCompletarY(boolean completarY) {
		this.completarY = completarY;
	}

	public float getMaxY() {
		return maxY;
	}

	public void setMaxY(float maxY) {
		this.maxY = maxY;
	}

	public float getMaxX() {
		return maxX;
	}

	public void setMaxX(float maxX) {
		this.maxX = maxX;
	}

	public float getMinY() {
		return minY;
	}

	public void setMinY(float minY) {
		this.minY = minY;
	}

	public float getMinX() {
		return minX;
	}

	public void setMinX(float minX) {
		this.minX = minX;
	}

	public boolean isCompletarX() {
		return completarX;
	}

	public boolean isCompletarY() {
		return completarY;
	}

}
