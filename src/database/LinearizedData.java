package database;

import java.util.ArrayList;

import utils.SingleVarInterface;

/**
 * The backbone of all data analysis. LinearizedData is the structure that holds
 * functional data for quick access (log(n)), modification (constant or log(n)),
 * and concatenation (constant)
 * 
 * @author Yuelong Li
 *
 */
public class LinearizedData implements SingleVarInterface{
	/**
	 * Unit testing
	 */
	public static void main(String[] args) {
		LinearizedData data = new LinearizedData();
		data.append(1, 2);
		data.append(2, 4);
		data.append(3, -9);
		System.out.println(data.get(4.13));
	}

	/**
	 * Concatenation sorts the concatenated data so that the concatenated of this is
	 * always earlier in its time range
	 */
	LinearizedData concatenated = null;
	/**
	 * Time, value entries
	 */
	public volatile ArrayList<DataEntry> dataArray = new ArrayList<DataEntry>();

	/**
	 * Binary searches and returns the data at the given time, takes a linear
	 * approximation if time is between two data points, returns NAN if time is not
	 * within range
	 * 
	 * @param time
	 * @return the data point at time
	 */
	public synchronized double get(double time) {
		int lastI = dataArray.size() - 1;
		if (lastI == -1)
			if (concatenated != null)
				return concatenated.get(time);
			else
				return Double.NaN;
		try {// Since this sometimes gets thrown
			if (dataArray.get(lastI) == null || time > dataArray.get(lastI).key) {
				return Double.NaN;
			}
		} catch (NullPointerException e) {// Fix by removing the null value if necessary
			e.printStackTrace();
			if (dataArray.get(lastI) == null)
				System.out.println(
						"null pointer exception fixed by removing " + dataArray.remove(lastI) + " from data array");
			return Double.NaN;
		}
		if (time < dataArray.get(0).key) {
			if (concatenated != null && concatenated.dataArray.size() != 0)
				if (concatenated.dataArray.get(concatenated.dataArray.size() - 1).key < time) {
					double x1 = concatenated.dataArray.get(concatenated.dataArray.size() - 1).key,
							y1 = concatenated.dataArray.get(concatenated.dataArray.size() - 1).value,
							x2 = dataArray.get(0).key, y2 = dataArray.get(0).value;
					return (x2 != x1) ? (y2 - y1) / (x2 - x1) * (time - x1) + y1 : y1;
				} else {
					return concatenated.get(time);
				}

			else
				return Double.NaN;
		}
		int leftBound = getLeftIndex(time, 0, dataArray.size() - 1);
		int rightBound = getRightIndex(time, 0, dataArray.size() - 1);
		if (leftBound == -1)
			return Double.NaN;
		if (rightBound >= dataArray.size())
			return Double.NaN;
		double x1 = dataArray.get(leftBound).key, y1 = dataArray.get(leftBound).value,
				x2 = dataArray.get(rightBound).key, y2 = dataArray.get(rightBound).value;
		return (x2 != x1) ? (y2 - y1) / (x2 - x1) * (time - x1) + y1 : y1;
	}

	/**
	 * Binary searches and returns the data at the data point up next to the given
	 * time, takes a linear approximation if time is between two data points,
	 * returns NAN if time is not within range
	 * 
	 * @param time
	 * @return the data point at time
	 */
	public synchronized double getNext(double time) {
		int lastI = dataArray.size() - 1;
		if (lastI == -1)
			if (concatenated != null)
				return concatenated.get(time);
			else
				return Double.NaN;
		if (time > dataArray.get(lastI).key) {
			return Double.NaN;
		}
		if (time < dataArray.get(0).key) {
			if (concatenated != null)
				if (concatenated.dataArray.get(concatenated.dataArray.size() - 1).key < time) {
					double x1 = concatenated.dataArray.get(concatenated.dataArray.size() - 1).key,
							y1 = concatenated.dataArray.get(concatenated.dataArray.size() - 1).value,
							x2 = dataArray.get(0).key, y2 = dataArray.get(0).value;
					return (x2 != x1) ? (y2 - y1) / (x2 - x1) * (time - x1) + y1 : y1;
				} else {
					return concatenated.get(time);
				}

			else
				return Double.NaN;
		}
		int leftBound = getLeftIndex(time, 0, dataArray.size() - 1);
		int rightBound = getRightIndex(time, 0, dataArray.size() - 1);
		if (leftBound == -1)
			return Double.NaN;
		if (rightBound >= dataArray.size())
			return Double.NaN;
		double y2 = dataArray.get(rightBound).value;
		return y2;
	}

