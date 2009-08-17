package br.org.gamexis.plataforma.motor;


public class ConfiguracaoInicial {
	String icone;
	String titulo;
	Cursor cursor = new Cursor();
	
	int comprimento; 
	int altura;	
	boolean telacheia = false;
	boolean debug = false;
	String cenaInicial;	
	int framerate = 300;
	
	int logicaMaxima = 6;
	int logicaMinima = 1;
	
	public String getIcone() {
		return icone;
	}
	public void setIcone(String icone) {
		this.icone = icone;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public int getComprimento() {
		return comprimento;
	}
	public void setComprimento(int comprimento) {
		this.comprimento = comprimento;
	}
	public int getAltura() {
		return altura;
	}
	public void setAltura(int altura) {
		this.altura = altura;
	}
	public boolean isTelacheia() {
		return telacheia;
	}
	public void setTelacheia(boolean telacheia) {
		this.telacheia = telacheia;
	}
	public boolean isDebug() {
		return debug;
	}
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	public String getCenaInicial() {
		return cenaInicial;
	}
	public void setCenaInicial(String cenaInicial) {
		this.cenaInicial = cenaInicial;
	}
	public int getFramerate() {
		return framerate;
	}
	public void setFramerate(int framerate) {
		this.framerate = framerate;
	}
	public int getLogicaMaxima() {
		return logicaMaxima;
	}
	public void setLogicaMaxima(int logicaMaxima) {
		this.logicaMaxima = logicaMaxima;
	}
	public int getLogicaMinima() {
		return logicaMinima;
	}
	public void setLogicaMinima(int logicaMinima) {
		this.logicaMinima = logicaMinima;
	}
	
	public Cursor getCursor() {
		return cursor;
	}
	
	public void setCursor(Cursor cursor) {
		this.cursor = cursor;
	}
	
	public class Cursor {
		public String entidade;
		public String referencia;
	}
}
