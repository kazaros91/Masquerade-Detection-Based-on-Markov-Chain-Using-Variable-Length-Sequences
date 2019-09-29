package view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.VectorGraphicsEncoder;
import org.knowm.xchart.VectorGraphicsEncoder.VectorGraphicsFormat;
import org.knowm.xchart.internal.series.Series;
import org.knowm.xchart.style.Styler.LegendPosition;


public class BarChart {
	private CategoryChart chart;
	
	public BarChart() {
//		super(height, height);
	    chart = this.buildChart("report", "X", "Y", "filename");
	    
	}
	 
	public CategoryChart buildChart(String title, String xName, String yName, String outputDirectory) {

	  // Create Chart
	  CategoryChart chart = new CategoryChartBuilder().width(800).height(600).title(title).xAxisTitle(xName).yAxisTitle(yName).build();
	  
	  // Customize Chart
	  chart.getStyler().setLegendPosition(LegendPosition.InsideNW);
	  chart.getStyler().setHasAnnotations(true);
	 
	  return chart;
	}
	  
//	// Series
//	public void add(Table table, String seriesName, List<String> xData, List<Float> yData) {
//		table.getTitles();
//		table.getMetrics();
//		chart.addSeries(seriesName, xData, yData);
//	}
	  
	public void draw(String outputDirectory) {
//		  new SwingWrapper<CategoryChart>(chart).displayChart();
		  try {
	    			VectorGraphicsEncoder.saveVectorGraphic(chart, outputDirectory, VectorGraphicsFormat.EPS);
	    			// BitmapEncoder.saveBitmap(chart, outputDirectory + plotName, BitmapFormat.JPG);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
	}
	  
	public static void main(String [] args) {
		BarChart chart = new BarChart();
//		chart.add(new Table(), "MarkovF2", Arrays.asList(new String[] { "Precision", "Recall", "F1", "FPR", "Accuracy" }), 
//							 Arrays.asList(new Float[] { -40.f, 30.f, 20.f, 60.f, 60.f}));
//		
		chart.draw( "/Users/ghazaros/eclipse-workspace/AnomalyDetection/Experiments11_Schonlau/TEST_N=2/table/view");
	}
}
