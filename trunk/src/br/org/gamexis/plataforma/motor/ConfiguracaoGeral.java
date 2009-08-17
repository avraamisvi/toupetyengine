package br.org.gamexis.plataforma.motor;

import br.org.gamexis.plataforma.motor.configuracao.Controles;
import br.org.gamexis.plataforma.motor.configuracao.Som;
import br.org.gamexis.plataforma.motor.configuracao.Video;

/**
 * Configuração geral
 * @author abraao
 *
 */
public class ConfiguracaoGeral {
	private int id = 0;
	private Video resolucao;
	private Som som;
	private Controles controles;
	
	public Video getResolucao() {
		return resolucao;
	}
	public void setResolucao(Video resolucao) {
		this.resolucao = resolucao;
	}
	public Som getSom() {
		return som;
	}
	public void setSom(Som som) {
		this.som = som;
	}
	public Controles getControles() {
		return controles;
	}
	public void setControles(Controles controles) {
		this.controles = controles;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}
