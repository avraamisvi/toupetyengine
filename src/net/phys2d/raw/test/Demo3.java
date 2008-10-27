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
import net.phys2d.raw.BasicJoint;
import net.phys2d.raw.Body;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;

/**
 * Pendulum test
 * 
 * @author Kevin Glass
 */
public class Demo3 extends AbstractDemo {
	/** The controllable body */
	private Body b2;
	
	/**
	 * Create a new demo
	 */
	public Demo3() {
		super("Phys2D D3 - Hit space");
	}

	/**
	 * @see net.phys2d.raw.test.AbstractDemo#keyHit(char)
	 */
	public void keyHit(char c) {
		super.keyHit(c);
		
		if (c == ' ') {
			b2.addForce(new Vector2f(-100000,0));
		}
	}
	
	/**
	 * @see net.phys2d.raw.test.AbstractDemo#init(net.phys2d.raw.World)
	 */
	protected void init(World world) {
		Body b1 = new StaticBody("Anchor", new Box(500.0f, 20.0f));
		b1.setFriction(0.2f);
		b1.setPosition(250.0f, 100);
		b1.setRotation(0);
		world.add(b1);

		for (int i=0;i<9;i++) {
			float size = 10;
			if (i == 0) {
				size = 15;
			}
			Body body = new Body(new Box(size, size), i == 0 ? 100.0f : 10.0f);
			body.setFriction(0.4f);
			body.setPosition(170.0f + (i*20), 171.0f);
			body.setRotation(0.0f);
			world.add(body);
	
			if (i == 0) {
				b2 = body;
			}
			
			BasicJoint j = new BasicJoint(b1, body, new Vector2f(170.0f + (i*20), 111.0f));
			world.add(j);
		}
	}

	/**
	 * Entry point for tetsing
	 * 
	 * @param argv The arguments to the test
	 */
	public static void main(String[] argv) {
		Demo3 demo = new Demo3();
		demo.start();
	}
}
