package br.org.gamexis.plataforma.motor.filesystem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.schlichtherle.io.ArchiveDetector;
import de.schlichtherle.io.DefaultArchiveDetector;
import de.schlichtherle.io.File;
import de.schlichtherle.io.FileInputStream;
import de.schlichtherle.io.FileOutputStream;
/**
 * Implementação de um fileSystem 
 * para "proteger" os dados de toupety
 * @author abraao
 *
 */
public class ToupetyFileSystem implements FileSystem{

	private static String RECURSOS_DAT = "recursos.dat";
		//"de.schlichtherle.io.archive.zip.Zip32Driver"
	static {
		File.setDefaultArchiveDetector(new DefaultArchiveDetector(
		        ArchiveDetector.NULL, // delegate
		        new String[] {
		            "dat", "de.schlichtherle.io.archive.zip.Zip32Driver"		            
		        }));
	}
	
	@Override
	public InputStream abrirInputStream(String path) throws IOException {
//		try {
			return new FileInputStream(RECURSOS_DAT + java.io.File.separator + path);
//		} catch(IOException e) {
//			Motor.obterInstancia().tratarExcecao(e);
//			throw e;
//		}
	}

	@Override
	public OutputStream abrirOutputStream(String path) throws IOException {
		return new FileOutputStream(RECURSOS_DAT + java.io.File.separator + path, true);
	}

}
