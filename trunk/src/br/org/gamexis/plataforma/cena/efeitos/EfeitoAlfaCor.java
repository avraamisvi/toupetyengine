package br.org.gamexis.plataforma.cena.efeitos;

import org.lwjgl.Sys;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import br.org.gamexis.plataforma.cena.Desenho;
import br.org.gamexis.plataforma.cena.Efeito;

public class EfeitoAlfaCor extends Efeito {
	
	private long duracao = 0;
	private long tempoInicial;
	private int frequencia = 0;
	private int quantidade = 0;
	private Desenho dono;
	Color corPrimaria = Color.white;
	Color corSecundaria = Color.white;
	Color corAtual = Color.white;
	
	public EfeitoAlfaCor(EfeitoAlfaCor alfaCor) {		
		dono = alfaCor.dono;
		corPrimaria = alfaCor.corPrimaria;
		corSecundaria = alfaCor.corSecundaria;
		duracao = alfaCor.duracao;		
		tempoInicial = Sys.getTime();
		frequencia = alfaCor.frequencia;
	}
	
	public EfeitoAlfaCor() {
		tempoInicial = Sys.getTime();
	}
	
	public EfeitoAlfaCor(Desenho dono) {
		this.dono = dono;
		tempoInicial = Sys.getTime();
	}
	
	public void setDuracao(long duracao) {
		this.duracao = duracao;
	}
	
	public long getDuracao() {
		return duracao;
	}
	
	public void setCorAtual(Color cor) {
		this.corAtual = cor;
	}
	
	public Color getCorAtual() {
		return corAtual;
	}	

	@Override
	public void desenhar(Graphics g) {
		dono.setEfeitoCor(corAtual);		
	}
	
	@Override
	public void atualizar(int delta) {
				
		if(quantidade >= frequencia) {
			if(dono.getEfeitoCor().equals(corPrimaria))
				dono.setEfeitoCor(corSecundaria);
			else
				dono.setEfeitoCor(corPrimaria);
				
			
			quantidade = 0;			
		}
		
		if(Sys.getTime() - tempoInicial >= duracao) {					
			dono.setEfeitoCor(Color.white);			
			excluir();
		}
		
		quantidade++;
	}
	
	public Efeito clone() {
		return new EfeitoAlfaCor(this);
	}
	
	public void setFrequencia(int frequencia) {
		this.frequencia = frequencia;
	}
	
	public int getFrequencia() {
		return frequencia;
	}
	
	public void setDono(Desenho dono) {
		this.dono = dono;
	}
	
	public Desenho getDono() {
		return dono;
	}

	public Color getCorPrimaria() {
		return corPrimaria;
	}

	public void setCorPrimaria(Color corPrimaria) {
		this.corPrimaria = corPrimaria;
	}

	public Color getCorSecundaria() {
		return corSecundaria;
	}

	public void setCorSecundaria(Color corSecundaria) {
		this.corSecundaria = corSecundaria;
	}
	
	@Override
	public String getNome() {
		return "EfeitoAlfaCor";
	}
}
