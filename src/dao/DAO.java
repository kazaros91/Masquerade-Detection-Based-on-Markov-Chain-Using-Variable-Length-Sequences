package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import dao.DAO;
import detection.data.DetectionData;
import methods.MarkovF2Method;
import methods.MarkovMethod;
import metrics.Metrics;
import view.Table;


public class Experiment {
	
	private List<Integer> lengths;
	private Map<Integer, Integer> weights;
	private Map<Integer, Integer> weights2;
	private int windowSize;
	private double eta;
	private float thresholdDecision;
	
	private String dir;
	
	private ArrayList<Metrics> metrics_list0;
	private ArrayList<Metrics> metrics_list1;
	private ArrayList<Metrics> metrics_list2;
	String [] methodNames = {"Markov [4]", "MarkovN2 [4]", "MarkovF2"};
	
	Experiment() {
		metrics_list0 = new ArrayList<Metrics>();
		metrics_list1 = new ArrayList<Metrics>();
		metrics_list2 = new ArrayList<Metrics>();
		dir = "";
	}
	
	public static void main(String [] args) {
		// performing greed search for parameters
		String [] datasetNames = {"SEA1v49", "PU", "Greenberg"};
		for (int windowSize = 21; windowSize <= 81; windowSize += 15 ) {
			for ( int dataset_idx = 1; dataset_idx <= 1; ++dataset_idx ) { // then 2
				int experiementCount  = 0; // 16
				Experiment experiment = new Experiment();
				String datasetName = datasetNames[dataset_idx];
				String dir0 = System.getProperty("user.dir") + "/" + datasetName
										+ "_window=" + windowSize +  "/";
				Utils.makeDir(dir0);
				for ( int W = 3; W <= 4; ++W ) {
				for (int i2 = 2; i2 <= 2; ++i2 ) { // i2 is used to define weights2, i2=2 corresponds e2={1,3,5}
					for ( int i1 = 2; i1 <= 2; ++i1 ) {  // i1 is used to define weights, i1=2 corresponds e1={2,3,4}
						for ( int i0 = 2; i0 <= 2; ++i0 ) { // i0 is used to define different l sets, i0=2 corresponds l={2,3,4}
								++experiementCount;
								String dir = experiment.setParameters(dir0, experiementCount, datasetName, i0, i1, i2, windowSize, W);
								experiment.performExperiments(dir, experiementCount);
							}
						}
					}
				}
			}
		}
	}
	
