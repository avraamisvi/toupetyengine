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

/**
 * Pool triangle
 * 
 * @author Kevin Glass
 */
public class Demo13 extends AbstractDemo {
	/**
	 * Create the demo
	 */
	public Demo13() {
		super("Phys2D Demo 13");
	}

	/**
	 * @see net.phys2d.raw.test.AbstractDemo#init(net.phys2d.raw.World)
	 */
	protected void init(World world) {
		world.setGravity(0,0);
		
		for (int y=0;y<4;y++) {
			for (int x=0;x<y+1;x++) {
				Body body1 = new Body("Ball"+x+","+y, new Circle(20.0f), 1);
				//Body body1 = new Body("Ball"+x+","+y, new Box(40.0f,40.0f), 1);
				body1.setPosition(250.0f+(x*40)-(y*20), 200+(y*35f));
				body1.setRestitution(1.0f);
				body1.setFriction(0);
				world.add(body1);
			}
		}

		Body body5 = new StaticBody("Ground1", new Box(20.0f, 500.0f));
		body5.setPosition(20.0f, 250);
		body5.setRestitution(1.0f);
		world.add(body5);
		Body body6 = new StaticBody("Ground2", new Box(20.0f, 500.0f));
		body6.setPosition(480.0f, 250);
		body6.setRestitution(1.0f);
		world.add(body6);
		Body body1 = new StaticBody("Ground3", new Box(500.0f, 20.0f));
		body1.setPosition(250.0f, 480);
		body1.setRestitution(1.0f);
		world.add(body1);
		
		Body body2 = new Body("Cue", new Circle(20.0f),1);
		//Body body2 = new Body("Cue", new Box(40.0f,40.0f),1);
		body2.setPosition(250,0);
		body2.adjustVelocity(new Vector2f(0,100));
		body2.setRestitution(1.0f);
		body2.setFriction(0);
		world.add(body2);
	}
	
	/**
	 * Entry point for tetsing
	 * 
	 * @param argv The arguments to the test
	 */
	public static void main(String[] argv) {
		Demo13 demo = new Demo13();
		demo.start();
	}
}
