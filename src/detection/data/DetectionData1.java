package detection.data;

import java.util.List;

import org.javatuples.Pair;

public class DetectionData1 {
	private Pair<List<String>, List<String>> data;

	public DetectionData1(List<String> falsePositiveData, List<String> truePositiveData) {
		data = new Pair<List<String>, List<String>>(falsePositiveData, truePositiveData);
	}

	public List<String> getFalsePositiveData()  {
		return data.getValue0();
	}
	
	public List<String> getTruePositiveData() {
		return data.getValue1();
	}
	
}