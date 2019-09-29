//package methods;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import data.ListString;
//import model.Library;
//import model.Profile;
//import model.Sequence;
//
//
//public class MarkovFVMethod extends MarkovMethod {
//	
//	protected Profile Lambda2;
//	protected List<Profile> Lambda_;
//	private int V;
//	
//	public MarkovFVMethod() {
//		super();
//		
//		Lambda2 = new Profile();
//		Lambda_ = new ArrayList<Profile>();
//		V = 2;
//	}
//
//	public List<String> mineBehavioralPattern(List<String> s, int j) {
//		
//		List<String> pattern = new ArrayList<String>();
//		for ( int v = 1; v <= V; ++v ) {
//			int kMax = 0;
//			String shortSequenceMax = ListString.getString( s, j, j + lengths.get(kMax) );
//	 		for ( int k = 1; k < W; ++k ) {
//	 			String shortSequence = ListString.getString( s, j, j + lengths.get(k) );   //  take l.get(i) characters from s starting at j
//		 		if ( LGS.getWeightedFrequency(shortSequence) > LGS.getWeightedFrequency(shortSequenceMax) )
//		 			shortSequenceMax = shortSequence;
//			}
//	
//		 	int length = LGS.getWeightedFrequency(shortSequenceMax) > 0 ? shortSequenceMax.length() : 1;
//	 		
//	 		String g_v = ListString.getString(s, j, j + length);
//	 		pattern.add(g_v);
//		}
// 		return pattern;
//	}
//	
//	public List<Integer> defineStates(List<String> s) {
//		List<Integer> states = new ArrayList<Integer>();
//		
//		int r = s.size();
//		int j = 0;
//		while ( j < r - lengths.get(W-1) + 1 ) {
//			List<String> pattern = mineBehavioralPattern(s, j);
//		    
//			int state = 0;  //   sigma_m
//			int iMax = 1;
//			for ( int v = 0; v < V; ++v ) {
//				String g = pattern.get(v);   //  getting behavioral pattern with maximum weighted frequency at position j
//				int state_v = Lambda_.get(v).getIndex(g);   //  getting index of the set in Lambda[v] where g belongs to
//				state += state_v * Math.pow(N, V-v-1);
//				
//				iMax = g.length() > iMax ? g.length() : iMax;
//				
//			}
//			states.add(state);
//			j += iMax;
//		}
//		
//		return states;
//	}
//
//	public void buildProbabilityDistributions(List<Integer> states) {	
//	    //  initializing the parameters
//		int NV = (int) Math.pow(N, V);
//		P = new double[NV][NV];
//		int [] Y = new int[NV];
//		for ( int i = 0; i < NV; ++i ) {    
//			Y[i] = 0;
//			for ( int j = 0; j < NV; ++j ) {
//				P[i][j] = 0;
//			}
//		}
//		
//		int m, n = 0;
//		int M = states.size();
//		for ( int i = 0; i < M; ++i ) {
//			m = states.get(i);
//			++Y[m];   //  sum up the occurrence times of the states
//			if ( i < M-1 ) {
//				n = states.get(i+1);
//				++P[m][n];   //  sum up the times of the transitions between states
//			}
//		}
//			
//		A = new ArrayList<Double>();
//		for (int i = 0; i < NV; ++i) {
//			A.add( (double) Y[i] / (double) M );   //  normalize the occurrence times of every state
//			for ( int j = 0; j < NV; ++j )
//				if ( Y[i] != 0 )
//					P[i][j] = P[i][j] / (double) Y[i];
//		}
//			
//		P[NV-1][NV-1] = 1;
//	}
// 
//	@Override
//	public void train(List<String> trainingSequence) {
//		System.out.println("Training F2:\ntrainingSequence = " + trainingSequence + "\ntrainingSequence's size = " +  trainingSequence.size() );
//		LGS = new Library();
//		LGS.generateVariableLengthSequences(trainingSequence, lengths, weights, weights2);
//		filterProfile();
//		
//		LGS.sort(Sequence.SequenceFrequencyComparator);
//		Lambda = divideToSets();    //  divide the LGS (Library of General Sequences) into N-1 sets and store it in a Profile
//		
//		LGS.sort(Sequence.SequenceFrequency2Comparator);
//		Lambda2 = divideToSets();
//		
//		Lambda_.add(Lambda); Lambda_.add(Lambda2);
//		
//		List<Integer> states = defineStates(trainingSequence);
////		System.out.println("states = " + states + ", size = " + states.size());
//
//		this.buildProbabilityDistributions(states);	
//	}
//
//}
