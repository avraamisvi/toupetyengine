package br.org.gamexis.plataforma.entidade;

import br.org.gamexis.plataforma.cena.Ator;
import br.org.gamexis.plataforma.cena.AtorFaceOrientacao;
import br.org.gamexis.plataforma.cena.Cenario;
import br.org.gamexis.plataforma.cena.Colisivel;
import br.org.gamexis.plataforma.cena.Desenho;
import br.org.gamexis.plataforma.cena.Efeito;
import br.org.gamexis.plataforma.cena.NivelCena;
import br.org.gamexis.plataforma.eventos.Evento;
import br.org.gamexis.plataforma.eventos.EventoColisao;
import br.org.gamexis.plataforma.exception.GXException;
import br.org.gamexis.plataforma.motor.RecursosFactory;

/**
 * Representa um projetil.
 * @author abraao
 *
 */
public class Projetil extends EntidadeAnimada {
	
	private float velocidade;
	private EntidadeArma arma;
	private float distanciaMaxima = 300;
	private float distanciaInicial = 0;
	private boolean primeiraAtualizacao = true; 
	private Efeito explosao;
	private TipoProjetil tipoProjetil = TipoProjetil.indiscriminado;
	private int poder = 0;
	
	public Projetil() {
	}
	
	public Projetil(Projetil copia) {
		try {
			setNome(copia.getNome());
			velocidade = copia.velocidade;
			arma = copia.arma;
			distanciaMaxima = copia.distanciaMaxima; 
			explosao = copia.explosao.clone();
			tipoProjetil = copia.tipoProjetil;
			poder = copia.poder;
			setAtor((Ator)((Ator)copia.getAtor()).clone());
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}
	
	public void setDistanciaMaxima(float distanciaMaxima) {
		this.distanciaMaxima = distanciaMaxima;
	}
	
	public float getDistanciaMaxima() {
		return distanciaMaxima;
	}
	
	@Override
	public void setAtor(Colisivel ator) {	
		((Ator)ator).setEntidade(this);
		super.setAtor(ator);
	}
	
	@Override
	public TipoEntidade getTipo() {
		return TipoEntidade.projetil;
	}
	
	public void setVelocidade(float velocidade) {
		this.velocidade = velocidade;
	}
	
	public float getVelocidade() {
		return velocidade;
	}
	
	public void setArma(EntidadeArma arma) {		
		this.arma = arma;
	}
	
	public Efeito getExplosao() {
		return explosao;
	}
	
	public void setExplosao(Efeito explosao) {
		this.explosao = explosao;
	}
	
	private void inicialHorizontal() {
		if(primeiraAtualizacao) {
			distanciaInicial = ((Ator)getAtor()).getX();
			primeiraAtualizacao = false;
		}
	}
	
	private void inicialVertical() {
		if(primeiraAtualizacao) {
			distanciaInicial = ((Ator)getAtor()).getY();
			primeiraAtualizacao = false;
		}
	}

	@Override
	public void atualizar(Evento evento) throws GXException {
		super.atualizar(evento);		
		
		try {
			if (evento instanceof EventoColisao) {
				Efeito an = explosao;
				Ator at = (Ator)getAtor();								
				EventoColisao colisaoEv = (EventoColisao)evento;
				
				switch(getFaceOrientacao()) {
				case direita:					
					an.setX(at.getX());
					an.setY(at.getY() - (an.getAltura()/2));
					break;
				case direitaAbaixo:
					an.setX(at.getX());
					an.setY(at.getY() + (an.getAltura()));
					break;
				case direitaAcima:
					an.setX(at.getX());
					an.setY(at.getY() - (an.getAltura()));
					break;
				case esquerda:					
					an.setX(at.getX() - (an.getComprimento()/2));
					an.setY(at.getY() - (an.getAltura()/2));
					break;
				case esquerdaAbaixo:
					an.setX(at.getX() + (an.getComprimento()/2));
					an.setY(at.getY() + (an.getAltura()/2));
					break;
				case esquerdaAcima:
					an.setX(at.getX() + (an.getComprimento()/2));
					an.setY(at.getY() + (an.getAltura()/2));
					break;
				}
				
				apagar();
				
				((Cenario)evento.getCena()).adicionarAnimacao(an, NivelCena.frente);
				Entidade fonte = colisaoEv.getFonte().getAtor().getEntidade();
//				if( fonte instanceof EntidadeInimigo) {
//					((EntidadeInimigo)fonte).sofrerDano(null, getPoder());
//				}
				
			} else {
				if(!primeiraAtualizacao) {
					switch(getFaceOrientacao()) {
					case direita:
					case esquerda:
						if(Math.abs(((Ator)getAtor()).getX() - distanciaInicial) > distanciaMaxima) {
							apagar();
						}
						break;
					case direitaAbaixo:
					case direitaAcima:
					case esquerdaAbaixo:
					case esquerdaAcima:
						if (Math.abs(((Ator)getAtor()).getY() - distanciaInicial) > distanciaMaxima) {
							apagar();
						}
						break;
					}				
				} else {
					switch(getFaceOrientacao()) {
					case direita:
					case esquerda:
						inicialHorizontal();
						break;
					case direitaAbaixo:
					case direitaAcima:
					case esquerdaAbaixo:
					case esquerdaAcima:
						inicialVertical();
						break;
					}					
				}
			}
			
		} catch (Throwable cause) {
			tratarExcecao(cause);
		}
	}
	
	@Override
	public void setFaceOrientacao(AtorFaceOrientacao face) {
		super.setFaceOrientacao(face);
		
		switch(getFaceOrientacao()) {
		case direita:
			((Ator)getAtor()).configureAnimacao("DISPARANDO_DIREITA");
			break;
		case direitaAbaixo:
			((Ator)getAtor()).configureAnimacao("DISPARANDO_ABAIXO");
			break;
		case direitaAcima:
			((Ator)getAtor()).configureAnimacao("DISPARANDO_ACIMA");
			break;
		case esquerda:
			((Ator)getAtor()).configureAnimacao("DISPARANDO_ESQUERDA");
			break;
		case esquerdaAbaixo:
			((Ator)getAtor()).configureAnimacao("DISPARANDO_ABAIXO");
			break;
		case esquerdaAcima:
			((Ator)getAtor()).configureAnimacao("DISPARANDO_ACIMA");
			break;
		}
	}
	
	public TipoProjetil getTipoProjetil() {
		return tipoProjetil;
	}
	
	@Override
	public void atualizarFisica(int delta) {
	}
	
	private void apagar() {
		if(arma != null)//PARA SUPORTAR INIMIGOS SEM ARMAS
			arma.removaProjetil(this);
		
		((Desenho)getAtor()).excluir();	
	}

	public int getPoder() {
		return poder;
	}

	public void setPoder(int poder) {
		this.poder = poder;
	}
	
	//CLONAGEM PARCIAL
	public Projetil clone() {
		Projetil p;
		p = (Projetil)RecursosFactory.getInstancia().getEntidade(this.getNome());
		p.setPoder(this.getPoder());
		p.setArma(arma);
		
//		p = new Projetil(this);
		return p;
	}
	
	@Override
	public void dispose() {
		super.dispose();
		arma = null;
		explosao = null;
	}
}
