package br.org.gamexis.plataforma.motor.particulas;

import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ConfigurableEmitterFactory;

public class ToupetyEmitterFactory implements ConfigurableEmitterFactory {

	@Override
	public ConfigurableEmitter createEmitter(String name) {
		return new ToupetyEmitter(name);
	}

}
