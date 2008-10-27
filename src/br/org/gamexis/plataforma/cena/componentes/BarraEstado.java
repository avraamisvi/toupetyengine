package br.org.gamexis.plataforma.cena.componentes;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import br.org.gamexis.plataforma.cena.Desenho;
import br.org.gamexis.plataforma.entidade.logestado.BarraLogEstado;

public class BarraEstado implements Desenho {
	
	private Color efeitoCor = Color.white;
	private float x, y;
	private float deslocamentoX, deslocamentoy;

	private Image contorno;
	private Image unidade;
	private BarraLogEstado barraEstadoPOJO;
	private float borda = 3;
	private boolean visivel = false;

	public BarraEstado() {
	}

	public String getNome() {
		return barraEstadoPOJO.getNome();
	}

	public void setBarraEstadoPOJO(BarraLogEstado barraEstadoPOJO) {
		this.barraEstadoPOJO = barraEstadoPOJO;
	}

	public float getDeslocamentoX() {
		return deslocamentoX;
	}

	public void setDeslocamentoX(float deslocamentoX) {
		this.deslocamentoX = deslocamentoX;
	}

	public float getDeslocamentoy() {
		return deslocamentoy;
	}

	public void setDeslocamentoy(float deslocamentoy) {
		this.deslocamentoy = deslocamentoy;
	}

	@Override
	public void desenhar(Graphics g) {
		if (visivel) {
			int perc = barraEstadoPOJO.getAtual() * 100
					/ barraEstadoPOJO.getMaximo();

			int perd = (contorno.getWidth() * perc / 100) - contorno.getWidth();
			int dy = (contorno.getHeight() / 2) - (unidade.getHeight() / 2);

			contorno.draw(x + deslocamentoX, y + deslocamentoy);
			g.fillRect(x + deslocamentoX + borda, y + deslocamentoy + dy,
					contorno.getWidth() + perd - (2 * borda), unidade
							.getHeight(), unidade, 0, 0);
			Color old = g.getColor(); 
			g.setColor(Color.black);
			g.drawString(barraEstadoPOJO.getAtual()+"/"+barraEstadoPOJO.getMaximo(), x + (deslocamentoX+(deslocamentoX/2)), (y + deslocamentoy)-1);
			g.setColor(old);
			
		}
	}

	@Override
	public void excluir() {
	}

	@Override
	public boolean excluido() {
		return false;
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

	public void atualizar() {
	}

	@Override
	public float getAltura() {
		return 0;
	}

	@Override
	public float getComprimento() {
		return 0;
	}

	public Image getContorno() {
		return contorno;
	}

	public void setContorno(Image contorno) {
		this.contorno = contorno;
	}

	public Image getUnidade() {
		return unidade;
	}

	public void setUnidade(Image unidade) {
		this.unidade = unidade;
	}

	public void setBorda(float borda) {
		this.borda = borda;
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
