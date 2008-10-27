package br.org.gamexis.plataforma.motor.som;

import org.jouvieje.Fmod.Fmod;
import org.jouvieje.Fmod.Defines.FSOUND_CAPS;
import org.jouvieje.Fmod.Defines.FSOUND_MISC_VALUES;
import org.jouvieje.Fmod.Defines.FSOUND_MODES;
import org.jouvieje.Fmod.Defines.FSOUND_REVERB_PRESETS;
import org.jouvieje.Fmod.Defines.PLATFORMS;
import org.jouvieje.Fmod.Defines.VERSIONS;
import org.jouvieje.Fmod.Enumerations.FSOUND_MIXERTYPES;
import org.jouvieje.Fmod.Enumerations.FSOUND_OUTPUTTYPES;
import org.jouvieje.Fmod.Structures.FSOUND_SAMPLE;

public class FMODSom implements Runnable, Som, FSOUND_OUTPUTTYPES, FSOUND_CAPS, FSOUND_MIXERTYPES, 
	FSOUND_MISC_VALUES, FSOUND_MODES, FSOUND_REVERB_PRESETS, PLATFORMS, VERSIONS{

	private FSOUND_SAMPLE samp1 = null; 
//	private ByteBuffer samp1Buff = null;
	String som;
	private boolean tocando = true;
	
	public FMODSom(String som) {
		this.som = som;
	}
	
	public void run()
	{
		samp1 = Fmod.FSOUND_Sample_Load(FSOUND_FREE, som, FSOUND_HW3D , 0, 0);
		if(samp1 == null)
		{
			System.out.println("samp1 : "+Fmod.FMOD_ErrorString(Fmod.FSOUND_GetError()));
			stop();
			return;
		}

		// increasing mindistnace makes it louder in 3d space
		Fmod.FSOUND_Sample_SetMinMaxDistance(samp1, 4.0f, 10000.0f);
		Fmod.FSOUND_Sample_SetMode(samp1, FSOUND_HW2D);

		float[] pos = new float[] {-10.0f, 0.0f, 0.0f};
		float[] vel = new float[] {0.f, 0.f, 0.f};

//		int channel1 = Fmod.FSOUND_PlaySoundEx(FSOUND_FREE, samp1, null, true);
//		Fmod.FSOUND_3D_SetAttributes(channel1, pos, vel);
		
//		if(!Fmod.FSOUND_SetPaused(channel1, false)) 	{
//			
//			System.out.println(Fmod.FMOD_ErrorString(Fmod.FSOUND_GetError())+"\n");
//		}
			
		int canal = Fmod.FSOUND_PlaySound(FSOUND_FREE, samp1);
		tocando = true;
		while(Fmod.FSOUND_IsPlaying(canal)) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		tocando = false;
//		System.out.println("FREE SOUND: " + tocando);
		stop();
	}
	
	public void stop()
	{
		if(samp1 != null && !samp1.isNull()) 
			Fmod.FSOUND_Sample_Free(samp1); 
//		samp1Buff = null;
	}	
	
	public boolean estaTocando() {
		return tocando;
	}

	@Override
	public void parar() {
	}

	@Override
	public void tocar() {
	}
}
