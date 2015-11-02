
/**
 * @author Andres Stadelmann
 */
public final class Filter {

    /**
     * Get a pixel without accessing out of bounds
     * @param gray a HxW float array
     * @param row Y coordinate
     * @param col X coordinate
     * @return nearest valid pixel color
     */
    public static float at(float[][] gray, int row, int col) {
        // TODO at
    	float check = 0.0f;
    	try { //if the sought pixel is out of bounds, we need to catch that exception
    		check = gray[row][col]; 
    	} catch (Exception e) {
    		int[] checkSides = {-1,1,0,0};
    		for (int k = 0, l = 3; k < 4; k++, l--) { //we first check in each of the cardinal positions
    			try {
    				check = gray[row + checkSides[k]][col + checkSides[l]];
    				return check; //if found it will end the loop and return the value
    			} catch (Exception f) {
    				continue;
    			}
    		}
    		int[] checkCorners = {-1,-1,1,1,1,-1,-1,1}; 
    		for (int k = 0; k < 4; k++) { //if all checks failed it will proceed to check the individual corners 
    			try {
					check = gray[row + checkCorners[k]][col + checkCorners[checkCorners.length - 1 - k]];
					break; //break if found to prevent overriding the found value and iterating unnecessarily 
				} catch (Exception g) {
					continue;
				}	
    		}
    	}
        return check; //instead of putting return check whenever it was found, I placed it here to act as a fail-safe in case no case matched (which should never happen, but still)
    }

    /**
     * Convolve a single-channel image with specified kernel.
     * @param gray a HxW float array
     * @param kernel a MxN float array, with M and N odd
     * @return a HxW float array
     */
    public static float[][] filter(float[][] gray, float[][] kernel) {
        // TODO filter
    	float current = 0.0f; //This value corresponds to the sum of all the "around" values in the 3x3 matrix around the current position 
    	float around = 0.0f; //each individual position around the current position that needs to be individually filtered
    	float[][] filtered = new float[gray.length][gray[0].length]; //need to create a separate matrix here because the filtered values depend on the original matrix values 
    	for (int row = 0; row < gray.length; row++) {
        	for (int col = 0; col < gray[0].length; col++) {
        		//first two for loops are used to do this procedure on every single pixel
                //the next two call upon the respective pixels around the one in question
        		for (int i = -1; i < 2; i++) {  
        			for (int j = -1; j < 2; j++) {
        				around = at(gray, i + row, j + col); //This calls a method which checks for the pixels around the one being modified
        				current += around * kernel[i+1][j+1];  //after the application of the filter these are then added to the new value
        			}
        		}
        		filtered[row][col] = current;  //The new value is now set into the filtered matrix
        		current = 0.0f; //reset
        	}
        }
        return filtered;
    }

    /**
     * Smooth a single-channel image
     * @param gray a HxW float array
     * @return a HxW float array
     */
    public static float[][] smooth(float[][] gray) {
        // TODO smooth
    	float[][] smoothKernel = {
				{0.1f,0.1f,0.1f},
				{0.1f,0.2f,0.1f},
				{0.1f,0.1f,0.1f}
		};
    	float [][] smooth = filter(gray, smoothKernel);
        return smooth;
    }

    /**
     * Compute horizontal Sobel filter
     * @param gray a HxW float array
     * @return a HxW float array
     */
    public static float[][] sobelX(float[][] gray) { //gray is a bit of a misnomer here because Main.java class passes the matrix smooth as a parameter here (but it can take any matrix anyways so whatever)
        // TODO sobelX
    	float[][] sobelX = {
				{-1.0f,0.0f,1.0f},
				{-2.0f,0.0f,2.0f},
				{-1.0f,0.0f,1.0f}
		};
    	float [][] sobel = filter(gray, sobelX);
        return sobel;
    }

    /**
     * Compute vertical Sobel filter
     * @param gray a HxW float array
     * @return a HxW float array
     */
    public static float[][] sobelY(float[][] gray) {
        // TODO sobelY
    	float[][] sobelY = {
				{-1.0f,-2.0f,-1.0f},
				{0.0f,0.0f,0.0f},
				{1.0f,2.0f,1.0f}
		};
    	float [][] sobel = filter(gray, sobelY);
        return sobel;
    }

    /**
     * Compute the magnitude of combined Sobel filters
     * @param gray a HxW float array
     * @return a HxW float array
     */
    public static float[][] sobel(float[][] gray) {
        // TODO sobel
    	float[][] sobelx = sobelX(gray);
    	float[][] sobely = sobelY(gray);
    	float[][] sobelSum = new float[gray.length][gray[0].length];
    	for (int row = 0; row < gray.length; row++) {
    		for (int col = 0; col < gray[0].length; col++) { //replace every position by the corresponding square root of the squared sums of the sobel counterparts
    			sobelSum[row][col] = (float) Math.sqrt(Math.pow(sobelx[row][col], 2.0) + Math.pow(sobely[row][col], 2.0));
    		}
    	} 	
        return sobelSum;
    }

}
