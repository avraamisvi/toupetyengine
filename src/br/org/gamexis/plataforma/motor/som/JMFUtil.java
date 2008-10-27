package br.org.gamexis.plataforma.motor.som;

public class JMFUtil {
	private static JMFUtil instancia;
	private JMFMusica musica;
	
	public static JMFUtil getInstancia() {
		
		if(instancia == null)
			instancia = new JMFUtil();
			
		return instancia;
	}
	
	public void tocarMusica(String musica, boolean repetir) {
		this.musica = new JMFMusica(musica, repetir);
		new Thread(this.musica).start();
	}
	
	public void pararMusica() {
		if(musica != null)
			this.musica.parar();
	}
	
	public void tocarMesmaMusica(boolean repetir) {
		this.musica.repetir = repetir;
		new Thread(this.musica).start();
	}
	
	public JMFMusica tocarSom(String som) {
		JMFMusica somTocar= new JMFMusica(som, false);
		new Thread(somTocar).start();
		return somTocar;
	}
	
	public boolean isTocandoMusica() {
		if(musica != null)
			return musica.estatocando;
		
		return false;
	}
	
	public String getNomeMusica() {
		return musica.getNome();
	}
}
