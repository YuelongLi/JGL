package utils;

import ui.Locator;

public abstract class Particle extends Dataset{
	
	protected double[] position;
	
	public Particle(int dim) {
		this.position = new double[dim];
	}

	@Override
	public void initialize(double... params) {
	}
	
	/**
	 * Radius of individual particles (not related to scales)
	 */
	public double radius = 0.25;
	
	/**
	 * Setter for radius
	 * @param newRadius
	 */
	public void setRadius(double newRadius) {
		this.radius = newRadius;
	}
	
	/**
	 * Getter for radius
	 * @return radius
	 */
	public double getRadius() {
		return radius;
	}
	
	abstract public double[] getParticle(double t);

	@Override
	public boolean isSelected(int selectionRange, int X, int Y, Locator locator) {
		int Xp = locator.toX(position);
		int Yp = locator.toY(position);
		selectionCoords = position;
		return selectionRange>=(X-Xp)*(X-Xp)+(Y-Yp)*(Y-Yp);
	}
}
