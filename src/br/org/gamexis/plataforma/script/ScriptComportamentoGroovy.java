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
public class ScriptComportamentoGroovy implements ScriptComportamento {
	
	//Indica se script esta com erro de codigo se sim, não executa.
	private boolean comErro = false;
	
	private String script;
    private ScriptEngine engine;
    
    private CompiledScript compilado;
    private ScriptContext contexto;
    
    private static ScriptEngineFactory factory;
    private static boolean primeiraVez = true;
    static {
    	factory = new ScriptEngineManager().getEngineByExtension("groovy").getFactory();
    }

	public ScriptComportamentoGroovy(String script) {
		this.script = script;
	}

	public ScriptComportamentoGroovy(ScriptComportamentoGroovy comportamento) {
		//String script, CompiledScript compilado, ScriptEngine engine
		this.script = comportamento.script;
		contexto = new SimpleScriptContext();
		
		this.engine = factory.getScriptEngine();
		this.compilado = comportamento.compilado;
		
		contexto.setBindings(engine.createBindings(), ScriptContext.ENGINE_SCOPE);
		
	}
	
	public void execute(String nome, Object ...argumentos) throws Exception {
		if(script == null || script.length() <= 0) {
			return;
		}
		
		try {
			if(!comErro) {
				executarMetodoGroovy(nome, argumentos);
			}
		} catch (ScriptException e) {
			comErro = true;
			Logger.getLogger(Motor.ARQUIVO_LOG).log(Level.SEVERE, "====#####ERRO SCRIPT: " + script +" #####======", e);
			e.printStackTrace();
		}catch (Exception e) {
			comErro = true;
			Logger.getLogger(Motor.ARQUIVO_LOG).log(Level.SEVERE, "====#####ERRO SCRIPT: " + script +" #####======", e);
			e.printStackTrace();			
			throw new ScriptException(e);
		}
	}

	public void compile() throws FileNotFoundException, ScriptException {
		if(engine == null) {
			
			engine = factory.getScriptEngine();//TODO COLOCAR COMO GLOBAL
			engine.put("CENA_ATUAL", Motor.obterInstancia().getCenaAtual());
			engine.put("MOTOR", Motor.obterInstancia());
			engine.put("FABRICA", RecursosFactory.getInstancia());
			engine.put("TIPOENTIDADE", TipoEntidade.class);
			engine.put("NIVELCENA", NivelCena.class);
			
			engine.eval(new FileReader(script));
		}
	}
	
	private void executarMetodoGroovy(String metodo, Object ...argumentos) throws Exception {
		compile();
		engine.put("CENA_ATUAL", Motor.obterInstancia().getCenaAtual());
		
		((Invocable)engine).invokeFunction(metodo, argumentos);		
	}
	
	public void setParametro(String nome, Object valor) {
		engine.put(nome, valor);
	}

}
