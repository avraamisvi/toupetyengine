package net.phys2d.util;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * A simple test for the triangulator
 * 
 * @author Kevin Glass
 */
public class TriangulatorTest extends Frame {
	/** The points of the polygon */
	private int[][] points;
	/** The total number of points */
	private int total;
	/** The triangulator under test */
	private Triangulator tris;
	
	/**
	 * Create the test
	 */
	public TriangulatorTest() {
		total = 8;
		points = new int[total][2];
		
		points[0][0] = 100;
		points[0][1] = 100;
		points[1][0] = 300;
		points[1][1] = 120;
		points[2][0] = 340;
		points[2][1] = 300;
		points[3][0] = 240;
		points[3][1] = 400;
		points[4][0] = 70;
		points[4][1] = 180;
		points[5][0] = 100;
		points[5][1] = 200;
		points[6][0] = 150;
		points[6][1] = 250;
		points[7][0] = 50;
		points[7][1] = 40;
		
		setSize(500,500);
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				tris = new Triangulator();
				for (int i=0;i<total;i++) {
					tris.addPolyPoint(points[i][0],points[i][1]);
				}
				tris.triangulate();
				System.out.println("Triangle: "+tris.getTriangleCount());
				repaint(0);
			}
		});
	}
	
	/**
	 * @see java.awt.Component#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		for (int i=0;i<total-1;i++) {
			g.setColor(Color.black);
			g.drawLine(points[i][0],points[i][1],points[i+1][0],points[i+1][1]);
		}
		g.setColor(Color.black);
		g.drawLine(points[total-1][0],points[total-1][1],points[0][0],points[0][1]);
	
		if (tris != null) {
			g.setColor(Color.red);
			for (int i=0;i<tris.getTriangleCount();i++) {
				for (int j=0;j<2;j++) {
					float[] p1 = tris.getTrianglePoint(i,j);
					float[] p2 = tris.getTrianglePoint(i,j+1);
					
					g.drawLine((int) p1[0],(int) p1[1],(int) p2[0],(int) p2[1]);
				}	
				float[] p1 = tris.getTrianglePoint(i,2);
				float[] p2 = tris.getTrianglePoint(i,0);
				
				g.drawLine((int) p1[0],(int) p1[1],(int) p2[0],(int) p2[1]);
			}
		}
	}
	
	/**
	 * Run the test
	 * 
	 * @param argv The arguments to run with
	 */
	public static void main(String[] argv) {
		new TriangulatorTest();
	}
}
