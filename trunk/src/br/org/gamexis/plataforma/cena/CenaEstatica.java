package br.org.gamexis.plataforma.cena;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.newdawn.slick.GameContainer;

import br.org.gamexis.plataforma.cena.evento.Listerner;
import br.org.gamexis.plataforma.cena.evento.ListernerManager;
import br.org.gamexis.plataforma.exception.GXException;

public class CenaEstatica extends CenaBase implements ListernerManager {
	
	private List<Listerner> listerners = new ArrayList<Listerner>();
	private Listerner atual = null;
	
	@Override
	public void atualizar(GameContainer container, int delta)
			throws GXException {		
		super.atualizar(container, delta);
		
		Iterator<Listerner> itl = listerners.iterator();
		while(itl.hasNext()){
			Listerner l = itl.next();		
			atual = l;//listerner atual em execução
			l.atualizar(delta);
			atual = null;
			if(l.excluido()) {
				itl.remove();
			}
		}		
	}

	public void adicionarListerner(Listerner listerner) {
		listerners.add(listerner);
	}

	@Override
	public Listerner getListernerAtual() {
		return atual;
	}
}
