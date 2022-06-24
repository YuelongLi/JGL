package utils;

import java.util.List;
import java.util.ArrayList;

import codeLibrary.Algorithms;
import codeLibrary.GradientDescent;

/**
 * A functional interface that is open for use. It uses derivative algorithm to
 * explore a single variable function with a constant plot density, and stores
 * the data into a matrix.
 * 
 * @author Yuelong Li
 *
 */
public class Function1V extends Curve implements FunctionPlot {

	public ArrayList<double[]> curve;

	double[] range = new double[2];

	public MultiVarInterface functionInterface;
	
	/**
	 * Creates a graphable dataset containing the 1 variable function
	 * @param functionInterface
	 */
	public Function1V(SingleVarInterface functionInterface) {
		this.functionInterface = (cds)->functionInterface.get(cds[0]);
		range[0] = -10;
		range[1] = 10;
		curve = new ArrayList<double[]>(10000);
	}
	
	private double I_x;
	private double I_y;
	private double soughtValue = 0;
	GradientDescent gd = new GradientDescent(100,
			(var) -> (function(var[0]) - soughtValue) * (function(var[0]) - soughtValue));
	private double[] init = new double[] { 1 };

	double xmin, xmax, ymin, ymax;

	@Override
	public void setRangeByBounds(double... bounds) {
		xmin = bounds[0];
		xmax = bounds[1];
		ymin = bounds[2];
		ymax = bounds[3];
		I_x = xmax - xmin;
		I_y = ymax - ymin;
		switch (dataType) {
		case cartesianY:
			range[0] = xmin;
			range[1] = xmax;
			break;
		case cartesianX:
			range[0] = ymin;
			range[1] = ymax;
			break;
		case polarR:
			soughtValue = Math.sqrt(I_x * I_x + I_y * I_y) / 50;
			init[0] = 1;
			gd.find(init, false);
			range[0] = gd.getResult()[0];
			break;
		case polarTheta:
			range[0] = -Math.sqrt((I_x) * (I_x) + (I_y) * (I_y)) / 2;
			range[1] = Math.sqrt((I_x) * (I_x) + (I_y) * (I_y)) / 2;
			break;
		default:
			break;
		}
	}

	@Override
	public List<double[]> getCurve() {
		return curve;
	}

	private double checkdv(double r, double theta, double dtheta) {
		double k = (function(theta + dtheta) - r) / dtheta;
		double dtheta0 = Math.signum(dtheta) * plotGap / Math.sqrt(r * r + k * k);
		return dtheta0;
	}

