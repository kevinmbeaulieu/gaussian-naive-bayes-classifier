import java.util.HashMap;
import java.util.Map;

/**
 * Stores the name of a category and the probability
 * the data has been correctly classified
 * 
 * @author kevinmbeaulieu
 *
 */
public class Classification {
	
	private Map<String, Double> categoryProbabilities;
	private String maxCategory;
	private double maxProbability;
	
	public Classification() {
		categoryProbabilities = new HashMap<String, Double>();
	}
	
	public Classification(String category) {
		this();
		addCategory(category, 1.0);
	}
	
	public void addCategory(String category, double probability) {
		categoryProbabilities.put(category, probability);
		
		if (probability > maxProbability) {
			maxCategory = category;
			maxProbability = probability;
		}
	}

	public double probabilityForCategory(String category) {
		return categoryProbabilities.getOrDefault(category, 0.0);
	}
	
	public Map<String, Double> getCategoryProbabilities() {
		return new HashMap<String, Double>(categoryProbabilities);
	}
	
	public String getCategory() {
		return maxCategory;
	}
}
