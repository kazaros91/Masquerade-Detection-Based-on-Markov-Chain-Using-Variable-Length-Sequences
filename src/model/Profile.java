package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import data.ListString;

public class Profile {
	private  List< List<Sequence> > sequences;
	
	private  Map<List<String>, Double> F;   //  frequencies
	private  Map<Double, Double> FF;   //  frequencies of frequencies  
	private Map<Integer, Integer> weights;   //  is the set of weights of variable-length of frequencies
	
	private static Map<List<String>, Integer> staticOccurrences = new HashMap<List<String>, Integer>();
	private static Map<List<String>, Double> IDF = new HashMap<List<String>, Double>();
	private static boolean IDF_VALUES_NOT_CALCULATED = false;
	
	public Profile() {
		sequences = new ArrayList< List<Sequence> >();
		
		F = new HashMap<List<String>, Double>();
		FF = new HashMap<Double, Double>();
		
		weights = new HashMap<Integer, Integer>();
	}
	
	public void add(List<Sequence> sequence) {
		sequences.add(sequence);
	}

	public boolean contains(int index, List<String> g) {
		List<Sequence> sequencesIndex = sequences.get(index);
		for (int j = 0; j < sequencesIndex.size(); ++j) {
			if ( sequencesIndex.get(j).getString().equals(g) ) {
				return true;
			}
		}
		return false;
	}
	
	public int getIndex(List<String> g) {
		int i = 0;
		for ( i = 0; i < this.size(); ++i ) {
			if ( contains(i, g) )
				break;
		}
		return i;
	}
	
	public void remove(int i, int j) {
		this.sequences.get(i).remove(j);
	}
	
	public Sequence get(int i, int j) {
		return sequences.get(i).get(j);
	}
	
	public int size() {
		return sequences.size();
	}

	public int size(int i) {
		return sequences.get(i).size();
	}

	
	public void generateVariableLengthSequences(List<String> inputSequence, List<Integer> l, Map<Integer, Integer> weights) {
		// getting all variable-length sequences and counting its occurrences
		this.weights = weights;
		
		int W = l.size();
		for (int i = 0; i < W; ++i) {
			calculateFrequencies(inputSequence, l, i, F);
		}
		for (int i = 0; i < W; ++i) {
			// getting all sequences of the length l.get(i)
			List<Sequence> sequence = new ArrayList<Sequence>();
			int k = inputSequence.size() - l.get(i) + 1;
			for (int j = 0; j < k; ++j) {
				List<String> shortSequence = ListString.getString( inputSequence, j, j + l.get(i) );
				sequence.add( new Sequence(shortSequence, getWeightedFrequency(shortSequence), 0.0) );
			}
			this.add(sequence);   //  add to the Profile
		}
	}
	
	protected Map<List<String>, Double> calculateFrequencies(List<String> inputSequence, List<Integer> l, int i, 
			Map<List<String>, Double> Frequencies) {
		// calculating the frequencies of all short sequences
		Map<List<String>, Integer> Ocurrence = new HashMap<List<String>, Integer>();
		int k = inputSequence.size() - l.get(i) + 1;
		for (int j = 0; j < k; ++j) { 
			List<String> shortSequence = ListString.getString( inputSequence, j, j + l.get(i) );
			//  calculating the occurrences of the short sequence
			if ( !Ocurrence.containsKey(shortSequence) ) {   
				Ocurrence.put(shortSequence, 1);   //  initialize with frequency 1
				// if the short sequence occurs first time in the Profile, calculate its occurrence in all Profiles
				calculateStaticOccurrences(shortSequence);
			}
			else
				Ocurrence.put(shortSequence, Ocurrence.get(shortSequence) + 1);   //  update frequency number;	
		}	
		
		// calculating the frequencies of all shortSequences
		for ( Entry<List<String>, Integer> entry : Ocurrence.entrySet() ) {
			List<String>shortSequence = entry.getKey();
			double occurrence = (double) entry.getValue();
			Frequencies.put( shortSequence, occurrence / (double) k );
		}
		
		return Frequencies;
	}
	
	private static void calculateStaticOccurrences(List<String> shortSequence) {
		if ( !staticOccurrences.containsKey(shortSequence) ) 
			staticOccurrences.put(shortSequence, 1);
		else 
			staticOccurrences.put(shortSequence, staticOccurrences.get(shortSequence) + 1);
	}

	public static void calculateIDFValues(int numberOfUSers) {
		if ( !IDF_VALUES_NOT_CALCULATED ) {
			for ( Entry<List<String>, Integer> entry : staticOccurrences.entrySet() ) {
				List<String> shortSequence = entry.getKey();
				int occurrence = entry.getValue();
				double idf = (double) numberOfUSers / (double) occurrence;
				IDF.put(shortSequence, idf);
			}
			IDF_VALUES_NOT_CALCULATED = true;
		}
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
			return FF.get(this.getFrequency(s));
		else 
			return 0.0;
	}
	
    // added
	public Double getWeightedFrequency2(List<String> s) {
		int length = s.size();
		return weights.get(length) * this.getFrequency2(s);
	}

	
	public static double getIDF(String g) {
		if ( IDF.containsKey(g) )
			return IDF.get(g);
		else
			return 10000;   //  just return enough big value, since it is high recognizable pattern, since hase not appear at the training stage;
	}
	
	
	public String toString() {
		StringBuilder s = new StringBuilder(); 
		for (int i = 0; i < sequences.size(); ++i) {
			s.append("\n");
			for (int j = 0; j < sequences.get(i).size(); ++j)
				s.append(sequences.get(i).get(j).toString() + " " );
		}
		
		return s.toString();
	}
	
	// uncomment for testing Profile class
//	public static void main(String [] args) {
//		List< List<String> > s = new ArrayList< List<String>>();
//			s.add( ListString.getList("122113213123121312311313") );
//		    s.add( ListString.getList("214312131321313213233232") );
//			s.add(  ListString.getList("431223123324122322421212") );
//			s.add(   ListString.getList("123124213213212323212342") );
//			s.add(   ListString.getList("321341413324313221342331") );
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
//		List<Profile> profiles = new ArrayList<Profile>();
//		for ( int i = 0; i < s.size(); ++i ) {
//			Profile profile = new Profile();
//			profile.generateVariableLengthSequences(s.get(i), lengths, weights);
//			profiles.add(profile);
//		}
//
//		Profile.calculateIDFValues(5);
//		System.out.println(profiles.get(0));
//		profiles.get(0).remove(2, 5);
//		System.out.println("After remove:\n" + profiles.get(0));
//	}
	
}
