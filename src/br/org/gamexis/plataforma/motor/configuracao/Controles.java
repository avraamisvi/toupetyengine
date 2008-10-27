package br.org.gamexis.plataforma.motor.configuracao;

import groovy.sql.InOutParameter;

import java.util.HashMap;

import org.newdawn.slick.Input;

/**
 * Configuração controles.
 * @author abraao
 *
 */
public class Controles {
	
	public static int CIMA = 0;
	public static int BAIXO = 1;
	public static int ESQUERDA = 2;
	public static int DIREITA = 3;
	
	public static int T_A = 4;
	public static int T_S = 5;
	public static int T_D = 6;
	public static int T_F = 7;
	public static int T_G = 8;
	public static int T_Z = 9;
	public static int T_X = 12;
	public static int T_C = 13;
	
	public static int T_CTRL = 10;
	public static int T_P = 11;	
	
	HashMap<Integer, Integer> teclado;
	HashMap<Integer, Integer> joystick;
	
	int tecladoSelecionado = 1;
	
	public Controles() {
		teclado = new HashMap<Integer, Integer>();
		joystick = new HashMap<Integer, Integer>();		
		
		padrao();
	}
	
	public void setComandoTeclado(int comando, int correspondente) {
		teclado.put(comando, correspondente);
	}
	
	public void setComandoJoystick(int comando, int correspondente) {
		teclado.put(comando, correspondente);
	}
	
	public void selecinarTeclado() {
		tecladoSelecionado = 1;
	}
	
	public void selecinarJoystick() {
		tecladoSelecionado = 0;
	}
	
	public int traduzir(int comando) {		
		if(tecladoSelecionado == 1) {
			Integer i = teclado.get(comando);			
			return  i != null? i : 0;
		} 
		
		return joystick.get(comando);
	}	
	
	public boolean isTecladoSelecionado() {
		return tecladoSelecionado == 1;
	}
	
	public void padrao() {
		teclado.put(CIMA, Input.KEY_UP);
		teclado.put(BAIXO, Input.KEY_DOWN);
		teclado.put(ESQUERDA, Input.KEY_LEFT);
		teclado.put(DIREITA, Input.KEY_RIGHT);
		
		teclado.put(T_A, Input.KEY_A);
		teclado.put(T_CTRL, Input.KEY_LCONTROL);
		teclado.put(T_D, Input.KEY_D);
		teclado.put(T_F, Input.KEY_F);
		teclado.put(T_G, Input.KEY_G);
		teclado.put(T_S, Input.KEY_S);
		teclado.put(T_X, Input.KEY_X);
		teclado.put(T_C, Input.KEY_C);
		
		teclado.put(T_P, Input.KEY_SPACE);
		teclado.put(T_Z, Input.KEY_Z);
	}
}
