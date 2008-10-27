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
package net.phys2d.raw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JFrame;

import net.phys2d.raw.shapes.AABox;
import net.phys2d.raw.shapes.Circle;
import net.phys2d.raw.strategies.QuadSpaceStrategy;

/**
 * A reasonably crap test of the quad space strategy
 * 
 * @author Kevin Glass
 */
public class QuadSpaceTest extends JFrame implements CollisionContext {
	/** Indicates the next step is to create bodies */
	public static final int CREATE = 1;
	/** Indicates the next step is to collide bodies */
	public static final int COLLIDE = 2;
	
	/** The bodies to check against each other */
	private BodyList bodies = new BodyList();
	/** The current step in the test process */
	private int step = CREATE;
	/** The strategy we're testing */
	private QuadSpaceStrategy strat = new QuadSpaceStrategy(10,5);
	/** The spaces that have been generated */
	private ArrayList spaces;
	
	/**
	 * Create a new test instance
	 */
	public QuadSpaceTest() {
		super("Quad Space Test");
		
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				nextStep();
			}
		});
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setSize(600,600);
		setResizable(false);
		setVisible(true);
	}
	
	/**
	 * Move to the next step - done on mouse click
	 */
	private void nextStep() {
		if (step == CREATE) {
			System.out.println("Generating 100");
			for (int i=0;i<100;i++) {
				float size = (float) (10 + (Math.random() * 20));
				Body body = new Body(new Circle(size),1);
				body.setPosition((float) (Math.random() * 500)+50, (float) (Math.random() * 500)+50);
				bodies.add(body);
			}
			step = COLLIDE;
		} else if (step == COLLIDE) {
			System.out.println("Colliding");
			strat.collideBodies(this, bodies, 0);
			spaces = strat.getSpaces();
			
		}
		repaint(0);
	}
	
	/**
	 * @see java.awt.Component#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		for (int i=0;i<bodies.size();i++) {
			Body b = bodies.get(i);

			g.setColor(Color.black);
			if (spaces != null) {
				g.setColor(Color.red);
			}
			float x = b.getPosition().getX();
			float y = b.getPosition().getY();
			float rad = ((Circle) b.getShape()).getRadius();
			g.drawOval((int) (x-rad),(int) (y-rad),(int) (rad*2),(int) (rad*2));
		
			AABox box = b.getShape().getBounds();
			float width2 = box.getWidth() / 2;
			float height2 = box.getWidth() / 2;
			g.setColor(Color.lightGray);
			g.drawRect((int) (x-width2),(int) (y-height2),
						(int) box.getWidth(),(int) box.getHeight());
		}
		
		if (spaces != null) {
			for (int i=0;i<spaces.size();i++) {
				QuadSpaceStrategy.Space space = (QuadSpaceStrategy.Space) spaces.get(i);
				
				g.setColor(Color.blue);
				int x1 = (int) space.getX1();
				int x2 = (int) space.getX2();
				int y1 = (int) space.getY1();
				int y2 = (int) space.getY2();
				g.drawRect(x1,y1,(x2-x1),(y2-y1));
			}
		}
	}
	
	/**
	 * @see net.phys2d.raw.CollisionContext#resolve(net.phys2d.raw.BodyList, float)
	 */
	public void resolve(BodyList bodies, float dt) {
	}
	
	/**
	 * Entry point to our quad test
	 * 
	 * @param argv The arguments to the test
	 */
	public static void main(String[] argv) {
		new QuadSpaceTest();
	}

}
