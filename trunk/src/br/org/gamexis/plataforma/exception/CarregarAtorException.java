package br.org.gamexis.plataforma.exception;

public class CarregarAtorException extends RuntimeException {
	
	private String nomeAtor;
	
	public CarregarAtorException(String ator, Throwable cause) {
		super(cause);
		nomeAtor = ator;
	}
	
	@Override
	public String getMessage() {
		
		return "Erro ao carregar ator " + nomeAtor + " causa: " + getCause().getMessage();
	}
}
