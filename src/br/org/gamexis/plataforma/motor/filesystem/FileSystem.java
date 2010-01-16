package br.org.gamexis.plataforma.motor.filesystem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Interface base para um File System simulado.
 * 
 * A idéia é a da classe que implementa essa interface, somente abrir
 * streams de leitura e escrita, e 
 * deixando transparente o processa para tal.
 * @author abraao
 *
 */
public interface FileSystem {
	
	InputStream abrirInputStream(String path) throws IOException;
	OutputStream abrirOutputStream(String path) throws IOException;
}
