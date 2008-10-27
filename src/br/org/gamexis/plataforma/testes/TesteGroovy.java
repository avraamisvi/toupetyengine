package br.org.gamexis.plataforma.testes;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;

import java.io.File;

public class TesteGroovy {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Binding bind = new Binding();
		
		GroovyShell shell = new GroovyShell(bind);
		GroovyClassLoader classLoader = new GroovyClassLoader();
		Class classeProxy = null;

		
		try {
			classeProxy = classLoader.parseClass(new File("/home/abraao/teste.groovy"));

			GroovyObject proxy;

			proxy = (GroovyObject) classeProxy.newInstance();
			
			PaiGroovy p = new FilhoGroovy();
			
			for(int i = 0; i < 100; i++) {
				proxy.invokeMethod("imprimeNome", new Object[]{p});
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
