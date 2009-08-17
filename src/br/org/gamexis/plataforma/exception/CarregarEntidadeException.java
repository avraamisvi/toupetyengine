package br.org.gamexis.plataforma.exception;

public class CarregarEntidadeException extends RuntimeException {
	
	private String nomeEntidade;
	
	public CarregarEntidadeException(String entidade, Throwable cause) {
		super(cause);
		nomeEntidade = entidade;
	}
	
	@Override
	public String getMessage() {
		
		return "Erro ao carregar entidade " + nomeEntidade + " causa: " + getCause().getMessage();
	}
}
