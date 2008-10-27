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
import net.phys2d.raw.shapes.Line;

/**
 * Edge test
 * 
 * @author Kevin Glass
 */
public class Demo20 extends AbstractDemo {
	/** The body we're pushing off the edge */
	private Body body2;
	
	/**
	 * Create the demo
	 */
	public Demo20() {
		super("Phys2D Edge test");
	}

	/**
	 * @see net.phys2d.raw.test.AbstractDemo#init(net.phys2d.raw.World)
	 */
	protected void init(World world) {
		Body body1 = new StaticBody("Ground1", new Line(-300.0f, 0, false, true));
		body1.setPosition(400, 300);
		world.add(body1);
		Body body3 = new StaticBody("Ground2", new Line(0,50, false, true));
		body3.setPosition(100, 301);
		world.add(body3);
		
		body2 = new Body("Mover1", new Box(40,40), 10.0f);
		body2.setPosition(150.0f, 280.0f);
		world.add(body2);
		body2 = new Body("Mover1", new Box(40,40), 10.0f);
		body2.setPosition(190.0f, 280.0f);
		world.add(body2);
		body2 = new Body("Mover1", new Box(40,40), 10.0f);
		body2.setPosition(230.0f, 280.0f);
		world.add(body2);
		body2 = new Body("Mover1", new Box(40,40), 10.0f);
		body2.setPosition(280.0f, 280.0f);
		world.add(body2);
		body2 = new Body("Mover1", new Circle(20), 10.0f);
		body2.setPosition(380.0f, 280.0f);
		world.add(body2);
	}
	
	/**
	 * @see net.phys2d.raw.test.AbstractDemo#update()
	 */
	protected void update() {
		body2.addForce(new Vector2f(-100,0));
	}
	
	/**
	 * Entry point for tetsing
	 * 
	 * @param argv The arguments to the test
	 */
	public static void main(String[] argv) {
		Demo20 demo = new Demo20();
		demo.start();
	}
}
