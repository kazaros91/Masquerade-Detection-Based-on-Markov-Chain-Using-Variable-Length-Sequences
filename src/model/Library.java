package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.ListString;

import java.util.Map.Entry;

public class Library {
//	protected List<Integer> lengths;  //  is the set of lengths of variable-length sequences
//	protected int W;  //  the size of lengths
	private Map<Integer, Integer> weights;  //  is the set of weights of variable-length of frequencies
	private Map<Integer, Integer> weights2;  //  is the set of weights of variable-length of frequencies
	
	private Map<List<String>, Double> F;   //  frequencies
	private Map<Double, Double> FF;   //  frequencies of frequencies  
	
	private List<Sequence> sequences;
	
	public Library() {
		sequences = new ArrayList<Sequence>();
		
		F = new HashMap<List<String>, Double>();
		FF = new HashMap<Double, Double>();
		
		weights = new HashMap<Integer, Integer>();
		weights2 = new HashMap<Integer, Integer>();
	}
	
	public void add(Sequence sequence) {
		sequences.add(sequence);
	}

	public boolean contains(String g) {
		for (int i = 0; i < sequences.size(); ++i) {
			if ( sequences.get(i).getString().equals(g) )
				return true;
		}
		
		return false;
	}
	
	public final Sequence get(int index) {
		return sequences.get(index);
	}
	
	public int size() {
		return sequences.size();
	}

	public void sort(final Comparator<Sequence> sequenceFrequencyComparator) {
		sequences.sort(Sequence.SequenceFrequencyComparator);
	}
	
	public void generateVariableLengthSequences(List<String> inputSequence, List<Integer> l, Map<Integer, Integer> weights, Map<Integer, Integer> weights2) {
		// getting all variable-length sequences and counting its frequencies
		this.weights = weights;
		this.weights2 = weights2;
		
		int W = l.size();
		
		for (int i = 0; i < W; ++i ) {
			calculateFrequencies(inputSequence, l, i, F);
		}
		// calculate Frequency of Frequencies
		for (int i = 0; i < W; ++i ) {
			calculateFrequencies2(inputSequence, l, i, FF);
		}
		
		for ( Entry<List<String>, Double> entry : F.entrySet() ) {
			List<String> shortSequence = entry.getKey();
			double wf = getWeightedFrequency(shortSequence);
			double wf2 = getWeightedFrequency2(shortSequence);
			this.add( new Sequence( shortSequence, wf, wf2) );
		}
	}
	
	public Map<List<String>, Double> calculateFrequencies(List<String> inputSequence, List<Integer> lengths, int i, 
			Map<List<String>, Double> Frequencies) {
		int k = inputSequence.size() - lengths.get(i) + 1;
		Map<List<String>, Integer> Occurrence = new HashMap<List<String>, Integer>();
		for (int j = 0; j < k; ++j) { 
			List<String> shortSequence = ListString.getString( inputSequence, j, j + lengths.get(i) );
			if ( !Occurrence.containsKey(shortSequence) ) {   //  calculating occurrences
				Occurrence.put(shortSequence, 1);   //  initialize with frequency 1
			}
			else
				Occurrence.put(shortSequence, Occurrence.get(shortSequence) + 1);   //  update frequency number;	
		}
		
		//   calculating the frequencies and storing in sequences
//		Map<String, Double> Frequencies = new HashMap<String, Double>();
		for ( Entry<List<String>, Integer> entry : Occurrence.entrySet() ) {
			List<String> shortSequence = entry.getKey();
			double occurrence = (double) entry.getValue();
//			System.out.println( shortSequence + " ---> " +  occurrence + " / " +  k + " = " + occurrence / (double) k + ", weight = " + weights.get(shortSequence.length() - 2) );
			Frequencies.put( shortSequence, occurrence / (double) k );
		}
		
		return Frequencies;
	}
	
	// calculate frequency of frequencies
	public Map<Double, Double> calculateFrequencies2(List<String> inputSequence, List<Integer> lengths, int i, Map<Double, Double> Frequencies2) {
		int k = inputSequence.size() - lengths.get(i) + 1;
		Map<Double, Integer> Ocurrence = new HashMap<Double, Integer>();
		for (int j = 0; j < k; ++j) { 
			List<String> shortSequence = ListString.getString( inputSequence, j, j + lengths.get(i) );
			double weightedFrequency = getWeightedFrequency(shortSequence);
			if ( !Ocurrence.containsKey(weightedFrequency) ) {   //  calculating occurrences
				Ocurrence.put(weightedFrequency, 1);   //  initialize with frequency 1.0
			}
			else
				Ocurrence.put(weightedFrequency, Ocurrence.get(weightedFrequency) + 1 );   //  increment FF[weightedFrequency]
		}
		
		//   correcting the frequencies and storing in sequences
//		Map<Double, Double> Frequencies = new HashMap<Double, Double>();
		for ( Entry<Double, Integer> entry : Ocurrence.entrySet() ) {
			double weightedFrequency  = entry.getKey();
			double occurrence = (double) entry.getValue();
			Frequencies2.put( weightedFrequency, occurrence / (double) k );
		}
		
		return Frequencies2;
	}
	
	public Double getFrequency(List<String> s) {
		if ( F.containsKey(s) )
			return F.get(s);
		else 
			return 0.0;
	}
	
	public Double getWeightedFrequency(List<String> s) {
		int length = s.size();
		return weights.get(length) * this.getFrequency(s);
	}
	
	// added
	private Double getFrequency2(List<String> s) {
		if ( F.containsKey(s) )
//			return FF.get( this.getFrequency(s) );
			return FF.get( this.getWeightedFrequency(s) );
		else 
			return 0.0;
	}
	
	// added
	public Double getWeightedFrequency2(List<String> s) {
		int length = s.size(); 
		return weights2.get(length) * this.getFrequency2(s);
	}

	
	public String toString() {
		StringBuilder s = new StringBuilder("\n"); 
		for (int i = 0; i < sequences.size(); ++i) {
			s.append(sequences.get(i).toString() + ",  " );
			if (i % 5 == 0)
				s.append('\n');
		}
		
		return s.toString();
	}

	public void remove(int index) {
		this.sequences.remove(index);
		
	}
	
	// uncomment for testing Library class
//	public static void main(String [] args) {
//		List<String> s = ListString.getList("122113213123121312311313");
//		
//		int W = 5;  // the number of lengths variable-length sequences
//		List<Integer> lengths = new ArrayList<Integer>();
//		for ( int i = 0; i < W; ++i )
//			lengths.add(i+2);
//		
//		Map<Integer, Integer> weights = new HashMap<Integer, Integer>();
//		for ( int i = 0; i < W; ++i ) 
//			weights.put( lengths.get(i), i+1 );
//		
//		Library library = new Library();
//		library.generateVariableLengthSequences(s, lengths, weights);
//		library.sort(Sequence.SequenceFrequencyComparator);
//		System.out.println(library);
//	}
	
}

