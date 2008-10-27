package br.org.gamexis.plataforma.motor.memoria;

import java.util.HashMap;

import org.lwjgl.Sys;

public class GerenciadorRecursosAlocados {	
	
	private HashMap<String, Recurso> recursos = new HashMap<String, Recurso>();
	
	private int maximo = 700;
	private long deltaMaximoAcesso = 50000;
	
	public GerenciadorRecursosAlocados(int maximo, long deltaMinimoAcesso) {
		this.maximo = maximo;		
		this.deltaMaximoAcesso = deltaMinimoAcesso;
		
	}
	
	public GerenciadorRecursosAlocados() {		
	}
		
	public void put(String ch, Object valor, int prioridade) {	
		
		Recurso r = new Recurso();
		r.prioridade = prioridade;
		r.nome = ch;
		r.objeto = valor;		
		r.acesso = Sys.getTime();
		
		if(recursos.containsKey(ch)) {
			recursos.put(ch, r);
			return;
		}
		
			
		//ELIMINANDO CHEIO
		if(recursos.size() >= maximo) {
			Recurso retirar = null;
			Recurso baixaPrioridade = null;
			
			for(Recurso rec : recursos.values()) {
				if(baixaPrioridade == null) {
					baixaPrioridade = rec;
				} else {
					if(baixaPrioridade.acesso > rec.acesso)
						baixaPrioridade = rec;
				}
				
				if(rec.prioridade < r.prioridade && (r.acesso - rec.acesso >= deltaMaximoAcesso)) {
					retirar = rec;
					return;
				}
			}
			if(retirar != null)
				recursos.remove(retirar.nome);
			else 
				recursos.remove(baixaPrioridade.nome);
		}

		recursos.put(ch, r);
	}
	
	public Object get(String ch) {
		Recurso r = recursos.get(ch);
		
		if(r != null)
			r.acesso = Sys.getTime();
		if(r != null)
			return r.objeto;
		
		return null;
	}
}