	/**
	 * Adds the time, value pair to the data array of this if time is within the
	 * range of it, if time is before the range, checks if the concatenated data
	 * exists before adding it to the data array of this (if concatenated == null).
	 * In this way the order in data streams can be maintained => time strictly
	 * increasing.
	 * 
	 * @param time
	 * @param value
	 */
	public synchronized void set(double time, double value) {
		int lastI = dataArray.size() - 1;
		if (dataArray.size() == 0) {
//			System.out.println("Found data array with size 0");
			if (concatenated != null && concatenated.dataArray.size() != 0
					&& concatenated.dataArray.get(concatenated.dataArray.size() - 1).key > time)
				concatenated.set(time, value);
			else
				dataArray.add(DataEntry.create(time, value));
			return;
		}
		if (lastI != -1 && time > dataArray.get(lastI).key) {
			dataArray.add(DataEntry.create(time, value));
			return;
		}
		if (lastI != -1 && time < dataArray.get(0).key) {
			if (concatenated != null)
				concatenated.set(time, value);
			else
				dataArray.add(0, DataEntry.create(time, value));
			return;
		}
		int leftBound = getLeftIndex(time, 0, dataArray.size() - 1);
		int rightBound = getRightIndex(time, 0, dataArray.size() - 1);
//		System.out.println("Bound: "+Arrays.toString(bound));
		if (leftBound != rightBound)
			dataArray.add(rightBound, DataEntry.create(time, value));
		else
			dataArray.set(leftBound, DataEntry.create(time, value));
	}

	/**
	 * Appends the specified time and value at the end of the data array if it is
	 * the latest in time, else returns false, defaults the storable attribute of
	 * the new data entry to true
	 * 
	 * @param time
	 * @param value
	 * @return whether the append have happened successfully
	 */
	public boolean append(double time, double value) {
		return append(time, value, true);
	}

	/**
	 * Appends the specified time and value at the end of the data array if it is
	 * the latest in time, else returns false
	 * 
	 * @param time
	 * @param value
	 * @param storable indicates whether the newly created data entry will be stored
	 *                 on the system disk
	 * @return
	 */
	public boolean append(double time, double value, boolean storable) {
		DataEntry entry = DataEntry.create(time, value);
		entry.storable = storable;
		dataArray.add(entry);
		return true;
	}

	/**
	 * 
	 * @param time
	 * @param begin inclusive
	 * @param end   inclusive
	 * @return left closest index
	 */
	private int getLeftIndex(double time, int begin, int end) {
//		System.out.println("begin: "+begin+", "+"end: "+end);
		if (end < begin)
			return -1;
		if (time == dataArray.get(end).key)
			return end;
		if (end - begin <= 1 && dataArray.get(begin).key <= time && dataArray.get(end).key > time)
			return begin;
		if (end == (begin + end) / 2)
			return end;
		int midPoint = (begin + end) / 2;
		if (dataArray.get(midPoint).key <= time)
			return getLeftIndex(time, midPoint, end);
		else
			return getLeftIndex(time, begin, midPoint);
	}

	/**
	 * 
	 * @param time
	 * @param begin inclusive
	 * @param end   inclusive
	 * @return left closest index
	 */
	private int getRightIndex(double time, int begin, int end) {
		if (end < begin)
			return -1;
		if (time == dataArray.get(begin).key)
			return end;
		if (end - begin <= 1 && dataArray.get(begin).key < time && dataArray.get(end).key >= time)
			return end;
		if (begin == (begin + end) / 2)
			return begin;
		int midPoint = (begin + end) / 2;
		if (dataArray.get(midPoint).key >= time)
			return getRightIndex(time, begin, midPoint);
		else
			return getRightIndex(time, midPoint, end);
	}

	/**
	 * Recursively finds the linearized data block that comes the earliest in the
	 * concatenation chain, and returns its first entry
	 * 
	 * @return the first entry of the linearized data chain
	 */
	public DataEntry getFirst() {
		if (concatenated == null) {
			if (dataArray.size() != 0)
				return dataArray.get(0);
			else
				return null;
		} else
			return concatenated.getFirst();
	}

	/**
	 * @return the last data entry in this.dataArray
	 */
	public DataEntry getLast() {
		if (dataArray.size() == 0)
			if (concatenated == null)
				return null;
			else
				return concatenated.getLast();
		return dataArray.get(dataArray.size() - 1);
	}

	LinearizedData getConcatenated() {
		return this.concatenated;
	}

	/**
	 * Concatenates this with another sheet of linearized data to form a entirety,
	 * the data range of them cannot overlap and the one with an later data range is
	 * sorted to the front, the process is recursive and maintains the content and
	 * reference of concatenated data.
	 * 
	 * @param b the other sheet of linearized data to be concatenated
	 * @return the linearized data that is the combination of b and this
	 */
	public LinearizedData concatenate(LinearizedData b) {
		if (this.dataArray.size() == 0) {
			if (concatenated == null) {
				this.concatenated = b;
				return this;
			} else {
				this.concatenated = b.concatenate(concatenated);
				return this;
			}
		}
		if (b.dataArray.size() == 0 || b.dataArray.get(0).key > dataArray.get(dataArray.size() - 1).key) {
			return b.concatenate(this);
		} else if (b.dataArray.get(b.dataArray.size() - 1).key < dataArray.get(0).key) {
			if (concatenated == null) {
				this.concatenated = b;
				return this;
			} else {
				this.concatenated = concatenated.concatenate(b);
				return this;
			}
		} else {
			System.out.println("You can't concatenated two data with overlapping range");
			return null;
		}
	}

	/**
	 * Clears the data contained in this without affecting the concatenated data
	 */
	public void clear() {
		for (DataEntry entry : dataArray) {
			entry.dispose();
		}
		this.dataArray.clear();
	}
}

//All rights reserved 2020