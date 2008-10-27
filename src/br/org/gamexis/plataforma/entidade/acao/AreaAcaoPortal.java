package br.org.gamexis.plataforma.entidade.acao;

import org.newdawn.slick.Color;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.cena.Ator;
import br.org.gamexis.plataforma.cena.Colisivel;
import br.org.gamexis.plataforma.entidade.AreaAcao;

public class AreaAcaoPortal extends AreaAcao {
	
	private float deslocY;
	private float deslocX;
	
	private boolean desX;
	private boolean desY;
	
	private float xFixo;
	private float yFixo;

	private float maxX;
	private float minX;
	
	private float maxY;
	private float minY;
	
	private String cena;
	
	private Color efeitoCor = Color.white;
	
	private boolean usada = false;
	@Override
	public void executar(Colisivel colisivel) {
		
		if(usada)
			return;
		
		if(Motor.obterInstancia().tipoJogavel(colisivel.getEntidade())) {
			if(deslocX != 0 || desX) {
				xFixo = ((Ator)Motor.obterInstancia().getJogavelCarregado().getAtor()).getX() + deslocX;
				
				if(xFixo > maxX)
					xFixo = maxX;
				else if(xFixo < minX)
					xFixo = minX;
			}
			
			if(deslocY != 0 || desY) {
				yFixo = ((Ator)Motor.obterInstancia().getJogavelCarregado().getAtor()).getY() + deslocY;
				
				if(yFixo > maxY)
					yFixo = maxY;
				else if(yFixo < minY)
					yFixo = minY;				
			}
			
			Motor.obterInstancia().putEntrada(xFixo, yFixo);
			Motor.obterInstancia().mudarCena(cena);
			usada = true;
		}
	}
	
	public void setEfeitoCor(Color efeitoCor) {
		this.efeitoCor = efeitoCor;
	}
	
	public Color getEfeitoCor() {
		return efeitoCor;
	}

	@Override
	public String getNome() {
		return null;
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

	public boolean isDesX() {
		return desX;
	}

	public void setDesX(boolean desX) {
		this.desX = desX;
	}

	public boolean isDesY() {
		return desY;
	}

	public void setDesY(boolean desY) {
		this.desY = desY;
	}

	public float getMaxX() {
		return maxX;
	}

	public void setMaxX(float maxX) {
		this.maxX = maxX;
	}

	public float getMinX() {
		return minX;
	}

	public void setMinX(float minX) {
		this.minX = minX;
	}

	public float getMaxY() {
		return maxY;
	}

	public void setMaxY(float maxY) {
		this.maxY = maxY;
	}

	public float getMinY() {
		return minY;
	}

	public void setMinY(float minY) {
		this.minY = minY;
	}

	public String getCena() {
		return cena;
	}

	public void setCena(String cena) {
		this.cena = cena;
	}

	public float getXFixo() {
		return xFixo;
	}

	public void setXFixo(float fixo) {
		xFixo = fixo;
	}

	public float getYFixo() {
		return yFixo;
	}

	public void setYFixo(float fixo) {
		yFixo = fixo;
	}

	
}
