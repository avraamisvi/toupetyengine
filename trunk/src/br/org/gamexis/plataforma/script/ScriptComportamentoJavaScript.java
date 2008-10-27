package br.org.gamexis.plataforma.script;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.motor.RecursosFactory;

/**
 * Representa um script. que define o comportamento da entidade.
 * 
 * @author abraao
 * 
 */
public class ScriptComportamentoJavaScript implements ScriptComportamento {
	
	private Context cx;
	private Script compilado;
	private Scriptable scope;
	private String script;
	private boolean comErro = false;
	private static Script importes;
	
	static {
		String importacao = "var NIVELCENA = Packages.br.org.gamexis.plataforma.cena.NivelCena; \n"
			+ " var TIPOENTIDADE = Packages.br.org.gamexis.plataforma.entidade.TipoEntidade; ";
		importes = Context.enter().compileString(importacao, "importar", 0, null);
	}
	
	public ScriptComportamentoJavaScript(String script) {
		this.script = script;
		cx = Context.enter();
		scope = cx.initStandardObjects();
		iniciar();
	}

	public ScriptComportamentoJavaScript(ScriptComportamentoJavaScript comportamento) {		
		cx = Context.enter();
		script = comportamento.script;
		scope = cx.initStandardObjects();		
		compilado = comportamento.compilado;
		iniciar();
		initScope();
	}
	
	public void iniciar() {

		
		setParametro("CENA_ATUAL", Motor.obterInstancia().getCenaAtual());
		setParametro("MOTOR", Motor.obterInstancia());
		setParametro("FABRICA", RecursosFactory.getInstancia());
		setParametro("out", System.out);
	}
	
	public void execute(String nome, Object ...argumentos) throws Exception {
		if(script == null || script.length() <= 0) {
			return;
		}
		
		try {
			if(!comErro) {
				executarMetodo(nome, argumentos);
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

	public void compile() throws Exception {
			
		if(compilado == null) {
			compilado = cx.compileReader(new FileReader(new File(script)), script, 0, null);
			compilado.exec(cx, scope);
		}
	}
	
	public void initScope() {
		//talvez n seja necessario executar um script 
		//num novo scopo bastando chamar a função nele
		if(compilado != null)
			compilado.exec(cx, scope);
			importes.exec(cx, scope);
	}
	
	private void executarMetodo(String metodo, Object ...argumentos) throws Exception {
		compile();
		setParametro("CENA_ATUAL", Motor.obterInstancia().getCenaAtual());
		
		Object fObj = scope.get(metodo, scope);
		if (!(fObj instanceof Function)) {
		    System.out.println("rodar is undefined or not a function.");
		} else {		    
		    Function func = (Function)fObj;
		    Object result = func.call(cx, scope, scope, argumentos);

		}		
	}
	
	public void setParametro(String nome, Object valor) {
        Object jsOut = Context.javaToJS(valor, scope);
        ScriptableObject.putProperty(scope, nome, jsOut);		
	}
	
	public void putClass(Class clazz) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        ScriptableObject.defineClass(scope, clazz);
	}
}
