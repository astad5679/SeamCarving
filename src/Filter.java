
/**
 * @author Andrés Stadelmann
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
    	try {
    		check = gray[row][col]; 
    	} catch (Exception e) {
    		int[] checkSides = {-1,1,0,0};
    		int[] checkCorners = {-1,-1,1,1,1,-1,-1,1};
    		int l = 3;
    		for (int k = 0; k < 4; k++, l--) {
    			try {
    				check = gray[row + checkSides[k]][col + checkSides[l]];
    				break;
    			} catch (Exception f) {
    				try {
    					check = gray[row + checkCorners[k]][col + checkCorners[checkCorners.length - 1 - k]];
    					break;
    				} catch (Exception g) {
    					continue;
    				}
    			}
    		}
    	}
        return check;
    }

    /**
     * Convolve a single-channel image with specified kernel.
     * @param gray a HxW float array
     * @param kernel a MxN float array, with M and N odd
     * @return a HxW float array
     */
    public static float[][] filter(float[][] gray, float[][] kernel) {
        // TODO filter
    	float current = 0.0f;
    	float around = 0.0f;
    	float[][] filtered = new float[gray.length][gray[0].length];
    	for (int col = 0; col < gray.length; col++) {
        	for (int row = 0; row < gray[0].length; row++) {
        		for (int i = -1; i < 2; i++) {
        			for (int j = -1; j < 2; j++) {
        				around = at(gray, i + col, j + row);
        				current += around * kernel[i+1][j+1];
        			}
        		}
        		filtered[col][row] = current; 
        		current = 0.0f;
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
    public static float[][] sobelX(float[][] gray) {
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
    	for (int col = 0; col < gray.length; col++) {
    		for (int row = 0; row < gray[0].length; row++) {
    			sobelSum[col][row] = (float) Math.sqrt(Math.pow(sobelx[col][row], 2.0) + Math.pow(sobely[col][row], 2.0));
    		}
    	} 	
        return sobelSum;
    }

}
