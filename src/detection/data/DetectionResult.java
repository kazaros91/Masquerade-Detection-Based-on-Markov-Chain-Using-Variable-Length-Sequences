package detection.data;

import java.util.List;


public class DetectionResult {
	List<Float> decisionValues;
	float negativesRate;
	float positivesRate;
	
	float negatives;
	float positives;
	
	public DetectionResult() {}
	
	public DetectionResult(List<Float> decisionValues, Float negativesRate, Float positivesRate, Float negatives, Float positives) {
		this.decisionValues = decisionValues;
		
		this.negativesRate = negativesRate;
		this.positivesRate = positivesRate;
		
		this.negatives = negatives;
		this.positives = positives;
	}
	
	public List<Float> getDecisionValues() {
		return decisionValues;
	}
	
	public float getNegativesRate() {
		return negativesRate;
	}
	
	public float getPositivesRate() {
		return positivesRate;
	}
	
	public float getNegatives() {
		return negatives;
	}
	
	public float getPositives() {
		return positives;
	}
	
}