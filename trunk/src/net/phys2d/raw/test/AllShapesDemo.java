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

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.StaticBody;
import net.phys2d.raw.World;
import net.phys2d.raw.shapes.Box;
import net.phys2d.raw.shapes.Circle;
import net.phys2d.raw.shapes.ConvexPolygon;
import net.phys2d.raw.shapes.Line;
import net.phys2d.raw.shapes.Polygon;

/**
 * A test to show odd shapes interacting
 * 
 * @author gideon
 */
public class AllShapesDemo extends AbstractDemo  {
	/** The world in which the simulation takes place */
	private World world;
	
	/**
	 * Create a new demo instance
	 */
	public AllShapesDemo() {
		super("All Shapes Demo");
	}
	
	/** A local random number generator */
	private Random random = new Random();
	
	/**
	 * @see net.phys2d.raw.test.AbstractDemo#keyHit(char)
	 */
	protected void keyHit(char c) {
		super.keyHit(c);
		
		Body newBody = null;
		
		if (c == 's') {		
			Vector2f[] circleVerts = new Vector2f[30];
			float[] radius = {20,10};
			for( int i = 0; i < 30; i++ ) {
				float angle = (float) (3*4 * i * Math.PI/180);
				circleVerts[i] = new Vector2f(
						(float) (Math.cos(angle) * radius[i%2]), 
						(float) (Math.sin(angle) * radius[i%2]));
			}
			Polygon circlePolygon = new Polygon(circleVerts);
			newBody = new Body(circlePolygon, 4);
		} else if ( c == 'w' ) {
			newBody = new Body(new Circle(15), 2);
		} else if ( c == 'l' ) {
			newBody = new Body(new Line(0,-50,0,50), 1f);
		} else if ( c == 't' ) {
			Vector2f[] triangleVerts = {new Vector2f(-20, -20), new Vector2f(20,-20), new Vector2f(20,20)};
			ConvexPolygon trianglePolygon = new ConvexPolygon(triangleVerts);
			newBody = new Body(trianglePolygon, 3);
		} else if ( c == 'b' ) {
			newBody = new Body(new Box(20,30), 3);
		} else {
			return;
		}
		
		newBody.setPosition(250, 150);
		newBody.setRotation((float) (random.nextFloat() * 2 * Math.PI));
		world.add(newBody);
		
	}
	
	/**
	 * @see net.phys2d.raw.test.AbstractDemo#renderGUI(java.awt.Graphics2D)
	 */
	protected void renderGUI(Graphics2D g) {
		g.setColor(Color.black);
		g.drawString("S - Drop a star",385,70);
		g.drawString("T - Drop a triangle",385,90);
		g.drawString("W - Drop a wheel",385,110);
		g.drawString("L - Drop a line",385, 130);
		g.drawString("B - Drop a box",385, 150);

		super.renderGUI(g);
//		
//		g.setFont(g.getFont().deriveFont(Font.BOLD));
//		g.drawString(currentDemo.getTitle(),300,470);
	}
	
	/**
	 * Entry point for tetsing
	 * 
	 * @param argv The arguments to the test
	 */
	public static void main(String[] argv) {
		AllShapesDemo demo = new AllShapesDemo();
		demo.start();
	}

