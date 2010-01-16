package br.org.gamexis.plataforma.script;


/**
 * Representa um script. que define o comportamento da entidade.
 * 
 * @author abraao
 * 
 */
public interface ScriptComportamento {
	
	void execute(String nome, Object ...argumentos)throws Exception;
	void compile() throws Exception;
	public void setParametro(String nome, Object valor);
}
