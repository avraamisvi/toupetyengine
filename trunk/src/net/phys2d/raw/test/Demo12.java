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
import net.phys2d.raw.shapes.Circle;

/**
 * A simple demo with so flat blocks falling
 * 
 * @author Kevin Glass
 */
public class Demo12 extends AbstractDemo {
	/**
	 * Create the demo
	 */
	public Demo12() {
		super("Phys2D Demo 12");
	}

	/**
	 * @see net.phys2d.raw.test.AbstractDemo#init(net.phys2d.raw.World)
	 */
	protected void init(World world) {
		Body body1 = new StaticBody("Ground1", new Box(400.0f, 20.0f));
		body1.setPosition(250.0f, 400);
		world.add(body1);
		Body body3 = new StaticBody("Ground2", new Box(200.0f, 20.0f));
		body3.setPosition(250.0f, 100);
		world.add(body3);
		
		Body swing = new Body("Swing", new Circle(10), 50);
		swing.setPosition(160.0f, 300);
		world.add(swing);
		Body swing2 = new Body("Swing", new Circle(10), 50);
		swing2.setPosition(340.0f, 300);
		world.add(swing2);
		Body swing3 = new Body("Swing", new Box(250.0f, 10.0f), 50);
		swing3.setPosition(250.0f, 285);
		swing3.setFriction(4.0f);
		world.add(swing3);
		
		Body box = new Body("Resting", new Box(30,30), 1);
		box.setPosition(250.0f, 200);
		box.setRotation(0.15f);
		world.add(box);
		
		BasicJoint j1 = new BasicJoint(body3, swing, new Vector2f(160,110));
		world.add(j1);
		BasicJoint j2 = new BasicJoint(body3, swing2, new Vector2f(340,110));
		world.add(j2);
		BasicJoint j3 = new BasicJoint(swing, swing3, new Vector2f(160,300));
		world.add(j3);
		BasicJoint j4 = new BasicJoint(swing2, swing3, new Vector2f(340,300));
		world.add(j4);
		
		swing.adjustVelocity(new Vector2f(-100,0));
	}
	
	/**
	 * Entry point for tetsing
	 * 
	 * @param argv The arguments to the test
	 */
	public static void main(String[] argv) {
		Demo12 demo = new Demo12();
		demo.start();
	}
}