	/**
	 * @see net.phys2d.raw.test.AbstractDemo#init(net.phys2d.raw.World)
	 */
	protected void init(World world) {
		this.world = world;
		
//		Vector2f[] groundVerts = {new Vector2f(-200, -10), new Vector2f(200,-10), new Vector2f(200,10), new Vector2f(-200,10)};
//		ConvexPolygon groundBox = new ConvexPolygon(groundVerts);
//		Box groundBox = new Box(400,20);
//		Body ground = new StaticBody("ground", groundBox);
//		ground.setPosition(250, 450);
//		world.add(ground);
		
//		Vector2f[] triangleVerts = {new Vector2f(-20, -20), new Vector2f(20,-20), new Vector2f(20,20)};
//		ConvexPolygon trianglePolygon = new ConvexPolygon(triangleVerts);
//		Body triangle = new Body("triangle", trianglePolygon, 3);
//		triangle.setPosition(240, 155);
////		triangle.setBitmask(0l);
//		world.add(triangle);
//		
//		Body other = new Body("Line", new Line(0,-50,0,50), 1);
//		other.setPosition(220,110);
//		other.setRotation(-0.5f);
//		world.add(other);
//		world.setGravity(0, 0);
		
		
//		{
//		Line line3 = new Line(100, -100);
//		Body ground3 = new StaticBody("line", line3);
//		ground3.setPosition(310, 470);
//		world.add(ground3);
//		
//		Vector2f[] triangleVerts = {new Vector2f(-20, -20), new Vector2f(20,-20), new Vector2f(20,20)};
//		ConvexPolygon trianglePolygon = new ConvexPolygon(triangleVerts);
//		Body triangle = new Body("triangle", trianglePolygon, 3);
//		triangle.setPosition(361, 420);
//		triangle.setRotation(-0.5f * (float) Math.PI);
////		triangle.setBitmask(0l);
//		world.add(triangle);
//		}
//		
//		{
//		Line line2 = new Line(100, 0);
//		Body ground2 = new StaticBody("line", line2);
//		ground2.setPosition(140, 420);
//		world.add(ground2);
//		
//		Vector2f[] triangleVerts = {new Vector2f(-20, -20), new Vector2f(20,-20), new Vector2f(20,20)};
//		ConvexPolygon trianglePolygon = new ConvexPolygon(triangleVerts);
//		Body triangle = new Body("triangle", trianglePolygon, 3);
//		triangle.setPosition(190, 395);
//		triangle.setRotation(-1.25f * (float) Math.PI);
////		triangle.setBitmask(0l);
//		world.add(triangle);
//		
//		}
//		world.setGravity(0, 0);
		
//		{
//		Line line2 = new Line(268.1871f, 380.28757f, 192.80246f, 314.5825f);
//		Body ground2 = new StaticBody("line", line2);
//		ground2.setPosition(0, 0);
//		world.add(ground2);
//		
//		Vector2f[] triangleVerts = {new Vector2f(287.69855f, 397.28024f), new Vector2f(247.7926f, 400.0214f), new Vector2f(245.05144f,360.11545f)};
//		ConvexPolygon trianglePolygon = new ConvexPolygon(triangleVerts);
//		Body triangle = new Body("triangle", trianglePolygon, 3);
//		triangle.setPosition(0, 0);
////		triangle.setRotation(-1.25f * (float) Math.PI);
////		triangle.setBitmask(0l);
//		world.add(triangle);
//		
//		}
//		world.setGravity(0, 0);
		
		
		
		
//		Line line1 = new Line(181.90248f, 290.56305f, 236.82835f, 374.1283f);
//		Body ground1 = new Body("line", line1, 1);
//		ground1.setPosition(0, 0);
//		world.add(ground1);
//		
//		Line line2 = new Line(156.98477f, 288.30258f, 256.5289f, 297.84006f);
//		Body ground2 = new Body("line", line2, 1);
//		ground2.setPosition(0, 0);
//		world.add(ground2);
//		world.setGravity(0, 0);
////		ground2.setBitmask(0l);

		
		
		Line line1 = new Line(300, 0);
		Body ground1 = new StaticBody("line", line1);
		ground1.setPosition(100, 400);
		world.add(ground1);
		
		Line line2 = new Line(-100, -100);
		Body ground2 = new StaticBody("line", line2);
		ground2.setPosition(140, 420);
		world.add(ground2);
		
		Line line3 = new Line(100, -100);
		Body ground3 = new StaticBody("line", line3);
		ground3.setPosition(360, 420);
		world.add(ground3);
		
		
		
//		Vector2f[] boxVerts = {new Vector2f(-10, -10), new Vector2f(10,-10), new Vector2f(10,10), new Vector2f(-10,10)};
//		ConvexPolygon boxPolygon = new ConvexPolygon(boxVerts);
//		Body box = new Body("box", boxPolygon, 1);
//		box.setPosition(250, 50);
//		box.setRotation((float) (1f/4f * Math.PI));
//		world.add(box);

		
//		{
//			Vector2f[] triangleVerts = {new Vector2f(269.5173f, 108.228836f), new Vector2f(307.98932f, 97.278854f), new Vector2f(318.9393f,135.75089f)};
//			ConvexPolygon trianglePolygon = new ConvexPolygon(triangleVerts);
//			Body triangle = new Body("triangleA", trianglePolygon, 2);
//			triangle.setPosition(0, 0);
////			triangle.setRotation(1.75f * (float) Math.PI );
////			triangle.setBitmask(0l);
//			world.add(triangle);
//		}
//		{
//			Vector2f[] triangleVerts = {new Vector2f(268.3686f, 107.55475f), new Vector2f(303.3081f,127.02861f), new Vector2f(283.83423f,161.96811f)};
//			ConvexPolygon trianglePolygon = new ConvexPolygon(triangleVerts);
//			Body triangle = new Body("triangleB", trianglePolygon, 2);
//			triangle.setPosition(0, 0);
////			triangle.setRotation(-1.25f * (float) Math.PI);
//			world.add(triangle);
//		}
//		world.setGravity(0, 0);
		
		
//		{
//			Vector2f[] triangleVerts = {new Vector2f(-20, -20), new Vector2f(20,-20), new Vector2f(20,20)};
//			ConvexPolygon trianglePolygon = new ConvexPolygon(triangleVerts);
//			Body triangle = new Body("triangleA", trianglePolygon, 2);
//			triangle.setPosition(210, 230);
//			triangle.setRotation(1.75f * (float) Math.PI );
////			triangle.setBitmask(0l);
//			world.add(triangle);
//		}
//		{
//			Vector2f[] triangleVerts = {new Vector2f(-20, -20), new Vector2f(20,-20), new Vector2f(20,20)};
//			ConvexPolygon trianglePolygon = new ConvexPolygon(triangleVerts);
//			Body triangle = new Body("triangleB", trianglePolygon, 2);
//			triangle.setPosition(200, 210);
//			triangle.setRotation(-1.25f * (float) Math.PI);
//			world.add(triangle);
//		}
//		world.setGravity(0, 0);
		
		
//		{
//			Vector2f[] triangleVerts = {new Vector2f(284.21323f, 400.01886f), new Vector2f(304.27252f,365.41214f), new Vector2f(338.87924f,385.47144f)};
//			ConvexPolygon trianglePolygon = new ConvexPolygon(triangleVerts);
//			Body triangle = new Body("triangle", trianglePolygon, 2);
//			triangle.setPosition(-100, 0);
////			triangle.setRotation((float) Math.PI + 0.4f);
////			triangle.setBitmask(0l);
//			world.add(triangle);
//		}
//		{
//			Vector2f[] triangleVerts = {new Vector2f(227.86116f, 400.01065f), new Vector2f(256.12158f, 371.70255f), new Vector2f(284.4297f, 399.96298f)};
//			ConvexPolygon trianglePolygon = new ConvexPolygon(triangleVerts);
//			Body triangle = new Body("triangle", trianglePolygon, 2);
//			triangle.setPosition(-100, 0);
////			triangle.setRotation((float) Math.PI);
//			world.add(triangle);
//		}
//		world.setGravity(0, 0);
//		
//		
//			
//		{
//			Vector2f[] triangleVerts = {new Vector2f(270.65256f, 400.02f), new Vector2f(295.5896f, 368.7447f), new Vector2f(326.8649f, 393.68173f)};
//			ConvexPolygon trianglePolygon = new ConvexPolygon(triangleVerts);
//			Body triangle = new Body("triangleA", trianglePolygon, 2);
//			triangle.setPosition(0, 0);
////			triangle.setBitmask(0l);
//			world.add(triangle);
//		}
//		{
//			Vector2f[] triangleVerts = {new Vector2f(221.1404f, 372.66193f), new Vector2f(259.576f, 361.58472f), new Vector2f(270.65323f, 400.02032f)};
//			ConvexPolygon trianglePolygon = new ConvexPolygon(triangleVerts);
//			Body triangle = new Body("triangleB", trianglePolygon, 2);
//			triangle.setPosition(0, 0);
//			world.add(triangle);
//		}
//		world.setGravity(0, 0);

		
		
		
//		Body other = new Body("Faller", new Circle(10), 1);
//		other.setPosition(200,50);
//		other.setRotation(-0.5f);
//		world.add(other);
//		other = new Body("Faller", new Circle(10), 1);
//		other.setPosition(225,50);
//		other.setRotation(-0.5f);
//		world.add(other);
//		other = new Body("Faller", new Circle(10), 1);
//		other.setPosition(275,50);
//		other.setRotation(-0.5f);
//		world.add(other);
//		other = new Body("Faller", new Circle(10), 1);
//		other.setPosition(300,50);
//		other.setRotation(-0.5f);
//		world.add(other);
//		
//		Vector2f[] nonConvexPoly = {new Vector2f(-20,-10), new Vector2f(20,-10), new Vector2f(10,0), new Vector2f(20,10), new Vector2f(-20,10), new Vector2f(-10,0)};
//		Polygon poly = new Polygon(nonConvexPoly);
//		Body nonConvexBody = new Body("poly", poly, 3);
//		nonConvexBody.setPosition(350, 100);
//		world.add(nonConvexBody);
		
//		Body other = new Body("Faller", new Circle(10), 1);
//		other.setPosition(250,60);
//		other.setRotation(-0.5f);
//		world.add(other);
		
//		world.setGravity(0, 0);
	}

}
