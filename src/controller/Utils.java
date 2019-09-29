package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Utils {
	
	public static void makeDir(String string) {
		File dir = new File(string);
		dir.mkdir();
	}

	public static void writeToFile(String dir, List<Integer> lengths, 
			Map<Integer, Integer> weights, Map<Integer, Integer> weights2,
			int windowSize, double eta) {
		File file = new File(dir + "/hyperparameters.txt");
		
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			int W = lengths.size();
			out.write("W = " + W + "\n");
		
			writeList(out, "l = lengths = ", lengths);
			writeList(out, "e1 = weights = ", weights.values());
			writeList(out, "e2 = weights2 = ", weights2.values());
		
			out.write("w = windowSize = " + windowSize + "\n");
			out.write("eta = " + eta);
			out.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeList(BufferedWriter out, String name, Collection<Integer> collection) throws IOException {
		Iterator<Integer> it = collection.iterator();
		out.write(name + "{" + it.next());
		while( it.hasNext() )
			out.write(", " + it.next());
		out.write("}\n");
	}

}
