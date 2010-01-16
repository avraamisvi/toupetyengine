package br.org.gamexis.plataforma.script;

public class ScriptException extends Exception {

	public ScriptException() {
	}

	public ScriptException(String message) {
		super(message);
	}

	public ScriptException(Throwable cause) {
		super(cause);
	}

	public ScriptException(String message, Throwable cause) {
		super(message, cause);
	}

}
