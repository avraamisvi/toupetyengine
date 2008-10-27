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
 * Rope bridge demo
 * 
 * @author Kevin Glass
 */
public class Demo5 extends AbstractDemo {
	/** The joint to break */
	private BasicJoint joint;
	/** The world in which the demo takes place */
	private World world;
	
	/**
	 * Create a new demo
	 */
	public Demo5() {
		super("Bridge Demo - Hit Space");
	}
	
	/**
	 * @see net.phys2d.raw.test.AbstractDemo#keyHit(char)
	 */
	protected void keyHit(char c) {
		if (c == ' ') {
			System.out.println("Removing joint");
			world.remove(joint);
		}
	}
	
	/**
	 * @see net.phys2d.raw.test.AbstractDemo#init(net.phys2d.raw.World)
	 */
	protected void init(World world) {
		this.world = world;
		float relax = 0.8f;
		
		
		Body bodyX = new Body("Ground1", new Box(20.0f, 20.0f), 100);
		bodyX.setPosition(250.0f, 50);
		world.add(bodyX);
		
		Body body1 = new StaticBody("Ground1", new Box(500.0f, 20.0f));
		body1.setPosition(250.0f, 400);
		world.add(body1);
		
		Body body2 = new Body("First", new Box(40.0f, 10.0f), 500);
		body2.setFriction(0.2f);
		body2.setPosition(80.0f, 300);
		world.add(body2);
		
		BasicJoint j = new BasicJoint(body1,body2,new Vector2f(40,300));
		j.setRelaxation(relax);
		world.add(j);
		
		int i;
		for (i=1;i<8;i++) {
			Body body3 = new Body("Teeter",new Box(40.0f, 10.0f), 500);
			body3.setFriction(0.2f);
			body3.setPosition(80.0f+(i*45), 300);
			world.add(body3);
			
			BasicJoint j2 = new BasicJoint(body2,body3,new Vector2f(65+(i*45),300));
			j2.setRelaxation(relax);
			world.add(j2);
			if (i == 4) {
				joint = j2;
			}
			
			body2 = body3;
		}

		BasicJoint j3 = new BasicJoint(body1,body2,new Vector2f(80+(i*45),300));
		j3.setRelaxation(relax);
		world.add(j3);
	}

	/**
	 * Entry point for tetsing
	 * 
	 * @param argv The arguments to the test
	 */
	public static void main(String[] argv) {
		Demo5 demo = new Demo5();
		demo.start();
	}
}
