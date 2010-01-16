package br.org.gamexis.plataforma.cena;

/**
 * Representa um nivel numa cena.
 * @author abraao
 *
 */
public interface NivelCena {	
	int fundo = 0x1;
	int frente = 0x2;
	int meio = 0x4;
	int animados = 0x8;	
	int plataforma = 0x10;
	int fixosFrente = 0x20;
	int fixosFundo = 0x40;
}
