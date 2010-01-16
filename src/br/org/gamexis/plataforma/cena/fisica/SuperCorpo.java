package br.org.gamexis.plataforma.cena.fisica;

import java.util.ArrayList;
import java.util.List;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.BasicJoint;
import net.phys2d.raw.Body;
import net.phys2d.raw.ElasticJoint;
import net.phys2d.raw.FixedJoint;
import net.phys2d.raw.Joint;
import net.phys2d.raw.shapes.DynamicShape;
import net.phys2d.raw.shapes.Shape;

/**
 * Corpo com a capacidade de agregar outros corpos 
 * por diferentes tipos de junção
 * @author abraao
 *TODO: ALTERAR ATORES PARA ESTE TIPO DE CORPO
 */
public class SuperCorpo extends Body {
	private List<Joint> juncoes = new ArrayList<Joint>();
	private List<Body> corpos = new ArrayList<Body>();
	
	public SuperCorpo(DynamicShape shape, float m) {
		super(shape, m);
	}
	
	public SuperCorpo(Shape shape, float m) {
		super(shape,m);
	}

	public SuperCorpo(String name,DynamicShape shape, float m) {
		super(name,(Shape) shape,m);
	}
		
	public void addCorpoFixedJoint(Body corpo, boolean excluirColisao) {
		FixedJoint j = new FixedJoint(this, corpo);
		addCorpo(corpo, j, excluirColisao);
	}
	
	public void addCorpoBasicJoint(Body corpo, Vector2f ancora, boolean excluirColisao) {
		BasicJoint j = new BasicJoint(this, corpo, ancora);
		addCorpo(corpo, j, excluirColisao);
	}
	
	public void addCorpoElasticJoint(Body corpo, boolean excluirColisao) {
		ElasticJoint j = new ElasticJoint(this, corpo);
		addCorpo(corpo, j, excluirColisao);
	}	
	
	protected void addCorpo(Body corpo, Joint juncao, boolean excluirColisao) {
		corpos.add(corpo);
		juncoes.add(juncao);
		this.addExcludedBody(corpo);
		corpo.addExcludedBody(this);
	}

	public List<Joint> getJuncoes() {
		return juncoes;
	}

	public void setJuncoes(List<Joint> juncoes) {
		this.juncoes = juncoes;
	}

	public List<Body> getCorpos() {
		return corpos;
	}

	public void setCorpos(List<Body> corpos) {
		this.corpos = corpos;
	}
	
	@Override
	public void addExclusionKey(Object exclusionKey) {
		super.addExclusionKey(exclusionKey);
		for(Body corpo : corpos) {
			corpo.addExclusionKey(exclusionKey);
		}
	}
	
	@Override
	public void setExclusionKey(Object exclusionKey) {
		super.setExclusionKey(exclusionKey);
		for(Body corpo : corpos) {
			corpo.setExclusionKey(exclusionKey);
		}			
	}
	
	@Override
	public void setSecondExclusionKey(Object secondExclusionKey) {
		super.setSecondExclusionKey(secondExclusionKey);
		for(Body corpo : corpos) {
			corpo.setSecondExclusionKey(secondExclusionKey);
		}	
	}
	
	@Override
	public void setThirdExclusionKey(Object thirdExclusionKey) {
		super.setThirdExclusionKey(thirdExclusionKey);
		for(Body corpo : corpos) {
			corpo.setThirdExclusionKey(thirdExclusionKey);
		}	
	}
	
	@Override
	public strictfp void setCanColide(boolean canColide) {
		super.setCanColide(canColide);
		for(Body corpo : corpos) {
			corpo.setCanColide(canColide);
		}		
	}
	
	@Override
	public void addExcludedBody(Body other) {
		super.addExcludedBody(other);
		for(Body corpo : corpos) {
			corpo.addExcludedBody(other);
		}
	}
		
	public void moveAll(float x, float y) {		
		super.move(x, y);
		
		for(Body corpo : corpos) {
			corpo.move(x, y);
		}
	}
	
	@Override
	public void setFriction(float friction) { 
		super.setFriction(friction);
		for(Body corpo : corpos) {
			corpo.setFriction(friction);
		}		
	}
	
	@Override
	public void setMaxVelocX(float maxVelocX) {
		super.setMaxVelocX(maxVelocX);
		for(Body corpo : corpos) {
			corpo.setMaxVelocX(maxVelocX);
		}				
	}
	
	@Override
	public void setMaxVelocY(float maxVelocY) {
		super.setMaxVelocY(maxVelocY);
		for(Body corpo : corpos) {
			corpo.setMaxVelocX(maxVelocY);
		}				
	}	
}
