package br.org.gamexis.plataforma.motor.memoria;

import java.util.HashMap;

import org.lwjgl.Sys;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.cena.MultImagem;

public class GerenciadorRecursosAlocados implements MemoriaValorCusto {

	private HashMap<String, Recurso> recursos = new HashMap<String, Recurso>();

	private int maximo = 700;
	private long deltaMaximoAcesso = 50000;
	private String nome;

	public GerenciadorRecursosAlocados(String nome) {
		this.nome = nome;
	}

	public void put(String ch, Object valor, int custo) { // TODO refazer
																// esse metodo

		Recurso r = new Recurso();
		r.custo = custo;
		r.nome = ch;
		r.objeto = valor;
		r.acesso = Sys.getTime();
		r.donos++;

//		if (recursos.get(ch) == null) {
//			// System.out.println("Novo: " + ch);
//			recursos.put(ch, r);
//			return;
//		}

//		// ELIMINANDO CHEIO
//		if (recursos.size() >= maximo) {
//			Recurso retirar = null;
//			Recurso baixaPrioridade = null;
//
//			for (Recurso rec : recursos.values()) {
//				if (baixaPrioridade == null) {
//					baixaPrioridade = rec;
//				} else {
//					if (baixaPrioridade.acesso > rec.acesso)
//						baixaPrioridade = rec;
//				}
//
//				if (rec.custo < r.custo
//						&& (r.acesso - rec.acesso >= deltaMaximoAcesso)) {
//					retirar = rec;
//					return;
//				}
//			}
//			if (retirar != null)
//				recursos.remove(retirar.nome);
//			else
//				recursos.remove(baixaPrioridade.nome);
//		}

		recursos.put(ch, r);
	}

	public Object get(String ch) {
		Recurso r = recursos.get(ch);
		
		if (r != null) {
			long acesso = Sys.getTime();
			r.custo = r.custo + (int)((r.acesso/acesso) * 10);
			r.custo = r.custo > CUSTO_MAIS_ALTO? CUSTO_MAIS_ALTO : r.custo;
			
			r.acesso = acesso;
			r.donos++;
		}

		if (r != null) {
			return r.objeto;
		}

		return null;
	}

	public void release(String ch) {
		Recurso r = recursos.get(ch);
		
		if(r != null && r.donos > 0) {
//			System.out.println("Libertando Recurso:" + ch.toString() + " donos:" + r.donos);
			r.donos--;
		}
	}
	
	public void limpar(int nivel) {
//		List<String> chaves = new ArrayList<String>();

		Object keys[] = recursos.keySet().toArray();
		for (Object key : keys) {
			Recurso rec = recursos.get(key);
			
			if (rec != null && rec.custo <= nivel && rec.donos <= 0) {
				Recurso o = recursos.remove(key);
				
				if(o != null) {
					try {
						if(o.objeto instanceof Image) {
//							System.out.println("Destruindo Imagem:" + key.toString());
							((Image)o.objeto).destroy();
						} else if(o.objeto instanceof MultImagem) {
//							System.out.println("Destruindo Mult Imagem:" + key.toString());
							((MultImagem)o.objeto).destroy();
						}
					} catch (SlickException e) {
						Motor.obterInstancia().tratarExcecao(e);
					}
				} 
			} else if(rec != null && rec.donos <= 0) {
				rec.custo = rec.custo - 5;
			}

		}

//		for (String key : recursos.keySet()) {
//			Recurso r = recursos.get(key);
//			System.out.println("FICOU:" + key + " custo:" + r.custo);
//		}
	}
}
