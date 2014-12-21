/**
 * Stores a vector of features with its classification (known or predicted)
 * 
 * @author kevinmbeaulieu
 *
 */
public class ClassifiedVector {
    
    public Vector vector;
    public Classification classification;
    
    public ClassifiedVector(Classification classification, Vector vector) {
    	this.classification = classification;
    	this.vector = vector;
    }
    
    public ClassifiedVector(String category, Vector vector) {
        this.classification = new Classification(category);
        this.vector = vector;
    }
    
    public String toString() {
    	return "\""+classification.getCategory()+"\", "+vector;
    }
}
