package controller;

import java.util.ArrayList;
import java.util.List;

import detection.data.DetectionData;
import detection.data.DetectionResult;
import methods.VariableLengthMethod;
import metrics.Metrics;
import view.PlotXY;
import view.Table;


public class ExperimentHelper {

	// get Decision Values
	public static List<List<Float>> getDecisionValues(VariableLengthMethod<?> method, DetectionData detectionData) {
		List<List<Float>> DD = new ArrayList<List<Float>>();
		DetectionResult result = method.detect( detectionData.getFalsePositiveData() );
		DD.add(result.getDecisionValues());
		
		result = method.detect( detectionData.getTruePositiveData() );
		DD.add(result.getDecisionValues());
		
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
			int id = (i < trainingId)? (i) : (i + 1); //  get the relative user id
			seriesName = "masquerader: userId = " + id;
			System.out.println(series.get(i));
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
			
			Metrics metrics = new Metrics();
			for ( double threshold = -1.0; threshold < 1.0; threshold = threshold + 0.002 ) 
			{
				DetectionResult F = method.detect(falseData, threshold);
				DetectionResult T = method.detect(trueData, threshold);
				xData_all.add( F.getPositivesRate() ); // FPR_all
				yData_all.add( T.getPositivesRate() ); // TPR_all
				if (F.getPositivesRate() <= 0.1) {
					xData.add( F.getPositivesRate() ); // FPR
					yData.add( T.getPositivesRate() ); // TPR
					
					float TP = T.getPositives();
					float FN = T.getNegatives();
					float FP = F.getPositives();
					float TN = F.getNegatives();
					metrics.update(TP, FN, FP, TN, threshold);
				}	
			}

		metrics.add_ROC_series(xData, yData);
		metrics.add_ROC_series_all(xData_all, yData_all);
		return metrics;
	}
	
	// plot ROCCurves and save classification_result table
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
	
}
