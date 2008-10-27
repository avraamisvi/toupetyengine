/*
 * Phys2D - a 2D physics engine based on the work of Erin Catto.
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
import net.phys2d.raw.shapes.ConvexPolygon;
import net.phys2d.raw.shapes.Polygon;

/**
 * A demo showing gears inteacting in a most un-believeable way
 * 
 * @author gideon
 */
public class GearDemo extends AbstractDemo {
	/** A wheel dropped into the scene */
	private Body wheel;
	
	/**
	 * Create a new gears demo instance
	 */
	public GearDemo() {
		super("Gears Demo");
	}

	/**
	 * @see net.phys2d.raw.test.AbstractDemo#init(net.phys2d.raw.World)
	 */
	protected void init(World world) {
		Vector2f[] groundVerts = {new Vector2f(-200, -10), new Vector2f(200,-10), new Vector2f(200,10), new Vector2f(-200,10)};
		ConvexPolygon groundBox = new ConvexPolygon(groundVerts);
		Body ground = new StaticBody("ground", groundBox);
		ground.setPosition(250, 50);
		world.add(ground);
		
		{
			int noVerts = 40;
			Vector2f[] circleVerts = new Vector2f[noVerts];
			float[] radius = {50,42,42,50};
			for( int i = 0; i < noVerts; i++ ) {
				float angle = (float) (i* 2 * Math.PI/noVerts);
				circleVerts[i] = new Vector2f(
						(float) (Math.cos(angle) * radius[i%radius.length]), 
						(float) (Math.sin(angle) * radius[i%radius.length]));
			}
			Polygon circlePolygon = new Polygon(circleVerts);
			Body circle = new Body("circle", circlePolygon, 2);
			circle.setPosition(250, 150);
			world.add(circle);
			
			BasicJoint joint = new BasicJoint(ground, circle, new Vector2f(circle.getPosition()));
			world.add(joint);
		}
		{
			int outerCircleVerts = 30;
			int noVerts = 120;
			Vector2f[] circleVerts = new Vector2f[outerCircleVerts+1 + noVerts+1];
			for( int i = 0; i <= outerCircleVerts; i++ ) {
				float angle = (float) (i* 2 * Math.PI/outerCircleVerts);
				circleVerts[i] = new Vector2f(
						(float) (Math.cos(angle) * 150), 
						(float) (Math.sin(angle) * 150));
			}
			float[] radius = {140, 133, 133, 140};
			for( int i = 0; i <= noVerts; i++ ) {
				float angle = (float) (i* 2 * Math.PI/noVerts);
				circleVerts[outerCircleVerts+1 + noVerts-i] = new Vector2f(
						(float) (Math.cos(angle) * radius[i%radius.length]), 
						(float) (Math.sin(angle) * radius[i%radius.length]));
			}
			Polygon circlePolygon = new Polygon(circleVerts);
			Body circle = new Body("circle", circlePolygon, 30);
			circle.setPosition(250, 220);
			world.add(circle);
		}
		
		{
			int noVerts = 20;
			Vector2f[] circleVerts = new Vector2f[noVerts];
			float[] radius = {30,20,20,30};
			for( int i = 0; i < noVerts; i++ ) {
				float angle = (float) (i* 2 * Math.PI/noVerts);
				circleVerts[i] = new Vector2f(
						(float) (Math.cos(angle) * radius[i%radius.length]), 
						(float) (Math.sin(angle) * radius[i%radius.length]));
			}
			Polygon circlePolygon = new Polygon(circleVerts);
			Body circle = new Body("circle", circlePolygon, 2);
			circle.setPosition(250, 300);
			world.add(circle);
			
			Vector2f[] nonConvexPoly = {new Vector2f(-20,-10), new Vector2f(20,-10), new Vector2f(10,0), new Vector2f(20,10), new Vector2f(-20,10), new Vector2f(-10,0)};
			Polygon poly = new Polygon(nonConvexPoly);
			Body nonConvexBody = new Body("poly", poly, 25);
			nonConvexBody.setPosition(250, 400);
			world.add(nonConvexBody);
			
			BasicJoint joint = new BasicJoint(circle,nonConvexBody, new Vector2f(circle.getPosition()));
			world.add(joint);
			
			wheel = circle;
			circle.setRotDamping(10);
		}

	}

	/**
	 * @see net.phys2d.raw.test.AbstractDemo#update()
	 */
	protected void update() {
		wheel.setTorque(20000);
	}
	
	/**
	 * Entry point to run this test stand alone
	 * 
	 * @param argv The arguments passed into the demo
	 */
	public static void main(String[] argv) {
		GearDemo demo = new GearDemo();
		demo.start();
	}

}
