package br.org.gamexis.plataforma.cena;

public interface CenarioIF extends Cena {
	
	public void adicionarAnimacao(Animado anim, int nivel);
	public void adicionarAtor(Ator ator, int nivel, int excluidos);
	public void adicionarAtor(Ator ator, int nivel, int excluidos, boolean semCorpo);
}
