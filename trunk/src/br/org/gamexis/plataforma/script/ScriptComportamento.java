package br.org.gamexis.plataforma.script;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.cena.NivelCena;
import br.org.gamexis.plataforma.entidade.TipoEntidade;
import br.org.gamexis.plataforma.motor.RecursosFactory;

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
