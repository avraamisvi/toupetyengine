package br.org.gamexis.plataforma.cena.proxy;

import java.io.IOException;
import java.util.HashMap;

import net.phys2d.raw.Body;

import org.newdawn.slick.SlickException;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.cena.Animado;
import br.org.gamexis.plataforma.cena.Ator;
import br.org.gamexis.plataforma.motor.RecursosFactory;

public class AtorProxy extends Ator {
	
	private HashMap<String, PseudoAnimacao> pseudoAnimacoes = new HashMap<String, PseudoAnimacao>();
	
	public AtorProxy(Body corpo) {
		super(corpo);
	}
	
	class PseudoAnimacao {
		public PseudoAnimacao(String base, int quadros, int comprimento, int altura, int linha, int coluna,
				float deslocx, float deslocy, float ex, float ey, String nome, String emissor,
				boolean repetir, boolean mista, boolean multi,
				int tempo, int velocidade) {
			super();
			this.altura = altura;
			this.base = base;
			this.comprimento = comprimento;
			this.linha = linha;
			this.quadros = quadros;
			this.coluna = coluna;
			this.deslocx = deslocx;
			this.deslocy = deslocy;
			this.ex = ex;
			this.ey = ey;			
			this.nome = nome;
			this.repetir = repetir;
			this.tempo = tempo;
			this.emissor = emissor;
			this.mista = mista;
			this.multi = multi;
			this.velocidade = velocidade;
		}
		
		public int velocidade;
		public String base;
		public int comprimento;
		public int quadros;
		public int altura;
		public int linha; 
		public int coluna;		
		public float deslocx;
		public float deslocy;
		public float ex;
		public float ey;		
		public String nome;
		public String emissor;
		public int tempo;
		public boolean repetir;
		public boolean mista;
		public boolean multi;
		public boolean carregado;
	}
	
	public void adicionarPseudoAnimacao(String base, int quadros, int comprimento, int altura, int linha, int coluna, 
			float deslocx, float deslocy, float ex, float ey, String nome, String emissor, int tempo,
			boolean repetir, boolean mista, boolean multi, int velocidade) {
		pseudoAnimacoes.put(nome, new PseudoAnimacao(base, quadros,  comprimento, altura, linha, coluna,
				deslocx, deslocy, ex, ey, nome, emissor, repetir, mista, multi, tempo, velocidade));
	}

	
	public boolean configureAnimacao(String animacao, boolean verificar) {
		
		try {
			Animado anim = carregarAnimacao(animacao);
			if (anim != null)
				adicioneAnimacao(animacao, anim);
		} catch (Throwable e) {
			Motor.obterInstancia().tratarExcecao(e);
		}
		return super.configureAnimacao(animacao, verificar);
	}

	public Animado carregarAnimacao(String nome) throws IOException, SlickException {
		PseudoAnimacao pseudoAnim = pseudoAnimacoes.get(nome);
		Animado anim = null;
		
		if(!pseudoAnim.carregado) {
			if(!pseudoAnim.mista && pseudoAnim.emissor != null) {//emissor
				anim = RecursosFactory.getInstancia().getEmissorParticulas(pseudoAnim.emissor, pseudoAnim.tempo);
			} else if(pseudoAnim.mista) {//mista
					
				if(!pseudoAnim.multi) {
					anim = RecursosFactory.getInstancia().getAnimacaoMista(pseudoAnim.nome, pseudoAnim.base, pseudoAnim.emissor, 
						pseudoAnim.comprimento, pseudoAnim.altura, pseudoAnim.linha, pseudoAnim.coluna, pseudoAnim.quadros, 
						pseudoAnim.repetir, pseudoAnim.velocidade, pseudoAnim.deslocx, pseudoAnim.deslocy,
						pseudoAnim.ex, pseudoAnim.ey);
				} else {
					anim = RecursosFactory.getInstancia().getAnimacaoMistaMultiImagem(pseudoAnim.nome, pseudoAnim.base, pseudoAnim.emissor, 
							pseudoAnim.comprimento, pseudoAnim.altura, pseudoAnim.linha, pseudoAnim.coluna, pseudoAnim.quadros, 
							pseudoAnim.repetir, pseudoAnim.velocidade, pseudoAnim.deslocx, pseudoAnim.deslocy,
							pseudoAnim.ex, pseudoAnim.ey);
				}
			} else {//anim
				if(!pseudoAnim.multi) {
					anim = RecursosFactory.getInstancia().getAnimacao(pseudoAnim.base, pseudoAnim.comprimento, pseudoAnim.altura, pseudoAnim.linha, 
							pseudoAnim.coluna, pseudoAnim.quadros, pseudoAnim.repetir, pseudoAnim.tempo);
				} else {
					anim = RecursosFactory.getInstancia().getAnimacaoMultImagem(pseudoAnim.base, pseudoAnim.comprimento, pseudoAnim.altura, pseudoAnim.linha, 
							pseudoAnim.coluna, pseudoAnim.quadros, pseudoAnim.repetir, pseudoAnim.tempo);
				}				
			}
	
			pseudoAnim.carregado = true;
		}
		
		return anim;
	}
}
