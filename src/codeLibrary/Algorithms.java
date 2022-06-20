package codeLibrary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * The algorithm class uses generic type and provides functions such as finding maximum sorting. 
 * 
 * @author Yuelong Li
 * @date 2017/06/23
 */
public class Algorithms {
	
	/**
	 * Finds the dot product of vector a and b
	 * @param a vector
	 * @param b vector
	 * @return a dot b
	 */
	public static double dot(double[] a, double[] b) {
		double total = 0;
		for(int i = 0; i<Math.min(a.length,b.length); i++) total += a[i]*b[i];
		return total;
	}
	
	/**
	 * Finds the cross product of vector a and b
	 * @param a vector
	 * @param b vector
	 * @return a cross b
	 */
	public static double[] cross(double[] a, double[] b) {
		return new double[] {a[1]*b[2]-a[2]*b[1],a[2]*b[0]-a[0]*b[2],a[0]*b[1]-a[1]*b[0]};
	}
	
	/**
	 * Finds the sum of vectors
	 * @param vs set of vectors
	 * @return a + b + c + ...
	 */
	public static double[] add(double[]... vs) {
		double[] statement = new double[vs[0].length];
		for(double[] v: vs)for(int i = 0; i < v.length; i++) statement[i] += v[i];
		return statement;
	}
	
	/**
	 * Subtracts vectors from the first one
	 * @param vs set of vectors
	 * @return a - b - c - ...
	 */
	public static double[] subtract(double[]...vs) {
		double[] statement = vs[0].clone();
		for(int a = 1; a<vs.length; a++) {
			double[]v = vs[a];
			for(int i = 0; i < v.length; i++) statement[i] -= v[i];
		}
		return statement;
	}
	
	/**
	 * Finds the product of scalar a and vector b
	 * @param a scalar
	 * @param b vector
	 * @return ab
	 */
	public static double[] multiply(double a, double[]b) {
		double[] statement = new double[b.length];
		for(int i = 0; i < b.length; i++) statement[i] = a*b[i];
		return statement;
	}
	
	/**
	 * Finds the magnitude of a vector
	 * @param v the vector
	 * @return the magnitude of v
	 */
	public static double magnitude(double[] v) {
		double sum=0;
		for(double comp : v) sum+=comp*comp;
		return Math.sqrt(sum);
	}
	
	/**
	 * The static method getMax returns the object with the max value from the list.
	 * @param list
	 * @return max the element that has the largest value in the list
	 * @date 2017/06/23
	 */
	public static <T extends Comparable<T>> T getMax(List<T> list){
		T max = list.get(0);
		for(T pass: list) if(pass.compareTo(max)>0) max=pass;
		return max;
	}
	
	/**
	 * The static method getMin returns the object with the min value from the list.
	 * @param list
	 * @return min the element that has the amallest value in the list
	 * @date 2017/06/23
	 */
	public static <T extends Comparable<T>> T getMin(List<T> list){
		T min = list.get(0);
		for(T pass: list) if(pass.compareTo(min)<0) min=pass;
		return min;
	}
	
	/**
	 * @param size the size of the random number list to be generated
	 * @param seed the seed to for generating random digits
	 * @param max the maximum number that may be found in the list
	 * @param min the minimum number that may be found in the list
	 * @date 2017/06/23
	 */
	public static List<Double> getRandomDoubles(int size, double max, double min,double...seed){
		List<Double> a = new ArrayList<Double>(size);
		for(int i = 0; i<size; i++) a.add(Math.random()*(max-min)+min);
		return a;
	}
	
	/**
	 * @param size the size of the random number list to be generated
	 * @param seed the seed to for generating random digits
	 * @param max the maximum number that may be found in the list
	 * @param min the minimum number that may be found in the list
	 * @date 2017/06/23
	 */
	public static List<Integer> getRandomIntegers(int size, int max, int min,double...seed){
		List<Integer> a = new ArrayList<Integer>(size);
		for(int i = 0; i<size; i++) a.add((int)(Math.random()*(max-min)+min));
		return a;
	}
	
	/**
	 * @param size the size of the random number list to be generated
	 * @param seed the seed to for generating random digits
	 * @param max the maximum number that may be found in the list
	 * @param min the minimum number that may be found in the list
	 * @date 2017/06/23
	 */
	public static double[] getRandomArray(int size, double max, double min,double...seed){
		double[] a = new double[size];
		for(int i = 0; i<size; i++) a[i]  = Math.random()*(max-min)+min;
		return a;
	}
	
