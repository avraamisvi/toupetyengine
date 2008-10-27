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
import net.phys2d.raw.shapes.Line;

/**
 * Lines terrain
 * 
 * @author Kevin Glass
 */
public class Demo16 extends AbstractDemo {
	/** The box falling into the simulation */
	private Body box;
	
	/**
	 * Create the demo
	 */
	public Demo16() {
		super("Phys2D Demo 16 - Lines Terrain");
	}

	/**
	 * @see net.phys2d.raw.test.AbstractDemo#init(net.phys2d.raw.World)
	 */
	protected void init(World world) {
		Body land = new StaticBody("Line1", new Line(130,30));
		land.setPosition(-30,200);
		world.add(land);
		Body land2 = new StaticBody("Line2", new Line(50,50));
		land2.setPosition(100,230);
		world.add(land2);
		Body land3 = new StaticBody("Line3", new Line(100,20));
		land3.setPosition(150,280);
		world.add(land3);
		Body land4 = new StaticBody("Line4", new Line(100,80));
		land4.setPosition(250,300);
		world.add(land4);
		Body land5 = new StaticBody("Line5", new Line(100,0));
		land5.setPosition(350,380);
		world.add(land5);
		Body land6 = new StaticBody("Line6", new Line(10,-200));
		land6.setPosition(450,380);
		world.add(land6);
		Body land7 = new StaticBody("Line7", new Line(80,-10));
		land7.setPosition(460,180);
		world.add(land7);
		
		box = new Body("Faller", new Box(50,50), 1) {
			@Override
			public strictfp void adjustRotation(float delta) {
				if(Math.abs(getRotation() + delta) <= 0.5)
					super.adjustRotation(delta);//HACK ABRAAO
			}
		};
		
		box.setPosition(50,50);
		box.setRotation(-0.5f);
		
		world.add(box);
		Body other = new Body("Faller", new Circle(10), 1);
		other.setPosition(200,50);
		other.setRotation(-0.5f);
		world.add(other);
		other = new Body("Faller", new Circle(10), 1);
		other.setPosition(225,50);
		other.setRotation(-0.5f);
		world.add(other);
		other = new Body("Faller", new Circle(10), 1);
		other.setPosition(250,50);
		other.setRotation(-0.5f);
		world.add(other);
		other = new Body("Faller", new Circle(10), 1);
		other.setPosition(275,50);
		other.setRotation(-0.5f);
		world.add(other);
		other = new Body("Faller", new Circle(10), 1);
		other.setPosition(300,50);
		other.setRotation(-0.5f);
		world.add(other);
	}
	
	/**
	 * Entry point for tetsing
	 * 
	 * @param argv The arguments to the test
	 */
	public static void main(String[] argv) {
		Demo16 demo = new Demo16();
		demo.start();
	}
	/**
	 * @see net.phys2d.raw.test.AbstractDemo#update()
	 */
	protected void update() {
		super.update();
	}
}
