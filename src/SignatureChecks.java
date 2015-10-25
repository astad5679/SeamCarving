
public class SignatureChecks {

	@SuppressWarnings("unused")
	public static void main(String[] argv) {
		
		// Check if the signatures of all required function in class Color are correct 
		float color;
		int value;
		float[][] gray = new float[4][4];
		int[][] image;
		color = Color.getRed(0);
		color = Color.getGreen(0);
		color = Color.getBlue(0);
		color = Color.getGray(0);
		value = Color.getRGB(0.0f, 0.0f, 0.0f);
		value = Color.getRGB(0.0f);
		image = Color.toRGB(gray);
		gray = Color.toGray(image);

		// Check if the signatures of all required function in class Filter are correct 
		float[][] kernel = new float[3][3];
		color = Filter.at(gray, -1, -1);
		gray = Filter.filter(gray, kernel);
		gray = Filter.smooth(gray);
		gray = Filter.sobel(gray);

		// Check if the signatures of all required function in class Filter are correct
		int[] path, seam;
		float[] costs = new float[] {1, 1, 1};
		int[][] successors = new int[][] { {0,1}, {}, {4}};
		float [][] energy = new float[4][4];
		path  = Seam.path(successors, costs, 0, 2);
		seam  = Seam.find(energy);
		image = Seam.merge(image, seam);		

	}
}
