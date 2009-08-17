package br.org.gamexis.plataforma.motor.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Implementação de FileSystem Nulo, 
 * voltado para desenvolvimento e debug.		
 * @author abraao
 *
 */
public class NullFileSystem implements FileSystem {

	private static String RECURSOS_DAT = "recursos";
	
	@Override
	public InputStream abrirInputStream(String path) throws IOException {
//		System.out.println("carregou:" + path);
		return new FileInputStream(new File(RECURSOS_DAT + File.separator + path));
	}

	@Override
	public OutputStream abrirOutputStream(String path) throws IOException {
		return new FileOutputStream(new File(RECURSOS_DAT + File.separator + path));
	}

}
