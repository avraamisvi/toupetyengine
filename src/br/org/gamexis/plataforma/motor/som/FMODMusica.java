package br.org.gamexis.plataforma.motor.som;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

import org.jouvieje.Fmod.Fmod;
import org.jouvieje.Fmod.Callbacks.FSOUND_STREAMCALLBACK;
import org.jouvieje.Fmod.Defines.FSOUND_INIT_FLAGS;
import org.jouvieje.Fmod.Defines.FSOUND_MISC_VALUES;
import org.jouvieje.Fmod.Defines.FSOUND_MODES;
import org.jouvieje.Fmod.Defines.PLATFORMS;
import org.jouvieje.Fmod.Defines.VERSIONS;
import org.jouvieje.Fmod.Enumerations.FSOUND_OUTPUTTYPES;
import org.jouvieje.Fmod.Misc.BufferUtils;
import org.jouvieje.Fmod.Misc.Pointer;
import org.jouvieje.Fmod.Structures.FSOUND_SAMPLE;
import org.jouvieje.Fmod.Structures.FSOUND_STREAM;

public class FMODMusica implements Runnable, PLATFORMS, FSOUND_OUTPUTTYPES,
		FSOUND_INIT_FLAGS, FSOUND_MODES, FSOUND_MISC_VALUES, VERSIONS {
	private boolean loop;
	private String musica;
	private FSOUND_STREAM stream = null;
	private boolean streamEnded = false;
	private boolean parar = false;

	public FMODMusica(String musica, boolean loop) {
		this.loop = loop;
		this.musica = musica;
	}

	public void run() {

		Fmod.FSOUND_SetDriver(0); /* Select sound card (0 = default) */

		while (loop && !parar) {
			FSOUND_SAMPLE sptr;
			int channel = -1;
			
			ByteBuffer streamingBuffer;
			Fmod.FSOUND_Stream_SetBufferSize(1000);
			File filename = new File(musica);

			String name = filename.getPath();
			
			int length = 0;
			byte[] datas = null;
			RandomAccessFile file = null;
			try
			{
				file = new RandomAccessFile(filename, "rw");		//rw = read and write
			}
			catch(FileNotFoundException e)
			{
				parar();
				return;
			}
			try
			{
				length = (int)file.length();
				datas = new byte[length];
				file.read(datas);
				file.close();
			}
			catch(Exception e)
			{
				parar();
				return;
			}
			
			streamingBuffer = BufferUtils.newByteBuffer(datas.length);
			streamingBuffer.put(datas);
			streamingBuffer.rewind();		//Go to the beginning of the buffer
			
			//The memory pointer MUST remain valid while streaming ! (keeps buffer referenced somewhere)
			stream = Fmod.FSOUND_Stream_Open(streamingBuffer,
					FSOUND_NORMAL | FSOUND_MPEGACCURATE | FSOUND_LOADMEMORY,
					0,
					length);
			
			
			Fmod.FSOUND_Stream_SetEndCallback(stream, aoFinalizarMusica, null);

			sptr = Fmod.FSOUND_Stream_GetSample(stream);
//			if (sptr != null) {
//				int[] freq = new int[1];
//				Fmod.FSOUND_Sample_GetDefaults(sptr, freq, null, null, null);
//			}

			do {
				if (channel < 0) {
					channel = Fmod.FSOUND_Stream_PlayEx(FSOUND_FREE, stream,
							null, true);
					Fmod.FSOUND_SetPaused(channel, false);
				}

				try {
					Thread.sleep(100);
				} catch (Exception e) {
				}
			} while (!streamEnded && !parar);

			if (loop && !parar)
				reiniciar();
		}//LOOP
	}

	public void reiniciar() {
		if (stream != null && !stream.isNull())
			Fmod.FSOUND_Stream_Close(stream);
		streamEnded = false;
	}

	public void parar() {
		parar = true;
		if (stream != null && !stream.isNull())
			Fmod.FSOUND_Stream_Close(stream);
	}

	private FSOUND_STREAMCALLBACK aoFinalizarMusica = new FSOUND_STREAMCALLBACK() {
		
		public boolean FSOUND_STREAMCALLBACK(FSOUND_STREAM stream, ByteBuffer buff, 
				int len, Pointer userdata) {
			
			if (buff == null) {
				streamEnded = true;
			}

			return true;
		}
	};
}
