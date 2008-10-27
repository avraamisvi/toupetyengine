package br.org.gamexis.plataforma.motor.som;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class JMFMusica implements Runnable, Som{
	private static final int EXTERNAL_BUFFER_SIZE = 128000;
	public boolean repetir;
	public boolean parar;
	public boolean estatocando;
	File soundFile;
	AudioInputStream audioInputStream;

	public JMFMusica(String musica, boolean repetir) {
		this.repetir = repetir;
		soundFile = new File(musica);
	}
	
	public void tocar() {		
		do {
			estatocando = true;
			try {
				audioInputStream = AudioSystem.getAudioInputStream(soundFile);
			} catch (Exception e) {
				e.printStackTrace();
				estatocando = false;
			}

			AudioFormat audioFormat = audioInputStream.getFormat();
			SourceDataLine line = null;
			DataLine.Info info = new DataLine.Info(SourceDataLine.class,
					audioFormat);
			try {
				line = (SourceDataLine) AudioSystem.getLine(info);
				line.open(audioFormat);
			} catch (Exception e) {
				estatocando = false;
				e.printStackTrace();
			}

			line.start();
			int nBytesRead = 0;
			byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];
			
			while (nBytesRead != -1) {
				if(parar)
					break;
				
				try {
					nBytesRead = audioInputStream
							.read(abData, 0, abData.length);
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (nBytesRead >= 0) {
					int nBytesWritten = line.write(abData, 0, nBytesRead);
				}
			}

			line.drain();
			line.close();
			estatocando = false;
		} while (repetir);
	}

	public void parar() {
		parar = true;
		repetir = false;
	}

	@Override
	public void run() {
		try {
			tocar();
		} finally {
			estatocando = false;
		}
	}

	@Override
	public boolean estaTocando() {
		return estatocando;
	}
	
	public String getNome() {
		return soundFile.getName();
	}
}
