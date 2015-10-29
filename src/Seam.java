
/**
 * @author Andres Stadelmann
 */
public final class Seam {

	public static int getStateID(int row, int col, int maxCol) {
		int stateID = (row * maxCol) + col;
		return stateID;
	}
	
	public static int getRow(int stateID, int maxCol) {
		int row = stateID / maxCol;
		return row;
	}
	
	public static int getCol(int stateID, int maxCol) {
		int col = stateID % maxCol;
		return col;
	}
	
	public static int[][] successors(float[][] energy) {
		int height = energy.length;
		int width = energy[0].length;
		int[][] successors = new int[height * width + 2][];
		successors[height * width] = new int[width];

		int[] costs = new int[height * width + 2];
		for (int row = 0; row < height; row++) {
			//System.out.print("{");
    		for (int col = 0; col < width; col++) {
    			//System.out.print("{");
    			int stateID = getStateID(row, col, width);
    			
    			if (row == 0) {
    				successors[height * width][col] = stateID;
    			}
    			   			
    			int[] sucIt;
    			if (col == 0) {
    				successors[stateID] = new int[2];
    				sucIt = new int[] {0,1};
    			} else if (col == width - 1) {
    				successors[stateID] = new int[2];
    				sucIt = new int[] {-1,0};
    			} else {
    				successors[stateID] = new int[3];
    				sucIt = new int[] {-1,0,1};
    			}
    			
    			for (int k = 0; k < sucIt.length; k++) {
    				successors[stateID][k] = getStateID(row + 1, col + sucIt[k], width);
    				//System.out.print(successors[stateID][k] + ",");
    			}
    	    	costs[stateID] = (int) energy[row][col];
    	    	//System.out.print("}");
    		}
    		//System.out.println("}");
    		
    	}
		successors[width * width + 1] = new int[] {};
		return successors;
	}
	
	
    /**
     * Compute shortest path between {@code from} and {@code to}
     * @param successors adjacency list for all vertices
     * @param costs weight for all vertices
     * @param from first vertex
     * @param to last vertex
     * @return a sequence of vertices, or {@code null} if no path exists
     */
    public static int[] path(int[][] successors, float[] costs, int from, int to) {
        // TODO path
        return null;
    }

    /**
     * Find best seam
     * @param energy weight for all pixels
     * @return a sequence of x-coordinates (the y-coordinate is the index)
     */
    public static int[] find(float[][] energy) {
        // TODO find
    	int[][] successors = successors(energy);
    	for (int row = 0; row < successors.length; row++) {
    		System.out.print("{");
    		for (int col = 0; col < successors[row].length; col++) {
    			System.out.print(energy[row][col] + ",");
    		}
    		System.out.println("}");
    	}	
    	
    	int[] hi = {0};
        return hi;
    }

    /**
     * Draw a seam on an image
     * @param image original image
     * @param seam a seam on this image
     * @return a new image with the seam in blue
     */
    public static int[][] merge(int[][] image, int[] seam) {
        // Copy image
        int width = image[0].length;
        int height = image.length;
        int[][] copy = new int[height][width];
        for (int row = 0; row < height; ++row)
            for (int col = 0; col < width; ++col)
                copy[row][col] = image[row][col];

        // Paint seam in blue
        for (int row = 0; row < height; ++row)
            copy[row][seam[row]] = 0x0000ff;

        return copy;
    }

    /**
     * Remove specified seam
     * @param image original image
     * @param seam a seam on this image
     * @return the new image (width is decreased by 1)
     */
    public static int[][] shrink(int[][] image, int[] seam) {
        int width = image[0].length;
        int height = image.length;
        int[][] result = new int[height][width - 1];
        for (int row = 0; row < height; ++row) {
            for (int col = 0; col < seam[row]; ++col)
                result[row][col] = image[row][col];
            for (int col = seam[row] + 1; col < width; ++col)
                result[row][col - 1] = image[row][col];
        }
        return result;
    }

}
