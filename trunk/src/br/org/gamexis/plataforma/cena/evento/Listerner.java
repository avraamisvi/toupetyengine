package br.org.gamexis.plataforma.cena.evento;

/**
 * Base para os listerners
 * @author abraao
 *
 */
public interface Listerner {
	public void atualizar(int delta);
	public boolean excluido();
}
