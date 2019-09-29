package metrics;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;


public class ROCHelper {
	private int positive;
	private int negative;
	ArrayList<Integer> decisionValues;
	
	private List<Float> positivesRates;
	private List<Float> negativesRates;
	
	public ROCHelper() {
		positive = 0;
		negative = 0;
		
		positivesRates = new ArrayList<Float>();
	    negativesRates = new ArrayList<Float>();
	}
	
	public void add(boolean isPositive) {
		if (isPositive)
			++positive;
		else
			++negative;
		
		positivesRates.add( getPositiveRate() );
		negativesRates.add( getNegativeRate() );
	}
	
	public void add(ArrayList<Integer> decisionValues) {
		this.decisionValues = decisionValues;
	}
	
	public void addPositive() {
		++positive;
	}
	
	public void addNegative() {
		++negative;
	}
	
	public float getPositive() {
		return positive;
	}
	
	public float getNegative() {
		return negative;
	}
	
	public float getPositiveRate() { 
		return (float) positive / (float) (positive + negative);
	}
	
	public float getNegativeRate() { 
		return (float) negative / (float) (positive + negative);
	}
	
	public List<Float> getPositiveRates() { 
		return positivesRates;
	}
	
	public List<Float> getNegativeRates() { 
		return negativesRates;
	}
	
	public List< Pair<Float, Float> > getROCValues(List<Float> negativesRates) {
		List< Pair<Float, Float> > roc = new ArrayList< Pair<Float, Float> >();
		for (int i = 0; i < negativesRates.size(); ++i) { 
			roc.add( new Pair<Float, Float>( negativesRates.get(i), this.positivesRates.get(i) ) );
		}
		
		return roc;
	}
	
//	public float getTPR() {
//		return TP / (TP + FN); 
//	}
//	
//	public void getFNR() {
//		return FN / (FN + TP); 
//	}
//	
//	
//	public void getFPR() {
//		return FP / (FP + TN); 
//	}
//	
//	public float getTNR() {
//		return TN / (TN + FP); 
//	}

}
