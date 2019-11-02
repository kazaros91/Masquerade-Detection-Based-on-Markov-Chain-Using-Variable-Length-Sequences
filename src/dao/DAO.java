package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
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

	
	public DAO() {}


	public List<String> getCommandsFromCSV(int userId) {
		List<String> commands = new ArrayList<String>();
		
		final String currentDirectory = System.getProperty("user.dir");
		final String inputFilename = currentDirectory + dataset_name + userId;
		
		BufferedReader input;
		
		try { 
			input = new BufferedReader(new FileReader(new File(inputFilename)));
			
			String line = "";
			String[] st = null;
			
			input.readLine();   //  first line is omitted since it contains fields of the data
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
//		 DAO.TRAINING_DATA_SIZE = User.size() - DAO.DETECTION_DATA_SIZE;
		 List<String> trainingData = User.subList(0, DAO.TRAINING_DATA_SIZE);
//		 List<String> trainingData = User.subList(User.size() - DETECTION_DATA_SIZE - DAO.TRAINING_DATA_SIZE, User.size() -  DAO.TRAINING_DATA_SIZE); // del
		 
		 return trainingData;
	}
	
	public DetectionData getDetectionData(int id) {
		 List<String> User = this.getCommandsFromCSV(id);
		 System.out.println("User" + id + " size: " + User.size());

		 // get the last the last DAO.DETECTION_DATA_SIZE number of commands of USER(id)
		 List<String> falsePositiveData = new ArrayList<String>();
		 falsePositiveData = User.subList(  DAO.TRAINING_DATA_SIZE, DAO.TRAINING_DATA_SIZE + DAO.DETECTION_DATA_SIZE );
		 System.out.println("falsePositiveData: userId = " + id + ", data size = " +  falsePositiveData.size() );
		 
		 // get the last the last DAO.DETECTION_DATA_SIZE number of commands of USER(id)
		 List<String> truePositiveData0 = new ArrayList<String>();
		 truePositiveData0 = User.subList(  DAO.TRAINING_DATA_SIZE + DAO.DETECTION_DATA_SIZE, DAO.TRAINING_DATA_SIZE + DAO.DETECTION_DATA_SIZE + DAO.DETECTION_DATA_SIZE2);
		 System.out.println("truePositiveData: userId = " + id + ", data size = " +  truePositiveData0.size() );
		 
		 // SEA1v49
		 // get the last the last DAO.DETECTION_DATA_SIZE number of commands of all users except USER(id)
		 List< List<String> > truePositiveData = new ArrayList< List<String> >();
		 for (int i = DAO.MIN_ID; i < id; ++i) {
			 List<String> Useri =  this.getCommandsFromCSV(i);
			 truePositiveData.add( Useri.subList( 0, DAO.TRAINING_DATA_SIZE ) );
		 }
		 for (int i = id + 1; i <= DAO.MAX_ID; ++i) {
			 List<String> Useri = this.getCommandsFromCSV(i);
			truePositiveData.add( Useri.subList(0, DAO.TRAINING_DATA_SIZE ) );
		 }
		 
//		 // PU
//		 // get the last the last DAO.DETECTION_DATA_SIZE number of commands of all users except USER(id)
//		 List< List<String> > truePositiveData = new ArrayList< List<String> >();
//		 for (int i = DAO.MIN_ID; i < id; ++i) {
//			 List<String> Useri =  this.getCommandsFromCSV(i);
//			 truePositiveData.add( Useri.subList( DAO.TRAINING_DATA_SIZE, DAO.TRAINING_DATA_SIZE + DAO.DETECTION_DATA_SIZE ) );
//		 }
//		 for (int i = id + 1; i <= DAO.MAX_ID; ++i) {
//			 List<String> Useri = this.getCommandsFromCSV(i);
//			truePositiveData.add( Useri.subList( DAO.TRAINING_DATA_SIZE, DAO.TRAINING_DATA_SIZE + DAO.DETECTION_DATA_SIZE ) );
//		 }
		 
		 DetectionData data = new DetectionData(falsePositiveData, truePositiveData0);
		 
		 return data;
	}

	public static void setParameters(String dataset_name) {
	    if (dataset_name.equals("Schonlau")) {
	        DAO.dataset_name = "/Schonlau/User";
			DAO.MIN_ID = 1;
			DAO.MAX_ID = 50; // 50
			DAO.TRAINING_DATA_SIZE = 10000; // 10000
			DAO.DETECTION_DATA_SIZE = 5000; // 5000
			DAO.DETECTION_DATA_SIZE2 = 5000; // 5000
	    }
	    if (dataset_name.equals("SEA")) {
	        DAO.dataset_name = "/Schonlau/User";
			DAO.MIN_ID = 1;
			DAO.MAX_ID = 50; // 50
			DAO.TRAINING_DATA_SIZE = 5000; // 10000
			DAO.DETECTION_DATA_SIZE = 9538; // 5000
			DAO.DETECTION_DATA_SIZE2 = 462; // 5000
	    }
	    if (dataset_name.equals("SEA1v49")) {
	        DAO.dataset_name = "/Schonlau/User";
			DAO.MIN_ID = 1;
			DAO.MAX_ID = 50; // 50
			DAO.TRAINING_DATA_SIZE = 5000; // 10000
			DAO.DETECTION_DATA_SIZE = 10000; // 5000
			DAO.DETECTION_DATA_SIZE2 = 5000; // 5000
	    }
	    else if (dataset_name.equals("PU")) {
			DAO.dataset_name = "/PU/USER";
			DAO.MIN_ID = 1;
			DAO.MAX_ID = 8; // 50
			DAO.TRAINING_DATA_SIZE = 1500; // 1500
			DAO.DETECTION_DATA_SIZE = 500; // 500
			DAO.DETECTION_DATA_SIZE2 = DAO.DETECTION_DATA_SIZE; // 1000
		}
	    else if (dataset_name.equals("Greenberg")) {
			DAO.dataset_name = "/Greenberg/USER";
//			DAO.dataset_name = "/Greenberg/USER";
			DAO.MIN_ID = 1;
			DAO.MAX_ID = 4; // 50
			DAO.TRAINING_DATA_SIZE = 1000; // 1000
			DAO.DETECTION_DATA_SIZE = 1000; // 1000
			DAO.DETECTION_DATA_SIZE2 = 300; // 1000 true_pos_data_size
		}
		else {
			System.out.println("no dataset with name: " + dataset_name);
		}
	}
}
