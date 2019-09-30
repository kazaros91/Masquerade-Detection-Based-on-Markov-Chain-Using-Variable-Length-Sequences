package view;

import java.io.File;
import java.io.FileOutputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import metrics.Metrics;


public class Table {
	
	private XSSFWorkbook  workbook;
	private XSSFSheet sheet;
	private int rowIndex;
	
	public List<Metrics> metrics_;
	
	
	public Table() {
	    //Blank workbook
	    workbook = new XSSFWorkbook();
	    //Create a blank sheet
	    sheet = workbook.createSheet("Metrics");
		
		setTitles();
		rowIndex = 0;
		metrics_ = new ArrayList<Metrics>();
	}
	
	private void setTitles() {
		// set titles of each column
		Row row = sheet.createRow(0);
		Cell cell;
		cell = row.createCell(0); 
		cell.setCellValue("\\");
		String [] titles = Metrics.titles();
		for (int i = 0; i < titles.length; ++i) {
			cell = row.createCell(i+1); cell.setCellValue(titles[i]);
		}
	}
	
	public void add(final Metrics metrics, String methoName) {
	    // adding row of metrics
		metrics_.add(metrics);

		++rowIndex;
	    	Row row = sheet.createRow(rowIndex);
	    	Cell cell;
	    	// add methodName
	    	cell = row.createCell(0); 
    		cell.setCellValue(methoName);
    		// adding precision_TPR_FPR_F1_accuracy_threshold values
	    	float [] precision_TPR_FPR_F1_accuracy_threshold = metrics.list();
	    	for (int i = 0; i < precision_TPR_FPR_F1_accuracy_threshold.length; ++i) {
	    		cell = row.createCell(i+1); 
	    		
	    		DecimalFormat df = new DecimalFormat("0.000");
	    		df.setRoundingMode(RoundingMode.UP);
	    		cell.setCellValue( df.format(precision_TPR_FPR_F1_accuracy_threshold[i]) );
	    	}
	}
	
	
	public void save(String outputFilename) {
		System.out.println( "The records will be stored in " + outputFilename);
		System.out.println( "please wait..." );
	    try 
	    {
	        // Write the workbook in outputFilename
	        FileOutputStream out = new FileOutputStream( new File(outputFilename) );
	        this.workbook.write(out);
	        out.close();
	        System.out.println("The table is written successfully on the disk.");
	    } 
	    catch (Exception e)
	    {
	        e.printStackTrace();
	    }
	}

}
