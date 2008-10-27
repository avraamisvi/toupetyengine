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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import net.phys2d.raw.World;

/**
 * A collection of physics demonstrations in a tidy box
 * 
 * @author Kevin Glass
 */
public class DemoBox extends AbstractDemo {
	/** The list of demos */
	private ArrayList demos = new ArrayList();
	/** The demo currently being played */
	private AbstractDemo currentDemo;
	/** The index of the current demo */
	private int current = 0;
	
	/**
	 * Create a new collection of demos
	 */
	public DemoBox() {
		super("Phys2D Demo Box");
	}
	
	/**
	 * Add a demo to the box
	 * 
	 * @param demo The demo to add
	 */
	public void add(AbstractDemo demo) {
		demos.add(demo);
	}

	/**
	 * @see net.phys2d.raw.test.AbstractDemo#update()
	 */
	protected void update() {
		currentDemo.update();
	}
	
	/**
	 * @see net.phys2d.raw.test.AbstractDemo#keyHit(char)
	 */
	public void keyHit(char c) {
		super.keyHit(c);
		
		if (c == 'n') {
			current++;
			if (current >= demos.size()) {
				current = 0;
			}
			needsReset = true;
		}
		if (c == 'p') {
			current--;
			if (current < 0) {
				current = demos.size() - 1;
			}
			needsReset = true;
		}
		
		currentDemo.keyHit(c);
	}
	
	/**
	 * @see net.phys2d.raw.test.AbstractDemo#init(net.phys2d.raw.World)
	 */
	protected void init(World world) {
		if (demos.size() == 0) {
			frame.setVisible(false);
			JOptionPane.showMessageDialog(null, "No Demos specified!");
			System.exit(0);
		}
		
		currentDemo = (AbstractDemo) demos.get(current);
		currentDemo.init(world);
	}

	/**
	 * @see net.phys2d.raw.test.AbstractDemo#renderGUI(java.awt.Graphics2D)
	 */
	protected void renderGUI(Graphics2D g) {
		g.setColor(Color.black);
		g.drawString("N - Next Demo",15,450);
		g.drawString("P - Previous Demo",15,470);

		currentDemo.renderGUI(g);
		
		g.setFont(g.getFont().deriveFont(Font.BOLD));
		g.drawString(currentDemo.getTitle(),300,470);
	}
	
	/**
	 * Entry point for the demo box
	 * 
	 * @param argv The arguments to the demo box
	 */
	public static void main(String[] argv) {
		DemoBox box = new DemoBox();

		box.add(new Demo1());
		box.add(new Demo2());
		box.add(new Demo3());
		box.add(new Demo4());
		box.add(new Demo5());
		box.add(new Demo6());
		box.add(new Demo7());
		box.add(new Demo8());
		box.add(new Demo9());
		box.add(new Demo10());
		box.add(new Demo11());
		box.add(new Demo12());
		box.add(new Demo13());
		box.add(new Demo14());
		box.add(new Demo15());
		box.add(new Demo16());
		box.add(new Demo17());
		box.add(new Demo18());
		box.add(new Demo19());
		box.add(new Demo20());
		box.add(new GearDemo());
		box.add(new AllShapesDemo());
		box.start();
	}
}
