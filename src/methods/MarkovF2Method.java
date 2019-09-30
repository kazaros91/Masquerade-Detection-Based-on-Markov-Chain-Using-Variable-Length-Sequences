package methods;

import java.util.ArrayList;
import java.util.List;

import data.ListString;
import model.Library;
import model.Profile;
import model.Sequence;


public class MarkovF2Method extends MarkovMethod {
	
	protected Profile Lambda2;
	
	public MarkovF2Method() {
		super();
		
		Lambda2 = new Profile();
	}

	public List<List<String>> mineBehavioralPattern(List<String> s, int j) {
		
		int kMax = 0;
		List<String> shortSequenceMax = ListString.getString( s, j, j + lengths.get(kMax) );
		List<String> shortSequenceMax2 = ListString.getString( s, j, j + lengths.get(kMax) );
 		for ( int k = 1; k < W; ++k ) {
 			List<String> shortSequence = ListString.getString( s, j, j + lengths.get(k) );   //  take l.get(i) characters from s starting at j
	 		if ( LGS.getWeightedFrequency(shortSequence) > LGS.getWeightedFrequency(shortSequenceMax) )
	 			shortSequenceMax = shortSequence;
	 			
	 		// different from MarkovMethod
	 		if ( LGS.getWeightedFrequency2(shortSequence) > LGS.getWeightedFrequency2(shortSequenceMax2) )
	 	 		shortSequenceMax2 = shortSequence;
		}

 		List<String> g;
 		if ( LGS.getWeightedFrequency(shortSequenceMax) > 0 )
 			g = shortSequenceMax;
 		else
 			return null;
 		List<String> gg = shortSequenceMax2;
 		
 		// different from MarkovMethod
 		List<List<String>> pattern = new ArrayList<List<String>>();
 		pattern.add(g); pattern.add(gg);
 		return pattern;
	}
	
	public List<Integer> defineStates(List<String> s) {
		List<Integer> states = new ArrayList<Integer>();
		
		int r = s.size();
		int j = 0;
		while ( j < r - lengths.get(W-1) + 1 ) {
			List<List<String>> pattern = mineBehavioralPattern(s, j);
		    
			int iMax;
			int state;   
			if ( pattern != null ) {  // pattern is not empty
				List<String> g = pattern.get(0);   //  getting behavioral pattern with maximum weighted frequency at position j
 				int state0 = Lambda.getIndex(g);   //  The High-Frequency-First scheme to  match the states
				  
				// added 
				List<String> gg = pattern.get(1);   //  getting behavioral pattern with maximum weighted frequency2 at position j
				int state1 = Lambda2.getIndex(gg);
				state = state0 * N + state1;
				iMax = g.size() > gg.size() ? g.size() : gg.size();
			}
			else {
				state = N*N;
				iMax = 1;
			}
			
			states.add(state);
			j += iMax;
		}
		
		return states;
	}

	public void buildProbabilityDistributions(List<Integer> states) {	
	    //  initializing the parameters
		int N2 = N*N + 1;   //  N1 is the number of different states
		P = new float[N2][N2];
		int [] Y = new int[N2];
		for ( int i = 0; i < N2; ++i ) {    
			Y[i] = 0;
			for ( int j = 0; j < N2; ++j ) {
				P[i][j] = 0;
			}
		}
		
		int m, n = 0;
		int M = states.size();
		for ( int i = 0; i < M; ++i ) {
			m = states.get(i);
			++Y[m];   //  sum up the occurrence times of the states
			if ( i < M-1 ) {
				n = states.get(i+1);
				++P[m][n];   //  sum up the times of the transitions between states
			}
		}
			
		A = new ArrayList<Float>();
		for (int i = 0; i < N2; ++i) {
			A.add( (float) Y[i] / (float) M );   //  normalize the occurrence times of every state
			for ( int j = 0; j < N2; ++j )
				if ( Y[i] != 0 )
					P[i][j] = P[i][j] / (float) Y[i];
		}
			
		P[N2-1][N2-1] = 1;
	}
 
	@Override
	public void train(List<String> trainingSequence) {
		System.out.println("Training F2:\ntrainingSequence = " + trainingSequence + "\ntrainingSequence's size = " +  trainingSequence.size() );
		LGS = new Library();
		LGS.generateVariableLengthSequences(trainingSequence, lengths, weights, weights2);
		
		LGS.sort(Sequence.SequenceFrequencyComparator);
		Lambda = divideToSets();    //  divide the LGS (Library of General Sequences) into N-1 sets and store it in a Profile
		
		LGS.sort(Sequence.SequenceFrequency2Comparator);
		Lambda2 = divideToSets();
		
		List<Integer> states = defineStates(trainingSequence);

		this.buildProbabilityDistributions(states);	
	}

}