	/**
	 * Obtains the mean of a dataset
	 * @param data 
	 * @return the mean
	 */
	public static double mean(List<Double> data) {
		double sum = 0;
		for(int i = 0; i<data.size(); i++) {
			sum+=data.get(i);
		}
		return sum/data.size();
	}
	
	/**
	 * Obains the standard deviation of a given dataset
	 * @param data the dataset
	 * @param mu mean of te dataset
	 * @return the standard deviation
	 */
	public static double standardDeviation(List<Double> data, double mu) {
		if(data.size()<2)
			return 0;
		double sqDeviation = 0;
		for(int i = 0; i<data.size(); i++) {
			sqDeviation += (data.get(i)-mu)*(data.get(i)-mu);
		}
		return Math.sqrt(sqDeviation/(data.size()-1));
	}
	
	/**
	 * This method uses insertion sort to sort things into ascending order
	 * @param list The list to be sorted
	 * @return sorted the sorted list
	 * @date 2017/06/23
	 */
	public static <T extends Comparable<T>> List<T> insertionSort(List<T> list){
		for(int i =0; i<list.size(); i++){
			T pass=null;
			while(i!=0&&(list.get(i).compareTo(pass = list.get(i-1))<0)){
				 list.set(i-1,list.get(i));
				 list.set(i, pass);
				 i--;
			}
		}
		return list;
	}
	
	static private double[] orgList, list2;
	static private int[] keyChain;
	
	public static int[] quickSort(int[] input)
	 {
	  quickSortRecursion(input,0,input.length-1);
	  return input;
	 }
	private static void quickSortRecursion(int[] input, int ini, int fin)
	 {
	  if(fin-ini==0)
	   return;
	  int counter1 = ini;
	  int counter2 = fin;
	  int pivot;
	  pivot = (input[ini] + input[fin])/2;
	  System.out.println(Arrays.toString(input));
	  System.out.println("pivot: "+pivot);
	  while(counter1<counter2)
	  {
	   while(input[counter1]<pivot) {
		   counter1++;
	   }
	   while(input[counter2]>pivot) {
		   counter2--;
	   }
	   System.out.println("counter1: "+counter1);
	   System.out.println("counter2: "+counter2);
	   if(counter1<=counter2)
	   {
	    int temp = input[counter1];
	    input[counter1] = input[counter2];
	    input[counter2] = temp;
	    counter1++;
	    counter2--;
	    System.out.println(Arrays.toString(input));
	   }
	  }
	  if(counter1<fin)
	   quickSortRecursion(input,counter1,fin);
	  if(counter2>ini)
	   quickSortRecursion(input,ini,counter2);
	 }
	
	/**
	 * A special sorting algorithm that works the best when the list comprises of random yet evenly scattered digits.
	 * This method sorts things into ascending order.
	 * This method only takes in a double array for the sake of high testing speed
	 * @param rgList the original list stored as a double array
	 * @return the sorted list
	 * @date 08/28/2017
	 */
	public static double[] yuelongSort(double[] rgList) {
		keyChain = new int[rgList.length*5/2];
		list2 = new double[rgList.length];
		orgList = rgList;
		yuelongRecursion(0, 0, rgList.length);
		
		return orgList;
	}
	
