package br.org.gamexis.plataforma.cena;

import org.newdawn.slick.Graphics;

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
	public String getIdentificacao();
	public void setIdentificacao(String identificacao);
}
