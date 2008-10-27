package br.org.gamexis.plataforma.cena.componentes;

import java.util.Collection;
import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import br.org.gamexis.plataforma.cena.Desenho;
import br.org.gamexis.plataforma.entidade.logestado.LogEstado;

/**
 * Representa um painel de estado visivel numa cena.
 * 
 * @author abraao
 * 
 */
public class PainelEstado implements Desenho {
	
	private Color efeitoCor = Color.white;
	private float x, y;
	private float aMx, aMy;
	private ArmaMostrador armaMostrador;
	private HashMap<String, BarraEstado> barrasEstado = new HashMap<String, BarraEstado>();
	private boolean visivel = true;

	private LogEstado logEstado = new LogEstado();

	public void setArma(String arma) {
		logEstado.setArma(arma);
		atualizar();
	}

	public void setArmaMostrador(ArmaMostrador armaMostrador) {
		this.armaMostrador = armaMostrador;
	}

	public void adicionarBarraEstado(BarraEstado barraEstado) {
		barrasEstado.put(barraEstado.getNome(), barraEstado);
	}

	@Override
	public void desenhar(Graphics g) {
		if (visivel) {
			armaMostrador.desenhar(g);

			Collection<BarraEstado> lstBarraEstado = barrasEstado.values();
			for (BarraEstado barraEstado : lstBarraEstado) {
				barraEstado.setX(x);
				barraEstado.setY(y);

				barraEstado.desenhar(g);
			}
		}
	}

	public void atualizar() {
		armaMostrador.setArma(logEstado.getArma());
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
		if (armaMostrador != null)
			armaMostrador.setX(x + aMx);
	}

	@Override
	public void setY(float y) {
		this.y = y;
		if (armaMostrador != null)
			armaMostrador.setY(y + aMy);
	}

	@Override
	public float getAltura() {
		return 0;
	}

	@Override
	public float getComprimento() {
		return 0;
	}

	public LogEstado getLogEstado() {
		return logEstado;
	}

	public void setLogEstado(LogEstado logEstado) {
		this.logEstado = logEstado;
	}

	@Override
	public boolean estaVisivel() {
		return visivel;
	}

	@Override
	public void setVisivel(boolean visivel) {
		this.visivel = visivel;
	}
	
	public ArmaMostrador getArmaMostrador() {
		return armaMostrador;
	}
	
	public BarraEstado getBarraEstado(String nome) {
		return barrasEstado.get(nome);
	}
	
	public void setEfeitoCor(Color efeitoCor) {
		this.efeitoCor = efeitoCor;
	}
	
	public Color getEfeitoCor() {
		return efeitoCor;
	}

	@Override
	public String getNome() {
		// TODO Auto-generated method stub
		return null;
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
