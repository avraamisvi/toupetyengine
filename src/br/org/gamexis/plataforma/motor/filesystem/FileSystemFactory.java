package br.org.gamexis.plataforma.motor.filesystem;

import br.org.gamexis.plataforma.Motor;

public class FileSystemFactory {
	private static String fileSystemClassName = "br.org.gamexis.plataforma.motor.filesystem.NullFileSystem";
	private static FileSystem instancia;
	
	public static void setFileSystem(String fileSystemClassName) {
		FileSystemFactory.fileSystemClassName = fileSystemClassName;
	}
	
	public static FileSystem getFileSystem() {
		
		if(instancia == null) {
			try {				
				instancia = (FileSystem) Class.forName(fileSystemClassName).newInstance();
				System.out.println("USANDO:" + fileSystemClassName);
			} catch (ClassNotFoundException e) {
				Motor.obterInstancia().tratarExcecao(e);
			} catch (InstantiationException e) {
				Motor.obterInstancia().tratarExcecao(e);
			} catch (IllegalAccessException e) {
				Motor.obterInstancia().tratarExcecao(e);
			} finally {
				if(instancia == null) {
					System.out.println("USANDO:" + fileSystemClassName);
					instancia = new NullFileSystem();
				}
			}
		}
		return instancia;
	}
}
