/**
 * Stores the name of a category and the probability
 * the data has been correctly classified
 * 
 * @author kevinmbeaulieu
 *
 */
public class Classification {
	
	String category;
	double probability; // Probability of correct classification
	
	public Classification(String category, double probability) {
		this.category = category;
		this.probability = probability;
	}

}
