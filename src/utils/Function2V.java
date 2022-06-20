package utils;

import java.util.ArrayList;

import codeLibrary.Algorithms;

public class Function2V extends Surface implements Function{

	protected double[] range = new double[4];

	MultiVarInterface functionInterface;
	
	public Function2V(MultiVarInterface functionInterface) {
		this.functionInterface = functionInterface;
		setRange(-20, 20, -20, 20);
		setDataType(DataType.cartesianZ);
		surface = new ArrayList<ArrayList<double[]>>(5000);
	}

	public void setRange(double var1min, double var1max, double var2min, double var2max) {
		range[0] = var1min;
		range[1] = var1max;
		range[2] = var2min;
		range[3] = var2max;
	}

	public double[] getRange() {
		return range;
	}

	public void setRangeByBounds(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {
		switch (dataType) {
		case cartesianZ:
			range[0] = xmin;
			range[1] = xmax;
			range[2] = ymin;
			range[3] = ymax;
			break;
		case cartesianX:
			range[0] = ymin;
			range[1] = ymax;
			range[2] = zmin;
			range[3] = zmax;
			break;
		case cartesianY:
			range[0] = xmin;
			range[1] = xmax;
			range[2] = zmin;
			range[3] = zmax;
			break;
		case sphericalTheta:
			break;
		case sphericalR:
			break;
		case sphericalPhi:
			break;
		case cylindricalZ:
			break;
		case cylindricalR:
			break;
		case cylindricalTheta:
			break;
		default:
			break;
		}
	}

	

	@Override
	public void initialize(double... bounds) {
		surface.clear();
		double var1, var2;
		var1 = range[0];
		while (var1 < range[1]) {
			ArrayList<double[]> curve = new ArrayList<double[]>();
			var2 = range[2];
			while (var2 < range[3]) {
				double result = function(var1, var2);
				switch (dataType) {
				case cartesianX:
					curve.add(new double[] { result, var1, var2 });
					break;
				case cartesianY:
					curve.add(new double[] { var1, result, var2 });
					break;
				case cartesianZ:
					curve.add(new double[] { var1, var2, result});
					break;
				case sphericalTheta:
					break;
				case sphericalR:
					break;
				case sphericalPhi:
					break;
				case cylindricalZ:
					break;
				case cylindricalR:
					break;
				case cylindricalTheta:
					break;
				default:
					break;
				}
				var2 += plotGap;
			}
			surface.add(curve);
			var1 += plotGap;
		}
	}

	public double getPartialDerivative(double var1, double var2, int outputIndex, int partialChoice) {
		if (partialChoice == 0)
			return Algorithms.getDerivative((double x) -> function(x, var2), var1);
		else
			return Algorithms.getDerivative((double x) -> function(var1, x), var2);
	}

	public double[] output = new double[3];

	public double function(double var1, double var2) {
		return  functionInterface.function(var1,var2);
	};

	@Override
	public void setFunction(MultiVarInterface newFunctionInterface) {
		this.functionInterface = newFunctionInterface;
	}

	@Override
	public MultiVarInterface getFunction() {
		return functionInterface;
	}

	@Override
	public void setRangeByBounds(double... bounds) {
		switch (dataType) {
		case cartesianZ:
			range = bounds;
			break;
		case cartesianX:
			range[0] = bounds[2];
			range[1] = bounds[3];
			if(bounds.length>4) {
				range[2] = bounds[4];
				range[3] = bounds[5];
			}
			break;
		case cartesianY:
			range = bounds;
			if(bounds.length>4) {
				range[2] = bounds[4];
				range[3] = bounds[5];
			}
			break;
		case sphericalTheta:
			break;
		case sphericalR:
			break;
		case sphericalPhi:
			break;
		case cylindricalZ:
			break;
		case cylindricalR:
			break;
		case cylindricalTheta:
			break;
		default:
			break;
		}
	}

	@Override
	public double[][] getRanges() {
		return null;
	}

	@Override
	public double function(double... var) {
		return functionInterface.function(var);
	}

}