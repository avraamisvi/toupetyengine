package br.org.gamexis.plataforma.motor;

import org.newdawn.slick.Input;

import br.org.gamexis.plataforma.motor.configuracao.Controles;

public class EntradaPerifericos {
	private static EntradaPerifericos entrada = new EntradaPerifericos();
	private Controles controle;
	private Input input;
	private static int CONTROLE_PADRAO = 0;	
	
	public static EntradaPerifericos getInstancia() {
		return entrada;
	}
	
	public static void setInput(Input input) {
		entrada.input = input;
	}

	public static void setControle(Controles controle) {
		entrada.controle = controle;
	}
	
	public Controles getControle() {
		return controle;
	}
	
	public static boolean foiPressionado(int comando) {
		int valor = entrada.controle.traduzir(comando);
		
		if(entrada.controle.isTecladoSelecionado()) {					
			return entrada.input.isKeyPressed(valor);
		}						
		
		return entrada.input.isButtonPressed(valor, CONTROLE_PADRAO);
	}

	public static boolean estaPressionado(int comando) {
		int valor = entrada.controle.traduzir(comando);		
		
		if(entrada.controle.isTecladoSelecionado()) {
			boolean ret = entrada.input.isKeyDown(valor);				
			return ret;
		}						
		
		return entrada.input.isButtonPressed(valor, CONTROLE_PADRAO);
	}
	
	public static boolean cimaFoiPressionado() {
		
		if(entrada.controle.isTecladoSelecionado()) {
			int valor = entrada.controle.traduzir(Controles.CIMA);			
			return entrada.input.isKeyPressed(valor);
		}
		
		return entrada.input.isControllerUp(CONTROLE_PADRAO);
	}
	
	public static boolean cimaEstaPressionado() {
		
		if(entrada.controle.isTecladoSelecionado()) {
			int valor = entrada.controle.traduzir(Controles.CIMA);			
			return entrada.input.isKeyDown(valor);
		}
		
		return entrada.input.isControllerUp(CONTROLE_PADRAO);
	}

	public static boolean baixoFoiPressionado() {
		
		if(entrada.controle.isTecladoSelecionado()) {
			int valor = entrada.controle.traduzir(Controles.CIMA);			
			return entrada.input.isKeyPressed(valor);
		}
		
		return entrada.input.isControllerUp(CONTROLE_PADRAO);
	}
	
	public static boolean baixoEstaPressionado() {
		if(entrada.controle.isTecladoSelecionado()) {
			int valor = entrada.controle.traduzir(Controles.BAIXO);			
			return entrada.input.isKeyDown(valor);
		}
		
		return entrada.input.isControllerDown(CONTROLE_PADRAO);
	}
	
	public static boolean direitaFoiPressionado() {
		
		if(entrada.controle.isTecladoSelecionado()) {
			int valor = entrada.controle.traduzir(Controles.DIREITA);			
			return entrada.input.isKeyPressed(valor);
		}
		
		return entrada.input.isControllerUp(CONTROLE_PADRAO);
	}
	
	public static boolean direitaEstaPressionado() {
		
		if(entrada.controle.isTecladoSelecionado()) {
			int valor = entrada.controle.traduzir(Controles.DIREITA);			
			return entrada.input.isKeyDown(valor);
		}
		
		return entrada.input.isControllerDown(CONTROLE_PADRAO);
	}
	
	public static boolean esquerdaFoiPressionado() {
		
		if(entrada.controle.isTecladoSelecionado()) {
			int valor = entrada.controle.traduzir(Controles.ESQUERDA);			
			return entrada.input.isKeyPressed(valor);
		}
		
		return entrada.input.isControllerUp(CONTROLE_PADRAO);
	}
	
	public static boolean esquerdaEstaPressionado() {
		
		if(entrada.controle.isTecladoSelecionado()) {
			int valor = entrada.controle.traduzir(Controles.ESQUERDA);			
			return entrada.input.isKeyDown(valor);
		}
		
		return entrada.input.isControllerDown(CONTROLE_PADRAO);
	}		
}
