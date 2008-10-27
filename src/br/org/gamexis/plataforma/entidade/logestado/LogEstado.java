package br.org.gamexis.plataforma.entidade.logestado;

import java.util.Collection;
import java.util.HashMap;

public class LogEstado {
	private HashMap<String, ArmaSelecionada> armas = new HashMap<String, ArmaSelecionada>();
	private HashMap<String, BarraLogEstado> barraEstadoLst = new HashMap<String, BarraLogEstado>();
	ArmaSelecionada armaSelecionada;
	
	public void adicionarBarraEstado(BarraLogEstado barraEstado) {
		barraEstadoLst.put(barraEstado.getNome(), barraEstado);
	}

	public void adicionarArma(ArmaSelecionada arma) {
		armas.put(arma.getNome(), arma);
	}
	
	public ArmaSelecionada getArma(String arma) {
		return armas.get(arma);
	}
	
	public ArmaSelecionada getArma() {
		return armaSelecionada;
	}

	public void setArma(String nome) {
		this.armaSelecionada = armas.get(nome);
	}
	
	public BarraLogEstado getBarraEstado(String nome) {
		return barraEstadoLst.get(nome);
	}
	
	Collection<BarraLogEstado> getBarrasEstado()  {
		return barraEstadoLst.values();
	}
}
