package utils;

import java.util.ArrayList;
import java.util.List;

import ui.Locator;

public class Curve extends Dataset{
	List<double[]> curve = new ArrayList<double[]>();
	@Override
	public void initialize(double... params) {
		
	}

	public List<double[]> getCurve() {
		return curve;
	}
	
	/**
	 * Currently implementing a very basic search algorithm that may break
	 * when there are large gaps between two vertices on the curve
	 */
	@Override
	public boolean isSelected(int selectionRange, int X, int Y, Locator locator) {
//		for(double[] point: curve) {
//			double distance = 0;
//			for(int i = 0; i<point.length; i++) {
//				distance += Math.pow(point[i]-coords[i],2);
//			}
//			if(Math.sqrt(distance)<=selectionRange)
//				return true;
//		}
		return false;
	}
}
