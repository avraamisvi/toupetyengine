package br.org.gamexis.plataforma.cena;

import org.lwjgl.Sys;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ParticleEmitter;
import org.newdawn.slick.particles.ParticleSystem;

import br.org.gamexis.plataforma.motor.RecursosFactory;
import br.org.gamexis.plataforma.motor.particulas.ToupetyEmitter;

/**
 * Representa uma animação para a emissão de partículas.
 * @author abraao
 *
 */
public class EmissorParticulas implements Animado {
	
	private Color efeitoCor = Color.white;
	private float x;
	private float y;
	private float dx;
	private float dy;
	private ParticleSystem system;
	private ParticleSystem copia;//o sistema n reseta ai faço cópia do estado original pra substituir
	private boolean excluido;
	private long tempoMaximo;
	private long tempoInicial = 0;
	private String nome;
	private long comecarEm = 0;
	private boolean disposed = false;
	private Image base;
	
	public EmissorParticulas(ParticleSystem sistema, long tempoMaximo) throws SlickException {
		this(sistema);
		//tempoInicial = Sys.getTime();
		this.tempoMaximo = tempoMaximo;
	}			

	public EmissorParticulas(ParticleSystem sistema) throws SlickException {
		system = sistema;
		copia = system.duplicate();
	}
	
	public EmissorParticulas(EmissorParticulas original) {
		try {			
			system = original.copia.duplicate();
			copia = original.copia.duplicate();
			
			//tempoInicial = Sys.getTime();
			this.tempoMaximo = original.tempoMaximo;
		} catch(Exception e) {			
		}
	}		
	
	public EmissorParticulas(Image img) throws SlickException {
		this(new ParticleSystem(img));
		base = img;
	}	
	
	private String identificacao;
	
	@Override
	public String getIdentificacao() {
		return identificacao;
	}
	
	public void setIdentificacao(String identificacao) {
		this.identificacao = identificacao;
	}
	
	public void adicionarEmissor(ParticleEmitter emissor) {
		system.addEmitter(emissor);
		copia.addEmitter(emissor);
	}
	
	@Override
	public void atualizar(int delta) {
		if(tempoInicial == 0) {
			tempoInicial = Sys.getTime();
			
			if(comecarEm > 0) {
				for(long i = 0; i < comecarEm; i=i+10) {
					system.update(delta+5);
				}
			}
			
		}
		
		system.setPosition(x-dx, y-dy);
		system.update(delta);
	}

	@Override
	public void reiniciar() {
		if(tempoMaximo > 0)	{
			//tempoInicial = Sys.getTime();
			tempoInicial = 0;
		}
		try {
			
			for(int i = 0; i < system.getEmitterCount(); i++) {//CORRETO
				((ToupetyEmitter)system.getEmitter(i)).replay();
			}
			
		} catch(Exception e) {
			Sys.alert("Erro!", "Erro interno: tentativa de duplicar emissor particula");
		}
	}
	
	@Override
	public boolean ultimoQuadro() {
		if(tempoMaximo > 0 && tempoInicial > 0)	{
			long tempoAtual = Sys.getTime();
			if((tempoAtual-tempoInicial) > tempoMaximo)
					return true;
		}
		return false;
	}

	@Override
	public void desenhar(Graphics g) {
		system.setPosition(x-dx, y-dy);
		system.render();
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

	public float getDeslocamentoX() {
		return dx;
	}

	public void setDeslocamentoX(float dx) {
		this.dx = dx;
	}

	public float getDeslocamentoY() {
		return dy;
	}

	public void setDeslocamentoY(float dy) {
		this.dy = dy;
	}	
	
	public void setEfeitoCor(Color efeitoCor) {
		this.efeitoCor = efeitoCor;
	}
	
	public Color getEfeitoCor() {
		return efeitoCor;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}	
	
	@Override
	public String getNome() {
		return this.nome;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return new EmissorParticulas(this);
	}

	@Override
	public void dispose() {
		system.removeAllEmitters();
		copia.removeAllEmitters();
		
		if(base != null)
			RecursosFactory.getInstancia().libertarRecurso(base.getReference());
		
		disposed = true;
	}

	@Override
	public boolean isDisposed() {
		return disposed;
	}

	public long getComecarEm() {
		return comecarEm;
	}

	public void setComecarEm(long comecarEm) {
		this.comecarEm = comecarEm;
	}

	@Override
	public void desenhar(Graphics g, float fatorX, float fatorY) {
		// TODO emissores ainda n podem ser redimensionados
		desenhar(g);
	}
	
	
}
