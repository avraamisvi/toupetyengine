package br.org.gamexis.plataforma.motor.som;

import org.newdawn.slick.Sound;

public class SomImpl implements Som {

	private Sound sound;
	
	public void setSound(Sound sound) {
		this.sound = sound;
	}
	
	@Override
	public boolean estaTocando() {
		return sound.playing();
	}

	@Override
	public void parar() {
		sound.stop();
	}

	@Override
	public void tocar() {
		sound.play();
	}

	@Override
	public void tocar(int x, int y, int z) {
		sound.playAt(x, y, z);
	}

}
