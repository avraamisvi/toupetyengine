package br.org.gamexis.plataforma.testes;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

class Teste {
	String dado = "nada";
	
	public String getDado() {
		return dado;
	}
	
	public String setDado(String dado) {
		return this.dado = dado;
	}
}

public class TesteJavaScript {	
	public static void main(String[] args) {
		Context cx = Context.enter();
		
			Scriptable scope = cx.initStandardObjects();
			
			Object wrappedOut = Context.javaToJS(new Teste(), scope);
			ScriptableObject.putProperty(scope, "TESTE", wrappedOut);
	        Object jsOut = Context.javaToJS(System.out, scope);
	        ScriptableObject.putProperty(scope, "out", jsOut);
	        
			Script script = null;
			Teste t = new Teste();
			
		for(int i = 0; i < 3; i++) {
			try {
				if(script == null) {
					script = cx.compileReader(new FileReader("teste.js"), "teste", 0, null);
					script.exec(cx, scope);
					Object out = Context.javaToJS(Boolean.TRUE, scope);
					scope.put("rodou", scope, out);
				}
				
				Object fObj = scope.get("rodar", scope);
				if (!(fObj instanceof Function)) {
				    System.out.println("rodar is undefined or not a function.");
				} else {
				    Object functionArgs[] = { "teste", t};
				    Function f = (Function)fObj;
				    Object result = f.call(cx, scope, scope, functionArgs);
				    String report = "rodar('my args') = " + Context.toString(result);
				    System.out.println(report);
				}
				
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
			
			
			Scriptable scope2 = cx.initStandardObjects();
			wrappedOut = Context.javaToJS(new Teste(), scope);
			ScriptableObject.putProperty(scope2, "TESTE", wrappedOut);
			jsOut = Context.javaToJS(System.out, scope2);
	        ScriptableObject.putProperty(scope2, "out", jsOut);
	        boolean flag = false;
	        
        for(int i = 0; i < 3; i++) {
			try {
				//script = cx.compileReader(new FileReader("teste.js"), "teste", 0, null);
				if(!flag) {
					script.exec(cx, scope2);
					flag = true;
				}
				Object fObj = scope2.get("rodar", scope);
				if (!(fObj instanceof Function)) {
				    System.out.println("rodar is undefined or not a function.");
				} else {
				    Object functionArgs[] = { "teste2", t};
				    Function f = (Function)fObj;
				    Object result = f.call(cx, scope2, scope2, functionArgs);
				    String report = "rodar('my args') = " + Context.toString(result);
				    System.out.println(report);
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}	
        }
	}
}
