package methods;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import java.util.HashMap;

import data.ListString;
import detection.data.DetectionResult;
import metrics.ROCHelper;
import model.Library;
import model.Profile;
import model.Sequence;


public class MarkovMethod implements VariableLengthMethod<String> {
	
	protected int W;   //  W is the maximum length of variable-length sequence
	protected List<Integer> lengths;  //  is the set of lengths of variable-length sequences
	protected int N;   //  N is the number of sets, N+1 is the the number of states
	protected Map<Integer, Integer> weights;   //  the weights of of frequencies of short sequences
	protected Map<Integer, Integer> weights2;   //  the weights of of frequencies of weighted frequencies
	
	protected Library LGS; 
	protected Profile Lambda;
	
	protected List<Float> A;
	protected float[][] P;
	
	protected int windowSize;
	protected double eta;
	protected float thresholdDecision;
	protected float thresholdIdf;
	
	
	public MarkovMethod() {
		// defining the lengths of variable-length sequences
		lengths = new ArrayList<Integer>();
		weights = new HashMap<Integer, Integer>();
		weights2 = new HashMap<Integer, Integer>();
		
		LGS = new Library();
		Lambda = new Profile();

	}
	
	@Override 
	public void filterProfile() {}

	//  Dividing LGS into N-1 sets 
	public Profile divideToSets() {   		

		int H = LGS.size();
		int u = Math.floorDiv(H, N); //  u is alfa in the paper
		int v = u + 1; //  v is beta in the paper
		int h = H - N*u;
		assert( H == h*v + (N-h)*u );		
		
		//  Creating lambda Profile
		Profile Lambda = new Profile();
		for ( int i = 0; i < h; ++i ) {   //  h*v sets  
			ArrayList<Sequence> sequences = new ArrayList<Sequence>();  // to build lambda[i] 
			for ( int j = 0; j < v; ++j ) {
				sequences.add( LGS.get( i*v + j ) );   //  lambda[i] = LGS
			}
			Lambda.add(sequences);
		}
		//  note that the last added element has index h*v-1
		
		for ( int i = 0; i < N-h; ++i ) {   //  (N-h)*u sets
			ArrayList<Sequence> sequences = new ArrayList<Sequence>();  // to build lambda[i] 
			for ( int j = 0; j < u; ++j ) {
				sequences.add( LGS.get( h*v + i*u + j ) );   //  lambda[i] = LGS
			}
			Lambda.add(sequences);
		}
		assert( N == Lambda.size() );
		//  note that the last added element has index H-1 == t(1) + t(2) + ... + t(W) - 1 
		//  end of creating lambda Profile
	
		return Lambda;
	}

	public List<List<String>> mineBehavioralPattern(List<String> s, int j) {
		
		int kMax = 0;
		List<String> shortSequenceMax = ListString.getString( s, j, j + lengths.get(kMax) );
 		for ( int k = 1; k < W; ++k ) {
 			List<String> shortSequence = ListString.getString( s, j, j + lengths.get(k) );   //  take l.get(i) characters from s starting at j
 			if ( LGS.getWeightedFrequency(shortSequence) > LGS.getWeightedFrequency(shortSequenceMax) )
 				shortSequenceMax = shortSequence;
		}
 		
 		List<String> g;
 		if ( LGS.getWeightedFrequency(shortSequenceMax) > 0 )
 			g = shortSequenceMax;
 		else
 			return null;
 		
 		List<List<String>> pattern = new ArrayList<List<String>>();
 		pattern.add(g);
 		
 		return pattern;
	}
	
	public List<Integer> defineStates(List<String> s) {
		List<Integer> states = new ArrayList<Integer>();
		
		int r = s.size();
		int j = 0;
		while ( j < r - lengths.get(W-1) + 1 ) {
			List<List<String>> pattern = mineBehavioralPattern(s, j);
			
			int i;
			int state;
			if ( pattern != null ) {   // pattern is not empty
				List<String> g = pattern.get(0);
				
				state = Lambda.getIndex(g);   // get index of set in Lambda where g belongs to
				i = g.size();
			}
			else {
				state = N;
				i = 1;
			}
			
			states.add(state);
			j = j+i;	
		}
		
		return states;
	}
	
