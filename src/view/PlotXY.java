package view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.VectorGraphicsEncoder;
import org.knowm.xchart.VectorGraphicsEncoder.VectorGraphicsFormat;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYSeries;


public class PlotXY {

	private XYChart chart;
	private String plotName;
	private String outputDirectory;
	
	public PlotXY(String plotName, String xName, String yName) {
		// call another constructor with the default outputDirectory = System.getProperty("user.dir");
		this( plotName, xName, yName, System.getProperty("user.dir") + "/" );
	}
	
	public PlotXY(String plotName, String xName, String yName, String outputDirectory) {
		this.plotName = plotName;
		this.outputDirectory = outputDirectory;
		
		// Create Chart
	    chart = new XYChart(800, 400);
	    chart.setTitle(plotName);
	    chart.setXAxisTitle(xName);
	    chart.setYAxisTitle(yName);
	}
	
	public void add(List<Float> yData, String seriesName) {
//		 this.add(null, yData, seriesName);
		 chart.addSeries(seriesName, null, yData);
	}
	
	public void add(List<Float> xData, List<Float> yData, String seriesName) {
		List<Float> xData_ = new ArrayList<Float>();
		List<Float> yData_ = new ArrayList<Float>();
		for ( int i = 0; i < xData.size(); ++i ) {
			if (xData.get(i) < 0.17) {
				xData_.add(xData.get(i));
				yData_.add(yData.get(i));
			}
		}
		chart.addSeries(seriesName, xData_, yData_);
	}
	
	public void draw() {
	    // Show it
//	    new SwingWrapper<XYChart>(chart).displayChart();
	 
		// write the drawing into the file
	    try {
	    		VectorGraphicsEncoder.saveVectorGraphic(chart, outputDirectory + plotName, VectorGraphicsFormat.EPS);
//	    	    BitmapEncoder.saveBitmap(chart, outputDirectory + plotName, BitmapFormat.JPG);
	    }
	    catch (IOException e) {
	    		e.printStackTrace();
	    }
    }
	
	// uncomment to test PlotXY
//	public static void main(String [] args) {
//		XYChart chart = new XYChart(500, 400);
//	    chart.setTitle("chartName");
//	    chart.setXAxisTitle("X");
//	    chart.setYAxisTitle("Y");
//	    
//	    int [] y1 =  {1, 2, 3, 4, 1};
//	    XYSeries series1 = chart.addSeries("series1", y1);
//	    
//	    int [] y2 =  {0, 3, 2, 5, 5};
//	    XYSeries series2 = chart.addSeries("series2", y2);
//	    
//	    String outputDirectory = System.getProperty("user.dir") + "/";
//	    try {
//	    		VectorGraphicsEncoder.saveVectorGraphic(chart, outputDirectory + "2", VectorGraphicsFormat.EPS);
//	    }
//	    catch (IOException e) {
//    		e.printStackTrace();
//	    }
//	}
	
	
}
