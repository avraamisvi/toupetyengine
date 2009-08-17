package br.org.gamexis.plataforma.motor.particulas;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleSystem;

import br.org.gamexis.plataforma.Motor;
import br.org.gamexis.plataforma.motor.RecursosFactory;

public class ToupetyEmitter extends ConfigurableEmitter {

	String imageName;

	public ToupetyEmitter(String name) {
		super(name);
	}

	public void update(ParticleSystem system, int delta) {
		if (imageName == null)
			imageName = getImageName();

		setUpdateImage(false);

		try {
			if (imageName != null) {
				if (getImage() == null)// MEMORY LEAK HERE
				{
//					System.out.println("leak?" + imageName);
					setImage(RecursosFactory.getInstancia().getImage(imageName));
				}
			}
		} catch (SlickException e) {
			Motor.obterInstancia().tratarExcecao(e);
		}
		super.update(system, delta);
	}
	
	@Override
	protected void finalize() throws Throwable {
		
		if (imageName != null)
			RecursosFactory.getInstancia().libertarRecurso(imageName);//funfando
		super.finalize();
	}
}
