package br.org.gamexis.plataforma.motor.som;

import java.awt.HeadlessException;

import org.jouvieje.Fmod.Fmod;
import org.jouvieje.Fmod.Init;
import org.jouvieje.Fmod.Defines.FSOUND_INIT_FLAGS;
import org.jouvieje.Fmod.Defines.FSOUND_MISC_VALUES;
import org.jouvieje.Fmod.Defines.FSOUND_MODES;
import org.jouvieje.Fmod.Defines.PLATFORMS;
import org.jouvieje.Fmod.Defines.VERSIONS;
import org.jouvieje.Fmod.Enumerations.FSOUND_MIXERTYPES;
import org.jouvieje.Fmod.Enumerations.FSOUND_OUTPUTTYPES;
import org.jouvieje.Fmod.Exceptions.InitException;

public class FMODUtil implements PLATFORMS, FSOUND_OUTPUTTYPES,
		FSOUND_INIT_FLAGS, FSOUND_MODES, FSOUND_MISC_VALUES, VERSIONS,
		FSOUND_MIXERTYPES {

	private static boolean init = false;
	private static FMODUtil fModUtil = new FMODUtil();
	private FMODMusica musica = null;
	private boolean musicaHabilitada = true;
	private boolean efeitosHabilitado = true;

	private FMODUtil() {
	}

	public static FMODUtil obterInstancia() {
		return fModUtil;
	}

	public boolean isMusicaHabilitada() {
		return musicaHabilitada;
	}

	public void setMusicaHabilitada(boolean musicaHabilitada) {
		if(!musicaHabilitada)
			pararMusica();
		this.musicaHabilitada = musicaHabilitada;
	}

	public boolean isEfeitosHabilitado() {		
		return efeitosHabilitado;
	}

	public void setEfeitosHabilitado(boolean efeitosHabilitado) {
		this.efeitosHabilitado = efeitosHabilitado;
	}

	public void iniciar() {
		if (init)
			return;

		try {
			Init.loadLibraries();
		} catch (InitException e) {
			System.out.println("NativeFmod error! " + e.getMessage());
			return;
		}

		try {
			Fmod.FSOUND_SetDriver(0);// padrão
			Fmod.FSOUND_SetMixer(FSOUND_MIXER_QUALITY_AUTODETECT);

			if (Fmod.getPlatform() == WIN32 || Fmod.getPlatform() == WIN_CE
					|| Fmod.getPlatform() == WIN64)
				Fmod.FSOUND_SetOutput(FSOUND_OUTPUT_WINMM);
			else if (Fmod.getPlatform() == LINUX)
				Fmod.FSOUND_SetOutput(FSOUND_OUTPUT_OSS);
			else if (Fmod.getPlatform() == MAC)
				Fmod.FSOUND_SetOutput(FSOUND_OUTPUT_MAC);

			if (!Fmod.FSOUND_Init(44100, 32, 0)) {
				String message = "FMOD Error: "
						+ Fmod.FMOD_ErrorString(Fmod.FSOUND_GetError());
				Fmod.FSOUND_Close();
				System.out.println(message);
				return;
			}

			if (NATIVEFMOD_LIBRARY_VERSION != NATIVEFMOD_JAR_VERSION) {
				System.out.println("Error!  NativeFmod library version ("
						+ NATIVEFMOD_LIBRARY_VERSION
						+ ") is different to jar version ("
						+ NATIVEFMOD_JAR_VERSION + ")");
				return;
			}

			if (Fmod.FSOUND_GetVersion() < FMOD_VERSION) {
				System.out
						.println("Error : You are using the wrong DLL version!  You should be using FMOD "
								+ FMOD_VERSION);
				return;
			}

		} catch (HeadlessException e) {
			System.out.println("Unexpected exception: " + e.getMessage());
			return;
		}

		init = true;
	}

	public void finalizar() {
		pararMusica();
		Fmod.FSOUND_Close();
	}

	public void tocarMusica(String musica, boolean loop) {
		if (!musicaHabilitada)
			return;
		if (init && (this.musica == null)) {
			this.musica = new FMODMusica(musica, loop);
			new Thread(this.musica).start();
		}
	}

	public void retocarMusica() {
		if (!musicaHabilitada)
			return;
		pararMusica();
		this.musica.reiniciar();
		new Thread(this.musica).start();
	}

	public void pararMusica() {
		if (init) {
			if (this.musica != null) {
				this.musica.parar();
				this.musica = null;
			}
		}
	}

	public FMODSom tocarSom(String som) {
		if (efeitosHabilitado) {
			if (init) {
				FMODSom fsom = new FMODSom(som);
				new Thread(fsom).start();
				return fsom;
			}
		}

		return null;
	}
}
