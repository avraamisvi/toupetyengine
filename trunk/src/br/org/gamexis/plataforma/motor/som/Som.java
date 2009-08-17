package br.org.gamexis.plataforma.motor.som;

public interface Som {
	void tocar();
	void tocar(int x, int y, int z);
	void parar();
	boolean estaTocando();
}