	public String setParameters(String dir0, int experimentCount, String datasetName, int i0, int i1, int i2, int windowSize, int W) {
		// set training and detection data sizes 
		DAO.setParameters(datasetName);
		
		// create a directory to store the experimental results
		// defining the directory to store the results
		dir = dir0 + experimentCount;
		Utils.makeDir(dir);
				        
		// BEGIN: defining the hyper parameters
		// BEGIN: hyper parameters those written in the hyperparameters.txt file		
		// hyper parameters in common for the training stage for all methods in common, by default this case is Experiement3
		lengths = new ArrayList<Integer>();
		for ( int k = 0; k < W; ++k )
			lengths.add(k+i0);  // lengths == l

		weights = new HashMap<Integer, Integer>();
		for ( int k = 0; k < W; ++k )
			weights.put( lengths.get(k), k+i1 );  // weights == e
		weights2 = new HashMap<Integer, Integer>();
		for ( int k = 0; k < W; ++k )
			weights2.put( lengths.get(k), i2*k+1 );  // weights2 == e2 	
//			weights2.put( lengths.get(k), k+i2 );  // weights2 == e2 // RM
		
		this.windowSize = windowSize;
						
		// parameters for Markov Chain based methods' detection stage
//		eta = 0.28;
		eta = 0.1;
		// THESE ARE DATA DEPENDANT HYPERPARAMETERS. 
//		Ideally, these parameters should be learned based on the data. However, it still remains an unsolved problem		
		
		Utils.writeToFile(this.dir, this.lengths, this.weights, this.weights2, this.windowSize, this.eta);
		// END: hyper parameters written in Experiments folder
		
		thresholdDecision = 0.79f;   //  in applications it should be defined by cross validation
		// END: defining the hyper parameters
		
		return dir;
	}	
	
	
	private void save_best_results(String dir0) {
		metrics_list0.sort(Metrics.MetricsComparator);
		metrics_list1.sort(Metrics.MetricsComparator);
		metrics_list2.sort(Metrics.MetricsComparator);
		
		// save 5 best results
		for ( int i = 1; i <= 5; ++ i) {
			Table table_best = new Table();
			table_best.add(metrics_list0.get(i), methodNames[0]);
			table_best.add(metrics_list1.get(i), methodNames[1]);
			table_best.add(metrics_list2.get(i), methodNames[2]);
			
			System.out.println();
			System.out.println( metrics_list0.get(i).getDir() );
			System.out.println( metrics_list1.get(i).getDir() );
			System.out.println( metrics_list1.get(i).getDir() );

			String path = dir0 + "best_reports/";
			Utils.makeDir(path);
			table_best.save(path + i + "_" + metrics_list0.get(i).experiment_count + ", " 
										   + metrics_list1.get(i).experiment_count + ", " 	
										   + metrics_list2.get(i).experiment_count +".xlsx");
		}
	}
	
	
	public void performExperiments(String dir, int experiment_count) {
		// BEGIN: hyper parameters for Markov Chain based methods' training stage
//		int N = 4;
//		Metrics metrics_max = new Metrics();
		for ( int N = 2; N <= 2; ++N ) {
			// END: hyper parameters for Markov Chain based methods' training stage
			String testDir = dir + "/TEST_N=" + N;
			Utils.makeDir(testDir);
			
			// BEGIN: MARKOV CHAIN BASED METHODS
			// Markov [4] method
			MarkovMethod markov = new MarkovMethod();
			markov.setTrainingParameters(lengths, N, weights, weights2);
			markov.setDetectionParameters(windowSize, eta, thresholdDecision);
			
			// MarkovN2 [4] method
			MarkovMethod markovN2 = new MarkovMethod();
			markovN2.setTrainingParameters(lengths, N*N, weights, weights2);
			markovN2.setDetectionParameters(windowSize, eta, thresholdDecision);
						
			// MarkovF2 method
			MarkovF2Method markovF2 = new MarkovF2Method();
			markovF2.setTrainingParameters(lengths, N, weights, weights2);
			markovF2.setDetectionParameters(windowSize, eta, thresholdDecision);
			// END: MARKOV CHAIN BASED METHODS
			
			// initializing of Datasource Object
			DAO dao = new DAO();
			Table table = new Table();
			Metrics [] metrics = {new Metrics(dir), new Metrics(dir), new Metrics(dir)};
//			ExperimentHelper.flush_ROC_data();
			// defining the user id to get his commands for the training stage
			for (int trainingId = 1; trainingId <= DAO.MAX_ID; ++trainingId )   //  trainingId == x in the paper
			{
//				int trainingId = 3;
				List<String> trainingSeq = dao.getTrainingData(trainingId);
				
				markov.train(trainingSeq);
				System.out.println();
				markovN2.train(trainingSeq);
				System.out.println();
				markovF2.train(trainingSeq);
				System.out.println();
				
				// defining detections stage's data
				DetectionData detectionData = dao.getDetectionData(trainingId);
				List<String> falsePositivesData = detectionData.getFalsePositiveData();
				List< List<Float> > DD0 = ExperimentHelper.getDecisionValues(markov, detectionData);
				List< List<Float> > DD1 = ExperimentHelper.getDecisionValues(markovN2, detectionData);
				List< List<Float> > DD2 = ExperimentHelper.getDecisionValues(markovF2, detectionData);
				
				ExperimentHelper.plotDecisionValues(testDir, windowSize, methodNames[0], trainingId, DD0);
				ExperimentHelper.plotDecisionValues(testDir, windowSize, methodNames[1], trainingId, DD1);
				ExperimentHelper.plotDecisionValues(testDir, windowSize, methodNames[2], trainingId, DD2);
				
				List<String> truePositivesData = detectionData.getTruePositiveData();
				final String idPair = "(trainingId = " + trainingId + ", detectionId = " + trainingId + ")";
				
				Metrics metrics0 = ExperimentHelper.getROCValues(markov, falsePositivesData, truePositivesData, methodNames[0]);
				Metrics metrics1 = ExperimentHelper.getROCValues(markovN2, falsePositivesData, truePositivesData, methodNames[1]);
				Metrics metrics2 = ExperimentHelper.getROCValues(markovF2, falsePositivesData, truePositivesData, methodNames[2]);
				
				metrics[0].add_all(metrics0); // metrics results for Markov [4]
				metrics[1].add_all(metrics1); // metrics results for MarkovN2 [4]
				metrics[2].add_all(metrics2); // metrics results for MarkovF2
				
				ExperimentHelper.plotROCCurves(testDir, windowSize, idPair, methodNames, metrics0, metrics1, metrics2);
				
			}
			
			metrics_list0.add(metrics[0]);
			metrics_list1.add(metrics[1]);
			metrics_list2.add(metrics[2]);
			
			ExperimentHelper.plotROCCurves(testDir, windowSize, "Average", methodNames, metrics[0], metrics[1], metrics[2]);
		}
	}

}
