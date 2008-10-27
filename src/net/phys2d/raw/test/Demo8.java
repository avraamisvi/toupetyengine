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

import net.phys2d.raw.Body;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.shapes.Circle;

/**
 * A simple demo with balls falling
 * 
 * @author Kevin Glass
 */
public class Demo8 extends AbstractDemo {
	/**
	 * Create the demo
	 */
	public Demo8() {
		super("Phys2D Demo 8 - Balls");
	}

	/**
	 * @see net.phys2d.raw.test.AbstractDemo#init(net.phys2d.raw.World)
	 */
	protected void init(World world) {
		Body body1 = new StaticBody("Ground1", new Box(400.0f, 20.0f));
		body1.setPosition(250.0f, 400);
		world.add(body1);
		Body body1a = new StaticBody("Ground2", new Box(200.0f, 20.0f));
		body1a.setPosition(350.0f, 280);
		body1a.setRotation(-0.7f);
		world.add(body1a);
		Body body1b = new StaticBody("Ground1", new Box(20.0f, 400.0f));
		body1b.setPosition(20.0f, 200);
		world.add(body1b);
		
		Body body2 = new Body("Mover1", new Circle(25), 100.0f);
		body2.setPosition(300.0f, 4.0f);
		world.add(body2);
		//Body body3 = new Body("Mover2", new Box(25,25), 10.0f);
		Body body3 = new Body("Mover2", new Circle(25), 10.0f);
		body3.setRotation(0.3f);
		body3.setPosition(380.0f, 100.0f);
		world.add(body3);
	}
	
	/**
	 * Entry point for tetsing
	 * 
	 * @param argv The arguments to the test
	 */
	public static void main(String[] argv) {
		Demo8 demo = new Demo8();
		demo.start();
	}
}
