
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
    	//for (row = 0; row < gray.length; row++) {
    	//	for (col = 0; col < gray[row].length; col++) {
    	//		
    	//	}
    	//}
    	//
    	float check = 0.0f;
    	try {
    		check = gray[row][col]; 
    	} catch (Exception e) {
    		int[] checkVars = {-1,1,0,0};
    		int[] checkCorners = {-1,-1,1,1,1,-1,-1,1};
    		int l = 3;
    		for (int k = 0; k < 4; k++, l--) {
    			try {
    				check = gray[row + checkVars[k]][col + checkVars[l]];
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
        return null;
    }

    /**
     * Smooth a single-channel image
     * @param gray a HxW float array
     * @return a HxW float array
     */
    public static float[][] smooth(float[][] gray) {
        // TODO smooth
        return null;
    }

    /**
     * Compute horizontal Sobel filter
     * @param gray a HxW float array
     * @return a HxW float array
     */
    public static float[][] sobelX(float[][] gray) {
        // TODO sobelX
        return null;
    }

    /**
     * Compute vertical Sobel filter
     * @param gray a HxW float array
     * @return a HxW float array
     */
    public static float[][] sobelY(float[][] gray) {
        // TODO sobelY
        return null;
    }

    /**
     * Compute the magnitude of combined Sobel filters
     * @param gray a HxW float array
     * @return a HxW float array
     */
    public static float[][] sobel(float[][] gray) {
        // TODO sobel
        return null;
    }

}
