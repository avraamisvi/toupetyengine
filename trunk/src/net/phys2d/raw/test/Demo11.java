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

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.shapes.Circle;
import net.phys2d.raw.shapes.DynamicShape;

/**
 * A stacking demonstration with balls!
 * 
 * @author Kevin Glass
 */
public class Demo11 extends AbstractDemo {
	/** The world in which the simulation takes place */
	private World world;
	
	/**
	 * Create the demo
	 */
	public Demo11() {
		super("Ball/Stack Demo - Hit space");
	}

	/**
	 * @see net.phys2d.raw.test.AbstractDemo#keyHit(char)
	 */
	protected void keyHit(char c) {
		super.keyHit(c);
		
		if (c == ' ') {
			Body body2 = new Body("Mover1", new Circle(20), 300.0f);
			body2.setPosition(-50, (float) (((Math.random() * 50) + 150)));
			world.add(body2);
			body2.adjustAngularVelocity(1);
			body2.adjustVelocity(new Vector2f(200,(float) (Math.random() * 200)));
		}
	}
	
	/**
	 * @see net.phys2d.raw.test.AbstractDemo#init(net.phys2d.raw.World)
	 */
	protected void init(World world) {
		this.world = world;
		
		Body body1 = new StaticBody("Ground1", new Box(400.0f, 20.0f));
		body1.setPosition(250.0f, 400);
		body1.setFriction(1);
		world.add(body1);
		
		for (int y=0;y<5;y++) {
			int xbase = 250 - (y * 21);
			for (int x=0;x<y+1;x++) {
				DynamicShape shape = new Box(40,40);
				if ((x == 1) && (y == 2)) {
					shape = new Circle(19);
				}
				if ((x == 1) && (y == 4)) {
					shape = new Circle(19);
				}
				if ((x == 3) && (y == 4)) {
					shape = new Circle(19);
				}
				Body body2 = new Body("Mover1", shape, 100.0f);
				body2.setPosition(xbase + (x * 42), y*45);
				world.add(body2);
			}
		}
	}
	
	/**
	 * Entry point for tetsing
	 * 
	 * @param argv The arguments to the test
	 */
	public static void main(String[] argv) {
		Demo11 demo = new Demo11();
		demo.start();
	}
}
