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
 * Dominos demo
 * 
 * @author Kevin Glass
 */
public class Demo6 extends AbstractDemo {
	/** The block to move */
	private Body ball;
	
	/**
	 * Create a simple demo
	 */
	public Demo6() {
		super("Demo 6 - Stuff! - hit space");
	}
	
	/**
	 * @see net.phys2d.raw.test.AbstractDemo#keyHit(char)
	 */
	protected void keyHit(char c) {
		if (c == ' ') {
			if (ball.getVelocity().length() == 0) {
				ball.addForce(new Vector2f(-2000000,0));
			}
		}
	}
	
	/**
	 * @see net.phys2d.raw.test.AbstractDemo#init(net.phys2d.raw.World)
	 */
	protected void init(World world) {
		this.world = world;
		
		Body body;
		BasicJoint j;
		
		Body base= new StaticBody("Ground1",new Box(500.0f, 20.0f));
		base.setPosition(250.0f, 400);
		world.add(base);
		
		body = new StaticBody("Ground2",new Box(250.0f, 20.0f));
		body.setPosition(225.0f, 200);
		body.setFriction(3.0f);
		world.add(body);
		
		// pendulum
		body = new StaticBody("Pen1",new Box(20.0f, 20.0f));
		body.setPosition(70.0f, 100);
		world.add(body);
		ball = new Body("Ball",new Box(10.0f, 10.0f), 1000);
		ball.setPosition(70.0f, 170);
		world.add(ball);
		
		j = new BasicJoint(body,ball,new Vector2f(70,110));
		world.add(j);
		
		// dominos
		for (int i=0;i<8;i++) {
			body = new Body("Domino "+i, new Box(10.0f, 40.0f), 10-i);
			body.setPosition(120.0f+(i*30), 170);
			world.add(body);
		}
		
		// ramp
		body = new StaticBody("Ground2",new Box(200.0f, 10.0f));
		body.setPosition(345.0f, 270);
		body.setRotation(-0.6f);
		body.setFriction(0);
		world.add(body);
		
		// teeter
		body = new Body("Teete",new Box(250.0f, 5.0f), 10);
		body.setPosition(250.0f, 360);
		//body.setFriction(3.0f);
		world.add(body);
		j = new BasicJoint(body,base,new Vector2f(250,360));
		world.add(j);
		
		// turner
		body = new Body("Turner", new Box(40.0f, 40.0f), 0.1f);
		body.setPosition(390.0f, 330);
		body.setFriction(0f);
		world.add(body);
		j = new BasicJoint(base,body,new Vector2f(390,335));
		world.add(j);
		Body top = new Body("Top",new Box(40.0f, 5.0f), 0.01f);
		top.setPosition(390.0f, 307.5f);
		top.setFriction(0f);
		world.add(top);
		j = new BasicJoint(top,body,new Vector2f(410,310));
		world.add(j);
	}

	/**
	 * Entry point for tetsing
	 * 
	 * @param argv The arguments to the test
	 */
	public static void main(String[] argv) {
		Demo6 demo = new Demo6();
		demo.start();
	}
}