	/**
	 * The underlying recursion mechanism of yuelong sort, which sorts the list into ascending order.
	 * @see Algorithms.yuelongSort
	 * @param orgList 
	 * @param keySize the starting position available for key storage in key chain
	 * @param start the starting of the sublist
	 * @param length of the sublist: start+length = endingIndex + 1 = firstIndexOfTheNextSublist
	 * @return the sorted list
	 */
	private static void yuelongRecursion(int keySize, int start, int length) {
		
		if(length <= 1) return;//special case1
		if(length == 2) {//special case 2
			if(orgList[start]<=orgList[start+1]) return;
			else {
				double temp = orgList[start];
				orgList[start] = orgList[start+1];
				orgList[start+1] = temp;
				return;
			}
		}
		
		double avgGap, min = orgList[start], max= orgList[start];
		for(int i = 0; i < length; i++) {
			double pas = orgList[i+start];
			if(min>pas) min = pas;
			if(max<pas) max = pas;
		}
		
		if(max==min)return;
		else avgGap = (max-min)/(length-1);
		
		for(int i = 0; i < length; i++) {
			double pas = orgList[i+start];
			list2[i+start] = pas;
			int index = (int)((pas-min)/avgGap);
			keyChain[index + keySize]++;
		}
		
		int maxIndex = 0;
		
		for(int i = keySize; i < keySize+length+1; i++) {//Accumulates the digit within the chain and shifts them to the right by 1 index
			double temp = keyChain[i];
			keyChain[i] = maxIndex; //Accumulates the digits of the chain
			maxIndex += temp;
		}
		
		for(int i = 0; i < length; i++) {
			double pas = list2[i+start];
			int index = (int)((pas-min)/avgGap);
			orgList[keyChain[index+keySize] + start] = pas;
			keyChain[index]++;
		}
		
		for(int i = keySize+1; i<keySize+length; i++) {
			yuelongRecursion(keySize+length,start+keyChain[i-1], keyChain[i]-keyChain[i-1]);
		}
	}
	
	/**
	 * A very special sorting algorithm that works the best when the list comprises of random yet evenly scattered digits
	 * This method sorts things into ascending order
	 * @param list The list to be sorted
	 * @return sorted the sorted list
	 * @date 2017/06/23
	 */
	@SuppressWarnings("unchecked")
	public static List<Double> yuelongSort(List<Double> list){
		double max = list.get(0);
		double min = list.get(0);
		for(double each:list){
			if(max<each) max = each;
			if(min>each) min = each;
		}
		if(min==max) return list;
		int size = list.size();
		double avgGap = (max-min)/(size-1);
		LinkedList<Double>[] tr = (LinkedList<Double>[])(new LinkedList<?>[size]);
		for(double each : list){
			int estimatedIndex = (int)((each-min)/avgGap);
			if(tr[estimatedIndex]==null) 
			tr[estimatedIndex] = new LinkedList<Double>();
			tr[estimatedIndex].add(0,each);
		}
		
		list.clear();
		for(List<Double> eachList: tr){
			if(eachList!=null){
				 eachList = yuelongSort(eachList);
				 list.addAll(eachList);
			}
		}
		return list;
	}
	
	static int compareTime =0;
	
	/**
	 * A very special sorting algorithm that works the best when the list comprises of random yet evenly scattered digits
	 * This method sorts things into ascending order
	 * @param list The list to be sorted
	 * @return sorted the sorted list
	 * @date 2017/06/23
	 */
	@SuppressWarnings("unchecked")
	public static List<Integer> yuelongSortInt(List<Integer> list){
		
		double max = list.get(0);
		double min = list.get(0);
		for(int each:list){
			if(max<each) max = each;
			if(min>each) min = each;
		}
		if(min==max) return list;
		int size = list.size();
		double avgGap = (max-min)/(size-1);
		LinkedList<Integer>[] tr = (LinkedList<Integer>[])(new LinkedList<?>[size]);
		for(double each : list){
			compareTime++;
			int estimatedIndex = (int)((each-min)/avgGap);
			if(tr[estimatedIndex]==null) 
			tr[estimatedIndex] = new LinkedList<Integer>();
			tr[estimatedIndex].add(0,(int)each);
		}
		
		list.clear();
		for(List<Integer> eachList: tr){
			if(eachList!=null){
				 eachList = yuelongSortInt(eachList);
				 list.addAll(eachList);
			}
		}
		return list;
	}
	
	/**
	 * Finds the derivative of a function at a certain point. The copyright of the mechanism is subjected of Yuelong Li
	 * @author Yuelong Li
	 * @param function function to be derived
	 * @param input defines the point where the derivative is to be found
	 * @return the derivative of the function at input
	 */
	public static double getDerivative(DoubleFunction function, double input) {
		double dx = setZero(function.function(input));
		return (function.function(input + dx)-function.function(input))/dx;
	}
	
	/**
	 * Returns a proper dx constant that helps with the process of derivatives
	 * @param numericSample Value of the function at x0
	 * @return the numeric value of dx
	 */
	public static double setZero(double numericSample) {
		int exponent = (int) Math.round(0.11536 * Math.log(100.58 + numericSample) - 6.8068);
		return Math.pow(10, exponent);
	}
	
	
	
	@FunctionalInterface
	public interface  DoubleFunction {
		public double function(double x);
	}

}
