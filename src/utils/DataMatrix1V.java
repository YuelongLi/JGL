package utils;

import java.util.ArrayList;
import java.util.List;

public class DataMatrix1V extends Curve{
	
	/**
	 * holds the data matrix for the surface
	 */
	List<double[]> curve = new ArrayList<double[]>();
	
	/**
	 * Adds a curve defined by an array of point coordinates
	 * @param curve
	 */
	public void setCurve(List<double[]> curve) {
		this.curve = curve;
	}
	
	/**
	 * Setting data type is only informational and optional for DataMatrix1V
	 */
	public void setDataType(DataType dataType) {
		super.setDataType(dataType);
	}

	@Override
	public List<double[]> getCurve() {
		return curve;
	}

	@Override
	public void initialize(double... params) {
		
	}
	
	public DataMatrix1V clone() {
		DataMatrix1V data = new DataMatrix1V();
		data.setCurve(curve);
		super.cloneFields(data);
		return data;
	}
}
