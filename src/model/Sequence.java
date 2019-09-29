package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Sequence {

	private List<String> sequence;
	private double wFrequency;
	private double wFrequency2;
	private double IDF;
	
	
	public Sequence() {
	}

	public Sequence(List<String> sequence, double wFrequency, double wFrequency2) {
		this.sequence = sequence;
		this.wFrequency = wFrequency;
		this.wFrequency2 = wFrequency2;
	}

	
	public static Comparator<Sequence> SequenceFrequencyComparator 
				= new Comparator<Sequence>() {
		public int compare (Sequence sequence1, Sequence sequence2) {
			double f1 = sequence1.getWeightedFrequency();
			double f2 = sequence2.getWeightedFrequency();
			// descending order
			return Double.compare(f2, f1);
		}
	};
	
	public static Comparator<Sequence> SequenceFrequency2Comparator 
		= new Comparator<Sequence>() {
			public int compare (Sequence sequence1, Sequence sequence2) {
			double f1 = sequence1.getWeightedFrequency2();
			double f2 = sequence2.getWeightedFrequency2();
			// descending order
			return Double.compare(f2, f1);
		}
	};
	
	
	public void setString(final List<String> sequence) {
		this.sequence = sequence;
	}
	
	public List<String> getString() {
		return this.sequence;
	}
	
	
	public void setWeightedFrequency(double wFrequency) {
		this.wFrequency = wFrequency;
	}
	
	public double getWeightedFrequency() {
		return this.wFrequency;
	}
	
	
	public void setFrequency2(double wFrequency2) {
		this.wFrequency2 = wFrequency2;
	}
	
	public double getWeightedFrequency2() {
		return this.wFrequency2;
	}
	
	
	public void setIDF(final double idf) {
		this.IDF = idf;
	}
	
	public double getIDF() {
		return this.IDF;
	}
	
	
	public String toString()  {
		return getString() + " -> " + "(f1 = " + String.valueOf( getWeightedFrequency() ) + " "
								  + "f2 = "	+ String.valueOf( getWeightedFrequency2() ) + ")" ;
	}
	
//  uncomment to test Sequence
//	public static void main(String [] args) {
//	
//		List<Sequence> list = new ArrayList<Sequence>();
//		list.add(new Sequence("1", 1.0, 2.0));
//		list.add(new Sequence("2", 3.0, 1.0));
//		list.add(new Sequence("4", 1.4, 5.0));
//		list.add(new Sequence("11", 0.8, 2.0));
//		list.add(new Sequence("21", 3.0, 4.0));
//		list.add(new Sequence("13", 1.4, 2.5));
//		
////		Collections.sort(list, Sequence.SequenceFrequencyComparator);
//		list.sort(Sequence.SequenceFrequencyComparator);
//		System.out.println(list);
//		
//		Collections.sort(list, Sequence.SequenceFrequency2Comparator);
//		System.out.println(list);
//	}

	
}