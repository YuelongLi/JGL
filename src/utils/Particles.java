package utils;

import java.util.ArrayList;
import java.util.List;

import ui.Locator;

public class Particles extends Dataset{

	@Override
	public void initialize(double... params) {
	}
	
	/**
	 * List of particles (dots) to be printed
	 */
	List<double[]> particles = new ArrayList<double[]>();
	
	/**
	 * Radius of individual particles (not related to scales)
	 */
	public double radius = 0.25;
	
	public void addParticles(List<double[]> particles) {
		this.particles.addAll(particles);
	}
	
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
	
	public List<double[]> getParticles(double t) {
		return particles;
	}

	@Override
	public boolean isSelected(int selectionRange, int X, int Y, Locator locator) {
//		for(double[] particle: particles) {
//			double distance = 0;
//			for(int i = 0; i<particle.length; i++) {
//				distance += Math.pow(particle[i]-coords[i],2);
//			}
//			if(Math.sqrt(distance)<=selectionRange)
//				return true;
//		}
		return false;
	}
}
