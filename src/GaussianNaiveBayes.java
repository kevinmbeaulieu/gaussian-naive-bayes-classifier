import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gaussian Naive Bayes classifier 
 * 
 * Supports arbitrary number of features, of type double
 * 
 * @author kevinmbeaulieu
 *
 */
public class GaussianNaiveBayes {
	
	private Map<String, Vector> averages;
    private Map<String, Vector> variances;
    
    /**
     * Construct classifier from previously calculated averages & variances
     */
    public GaussianNaiveBayes(Map<String, Vector> averages, Map<String, Vector> variances) {
        this.averages = averages;
        this.variances = variances;
    }
    
    /**
     * Construct new classifier
     */
    public GaussianNaiveBayes() {
        this(null, null);
    }
    
    /**
     * Get each category's average value for each feature
     */
    public Map<String, Vector> getAverages() {
    	return new HashMap<String, Vector>(averages);
    }
    
    /**
     * Get each category's variance for each feature
     */
    public Map<String, Vector> getVariances() {
    	return new HashMap<String, Vector>(averages);
    }

    /**
     * Train classifier
     * 
     * @param trainingDataset Data to train classifier with
     */
    public void train(List<ClassifiedVector> trainingDataset) {
    	averages = new HashMap<>();
    	variances = new HashMap<>();
    	
    	Map<String, Integer> numInCategory = new HashMap<>();
    	for (ClassifiedVector entry : trainingDataset) {
    		String category = entry.classification.getCategory();
    		
    		Vector average = averages.get(category);
    		if (average == null) {
    			average = Vector.getVectorWithSize(entry.vector.size());
    			averages.put(category, average);
    		}
    		for (int i = 0; i < entry.vector.size(); i++) {
    			average.translateValue(i, entry.vector.getValue(i));
    		}
    		numInCategory.put(category, numInCategory.getOrDefault(category, 0) + 1);
    	}
    	for (Map.Entry<String, Vector> entry : averages.entrySet()) {
    		Vector average = entry.getValue();
    		int n = numInCategory.get(entry.getKey());
    		average.div(n);
    	}
    	
    	for (ClassifiedVector entry : trainingDataset) {
    		String category = entry.classification.getCategory();
    		Vector variance = variances.get(category);
    		if (variance == null) {
    			variance = Vector.getVectorWithSize(entry.vector.size());
    			variances.put(category, variance);
    		}
    		Vector mean = averages.get(category);
    		Vector deltaFromMean = entry.vector.clone();
    		deltaFromMean.sub(mean);
    		for (int i = 0; i < variance.size(); i++) {
    			variance.translateValue(i, deltaFromMean.getValue(i) * deltaFromMean.getValue(i));
    		}
    	}
    	for (Map.Entry<String, Vector> entry : variances.entrySet()) {
    		Vector variance = entry.getValue();
    		int n = numInCategory.get(entry.getKey());
    		variance.div(n);
    	}
    }
    
    /**
     * Predict classification of a data point
     * 
     * @param v Data to classify
     * @return Classification of v
     * @throws IllegalArgumentException Fail if classifier has not been trained
     */
    public Classification predict(Vector v) throws IllegalArgumentException {
    	if (averages == null || variances == null) {
    		throw new IllegalArgumentException("Knowledge Bases missing: Make sure you train first a classifier before you use it.");
    	}
    	
    	double pC = 1.0 / averages.size();
    	
    	double pV = 0;
    	for (Map.Entry<String, Vector> entry : averages.entrySet()) {
    		String category = entry.getKey();
    		Vector categoryAverage = entry.getValue();
    		Vector categoryVariance = variances.get(category);
    		
    		double pVGivenC = probFeaturesGivenCategory(v, categoryAverage, categoryVariance);
    		pV += pVGivenC * pC;
    	}
    	
    	Classification prediction = new Classification();
    	for (Map.Entry<String, Vector> entry : averages.entrySet()) {
    		String category = entry.getKey();
    		Vector categoryAverage = entry.getValue();
    		Vector categoryVariance = variances.get(category);
    		
    		double pVGivenC = probFeaturesGivenCategory(v, categoryAverage, categoryVariance);
    		
    		double pCGivenV = pC * pVGivenC / pV;
    		double score = pCGivenV; // (If we did not need probabilities, pVGivenC would be sufficient)
    		
    		prediction.addCategory(category, score);
    	}
    	
    	return prediction;
    }
    
    /**
     * Calculate probability of value v given category with average avg, variance var
     * 
     * @return p(feature | category)
     */
    private double probFeatureGivenCategory(double v, double avg, double var) {
    	double exp = -(v - avg) * (v - avg) / (2 * var);
    	double discrim = 2 * Math.PI * var;
    	return Math.exp(exp) / Math.sqrt(discrim);
    }
    
    /**
     * Calculate probability of values for all features given category with averages avgs, variances vars
     * 
     * @return p(feature 1, ..., feature n | category)
     */
    private double probFeaturesGivenCategory(Vector features, Vector avgs, Vector vars) {
    	double pVGivenC = 1.0;
    	for (int i = 0; i < avgs.size(); i++) {
			pVGivenC *= probFeatureGivenCategory(features.getValue(i), avgs.getValue(i), vars.getValue(i));
		}
    	return pVGivenC;
    }
}
