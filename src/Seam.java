
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
		int[][] successors = new int[energy.length * energy[0].length + 2][];
		int[] costs = new int[energy.length * energy[0].length + 2];
		for (int row = 0; row < energy.length; row++) {
    		for (int col = 0; col < energy[0].length; col++) {
    			if (row == 0) {
    				successors[energy.length * energy[0].length][col] = (int) energy[row][col];
    			}
    			int stateID = getStateID(row, col, energy[0].length);
    			
    			int[] sucIt = {-1,0,1};
    			successors[stateID] = new int[3];
    			for (int k = 0; k < 2; k++) {
    				successors[stateID][k] = sucIt[k];
    			}
    	    	costs[stateID] = (int) energy[row][col];
    		}
    	}
		successors[energy.length * energy[0].length + 1] = new int[] {};
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

        return null;
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
