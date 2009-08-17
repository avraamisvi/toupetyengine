package br.org.gamexis.plataforma.cena;

import net.phys2d.raw.Body;
import br.org.gamexis.plataforma.entidade.Entidade;

public interface Colisivel {
//	float densidadeVirtual();
//	float area
	Body getCorpo();
	void atualizar(int delta);
	Entidade getEntidade();
	void setEntidade(Entidade entidade);

	public void excluir();
	public boolean excluido();
	public float getAltura();
	public float getComprimento();
	public String getNome();
	public void dispose();
}
