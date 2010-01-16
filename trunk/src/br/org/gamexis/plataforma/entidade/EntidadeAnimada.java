package br.org.gamexis.plataforma.entidade;

import net.phys2d.math.Vector2f;
import br.org.gamexis.plataforma.cena.Ator;
import br.org.gamexis.plataforma.cena.AtorFaceOrientacao;

public abstract class EntidadeAnimada extends Entidade implements
		EntidadeOrientavel {

	private AtorFaceOrientacao face;
	/**
	 * Usado para impedir a mudança automatica de animação.
	 */
	private boolean trancarAnimacao = false;

	public void setFaceOrientacao(AtorFaceOrientacao face) {
		this.face = face;
	}

	public AtorFaceOrientacao getFaceOrientacao() {
		return face;
	}

	/**
	 * Configura uma animação por nome.
	 * 
	 * @param animNome
	 */
	public void configureAnimacaoPorNome(String animNome) {
		Ator ator = (Ator) getAtor();

		switch (getFaceOrientacao()) {
		case direita:
		case direitaAbaixo:
		case direitaAcima:
			ator.configureAnimacao(animNome + "_" + "DIREITA");
			break;
		case esquerda:
		case esquerdaAbaixo:
		case esquerdaAcima:
			ator.configureAnimacao(animNome + "_" + "ESQUERDA");
			break;
		}
	}

	/**
	 * Configura uma animação por nome.
	 * 
	 * @param animNome
	 */
	public void configureAnimacaoPorNome(String animNome, boolean verifica) {
		Ator ator = (Ator) getAtor();

		switch (getFaceOrientacao()) {
		case direita:
		case direitaAbaixo:
		case direitaAcima:
			ator.configureAnimacao(animNome + "_" + "DIREITA", verifica);
			break;
		case esquerda:
		case esquerdaAbaixo:
		case esquerdaAcima:
			ator.configureAnimacao(animNome + "_" + "ESQUERDA", verifica);
			break;
		}
	}

	public void atualizarFisica(int delta) {
		Ator ator = ((Ator) getAtor());

		if (trancarAnimacao)
			return;

		if (ator.isParado()) {

			if (isFaceDireita())
				ator.configureAnimacao("PARADO_DIREITA", true);
			else
				ator.configureAnimacao("PARADO_ESQUERDA", true);
		}

		if (ator.isQuicando()) {
			if (ator.getAnimacaoAtual().ultimoQuadro()) {
				ator.setParado(true);
				ator.zerarVelocidadeY();
				configureAnimacaoPorNome("PARADO");
			}
		}

		if (ator.isPulando()) {
			if (ator.getAnimacaoAtual().ultimoQuadro()) {
				ator.setNoar(true);
				ator.getCorpo().adjustVelocity(new Vector2f(0, -8f));
				if (isFaceDireita()) {
					ator.configureAnimacao("SUBINDO_DIREITA");
				} else {
					ator.configureAnimacao("SUBINDO_ESQUERDA");
				}
			}
		}

		// if (!ator.isVoando()) {
		if (ator.getCorpo().getVelocity().getY() > 2) {
			if (!ator.isCaindo()) {
				if (isFaceDireita()) {
					ator.configureAnimacao("CAINDO_DIREITA");
				} else {
					ator.configureAnimacao("CAINDO_ESQUERDA");
				}
				ator.setCaindo(true);
			}
		} else {

			if (ator.isCaindo()) {
				if (ator.estaNochao()) {
					ator.setQuicando(true);
					ator.zerarVelocidadeY();
					
					if (isFaceDireita()) {
						ator.configureAnimacao("QUICANDO_DIREITA");
					} else {
						ator.configureAnimacao("QUICANDO_ESQUERDA");
					}
				}
			}
		}
		// }
	}

	public boolean isTrancarAnimacao() {
		return trancarAnimacao;
	}

	public void setTrancarAnimacao(boolean trancarAnimacao) {
		this.trancarAnimacao = trancarAnimacao;
	}

	public boolean isFaceDireita() {
		return face == AtorFaceOrientacao.direita || isFaceDireitaAbaixo()
				|| isFaceDireitaAcima();
	}

	public boolean isFaceEsquerda() {
		return face == AtorFaceOrientacao.esquerda || isFaceEsquerdaAbaixo()
				|| isFaceDireitaAcima();
	}

	public boolean isFaceDireitaAbaixo() {
		return face == AtorFaceOrientacao.direitaAbaixo;
	}

	public boolean isFaceEsquerdaAbaixo() {
		return face == AtorFaceOrientacao.esquerdaAbaixo;
	}

	public boolean isFaceDireitaAcima() {
		return face == AtorFaceOrientacao.direitaAcima;
	}

	public boolean isFaceEsquerdaAcima() {
		return face == AtorFaceOrientacao.esquerdaAcima;
	}

	public void setFaceDireita() {
		face = AtorFaceOrientacao.direita;
	}

	public void setFaceEsquerda() {
		face = AtorFaceOrientacao.esquerda;
	}

	public void setFaceDireitaAbaixo() {
		face = AtorFaceOrientacao.direitaAbaixo;
	}

	public void setFaceEsquerdaAbaixo() {
		face = AtorFaceOrientacao.esquerdaAbaixo;
	}

	public void setFaceDireitaAcima() {
		face = AtorFaceOrientacao.direitaAcima;
	}

	public void setFaceEsquerdaAcima() {
		face = AtorFaceOrientacao.esquerdaAcima;
	}
}
