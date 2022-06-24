package examples;

import java.awt.Color;
import java.awt.Graphics;
import ui.Locator;

class SimObject {
	
	Locator loc;
	
	boolean selected;
	
	Color color;
	
	/**
	 * Position, Velocity, Acceleration
	 */
	double[][] linear = new double[3][3];
	
	/**
	 * Angle, Angular Velocity, Angular Acceleration
	 */
	double[][] rotational = new double[3][3];
	
	SimObject(Locator loc){
		this.loc=loc;
	}
	
	public void setLocation(double... p){
		linear[0] = p;
	}
	
	public double X(){
		return loc.toX(linear[0]);
	}
	
	public double Y(){
		return loc.toY(linear[0]);
	}
	
	public int XonScreen() {
		return loc.toX(linear[0])-loc.toWidth(size/2);
	}
	public int YonScreen() {
		return loc.toY(linear[0])-loc.toHeight(size/2);
	}
	
	public void setColor(Color color){
		this.color=color;
	}
	
	double size;
	
	public void setSize(double size) {
		this.size = size;
	}
	
	protected void paintShape(Graphics g){
		g.fillOval(XonScreen(), YonScreen(), loc.toWidth(size), loc.toHeight(size));
	
	}
}
