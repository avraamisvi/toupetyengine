/*
 * Phys2D - a 2D physics engine based on the work of Erin Catto. The
 * original source remains:
 * 
 * Copyright (c) 2006 Erin Catto http://www.gphysics.com
 * 
 * This source is provided under the terms of the BSD License.
 * 
 * Copyright (c) 2006, Phys2D
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or 
 * without modification, are permitted provided that the following 
 * conditions are met:
 * 
 *  * Redistributions of source code must retain the above 
 *    copyright notice, this list of conditions and the 
 *    following disclaimer.
 *  * Redistributions in binary form must reproduce the above 
 *    copyright notice, this list of conditions and the following 
 *    disclaimer in the documentation and/or other materials provided 
 *    with the distribution.
 *  * Neither the name of the Phys2D/New Dawn Software nor the names of 
 *    its contributors may be used to endorse or promote products 
 *    derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND 
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, 
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS 
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, 
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY 
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR 
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT 
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY 
 * OF SUCH DAMAGE.
 */
package net.phys2d.raw.test;

import java.awt.event.KeyEvent;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.FixedJoint;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.shapes.Circle;

/**
 * Pendulum test
 * 
 * @author Kevin Glass
 */
public class MeusTestes extends AbstractDemo {
	/** The controllable body */
	private Body b2;
	private Body body;
	
	/**
	 * Create a new demo
	 */
	public MeusTestes() {
		super("Phys2D D3 - Hit space");
	}

	/**
	 * @see net.phys2d.raw.test.AbstractDemo#keyHit(char)
	 */
	public void keyHit(char c) {
		super.keyHit(c);
		
		if (c == ' ') {
			body.addForce(new Vector2f(-100000,0));
		}
	}

	protected void keyPressed(int code) {
		super.keyPressed(code);
		
		if(code == KeyEvent.VK_RIGHT) {
			if(body.getVelocity().getX() < 32f)
				body.adjustVelocity(new Vector2f(8f, 0));
		} else if(code == KeyEvent.VK_LEFT) {
			if(body.getVelocity().getX() > -32f)
				body.adjustVelocity(new Vector2f(-8f, 0));
		}
	}	
	
	/**
	 * @see net.phys2d.raw.test.AbstractDemo#init(net.phys2d.raw.World)
	 */
	protected void init(World world) {
		Body b1 = new StaticBody("Anchor", new Box(600.0f, 10.0f));
		b1.setFriction(1f);
		b1.setPosition(250.0f, 400);
		b1.setRotation(0);
		
		Body b2 = new StaticBody("Anchor2", new Box(400.0f, 5.0f));
		b2.setFriction(1f);
		b2.setPosition(450.0f, 397);
		b2.setRotation(0);	
		
		body = new Body("Quadrado", new Box(52, 52), 100.0f);
		body.setFriction(0.6f);
		body.setPosition(250, 100);
		body.setRotation(0.0f);
		body.setRotatable(false);
		
		Body circulo = new Body("Circulo", new Circle(body.getShape().getBounds().getWidth()/2.5f), 200.0f);
		circulo.setRotatable(false);
		circulo.setPosition(250.0f, 100f);
		circulo.setFriction(0.6f);
		
		body.addExcludedBody(circulo);
		circulo.addExcludedBody(body);
		b2.addExcludedBody(b1);
		b1.addExcludedBody(b2);
		
		world.add(b1);
		world.add(b2);
		world.add(circulo);
		world.add(body);
		
		
//		Body body = new Body(new Box(size, size), i == 0 ? 100.0f : 10.0f);
//		body.setFriction(0.4f);
//		body.setPosition(170.0f + (i*20), 171.0f);
//		body.setRotation(0.0f);
//		world.add(body);
	
			
			FixedJoint j = new FixedJoint(body, circulo);
			//BasicJoint j = new BasicJoint(b1, circulo, new Vector2f(250.0f, 111.0f));
			world.add(j);
//		}
	}

	/**
	 * Entry point for tetsing
	 * 
	 * @param argv The arguments to the test
	 */
	public static void main(String[] argv) {
		MeusTestes demo = new MeusTestes();
		demo.start();
	}
}
