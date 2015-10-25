
import org.junit.Assert;
import org.junit.Test;
import org.junit.internal.InexactComparisonCriteria;

public class Tests {
	
	// Tolerance used to compare two floating point numbers
	private static final float EPSILON = 0.0001f;
	
	// Check whether value is equal to expected integer
	private void assertEquals(int expected, int actual) {
		Assert.assertEquals(expected, actual);
	}
	
	// Check whether value is approximately equal to expected floating point number
	private void assertEquals(float expected, float actual) {
		Assert.assertEquals(expected, actual, EPSILON);
	}
	
	// Check whether integer array has correct size and values
	private void assertEquals(int[] expected, int[] actual) {
		Assert.assertArrayEquals(expected, actual);
	}
	
	// Check whether integer two-dimensional array has correct size and values
	private void assertEquals(int[][] expected, int[][] actual) {
		Assert.assertArrayEquals(expected, actual);
	}
	
	// Check whether floating point two-dimensional array has correct size and similar values
	private void assertEquals(float[][] expected, float[][] actual) {
		new InexactComparisonCriteria(EPSILON).arrayEquals(null, expected, actual);
	}
	
	// Ignore alpha channel
	private int removeAlpha(int color) {
		return color & 0xffffff;
	}
	
	// Ignore alpha channel
	private int[][] removeAlpha(int[][] color) {
		if (color == null)
			return null;
		int[][] result = new int[color.length][];
		for (int row = 0; row < color.length; ++row)
			if (color[row] != null) {
				result[row] = new int[color[row].length];
				for (int col = 0; col < color[row].length; ++col)
					result[row][col] = removeAlpha(color[row][col]);
			}
		return result;
	}
	
	// Check whether color is equal to expected value (alpha is ignored)
	private void assertEqualsColor(int expected, int actual) {
		assertEquals(removeAlpha(expected), removeAlpha(actual));
	}
	
	// Check whether color two-dimensional array has correct size and values (alpha is ignored)
	private void assertEqualsColor(int[][] expected, int[][] actual) {
		assertEquals(removeAlpha(expected), removeAlpha(actual));
	}

	// Test based on example in section 3.1 (pixel coding)
	@Test
	public void testColorSinglePixel() {
		
		// Check RGB components and gray level of this bluish color
		int color = 0b00100000_11000000_11111111;
		assertEquals(0.1254902f, Color.getRed(color));
		assertEquals(0.7529412f, Color.getGreen(color));
		assertEquals(1.0f, Color.getBlue(color));
		assertEquals(0.6261438f, Color.getGray(color));
		
		// Encode some colors
		assertEqualsColor(0x0000ff, Color.getRGB(0.0f, 0.0f, 1.0f));
		assertEqualsColor(0x7f7f7f, Color.getRGB(0.5f));
		assertEqualsColor(0x0000ff, Color.getRGB(-0.5f, 0, 2));
		assertEqualsColor(0x000000, Color.getRGB(-1.0f));
		
	}
	
	// Test based on example in section 3.2 (image coding)
	@Test
	public void testColorWholeImage() {
		
		// A 2x2 image
		int[][] image = {
	        {0x20c0ff, 0x123456},
	        {0xffffff, 0x000000}
	    };
		
		// Convert to grayscale
		float[][] gray = {
		    {0.62614375f, 0.20392157f},
		    {1.0f, 0.0f}
		};
		assertEquals(gray, Color.toGray(image));
		
		// Convert back to integer representation 
		int[][] back = {
            {0x9f9f9f, 0x343434},
            {0xffffff, 0x000000}
		};
	    assertEqualsColor(back, Color.toRGB(gray));
	    
	}
	
	// Test based on example in section 4.1 (image filtering)
	@Test
	public void testFilterConvolution() {
		
		// A 2x2 grayscale image
		float[][] gray = {
		    {0.5f, 1.0f},
			{0.2f, 0.0f}
		};
		
		// Access some pixels
		assertEquals(0.5f, Filter.at(gray, 0, 0));
		assertEquals(0.2f, Filter.at(gray, 1, 0));
		assertEquals(0.2f, Filter.at(gray, 2, -1));
		
		// Apply smoothing and Sobel
		assertEquals(new float[][] {
			{0.49f, 0.62f},
			{0.3f, 0.29f}
		}, Filter.smooth(gray));
		assertEquals(new float[][] {
			{1.3f, 1.3f},
			{-0.1f, -0.1f}
		}, Filter.sobelX(gray));
		assertEquals(new float[][] {
			{-1.9f, -3.3f},
			{-1.9f, -3.3f}
		}, Filter.sobelY(gray));
		assertEquals(new float[][] {
			{2.302173f, 3.5468295f},
			{1.9026297f, 3.3015149f}
		}, Filter.sobel(gray));
		
	}
	
	// Test based on example in section 5.1 (shortest path)
	@Test
	public void testSeamPath() {
		
		// Simple graph stored as an adjacency list
		int[][] successors = new int[][] {
			/* 0 -> */ {1, 3},
			/* 1 -> */ {2},
			/* 2 -> */ {},
			/* 3 -> */ {4},
			/* 4 -> */ {1, 5},
			/* 5 -> */ {2}
		};
		float[] costs = new float[] {
			/* 0 : */ 1.0f,
			/* 1 : */ 9.0f,
			/* 2 : */ 2.0f,
			/* 3 : */ 1.0f,
			/* 4 : */ 3.0f,
			/* 5 : */ 1.0f
		};
		
		// Compute and check shortest path
		int[] vertices = Seam.path(successors, costs, 0, 2);
		assertEquals(new int[] {0, 3, 4, 5, 2}, vertices);
		
	}
	
	// Test based on example in section 5.2 (resizing)
	@Test
	public void testSeamResize() {
		
		// A 3x3 image
		int[][] image = {
			{0x888888, 0x666666, 0xcccccc},
		    {0x000000, 0x111111, 0x222222},
		    {0xbbbbbb, 0xffffff, 0x666666}
		};
		float[][] gray = Color.toGray(image);
		
		// Find best seam, using gray level as energy
		int[] seam = Seam.find(gray);
		assertEquals(new int[] {1, 1, 2}, seam);
		
		// Remove seam
		int[][] resized = Seam.shrink(image, seam);
		assertEqualsColor(new int[][] {
			{0x888888, 0xcccccc},
			{0x000000, 0x222222},
			{0xbbbbbb, 0xffffff}
		}, resized);
		
	}
	
}
