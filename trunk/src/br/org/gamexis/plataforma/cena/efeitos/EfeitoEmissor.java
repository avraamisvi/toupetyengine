package br.org.gamexis.plataforma.cena.efeitos;

import org.newdawn.slick.Graphics;

import br.org.gamexis.plataforma.cena.Desenho;
import br.org.gamexis.plataforma.cena.Efeito;
import br.org.gamexis.plataforma.cena.EmissorParticulas;

public class EfeitoEmissor extends Efeito {
	
	private float x;	
	private float y;
	private float comprimento;
	private float altura;	
	
	private Desenho dono;
	private EmissorParticulas emissor;
	
	
	public EfeitoEmissor(Desenho dono, EmissorParticulas emissor) {
		this.dono = dono;
		this.emissor = emissor;
	}					
	
	public EfeitoEmissor(EfeitoEmissor fonte) {
		this.dono = fonte.dono;
		this.emissor = (EmissorParticulas) new EmissorParticulas(fonte.emissor);		
	}		
	
	@Override
	public void desenhar(Graphics g) {
		emissor.desenhar(g);
	}
	
	@Override
	public void atualizar(int delta) {
		if(ultimoQuadro()) {
			if(!naoExcluir()) {
				excluir();
			}
		}
		
		//super.atualizar(delta);
		
		if(dono != null) {
			setX(dono.getX());
			setY(dono.getY());
		}		
		
		emissor.atualizar(delta);
		emissor.setX(getX());
		emissor.setY(getY());
	}

	@Override
	public boolean ultimoQuadro() {	
		return emissor.ultimoQuadro();
	}
	
	public void setDono(Desenho dono) {
		this.dono = dono;
	}
	
	public Desenho getDono() {
		return dono;
	}
	
	public Efeito clone() {		
		return new EfeitoEmissor(this);
	}	
	
	@Override
	public void setX(float x) {
		this.x = x;
	}
	
	@Override
	public void setY(float y) {
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}	
	
	@Override
	public float getComprimento() {
		return comprimento;
	}
	
	@Override
	public float getAltura() {
		return altura;
	}
	
	@Override
	public void setDeslocX(float deslocX) {
		super.setDeslocX(deslocX);
		emissor.setDeslocamentoX(deslocX);
	}
	
	@Override
	public void setDeslocY(float deslocY) {
		super.setDeslocY(deslocY);
		emissor.setDeslocamentoY(deslocY);
	}
	
	@Override
	public String getNome() {
		return emissor.getNome();
	}
}
