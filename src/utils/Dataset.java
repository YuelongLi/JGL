package utils;

import java.awt.Color;

import ui.Locator;

/**
 * Dataset is a abstract class that provides a unified interface for its subclasses. 
 * It serves the function of storing and manipulating coordinate objects.
 * @author Yuelong Li
 * @version 0.2
 */

public abstract class Dataset implements Cloneable{
	protected String name = "Dataset";
	protected DataType dataType = DataType.cartesianY;
	protected Color color = Color.BLACK;
	
	protected double plotGap = 0.01;
	protected Dataset self = this;
	
	protected double offsetx = 0, offsety = 0;
	public boolean isSelected = false;
	//The coordinates at which the dataset is selected
	public double[] selectionCoords;
	
	public boolean visible = true;
	
	public abstract void initialize(double... params);
	
	/**
	 * Indicates whether the mouse action is selecting this dataset
	 * @param selectionRange the square of the radius
	 *  around the point within which the function is considered selected (in pixels)
	 * @param coords the pixel coordinat where the mouse is at
	 * @return whether the dataset is being selected
	 */
	public abstract boolean isSelected(int selectionRange, int X, int Y, Locator locator);
	
	/**
	 * Sets the average plotting gap between each points in the data set. 
	 * This helps to keep the resolution of a data graph consistent
	 * @param plotGap
	 */
	public void setPlotGap(double plotGap) {
		this.plotGap = plotGap;
		
	}
	
	/**
	 * Returns the average plotting gap between each points in the data set. 
	 * This helps to keep the resolution of a data graph consistent
	 * @param plotGap
	 */
	public double getPlotGap() {
		return plotGap;
	}
	
	public Dataset setName(String name) {
		this.name=name;
		return this;
	}
	
	public String getName() {
		return name;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}
	
	public DataType getDataType() {
		return dataType;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
	
	public Dataset cloneFields(Dataset cloned) {
		cloned.setName("cloned "+name);
		cloned.setDataType(dataType);
		cloned.setColor(color);
		cloned.plotGap = plotGap;
		return cloned;
	}
	
	public Dataset clone() {
		
		return cloneFields(new Dataset() {
			
			@Override
			public void initialize(double... params) {
				self.initialize(params);
			}

			@Override
			public boolean isSelected(int selectionRange, int X, int Y, Locator locator) {
				return self.isSelected(selectionRange, X, Y, locator);
			}});
	}
}

//copyright 2017, can not be used without authorization