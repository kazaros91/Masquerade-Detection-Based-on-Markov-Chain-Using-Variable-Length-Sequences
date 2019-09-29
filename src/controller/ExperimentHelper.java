package controller;

import java.util.ArrayList;
import java.util.List;

import detection.data.DetectionData;
import detection.data.DetectionData1;
import detection.data.DetectionResult;
import methods.VariableLengthMethod;
import metrics.Metrics;
import view.PlotXY;
import view.Table;


public class ExperimentHelper {
	
//	private static List<Float> xData_all = new ArrayList<Float>();
//	private static List<Float> yData_all = new ArrayList<Float>();
	
	// DELETE
	public static void flush_ROC_data() {
//		xData_all = new ArrayList<Float>();
//		yData_all = new ArrayList<Float>();
	}

	// get Decision Values
	public static List<List<Float>> getDecisionValues(VariableLengthMethod<?> method, DetectionData1 detectionData) {
		List<List<Float>> DD = new ArrayList<List<Float>>();
		DetectionResult result = method.detect( detectionData.getFalsePositiveData() );
		DD.add(result.getDecisionValues());
		
		result = method.detect( detectionData.getTruePositiveData() );
		DD.add(result.getDecisionValues());
		
		// PU
//		for ( int i = 0; i < detectionData.getTruePositiveData().size(); ++i) {
//			result = method.detect( detectionData.getTruePositiveData(i) );
//			DD.add(result.getDecisionValues());
//		}
		
		return DD;
	}
	
	public static void plotDecisionValues(String testDir, int windowSize, String plotName, int trainingId, List<List<Float>> series) {
		final String outputDirectory = testDir + "/decision values/";
		Utils.makeDir(outputDirectory);
			
		PlotXY view = new PlotXY(plotName + " (trainingId = " + trainingId + ")", "index", "Decision Values", outputDirectory);   //  appName, plotName, Xname, Yname, outputDirectory;
		String seriesName = "normal user: userId = " + trainingId;
		view.add( series.get(0), seriesName );
		System.out.println("Decision Values(" + 0 + ") = "  + series.get(0));
		for ( int i = 1; i < series.size(); ++i ) {
			int id = (i < trainingId)? (i) : (i + 1);   //  get the relative user id
			seriesName = "masquerader: userId = " + id;
			view.add(series.get(i), seriesName);
			System.out.println("Decision Values(" + i + ") = "  + series.get(i));
		}
		view.draw();
	}
	
	// get ROCValues
	public static Metrics getROCValues(VariableLengthMethod<?> method, List<String> falseData, List<String> trueData, String methodName) {
			List<Float> xData = new ArrayList<Float>();
			List<Float> yData = new ArrayList<Float>();
			List<Float> xData_all = new ArrayList<Float>();
			List<Float> yData_all = new ArrayList<Float>();
			
//			System.out.println(methodName + "\nbegin roc: ");
			float avg_precision = 0f, avg_TPR = 0f, avg_FPR = 0f, avg_F1 = 0f, avg_accuracy = 0f;
			Metrics metrics = new Metrics();
//			double threshold = -0.041;
			for ( double threshold = -0.5; threshold < 1.0; threshold = threshold + 0.002 ) 
			{
				DetectionResult F = method.detect(falseData, threshold);
				DetectionResult T = method.detect(trueData, threshold);
				xData_all.add( F.getPositivesRate() ); // FPR_all
				yData_all.add( T.getPositivesRate() ); // TPR_all
				if (F.getPositivesRate() <= 0.17) {
					xData.add( F.getPositivesRate() ); // FPR
					yData.add( T.getPositivesRate() ); // TPR
					
					float TP = T.getPositives();
					float FN = T.getNegatives();
					float FP = F.getPositives();
					float TN = F.getNegatives();
					metrics.update(TP, FN, FP, TN, threshold);
//					boolean updated = metrics.update(TP, FN, FP, TN);
				}	
				
				// uncomment to print FPR and TPR
//				float FPR = falsePositive.getPositivesRate();
//				float TPR = truePositive.getPositivesRate();
//				if ( !(TPR == 1 && FPR == 1) && (TPR - FPR) > 0.2 )
//				{
//					System.out.println( "FPR = " + FPR + ", TPR = " + TPR + ", threshold = " + threshold);
//				}
			}
//			Metrics metrics = new Metrics(avg_precision / (float)count, avg_TPR / (float)count, avg_FPR / (float)count, avg_F1 / (float)count, avg_accuracy / (float)count);
//			System.out.println("end roc\n");

		metrics.add_ROC_series(xData, yData);
		metrics.add_ROC_series_all(xData_all, yData_all);
		return metrics;
	}
	
	// plot ROCCurves
	public static Table plotROCCurves(String testDir, int windowSize, String name, String [] seriesNames, Metrics... metrics) {
		
		// plot ROCCurves
		final String plotDirectory = testDir + "/roc/";
		Utils.makeDir(plotDirectory);
		// save table in the disk
		final String tableDirectory = testDir + "/table/";
		Utils.makeDir(tableDirectory);
		
		PlotXY view = new PlotXY(name, "FPR", "TPR", plotDirectory);   //  appName, plotName, Xname, Yname, outputDirectory;
		Table table = new Table();
		for ( int i = 0; i < metrics.length; ++i ) {
			view.add(metrics[i].getFPRValues(), metrics[i].getTPRValues(), seriesNames[i]);
			table.add(metrics[i], seriesNames[i]);
		}
		view.draw();
		table.save(tableDirectory + name + ".xlsx");
		
		return table;
	}

//	// plot ROCCurves
//	public static void plotAvgROCCurves(String testDir, int windowSize, String name, String [] seriesNames, Metrics... metrics) {
//		
//		// plot ROCCurves
//		final String plotDirectory = testDir + "/roc/";
//		Utils.makeDir(plotDirectory);
//		PlotXY view = new PlotXY(name, "FPR", "TPR", plotDirectory);   //  appName, plotName, Xname, Yname, outputDirectory;
//		Table table = new Table();
//		for ( int i = 0; i < metrics.length; ++i ) {
//			view.add(metrics_avg.xData_all[0], metrics_avg.yData_all[0], seriesNames[i]);
//		}
//		view.draw();
//	}
	
	
}