package br.org.gamexis.plataforma.cena.evento;

import br.org.gamexis.plataforma.cena.Colisivel;

public class EventoColisaoCorpoColisao {
	String nomeRetanguloColisao;
	Colisivel fonte;
	
	public EventoColisaoCorpoColisao(String nomeRetanguloColisao, Colisivel fonte) {
		this.nomeRetanguloColisao = nomeRetanguloColisao;
		this.fonte = fonte;
	}

	public Colisivel getFonte() {
		return fonte;
	}
	
	public String getNomeRetanguloColisao() {
		return nomeRetanguloColisao;
	}
}
