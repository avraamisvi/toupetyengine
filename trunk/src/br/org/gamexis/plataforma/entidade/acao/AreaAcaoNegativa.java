package br.org.gamexis.plataforma.entidade.acao;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.cena.Colisivel;
import br.org.gamexis.plataforma.cena.Desenho;
import br.org.gamexis.plataforma.cena.Efeito;
import br.org.gamexis.plataforma.cena.efeitos.EfeitoAlfaCor;
import br.org.gamexis.plataforma.entidade.AreaAcao;
import br.org.gamexis.plataforma.entidade.EntidadeJogavel;
import br.org.gamexis.plataforma.entidade.TipoEntidade;

/**
 * Causa dano quando ocorre intercessão
 * 
 * @author abraao
 * 
 */
public class AreaAcaoNegativa extends AreaAcao {

	private Efeito efeito = new EfeitoAlfaCor();
	private int dano = 100;
	private int tempoInvencibilidade = 1000;
	private int velocidadeRepulsaoX = 30;
	private int velocidadeRepulsaoY = 30;

	public AreaAcaoNegativa() {
		efeito = new EfeitoAlfaCor();
		Color corF = Color.red;
		corF.a = .3f;
		((EfeitoAlfaCor)efeito).setCorPrimaria(corF);
		((EfeitoAlfaCor)efeito).setCorSecundaria(Color.white);
		((EfeitoAlfaCor)efeito).setDuracao(1000);
		((EfeitoAlfaCor)efeito).setFrequencia(3);
		((EfeitoAlfaCor)efeito).setDuracao(1000);
	}
	
	@Override
	public void executar(Colisivel colisivel) {

		if (colisivel.getEntidade().getTipo().equals(TipoEntidade.jogavel)) {
			EntidadeJogavel jogavel = (EntidadeJogavel) colisivel
					.getEntidade();

			if (!jogavel.isInvencivel() && !jogavel.escudoHabilitado()) {

				if (efeito != null) {
					Efeito efeitoTemp = null;
					efeitoTemp = efeito.clone();
					((EfeitoAlfaCor)efeitoTemp).setDono((Desenho)jogavel.getAtor());
					
					jogavel.sofrerDano(getDano(), efeitoTemp,
							getTempoInvencibilidade(),
							getVelocidadeRepulsaoX(), getVelocidadeRepulsaoY());
					
				} else {
					jogavel.sofrerDano(getDano(), getTempoInvencibilidade(),
							getVelocidadeRepulsaoX(), getVelocidadeRepulsaoY());
				}
				
				Motor.obterInstancia().exibirDanoPadrao(jogavel, getDano());
			}
		}
	}

	public void setTempoInvencibilidade(int iteracoes) {
		this.tempoInvencibilidade = iteracoes;
	}

	public int getTempoInvencibilidade() {
		return tempoInvencibilidade;
	}

	public int getDano() {
		return dano;
	}

	public void setDano(int dano) {
		this.dano = dano;
	}

	public void setEfeito(Efeito efeito) {
		this.efeito = efeito;
	}
	
	public Efeito getEfeito() {
		return efeito;
	}
	
	public int getVelocidadeRepulsaoX() {
		return velocidadeRepulsaoX;
	}

	public void setVelocidadeRepulsaoX(int velocidadeRepulsaoX) {
		this.velocidadeRepulsaoX = Math.abs(velocidadeRepulsaoX);
	}

	public int getVelocidadeRepulsaoY() {
		return velocidadeRepulsaoY;
	}

	public void setVelocidadeRepulsaoY(int velocidadeRepulsaoY) {
		this.velocidadeRepulsaoY = Math.abs(velocidadeRepulsaoY);
	}

	@Override
	public Color getEfeitoCor() {
		return null;
	}

	@Override
	public void setEfeitoCor(Color cor) {
	}

	@Override
	public String getNome() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void desenhar(Graphics g, float fatorX, float fatorY) {
		desenhar(g);
	}

}
