package ui;

import java.util.ArrayList;
import java.util.List;

public class Conversion {
	public static String[] toStringList(String input){
		List<String> sort = new ArrayList<String>(0);
		String StringBundle = "";
		char[] inputList;
		inputList = input.toCharArray();
		for(char a:inputList){
			if(a == ' '){
				sort.add(StringBundle);
				StringBundle = "";
			}
			else StringBundle += Character.toString(a);
		}
		sort.add(StringBundle);
		return sort.toArray(new String[sort.size()]);
	}
	
	public static String[] toStringList(String input,String divider){
		List<String> sort = new ArrayList<String>(0);
		String StringBundle = "";
		char[] inputList;
		inputList = input.toCharArray();
		for(char a:inputList){
			if(divider.indexOf(a)>=0){
				if(StringBundle!="")
				sort.add(StringBundle);
				StringBundle = "";
			}
			else StringBundle += Character.toString(a);
		}
		if(StringBundle!="")
		sort.add(StringBundle);
		return sort.toArray(new String[sort.size()]);
	}
	public static String[] toStringList(String input,String divider,String visibleDividers){
		List<String> sort = new ArrayList<String>(0);
		String StringBundle = "";
		char[] inputList;
		inputList = input.toCharArray();
		for(char a:inputList){
			if(divider.indexOf(a)>=0){
				if(StringBundle!="")
					sort.add(StringBundle);
				if(visibleDividers.indexOf(a)>=0)
					sort.add(String.valueOf(a));
				StringBundle = "";
			}
			else StringBundle += Character.toString(a);
		}
		if(StringBundle!="")
		sort.add(StringBundle);
		return sort.toArray(new String[sort.size()]);
	}
	public static String[] toStringList(String input, char divider){
		List<String> sort = new ArrayList<String>(0);
		String StringBundle = "";
		char[] inputList;
		inputList = input.toCharArray();
		for(char a:inputList){
			if(a == divider){
				if(StringBundle!="")
				sort.add(StringBundle);
				StringBundle = "";
			}
			else StringBundle += Character.toString(a);
		}
		sort.add(StringBundle);
		return sort.toArray(new String[sort.size()]);
	}
	
	public static String filter(String input, String divider){
		StringBuilder filterString = new StringBuilder();
		for(String piece:toStringList(input,divider)){
			filterString.append(piece);
		}
		return filterString.toString();
	}
	
	public static String replace(String input, String divider,String replacer){
		List<String> sort = new ArrayList<String>(0);
		String StringBundle = "";
		char[] inputList;
		inputList = input.toCharArray();
		for(char a:inputList){
			if(divider.indexOf(a)>=0){
				if(StringBundle!="")
					sort.add(StringBundle+replacer);
				else
					sort.add(replacer);
				StringBundle = "";
			}
			else StringBundle += Character.toString(a);
		}
		if(StringBundle!="")
		sort.add(StringBundle);
		StringBuilder filterString = new StringBuilder();
		for(String piece:sort){
			filterString.append(piece);
		}
		return filterString.toString();
	}
	
	public static List<char[]> StringtoChar(String input, String divider){
		String[] Strings;
		List <char[]> chars = new ArrayList<char[]>(0);
		Strings = toStringList(input,divider);
		for(int i = 0; i<Strings.length; i++){
			chars.add(Strings[i].toCharArray());
		}
		return chars;
	}
	
	public static ArrayList<ArrayList<Double>> clone(List<ArrayList<Double>> list) {
		ArrayList<ArrayList<Double>> cloned = new ArrayList<ArrayList<Double>>();
		for(ArrayList<Double> pass: list){
			cloned.add((ArrayList<Double>)pass.clone());
		}
		return cloned;
	}
	
	public static String getScientific(double num, int roundplace){
		if(num == 0.0)return String.valueOf(0.0);
		int log=0;
		if(Math.abs(num)>1){
			log = (int)Math.log10(Math.abs(num));
		}
		if(Math.abs(num)<1) log = (int)Math.floor(Math.log10(Math.abs(num)));
		num/=Math.pow(10, log-roundplace);
		num = (int)num;
		num/=Math.pow(10, roundplace);
		return num + " E" +log;
	};
	
	public static String getNumberLabel(double num, int roundplace){
		if(num == 0.0)return String.valueOf(0.0);
		int log=0;
		if(Math.abs(num)>1){
			log = (int)Math.log10(Math.abs(num));
		}
		if(Math.abs(num)<1) log = (int)Math.floor(Math.log10(Math.abs(num)));
		num/=Math.pow(10, log-roundplace);
		num = (int)num;
		num*=Math.pow(10, -roundplace);
		if(log<3&&log>=0) {
			num*=Math.pow(10, log);
		}
		String numb = String.valueOf(num);
		if(numb.length()>5)numb = numb.substring(0, 5);
		return  numb + ((log<3&&log>=0)?"":(" E" +log));
	};
	
}
