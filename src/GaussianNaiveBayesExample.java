import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * Example test for Gaussian Naive Bayes classifier
 * 
 * @author kevinmbeaulieu
 *
 */
public class GaussianNaiveBayesExample extends Applet {
	
	List<ClassifiedVector> training;
	List<ClassifiedVector> predicted;
	
	public GaussianNaiveBayesExample() {
		training = new ArrayList<>();
		
		// Moons training data
		String input = "r -172 -11,r 20 88,r -75 131,r -17 150,r 3 127,r -177 -15,b 135 60,b 89 -73,b 98 -175,b 88 -122,r 115 -46,b 23 -116,b -34 -11,b 37 -130,b -36 3,b -48 57,b -31 -149,r -114 76,r -98 84,r -163 114,r -161 -116,r -3 53,r -83 93,b 189 -157,r -119 140,r -71 172,b -85 -6,r 11 14,b -9 -93,b -53 -20,r -42 130,b 169 -91,r -31 185,b 50 -166,r 15 89,b 189 -42,r -155 46,r -130 -60,r -155 -26,r -22 94,b -62 -45,b 133 -73,r 75 62,b -87 65,r 19 227,b 153 -40,b -16 -153,b 160 -9,b 182 -46,b 171 -17,r 81 164,b 185 39,r -20 9,b 24 -174,b 0 -58,b -4 -71,r 43 -51,b 51 -109,r -205 32,r 59 25";
		
		// Circles training data
		//String input = "b 83 -54,r -102 -125,r 40 146,r 101 -54,b -76 -7,r -7 -148,r -118 101,r 23 164,b 60 23,b 37 -65,r 58 -183,b -57 90,b -5 52,r -78 132,r -178 146,r 8 89,b 124 -31,r 160 60,b -5 -72,r -62 -193,r 135 -76,b -7 37,b 73 75,b -44 79,r -139 -116,b -133 28,b 30 47,b 42 -147,b -17 137,b -11 44,r -64 150,b 26 -40,b 32 -88,r 198 11,r -54 -145,r -22 137,b -62 5,b -76 -3,b 44 94,r -183 44,r 185 0,b 141 -24,r -76 -128,r 186 122,b 45 -97,r -103 84,r -185 -132,b 97 -26,b 4 -113,r 155 -124,r -38 122,r -132 8,b -77 -21,r 126 108,b -38 97,r 29 -157,r 84 190,r 92 177,b 16 -18,b -7 75";
		
		// Linearly separable training data
		//String input = "b -41 18,r 95 -184,r 24 -63,r 276 275,b -109 121,r 10 -33,r 159 183,r 52 0,r 91 -18,r 46 18,b -114 -5,b -156 -43,b -84 -116,r 116 -122,r 53 -219,b -168 -57,b -95 -66,r 112 48,r 138 -39,r 64 -8,b 0 89,r 12 -20,r 28 116,r 75 216,r -64 -181,b -148 -44,r 48 -85,r 250 -26,r 193 179,r 120 5,r 91 119,r 44 19,b -187 -144,b -41 49,r -2 0,r 44 -19,b -51 43,b -84 -187,b -141 123,b -70 81,r -11 10,b -130 -28,b -27 -90,b -162 93,b -46 -18,r 9 9,b 3 -10,b -126 -73,r -30 60,r 242 252,b -51 -25,b -110 44,r 141 -73,r -7 55,b -125 138,b -57 -14,b 3 -37,r 86 -11,r 51 -133,r 145 68";
		
		// Parse training data input string
		String[] inputSplit = input.split(",");
		for (String str : inputSplit) {
			String[] strSplit = str.split(" ");
			
			String category;
			if (strSplit[0].equals("r")) category = "red";
			else category = "blue";
			
			double x = Integer.parseInt(strSplit[1]) + 400;
			double y = Integer.parseInt(strSplit[2]) + 400;
			Vector point = new Vector(x, y);
			training.add(new ClassifiedVector(category, point));
		}

		// Train classifier
        GaussianNaiveBayes nb = new GaussianNaiveBayes();
        nb.train(training);
        
        // Initialize test data
        ArrayList<Vector> test = new ArrayList<Vector>();
        for (int x = 0; x < 800; x++) {
    		for (int y = 0; y < 800; y++) {
    			test.add(new Vector(x, y));
    		}
    	}
    	
        // Classify test data
    	predicted = new ArrayList<>();
    	for (Vector p : test) {
    		predicted.add(new ClassifiedVector(nb.predict(p), p));
    	}
	}
	
	/**
	 * Draw prediction output to applet window and save to output.png
	 */
	public void paint(Graphics g) {
		BufferedImage outputImage = new BufferedImage(800, 800, BufferedImage.TYPE_INT_RGB);
		Graphics2D outputGraphics = outputImage.createGraphics();
		
		// Draw classifier's prediction model
		for (ClassifiedVector point : predicted) {
			Color color;
			if (point.classification.category != null) {
				double redProb;
				if (point.classification.category.equals("red")) redProb = point.classification.probability;
				else redProb = 1 - point.classification.probability;
				
				color = new Color (100 + (int)(155 * redProb),
									100,
									100 + (int)(155 * (1 - redProb)));
			} else color = new Color(0, 0, 0, 255);
			outputGraphics.setColor(color);
			
			int markX = (int)point.vector.getX(),
					markY = (int)point.vector.getY(),
					markWidth = 2,
					markHeight = 2;
				outputGraphics.fillOval(markX, markY, markWidth, markHeight);
		}
		
		// Draw training data points
		for (ClassifiedVector point : training) {
			Color color;
			if (point.classification.category.equals("red")) color = Color.red;
			else color = Color.blue;
			outputGraphics.setColor(color);
			
			int markX = (int)point.vector.getX() - 2,
				markY = (int)point.vector.getY() - 2,
				markWidth = 5,
				markHeight = 5;
			outputGraphics.fillOval(markX, markY, markWidth, markHeight);
		}
		
		// Draw output to applet window
		g.drawImage(outputImage, 0, 0, null);
		
		// Save output to image file 
		try {
			ImageIO.write(outputImage, "png", new File("output.png"));
		} catch (IOException e) {
			System.err.println("Could not save output image: " + e);
		}
	}
}
