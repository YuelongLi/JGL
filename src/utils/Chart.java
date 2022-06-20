package utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import ui.Locator;

/**
 * 
 * @author Yuelong Li
 *
 */
public class Chart extends Dataset {
	
	public ChartType chartType = ChartType.bar;
	ArrayList<Double> statistics;
	public double mu;
	public double std;
	public double dataSize;
	public ArrayList<ArrayList<double[]>> geometries=new ArrayList<ArrayList<double[]>>();
	public ArrayList<Color> colors;
	double binSizeToStdRatio;
	
	public Chart(ArrayList<Double> statistics, double binSizeToStdRatio) {
		this.statistics = statistics;
		mu = Algorithms.mean(statistics);
		std = Algorithms.standardDeviation(statistics, mu);
		this.dataSize = statistics.size();
		this.binSizeToStdRatio = binSizeToStdRatio;
	}

	@Override
	public void initialize(double... params) {
		switch(chartType) {
		case bar:
			geometries.clear();
			double binSize = std*binSizeToStdRatio;
			double normalization = 1.0/binSize/dataSize;
			Collection<double[]> bins = computeBins(binSize, mu, statistics);
			for(double[] bin:bins) {
				ArrayList<double[]> rectangle = new ArrayList<double[]>();
				rectangle.add(new double[] {bin[0]-binSize/2, 0});
				rectangle.add(new double[] {bin[0]+binSize/2, 0});
				rectangle.add(new double[] {bin[0]+binSize/2, normalization*bin[1]});
				rectangle.add(new double[] {bin[0]-binSize/2, normalization*bin[1]});
				geometries.add(rectangle);
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * Counts data by bins of given sizes until the dataset is exhausted 
	 * @param binSize the size of each bin
	 * @param center the center from which the counting begins
	 * @param data the data set of values
	 * @return the first number in elements returned is the center of the bin, 
	 * while the second number is the number of data that belongs to the bin
	 */
	Collection<double[]> computeBins(double binSize, double center, ArrayList<Double> data){
		HashMap<Integer, double[]> bins = new HashMap<Integer, double[]>();
		for(double dp: data) {
			if(dp>center+binSize/2) {
				int index = (int)((dp-(center+binSize/2))/binSize)+1;
				if(bins.get(index)==null) 
					bins.put(index, new double[] {center+index*binSize, 1});
				else
					bins.get(index)[1]++;
			}else if(dp<center-binSize/2) {
				int index = (int)((dp-(center-binSize/2))/binSize)-1;
				if(bins.get(index)==null) 
					bins.put(index, new double[] {center+index*binSize, 1});
				else
					bins.get(index)[1]++;
			}
			else {
				if(bins.get(0)==null) 
					bins.put(0, new double[] {center, 1});
				else
					bins.get(0)[1]++;
			}
		}
		return bins.values();
	}
	
	/**
	 * Counts elements in data that are within the specified range
	 * @param begin
	 * @param end
	 * @param data
	 * @return the number of data that are within the specified range
	 */
	int countRange(double begin, double end, ArrayList<Double> data) {
		int c = 0;
		for(double dp: data) {
			if(dp<end&&dp>=begin)
				c++;
		}
		return c;
	}
	
	@Override
	public boolean isSelected(int selectionRange, int X, int Y, Locator locator) {
		// TODO Auto-generated method stub
		return false;
	}

}

enum ChartType {
	bar,
	fan,
}
