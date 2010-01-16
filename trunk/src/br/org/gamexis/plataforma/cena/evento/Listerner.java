package br.org.gamexis.plataforma.cena.evento;

/**
 * Base para os listerners
 * @author abraao
 *
 */
public interface Listerner {
	public void atualizar(int delta);
	public boolean excluido();
	public void setVariavel(String nome, Object valor);
	public Object getValorVariavel(String nome);
	public void aoAdicionar();
}
