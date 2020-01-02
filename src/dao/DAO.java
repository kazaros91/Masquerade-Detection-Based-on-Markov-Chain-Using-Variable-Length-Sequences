package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import detection.data.DetectionData;


public class DAO {
	
	private static boolean isRead = false;
	private static String input;
	
	public static int MIN_ID = 1;
	public static int MAX_ID = 5;
	
	private static int TRAINING_DATA_SIZE;
	private static int DETECTION_DATA_SIZE;
	private static int DETECTION_DATA_SIZE2;
	
	private static String dataset_name;
	
	private List<Integer> positions_;
 
	
	public DAO() {
		getPositionsFromTXT("masquerade_SEA.txt");
	}

	private void getPositionsFromTXT(String inputFilename) {
		inputFilename = System.getProperty("user.dir") + "/" + inputFilename;
//		System.out.println("FILENAME: " + inputFilename);	
		
		BufferedReader input;
		positions_ =  new ArrayList<Integer>();
		try { 
			input = new BufferedReader(new FileReader(new File(inputFilename)));
			
			int idx = 0;
			String line = "";
			String[] row = null;
			while ( ( line = input.readLine() ) != null ) {
				row = line.replace("\\", "").split(" ");		
				++idx;
//				System.out.println("line: " + idx + " " + line);
				for ( int j = 0; j < row.length; ++j ) {
					try {
						Integer position = Integer.parseInt(row[j]);
						positions_.add(position);
					}
					catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
			}
			input.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}     
		catch (IOException e) {
			e.printStackTrace();
		}
			finally {
		}
	}

	public List<String> getCommandsFromCSV(int userId) {
		List<String> commands = new ArrayList<String>();
		
		final String currentDirectory = System.getProperty("user.dir");
		final String inputFilename = currentDirectory + dataset_name + userId;
		
		BufferedReader input;
		
		try { 
			input = new BufferedReader(new FileReader(new File(inputFilename)));
			
			String line = "";
			String[] st = null;
			
			while ( ( line = input.readLine() ) != null ) {
				st = line.split("\n");
				String command = st[0];
				commands.add(command);
			}
			input.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}     
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
		}
		
		return commands;   //  note that the actions are ordered chronologically in the .csv file
	}
	
	public List<List<String>> getAllData() {
		List<List<String>> data = new ArrayList<List<String>>();
		for (int id = DAO.MIN_ID; id <= DAO.MAX_ID; ++id) {
			data.add(this.getCommandsFromCSV(id));
		}	
		
		return data;
	}
	
	//  all of the following methods to get the data for training and detection stages
	public List<String> getTrainingData(int id) {
		 List<String> User = this.getCommandsFromCSV(id);
		 List<String> trainingData = User.subList(0, DAO.TRAINING_DATA_SIZE);
		 
		 return trainingData;
	}
	
	public DetectionData getDetectionData(int id) {
		 List<String> User = this.getCommandsFromCSV(id);
		 System.out.println("User" + id + " size: " + User.size());

		 // get the last the last DAO.DETECTION_DATA_SIZE number of commands of USER(id)
		 List<String> falsePositiveData = new ArrayList<String>();
		 falsePositiveData = User.subList(  DAO.TRAINING_DATA_SIZE, DAO.TRAINING_DATA_SIZE + DAO.DETECTION_DATA_SIZE );
		 System.out.println("falsePositiveData: userId = " + id + ", data size = " +  falsePositiveData.size() );
		 
//		 // Greenberg
//		 List<String> truePositiveData = new ArrayList<String>();
//		 truePositiveData = User.subList(  DAO.TRAINING_DATA_SIZE + DAO.DETECTION_DATA_SIZE, DAO.TRAINING_DATA_SIZE + DAO.DETECTION_DATA_SIZE + DAO.DETECTION_DATA_SIZE2);
//		 System.out.println("falsePositiveData: userId = " + id + ", data size = " +  falsePositiveData.size() );
//		 System.out.println("truePositiveData: userId = " + id + ", data size = " +  truePositiveData.size() );
		 
//		 // SEA
//		 falsePositiveData = new ArrayList<String>();
//		 List<String> truePositiveData = new ArrayList<String>();
//		 // get User_id_positions
//		 int index = id - 1;
//		 List<Integer> Useri_positions = new ArrayList<Integer>();
//		 for ( int i = 0; i < 100; ++i ) {
//			 Useri_positions.add( positions_.get(50*i + index) );
//		 } 
//		 // yield normal and masquerade test sets
//		 // get the last the last DAO.DETECTION_DATA_SIZE commands of User
//		 List<String> PositiveData = User.subList(  DAO.TRAINING_DATA_SIZE, DAO.TRAINING_DATA_SIZE + DAO.DETECTION_DATA_SIZE );
//		 for ( int j=0; j<100; ++j) {
//			 List<String> block = PositiveData.subList(100*j, 100*j + 100);
//			 int value = Useri_positions.get(j); 
//			 if (value == 1)
//				 truePositiveData.addAll(block);
//			 else
//				 falsePositiveData.addAll(block);
//			 
//		 }
//		 System.out.println("falsePositiveData: userId = " + id + ", data size = " +  falsePositiveData.size() );
//		 System.out.println("truePositiveData: userId = " + id + ", data size = " +  truePositiveData.size() );
	
		 //	PU
		 // get the last the last DAO.DETECTION_DATA_SIZE number of commands of all users except USER(id)
		 List<String> truePositiveData = new ArrayList<String>();
		 for (int i = DAO.MIN_ID; i < id; ++i) {
			 List<String> Useri =  this.getCommandsFromCSV(i);
			 truePositiveData.addAll( Useri.subList( DAO.TRAINING_DATA_SIZE, DAO.TRAINING_DATA_SIZE + DAO.DETECTION_DATA_SIZE ) );
		 }
		 for (int i = id + 1; i <= DAO.MAX_ID; ++i) {
			 List<String> Useri = this.getCommandsFromCSV(i);
			truePositiveData.addAll( Useri.subList( DAO.TRAINING_DATA_SIZE, DAO.TRAINING_DATA_SIZE + DAO.DETECTION_DATA_SIZE ) );
		 }
		 DetectionData data = new DetectionData(falsePositiveData, truePositiveData);
		 
		 return data;
	}

	public static void setParameters(String dataset_name) {
	    if (dataset_name.equals("SEA")) {
	        DAO.dataset_name = "/Schonlau/User";
			DAO.MIN_ID = 1;
			DAO.MAX_ID = 50;
			DAO.TRAINING_DATA_SIZE = 5000; // 10000
			DAO.DETECTION_DATA_SIZE = 10000; // 5000
	    }
	    else if (dataset_name.equals("PU")) {
			DAO.dataset_name = "/PU/USER";
			DAO.MIN_ID = 1;
			DAO.MAX_ID = 8;
			DAO.TRAINING_DATA_SIZE = 1500; // 1500
			DAO.DETECTION_DATA_SIZE = 500; // 500
			DAO.DETECTION_DATA_SIZE2 = DAO.DETECTION_DATA_SIZE; // 1000
		}
	    else if (dataset_name.equals("Greenberg")) {
			DAO.dataset_name = "/Greenberg/USER";
			DAO.MIN_ID = 1;
			DAO.MAX_ID = 50; // 50
			DAO.TRAINING_DATA_SIZE = 1000; // 1000
			DAO.DETECTION_DATA_SIZE = 1000; // 1000 normal test data size
			DAO.DETECTION_DATA_SIZE2 = 300; // 300 masquerade test data size
		}
		else {
			System.out.println("no dataset with name: " + dataset_name);
		}
	}
}