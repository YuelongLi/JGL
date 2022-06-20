package utils;

public interface Function{
	
	public void setFunction(MultiVarInterface newFunctionInterface);
	public MultiVarInterface getFunction();
	
	public void setRangeByBounds(double...bounds);

	public double[][] getRanges();

	public double function(double... var);
}