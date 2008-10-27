package br.org.gamexis.plataforma.cena;

/**
 * Animação
 * @author abraao
 *
 */
public interface Animado extends Desenho, Atualizavel, Cloneable {
	public void reiniciar();	
//	public void atualizar(int delta);
	public boolean ultimoQuadro();
	public Object clone() throws CloneNotSupportedException;
}