	public void buildProbabilityDistributions(List<Integer> states) {	
	    	// initializing the parameters
		int N1 = N + 1;   // N1 is the number of of different states
		P = new float[N1][N1];
		int [] Y = new int[N1];
		for ( int i = 0; i < N1; ++i ) {    
			Y[i] = 0;
			for ( int j = 0; j < N1; ++j ) {
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
		for (int i = 0; i < N1; ++i) {
			A.add( (float) Y[i] / (float) M );   //  normalize the occurrence times of every state
			for ( int j = 0; j < N1; ++j )
				if ( Y[i] != 0 )
					P[i][j] = P[i][j] / (float) Y[i];
		}
			
		P[N1-1][N1-1] = 1;
	}

	@Override
	public void train(List<String> trainingSequence) {
		System.out.println("Training:\ntrainingSequence = " + trainingSequence + "\ntrainingSequence's size = " +  trainingSequence.size() );

		LGS = new Library();
		LGS.generateVariableLengthSequences(trainingSequence, lengths, weights, weights2);
		
		LGS.sort(Sequence.SequenceFrequencyComparator);
		Lambda = divideToSets();   //  divide the LGS (Library of General Sequences) into N-1 sets and store it in a Profile
		
		List<Integer> states = defineStates(trainingSequence);
		System.out.println("states = " + states + ", size = " + states.size());
		
		this.buildProbabilityDistributions(states);		
	}
	
	
	@Override
	public DetectionResult detect(List<String> monitoredUserBehavior) {
		return detect(monitoredUserBehavior, thresholdDecision);
	}
	
	@Override
	public DetectionResult detect(List<String> monitoredUserBehavior, double thresholdDecision) {
		//  checking if monitoredUserBehavior data size is bigger than minSize to start the detection
		checkIfStartDetection( monitoredUserBehavior.size() );
			
		ROCHelper rocHelper =  new ROCHelper();                                     
		
		List<Integer> states = defineStates(monitoredUserBehavior);
		
		List<String> stateSequences = preprocessStates(states);
		Map<String, Float> Probabilities = calculateProbabilities(states, stateSequences);
		List<Float> D = calculateDecisionValues(stateSequences, Probabilities, windowSize);
	
		// classify the decision values
		for ( int i = 0; i < D.size(); ++i ) {
				boolean decisionResult = ( D.get(i) >= thresholdDecision ) ? false : true;
				rocHelper.add(decisionResult);
		}
 		
		return new DetectionResult(D, rocHelper.getNegativeRate(), rocHelper.getPositiveRate(), rocHelper.getNegative(), rocHelper.getPositive() );
	}
	
	protected void checkIfStartDetection(int size) {
		int minSize = lengths.get( lengths.size()-1 ) + windowSize;   //  minSize for monitored user behavior data
		if ( size > minSize )
			System.out.print("");
		else 
			System.out.println("The condition to start the detection stage is not satisfied!");
	}

	protected List<String> preprocessStates(List<Integer> states) {
		List<String> stateSequences =  new ArrayList<String>();
		
		int u = 2; 
		for ( int i = 0; i < states.size() - u + 1; ++i) {
			StringBuilder stateSequence_i = new StringBuilder(); 
			for ( int j = i; j < i + u; ++j)
				stateSequence_i.append( states.get(j) ); 
			
			stateSequences.add( stateSequence_i.toString() );
		}
		
		return stateSequences;
	}
	
	protected Map<String, Float> calculateProbabilities(List<Integer> states, List<String> stateSequences) {
		Map<String, Float> Probabilities = new HashMap<String, Float>();
		
		int u = 2;
		for ( int i = 0; i < states.size() - u + 1; ++i) {
				float stateSequenceProbability = A.get( states.get(i) );
				for ( int j = i; j < i + u - 1; ++j ) {
					stateSequenceProbability *=  P [states.get(j)] [states.get(j+1)];   //  A[st(i)] x P[st(i)][st(i+1)]
				}
				Probabilities.put( stateSequences.get(i), stateSequenceProbability );
			}
				
		return Probabilities;
	}

	protected List<Float> calculateDecisionValues(List<String> stateSequences, Map<String, Float> Probabilities, int windowSize) {
		List<Float> D = new ArrayList<Float>();
		
		for ( int n = windowSize; n < stateSequences.size(); ++n )  {
			// getting the summ of Pr(qi) over the window windowSize
			float summ = 0;
			for ( int i = n - windowSize; i < n; ++i ) {
				String stateSequence_i = stateSequences.get(i);   //  g contains l.get(i) elements
				summ += ( Probabilities.get(stateSequence_i) - eta);
			}
			
			D.add( summ / (float) windowSize );   //  D[n] = summ 
		}
	 	
	 	return D;
	}

	private float getProb(float probability) {
		float delta = (float) Math.pow(10, -20);
		if ( probability > delta )
			return probability;
		return delta;
	}
	
	public void setTrainingParameters(List<Integer> lengths, int N, Map<Integer, Integer> weights, Map<Integer, Integer> weights2) {
		this.lengths = lengths;
		this.W = lengths.size();
		this.N = N;
		this.weights = weights;
		this.weights2 = weights2;   //  only used for MarkovF2 based methods
	}
	
	public void setDetectionParameters(int windowSize, double eta, float thresholdDecision) {
		this.windowSize = windowSize;
		this.eta = eta;
		this.thresholdDecision = thresholdDecision;
	}
	
}
