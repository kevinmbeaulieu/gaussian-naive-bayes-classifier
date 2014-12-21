import java.util.ArrayList;
import java.util.List;

/**
 * Class containing a vector of doubles and
 * some useful operations.
 * 
 * @author kevinmbeaulieu
 *
 */
public class Vector {
	
	private List<Double> values;
	
	public Vector(double ... values) {
		this.values = new ArrayList<>();
		for (double v : values) {
			this.values.add(v);
		}
	}
	
	public Vector(List<Double> values) {
		this.values = new ArrayList<>(values);
	}
	
	public Vector() {
		this.values = new ArrayList<Double>();
	}
	
	public static Vector getVectorWithSize(int size) {
		List<Double> values = new ArrayList<Double>();
		for (int i = 0; i < size; i++) {
			values.add(0.0);
		}
		
		return new Vector(values);
	}
	
	public double getValue(int i) {
		return values.get(i);
	}
	
	public void setValue(int i, double val) {
		values.set(i, val);
	}
	
	public void translateValue(int i, double delta) {
		setValue(i, getValue(i) + delta);
	}

	public double getX() {
		return values.get(0);
	}
	
	public double getY() {
		return values.get(1);
	}
	
	public double getZ() {
		return values.get(2);
	}
	
	public int size() {
		return values.size();
	}
	
	public void div(double n) {
		for (int i = 0; i < size(); i++) {
			setValue(i, getValue(i) / n);
		}
	}
	
	/**
	 * In-place subtraction of another vector from this
	 * 
	 * @param other Vector to subtract
	 */
	public void sub(Vector other) {
		if (size() != other.size()) return;
		
		for (int i = 0; i < size(); i++) {
			setValue(i, getValue(i) - other.getValue(i));
		}
	}
	
	public Vector clone() {
		return new Vector(values);
	}
	
	public String toString() {
		String str = "<";
		for (int i = 0; i < values.size() - 1; i++) str += values.get(i) + ", ";
		str += values.get(values.size() - 1) + ">";
		return str;
	}
}
