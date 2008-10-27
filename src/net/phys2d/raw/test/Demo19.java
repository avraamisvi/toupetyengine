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
import net.phys2d.raw.FixedJoint;
import net.phys2d.raw.Joint;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.shapes.Circle;

/**
 * Rejoing test
 * 
 * @author Kevin Glass
 */
public class Demo19 extends AbstractDemo {
	/**
	 * Create the demo
	 */
	public Demo19() {
		super("Phys2D rejoints");
	}

	/**
	 * @see net.phys2d.raw.test.AbstractDemo#init(net.phys2d.raw.World)
	 */
	protected void init(World world) {
		Body body1 = new StaticBody("Ground1", new Box(400.0f, 20.0f));
		body1.setPosition(250.0f, 300);
		world.add(body1);
		body1 = new StaticBody("Ground1", new Box(20.0f, 400.0f));
		body1.setPosition(40.0f, 150);
		world.add(body1);
		body1 = new StaticBody("Ground1", new Box(20.0f, 400.0f));
		body1.setPosition(460.0f, 150);
		world.add(body1);
		
		Body body2 = new Body("Mover1", new Circle(15), 10.0f);
		body2.setPosition(200.0f, 30.0f);
		world.add(body2);
		Body body5 = new Body("Mover1", new Circle(15), 10.0f);
		body5.setPosition(-20.0f, 80.0f);
		world.add(body5);
		Body body3 = new Body("Mover2", new Circle(15), 10.0f);
		body3.setPosition(300.0f, 50.0f);
		world.add(body3);
		Body body4 = new Body("Mover3", new Circle(15), 10.0f);
		body4.setPosition(250.0f, 70.0f);
		world.add(body4);
		
		Joint j = new FixedJoint(body2,body3);
		world.add(j);
		j = new FixedJoint(body3,body4);
		world.add(j);
		j = new FixedJoint(body2,body5);
		world.add(j);
	}
	
	/**
	 * Entry point for tetsing
	 * 
	 * @param argv The arguments to the test
	 */
	public static void main(String[] argv) {
		Demo19 demo = new Demo19();
		demo.start();
	}
}
