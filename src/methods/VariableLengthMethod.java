package methods;

import java.util.List;
import detection.data.DetectionResult;


public interface VariableLengthMethod<T> {
	
	public void train(List<T> trainingData);
	public DetectionResult detect(List<String> detectionData, double threshold);
	public DetectionResult detect(List<String> truePositiveData);

}
