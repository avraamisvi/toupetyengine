package br.org.gamexis.plataforma.debug;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.entidade.EntidadeArma;

/**
 * Classe utilitaria para acessar com facilidade as funcionalidades diversas.
 * 
 * @author abraao
 * 
 */
public class DebugMestre {

	public void setDeslocDireita(float deslocamentoBrilhoX,
			float deslocamentoBrilhoY, float deslocamentoTiroX,
			float deslocamentoTiroY, float deslocamentoCarregamentoX,
			float deslocamentoCarregamentoY) {
		EntidadeArma arm = Motor.obterInstancia().getJogavelCarregado()
				.getArma();
		arm.configureOrientacaoDireita(deslocamentoBrilhoX,
				deslocamentoBrilhoY, deslocamentoTiroX, deslocamentoTiroY,
				deslocamentoCarregamentoX, deslocamentoCarregamentoY);
	}

	public void setDeslocDireitaAcima(float deslocamentoBrilhoX,
			float deslocamentoBrilhoY, float deslocamentoTiroX,
			float deslocamentoTiroY, float deslocamentoCarregamentoX,
			float deslocamentoCarregamentoY) {
		EntidadeArma arm = Motor.obterInstancia().getJogavelCarregado()
				.getArma();

		arm.configureOrientacaoDireitaAcima(deslocamentoBrilhoX,
				deslocamentoBrilhoY, deslocamentoTiroX, deslocamentoTiroY,
				deslocamentoCarregamentoX, deslocamentoCarregamentoY);
	}

	public void setDeslocDireitaAbaixo(float deslocamentoBrilhoX,
			float deslocamentoBrilhoY, float deslocamentoTiroX,
			float deslocamentoTiroY, float deslocamentoCarregamentoX,
			float deslocamentoCarregamentoY) {
		EntidadeArma arm = Motor.obterInstancia().getJogavelCarregado()
				.getArma();
		arm.configureOrientacaoDireitaAbaixo(deslocamentoBrilhoX,
				deslocamentoBrilhoY, deslocamentoTiroX, deslocamentoTiroY,
				deslocamentoCarregamentoX, deslocamentoCarregamentoY);
	}

	public void setDeslocEsquerda(float deslocamentoBrilhoX,
			float deslocamentoBrilhoY, float deslocamentoTiroX,
			float deslocamentoTiroY, float deslocamentoCarregamentoX,
			float deslocamentoCarregamentoY) {
		EntidadeArma arm = Motor.obterInstancia().getJogavelCarregado()
				.getArma();
		arm.configureOrientacaoEsquerda(deslocamentoBrilhoX,
				deslocamentoBrilhoY, deslocamentoTiroX, deslocamentoTiroY,
				deslocamentoCarregamentoX, deslocamentoCarregamentoY);
	}

	public void setDeslocEsquerdaAcima(float deslocamentoBrilhoX,
			float deslocamentoBrilhoY, float deslocamentoTiroX,
			float deslocamentoTiroY, float deslocamentoCarregamentoX,
			float deslocamentoCarregamentoY) {
		EntidadeArma arm = Motor.obterInstancia().getJogavelCarregado()
				.getArma();
		arm.configureOrientacaoEsquerdaAcima(deslocamentoBrilhoX,
				deslocamentoBrilhoY, deslocamentoTiroX, deslocamentoTiroY,
				deslocamentoCarregamentoX, deslocamentoCarregamentoY);
	}

	public void setDeslocEsquerdaAbaixo(float deslocamentoBrilhoX,
			float deslocamentoBrilhoY, float deslocamentoTiroX,
			float deslocamentoTiroY, float deslocamentoCarregamentoX,
			float deslocamentoCarregamentoY) {
		EntidadeArma arm = Motor.obterInstancia().getJogavelCarregado()
				.getArma();
		arm.configureOrientacaoEsquerdaAbaixo(deslocamentoBrilhoX,
				deslocamentoBrilhoY, deslocamentoTiroX, deslocamentoTiroY,
				deslocamentoCarregamentoX, deslocamentoCarregamentoY);
	}
	
	public void modoAcumulador() {
		EntidadeArma arm = Motor.obterInstancia().getJogavelCarregado()
		.getArma();		
		arm.configureModoAcumulador();
	}
	
	public void modoNormal() {
		EntidadeArma arm = Motor.obterInstancia().getJogavelCarregado()
		.getArma();		
		arm.configureModoNormal();
	}
	
	public void modoMetralhadora() {
		EntidadeArma arm = Motor.obterInstancia().getJogavelCarregado()
		.getArma();
		arm.configureModoMetralhadora();
	}
	
	public void habilitarEscudo(boolean flag) {
		if(flag) {
			Motor.obterInstancia().getJogavelCarregado().getEscudo().destravar();
		} else {
			Motor.obterInstancia().getJogavelCarregado().getEscudo().travar();
		}
	}
}
