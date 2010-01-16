package br.org.gamexis.plataforma.cena;

import java.util.HashMap;

import org.newdawn.slick.SlickException;


public class MultImagem {
	
	private String referencia;
	private HashMap<String, Tile> tiles;
	
	public String getReferencia() {
		return referencia;
	}
	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
	public HashMap<String, Tile> getTiles() {
		return tiles;
	}
	public void setTiles(HashMap<String, Tile> tiles) {
		this.tiles = tiles;
	}
	
	public void destroy() throws SlickException {
		if(tiles != null) {
			for(Tile t : tiles.values()) {
				t.getImagem().destroy();
			}
		}
	}
}
