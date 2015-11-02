
/**
 * @author Andres Stadelmann
 */
public final class Color {

    /**
     * Returns red component from given packed color.
     * @param rgb 32-bits RGB color
     * @return a float between 0.0 and 1.0
     * @see #getGreen
     * @see #getBlue
     * @see #getRGB(float, float, float)
     */
    public static float getRed(int rgb) {
        // TODO getRed
    	float red = rgb >> 16 & 0b1111_1111;
        return red/255.0f;
    }

    /**
     * Returns green component from given packed color.
     * @param rgb 32-bits RGB color
     * @return a float between 0.0 and 1.0
     * @see #getRed
     * @see #getBlue
     * @see #getRGB(float, float, float)
     */
    public static float getGreen(int rgb) {
        // TODO getGreen
    	float blue = rgb >> 8 & 0b1111_1111;
        return blue/255.0f;
    }

    /**
     * Returns blue component from given packed color.
     * @param rgb 32-bits RGB color
     * @return a float between 0.0 and 1.0
     * @see #getRed
     * @see #getGreen
     * @see #getRGB(float, float, float)
     */
    public static float getBlue(int rgb) {
        // TODO getBlue
    	float blue = rgb & 0b1111_1111;
        return blue/255.0f;
    }
    
    /**
     * Returns the average of red, green and blue components from given packed color.
     * @param rgb 32-bits RGB color
     * @return a float between 0.0 and 1.0
     * @see #getRed
     * @see #getGreen
     * @see #getBlue
     * @see #getRGB(float)
     */
    public static float getGray(int rgb) {
        // TODO getGray
    	float red = getRed(rgb);
    	float green = getGreen(rgb);
    	float blue = getBlue(rgb);
        return (red + green + blue)/3.0f;
    }

    /**
     * Returns packed RGB components from given red, green and blue components.
     * @param red a float between 0.0 and 1.0
     * @param green a float between 0.0 and 1.0
     * @param blue a float between 0.0 and 1.0
     * @return 32-bits RGB color
     * @see #getRed
     * @see #getGreen
     * @see #getBlue
     */
    public static int getRGB(float red, float green, float blue) {
        // TODO getRGB
    	int rgb = (int) (checkFloat(red) * 255.0f);
		rgb = (rgb << 8) + (int) (checkFloat(green) * 255.0f);
		rgb = (rgb << 8) + (int) (checkFloat(blue) * 255.0f);
        return rgb;
    }
    
    /*
     * This method is used to check if a float given as a parameter in the two getRGB() methods is between 0.0f and 1.0f
     * If this fails to be the case, then the values are set to equal their respective boundaries
     */
    public static float checkFloat(float color) {
    	if (color < 0.0f) {
    		color = 0.0f;
    	} else if (color > 1.0f) {
    		color = 1.0f;
    	}
    	return color;
    }
    
    /**
     * Returns packed RGB components from given grayscale value.
	 * @param gray a float between 0.0 and 1.0
     * @return 32-bits RGB color
     * @see #getGray
     */
    public static int getRGB(float gray) { //the removed code seemed redundant since the other getRGB does the same thing. I just ended up calling it using the same parameter 3 times
    	// TODO getRGB
    	//int gray1 = (int) (checkFloat(gray) * 255.0f);
    	//int rgb = gray1;
		//rgb = (rgb << 8) + gray1;
		//rgb = (rgb << 8) + gray1;
    	int rgb = getRGB (gray, gray, gray);
        return rgb;
    }

    /**
     * Converts packed RGB image to grayscale float image.
     * @param image a HxW int array
     * @return a HxW float array
     * @see #toRGB
     * @see #getGray
     */
    public static float[][] toGray(int[][] image) {
        // TODO toGray
    	float[][] gray = new float[image.length][image[0].length];
    	for (int i = 0; i < image.length; i++) {
    		for (int j = 0; j < image[0].length; j++) {
    			int val = image[i][j];
    			gray[i][j] = getGray(val);
    		}
    	}
        return gray;
    }

    /**
     * Converts grayscale float image to packed RGB image.
     * @param channels a HxW float array
     * @return a HxW int array
     * @see #toGray
     * @see #getRGB(float)
     */
    public static int[][] toRGB(float[][] gray) {
    	// TODO toRGB
    	int[][] fullGray = new int[gray.length][gray[0].length];
    	for (int i = 0; i < gray.length; i++) {
    		for (int j = 0; j < gray[0].length; j++) {
    			float val = gray[i][j];
    			fullGray[i][j] = getRGB(val);
    		}
    	}
    	return fullGray;
    }
}
