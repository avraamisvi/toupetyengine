package br.org.gamexis.plataforma.testes;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class Resolusoes {
	
	public static void teste() {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice devs[] = env.getScreenDevices();
		
		for(GraphicsDevice dev : devs) {
			for(GraphicsConfiguration c : dev.getConfigurations()) {
				System.out.println(c);
			}
		}
		
	}
	
	public static void main(String[] args) {
		teste();
	}
}
