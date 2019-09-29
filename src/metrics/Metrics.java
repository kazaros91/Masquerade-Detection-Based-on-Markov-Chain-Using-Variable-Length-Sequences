package metrics;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class Metrics {
	public float precision;
	public float TPR;
	public float FPR;
	public float F1;
	public float accuracy;
	
	private List<Float> FPRlist;
	private List<Float> TPRlist;
	private List<Float> FPRlist_all;
	private List<Float> TPRlist_all;
	
	public double threshold_decision;
	private List<Double> thresholds;
	public float threshold_mean;
	public float threshold_variance;
	
	private int roc_all_count;
	private int count;
	private float max = 0;
	private int roc_count;
	private String dir;
	public int experiment_count;
	
	
	public Metrics(String dir) {
		this.dir = dir;
		this.count =  0;
		this.roc_count = 0;
		this.roc_all_count = 0;
		this.max = 0f;
		thresholds = new ArrayList<Double>();
	}
	
	public Metrics() {
		this("");
	}
	
	public String getDir() {
		return this.dir;
	}
	
	public static Comparator<Metrics> MetricsComparator
		= new Comparator<Metrics>() {
			public int compare (Metrics metrics1, Metrics metrics2) {
			int diff = 0;
			diff += (Float.compare(metrics2.precision, metrics1.precision));
			diff += (Float.compare(metrics2.TPR, metrics1.TPR));
			diff += (Float.compare(metrics1.FPR, metrics2.FPR));
			diff += (Float.compare(metrics2.F1, metrics1.F1));
			diff += (Float.compare(metrics2.accuracy, metrics1.accuracy));
			diff += (Double.compare(metrics1.threshold_decision, metrics2.threshold_decision));
 			// ascending
			return diff;
		}	
	};
	
//	public Metrics(float precision, float TPR, float FPR, float F1, float accuracy) {
//		this.precision = precision;
//		this.TPR = TPR;
//		this.FPR = FPR;
//		this.F1 = F1;
//		this.accuracy = accuracy;
//		
//		this.threshold_decision = threshold_decision;
//		
//		this.count = 1;
//		this.roc_count = 0;
//		this.roc_all_count = 0;
//	}

	
	public static String [] titles() {
		String [] titles = {"Precision", "TPR", "FPR", "F1", "Accuracy", "threshold_variance", "threshold_mean"};
		return titles;
	}
	
	public float [] list() {
		float [] metrics = {precision, TPR, FPR, F1, accuracy, threshold_variance, threshold_mean};
		return metrics;
	}
	
	
	public void update(float TP, float FN, float FP, float TN, double threshold) {
		if (2*TP - FN - FP > this.max) {   // (TP - FP) + (TP - FN)
			this.max = 2*TP - FP - FN;   // (TP - FP) + (TP - FN)
		     
		    this.precision = TP / (TP + FP);
		    this.TPR = TP / (TP + FN); // recall
		    this.FPR = FP / (FP + TN);
		    this.F1 = 2*precision*TPR / (precision + TPR);
		    this.accuracy = (TP + TN) / (TP + TN + FP + FN);
		    this.threshold_decision = threshold;
		}
	}
	
	public void add_all(Metrics metrics) {
		this.precision += metrics.precision;
		this.TPR += metrics.TPR;
		this.FPR += metrics.FPR;
		this.F1 += metrics.F1;
		this.accuracy += metrics.accuracy;
		this.thresholds.add(metrics.threshold_decision);
		
		this.add_ROC_series_all(metrics.getFPRValues_all(), metrics.getTPRValues_all());
		++this.count;
	}
	
	public Metrics avg(int experiment_count) {
		this.precision /= (float) count;
		this.TPR /= (float) count;
		this.FPR /= (float) count;
		this.F1 /= (float) count;
		this.accuracy /= (float) count;
		
		threshold_mean_variance();
		normalizeROC_all();
		
		this.experiment_count = experiment_count;
		
		return this;
	}
	
	public void threshold_mean_variance() {
		// calculating mean
		threshold_mean = 0;
		int N = thresholds.size();
		for ( int i = 0; i < N; ++i ) {
			threshold_mean += thresholds.get(i);
		}
		threshold_mean /= thresholds.size(); 
		
		// calculating mean
		threshold_variance = 0;
		for ( int i = 0; i < N; ++i ) {
			threshold_variance += Math.pow((thresholds.get(i) - threshold_mean), 2);
		}
		threshold_variance = (float) Math.sqrt(threshold_variance / (float)N);
	}
	
	private void normalizeROC_all() {
//		assert(roc_count == count);
		this.FPRlist = new ArrayList<Float>();
		this.TPRlist = new ArrayList<Float>();
		
		assert(FPRlist_all.size() == TPRlist_all.size());
		for ( int i = 0; i < TPRlist_all.size(); ++i) {
			this.FPRlist.add(this.FPRlist_all.get(i) / (float)count); //  elementwise plus
			this.TPRlist.add(this.TPRlist_all.get(i) / (float)count); //  elementwise plus
		}
		
//		this.FPRlist = FPRlist_all;
//		this.TPRlist = TPRlist_all;
	}

	
	public void add_ROC_series(List<Float> FPRData, List<Float> TPRData) {
		if (roc_count == 0) {
			this.FPRlist = FPRData;
			this.TPRlist = TPRData;
		}
		else {
			assert( FPRlist.size() == TPRlist.size());
			for ( int i = 0; i < TPRlist.size(); ++i) {
				this.FPRlist.set(i, this.FPRlist.get(i) + FPRData.get(i)); //  elementwise plus
				this.TPRlist.set(i, this.TPRlist.get(i) + TPRData.get(i)); //  elementwise plus
			}
		}
		
		++roc_count;
	}
	
	public void add_ROC_series_all(List<Float> FPRData_all, List<Float> TPRData_all) {
		if (roc_all_count == 0) {
			this.FPRlist_all = FPRData_all;
			this.TPRlist_all = TPRData_all;
		}
		else {
			assert( TPRlist_all.size() == TPRlist_all.size());
			for ( int i = 0; i < TPRlist_all.size(); ++i) {
				this.FPRlist_all.set(i, this.FPRlist_all.get(i) + FPRData_all.get(i)); //  elementwise plus
				this.TPRlist_all.set(i, this.TPRlist_all.get(i) + TPRData_all.get(i)); //  elementwise plus
			}
		}
		
		++roc_all_count;
	}

	public List<Float> getFPRValues() {
		return this.FPRlist;
	}

	public List<Float> getTPRValues() {
		return this.TPRlist;
	}
	
	public List<Float> getFPRValues_all() {
		return this.FPRlist_all;
	}

	public List<Float> getTPRValues_all() {
		return this.TPRlist_all;
	}


}