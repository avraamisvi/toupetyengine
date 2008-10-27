package br.org.gamexis.plataforma.motor;

import java.util.HashMap;

/**
 * Banco de variaveis de ambiente.
 * @author abraao
 *
 */
public class VariaveisAmbiente {
	private static HashMap<String, Object> variaveis = new HashMap<String, Object>();
	
	public static void inserirVariavel(String nome, Object valor) {
		variaveis.put(nome, valor);
	}
	
	public static Object getValorVariavel(String nome) {
		return variaveis.get(nome);
	}
}