	/**
	 * Functions are reinitialized every time the size of the data panel is changed.
	 * During intialization, segments of the function are calculated, using
	 * derivative of the function to ensure the quality of the curve.
	 */
	@Override
	public void initialize(double... params) {
		curve.clear();
		double var = range[0];
		double dv, diff;
		if (dataType.equals(DataType.polarR)) {
			ArrayList<double[]> descending = new ArrayList<double[]>();
			double r;
			while (!Double.isFinite(r = function(var)))
				var += plotGap / 10;
			double I = Math.sqrt(I_x * I_x + I_y * I_y);
			diff = getDerivative(var);
			double markera = Double.NaN, markerb = Double.NaN;
			double direction1 = -((diff > 0) ? 1 : -1);
			double direction2 = (diff > 0) ? 1 : -1;
			int maxIter = 25000;
			int iter = 0;
			while (r > -I && iter < maxIter) {
				while (!Double.isFinite(r = function(var)))
					var += direction1 * plotGap / 10;
				descending.add(new double[] { r * Math.cos(var), r * Math.sin(var) });
				diff = getDerivative(var);
				if (Math.abs(diff) * 2 * Math.PI / r < plotGap / 10) {
					if (Double.isNaN(markera))
						markera = var;
					else {
						markerb = var;
						if (Math.abs(markerb - markera) > Math.PI * 6)
							break;
					}

				} else {
					if (!Double.isNaN(markera))
						markera = Double.NaN;
				}
				dv = plotGap / Math.sqrt(diff * diff + r * r) * direction1; // -Math.signum(diff) moves var toward the
																			// direction which r is descending
				dv = checkdv(r, var, dv);
				var += dv;
				iter++;
			}
			for (int i = 0; i < descending.size(); i++) {
				curve.add(descending.get(descending.size() - 1 - i));
			}
			iter = 0;
			var = range[0];
			while (r < I && iter < maxIter) {
				while (!Double.isFinite(r = function(var)))
					var += direction2 * plotGap / 10;
				curve.add(new double[] { r * Math.cos(var), r * Math.sin(var) });
				diff = getDerivative(var);
				if (Math.abs(diff) * 2 * Math.PI / r < plotGap / 10) {
					if (Double.isNaN(markera))
						markera = var;
					else {
						markerb = var;
						if (Math.abs(markerb - markera) > Math.PI * 6)
							break;
					}

				} else {
					if (!Double.isNaN(markera))
						markera = Double.NaN;
				}
				dv = plotGap / Math.sqrt(diff * diff + r * r) * direction2; // Math.signum(diff) moves var toward the
																			// direction which r is increasing
				dv = checkdv(r, var, dv);
				var += dv;
				iter++;
			}
		} else {
			double val;
			while (var < range[1]) {
				val = function(var, 0);
				diff = getDerivative(var);
				dv = plotGap;
				switch (dataType) {
				case cartesianY:
					if (val > ymin && val < ymax) 
						curve.add(new double[] { var, function(var, 0) });
					else
						curve.add(new double[] { Double.NaN, Double.NaN });
					dv = plotGap / Math.sqrt(1 + diff * diff);
					break;
				case cartesianX:
					if (val > xmin && val < xmax)
						curve.add(new double[] { function(var, 0), var });
					else
						curve.add(new double[] { Double.NaN, Double.NaN });
					dv = plotGap / Math.sqrt(1 + diff * diff);
					break;
				case polarTheta:
					curve.add(new double[] { var * Math.cos(function(var, 0)), var * Math.sin(function(var, 0)) });
					dv = plotGap / Math.sqrt((var * diff) * (var * diff) + 1);
				default:
					break;
				}
				if(Double.isNaN(dv))
					dv = plotGap;
				var += (dv < plotGap) ? plotGap  : dv;
			}

		}
	}

	/**
	 * Currently implemented only for cartesian coordinates and polar theta
	 * coordinates (searches within -200pi to 200pi for polar coordinates), one
	 * cannot search in polar r coordinate yet
	 */
	public boolean isSelected(double selectionRange, double... coords) {
		switch (dataType) {
		case cartesianX: {
			double y = function(coords);
			if (Math.abs(y - coords[1]) <= selectionRange)
				return true;
			break;
		}
		case cartesianY: {
			double x = function(coords);
			if (Math.abs(x - coords[0]) <= selectionRange)
				return true;
			break;
		}
		case polarTheta:
			double angle = Math.atan2(coords[1], coords[0]);
			for (int i = -100; i <= 100; i++) {
				double theta = angle + Math.PI * 2 * i, r = function(theta);
				double x = Math.cos(theta) * r, y = Math.sin(theta) * r;
				if(Math.sqrt(Math.pow(x-coords[0],2)+Math.pow( y-coords[1], 2))<=selectionRange)
					return true;
			}
			break;
		default:
			System.out.println("Value search is not yet supported for "+dataType+"functions, returned not selected by default");
			break;
		}
		return false;
	}

	public double getDerivative(double input) {
		return Algorithms.getDerivative((double x) -> function(x, 0), input);
	}

	public double function(double... var) {
		return functionInterface.function(var);
	}

	@Override
	public void setFunction(MultiVarInterface newFunctionInterface) {
		this.functionInterface = newFunctionInterface;
	}

	@Override
	public MultiVarInterface getFunction() {
		return functionInterface;
	}

	@Override
	public double[][] getRanges() {
		// TODO Auto-generated method stub
		return null;
	}
}
