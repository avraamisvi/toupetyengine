package br.org.gamexis.plataforma.entidade.arma;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.org.gamexis.plataforma.entidade.EntidadeArma;

public class GerenciadorArmas {
	List<String> itens = new ArrayList<String>();
	HashMap<String, EntidadeArma> armas = new HashMap<String, EntidadeArma>();
	int indice = 0;
	
	public void habilitar(String nome) {
		itens.add(nome);
	}
	
	public void desabilitar(String nome) {
		itens.remove(nome);
	}
	
	public void adicionar(String nome, EntidadeArma arma) {
		armas.put(nome, arma);
		
		if(itens.isEmpty()) {
			itens.add(nome);
		}
	}
	
	public EntidadeArma proximo() {
		if(indice < itens.size()-1) {
			indice++;
		}			
		
		return armas.get(itens.get(indice));
	}
	
	public EntidadeArma selecionar(String nome) {
		
		return armas.get(nome);
	}
	
	public EntidadeArma anterior() {
		if(indice > 0) {
			indice--;
		}			
		
		return armas.get(itens.get(indice));		
	}
	
	public EntidadeArma get(String arma) {
		return armas.get(arma);
	}
}
 