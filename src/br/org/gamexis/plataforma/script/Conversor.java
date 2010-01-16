package br.org.gamexis.plataforma.script;

/**
 * Classe de apoio ao javascript. Devido as limitação de numeros serem
 * interpretados apenas como Doubles internamente.
 * 
 * @author abraaoisvioliveirasouzadocarmo
 * 
 */
public class Conversor {

	public static Integer converterParaInteiro(double entry) {
		
		return Double.valueOf(entry).intValue();
	}
	
}
