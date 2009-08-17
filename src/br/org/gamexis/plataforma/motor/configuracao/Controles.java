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
	HashMap<Integer, Integer> fromTeclado;
	HashMap<Integer, Integer> joystick;
	HashMap<Integer, Integer> fromJoystick;
	
	int tecladoSelecionado = 1;
	
	public Controles() {
		teclado = new HashMap<Integer, Integer>();
		joystick = new HashMap<Integer, Integer>();		
		fromTeclado = new HashMap<Integer, Integer>();
		fromJoystick = new HashMap<Integer, Integer>();		
		
		padrao();
	}
	
	public void setComandoTeclado(int comando, int correspondente) {
		teclado.put(comando, correspondente);
	}
	
	public void setComandoJoystick(int comando, int correspondente) {
		teclado.put(comando, correspondente);
	}
	
	public void selecionarTeclado() {
		tecladoSelecionado = 1;
	}
	
	public void selecionarJoystick() {
		tecladoSelecionado = 0;
	}
	
	public int traduzir(int comando) {		
		if(tecladoSelecionado == 1) {
			Integer i = fromTeclado.get(comando);			
			return  i != null? i : -1;
		} 
		
		Integer i = fromJoystick.get(new Integer(comando));
		return i!= null? i : -1;
	}	
	
	public int traduzirParaChave(int comando) {		
		if(tecladoSelecionado == 1) {
			Integer i = teclado.get(comando);			
			return  i != null? i : -1;
		} 
		
		Integer i = joystick.get(new Integer(comando));
		return i!= null? i : -1;
	}		
	
	public boolean isTecladoSelecionado() {
		return tecladoSelecionado == 1;
	}
	
	public void padrao() {
		padraoTeclado();
		padraoJoystick();
	}
	
	private void padraoJoystick() {
		
		fromJoystick.put(1, T_A);
		fromJoystick.put(2, T_CTRL);
		fromJoystick.put(3, T_X);
		fromJoystick.put(4, T_S);
		fromJoystick.put(5, T_C);
		fromJoystick.put(6, T_D);
		fromJoystick.put(7, T_Z);
		fromJoystick.put(8, T_F);
		
		joystick.put(T_A, 1);
		joystick.put(T_CTRL, 2);
		joystick.put(T_X, 3);
		joystick.put(T_S, 4);
		joystick.put(T_C, 5);
		joystick.put(T_D, 6);
		joystick.put(T_Z, 7);
		joystick.put(T_F, 8);		
	}
	
	private void padraoTeclado() {
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
		
		
		fromTeclado.put(Input.KEY_UP, CIMA);
		fromTeclado.put(Input.KEY_DOWN, BAIXO);
		fromTeclado.put(Input.KEY_LEFT, ESQUERDA);
		fromTeclado.put(Input.KEY_RIGHT, DIREITA);
		
		fromTeclado.put(Input.KEY_A, T_A);
		fromTeclado.put(Input.KEY_LCONTROL, T_CTRL);
		fromTeclado.put(Input.KEY_D, T_D);
		fromTeclado.put(Input.KEY_F, T_F);
		fromTeclado.put(Input.KEY_G, T_G);
		fromTeclado.put(Input.KEY_S, T_S);
		fromTeclado.put(Input.KEY_X, T_X);
		fromTeclado.put(Input.KEY_C, T_C);
		
		fromTeclado.put(Input.KEY_SPACE, T_P);
		fromTeclado.put(Input.KEY_Z, T_Z);
	}
}
