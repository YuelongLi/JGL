package database;

import java.util.ArrayList;

public class DataEntry{
	public double key;
	public double value;
	public static ArrayList<DataEntry> reusableEntries = new ArrayList<DataEntry>();
	/**
	 * For internal re-using purposes and reduce stress of garbage collection,
	 * use a simple memory re-use mechanism
	 */
	public static synchronized DataEntry create(double key, double value) {
		int size = reusableEntries.size();
		if(size==0) {
			return new DataEntry(key, value);
		}else {
			DataEntry entry = reusableEntries.remove(size-1);
			entry.key = key;
			entry.value = value;
			entry.storable = true;
			return entry;
		}
	}
	public void dispose(){
		reusableEntries.add(this);
	}
	/*
	 * Indicates whether the current instance of DataEntry
	 * will be stored into the database upon program closing.
	 */
	boolean storable = true;
	private DataEntry(double key, double value){
		this.key = key;
		this.value = value;
	}
	public String toString() {
		return "["+key+": "+value+"]\n";
	}
}
