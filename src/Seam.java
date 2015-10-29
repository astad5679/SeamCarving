
/**
 * @author Andres Stadelmann
 */
import java.util.ArrayList;

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
	
	/*public static int bestPredecessor(int staticID) {
		return staticID;
	}
	*/
	
	public static int[][] successors(float[][] energy) {
		int height = energy.length;
		int width = energy[0].length;
		int[][] successors = new int[height * width + 2][];
		successors[height * width] = new int[width];
		
		for (int row = 0; row < height; row++) {
			//System.out.print("{");
    		for (int col = 0; col < width; col++) {
    			//System.out.print("{");
    			int stateID = getStateID(row, col, width);
    			
    			if (row == 0) {
    				successors[height * width][col] = stateID;
    			} else if (row == height - 1) {
    				successors[stateID] = new int[] {width * height + 1};
    				//System.out.print(successors[stateID][0]);
    			} if (row >= 0 && row < height - 1){
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
    			}
    			   					
    	    	//costs[stateID] = energy[row][col];
    	    	//System.out.print(costs[stateID]);
    	    	//System.out.print("}");
    		}
    		//System.out.println("}");
    		
    	}
		successors[width * width + 1] = new int[] {};
		return successors;
	}
	
	public static int[] costs(int[][] successors, float[][] energy) {
		float[] costs = new float[successors.length];
    	for (int item = 0; item < costs.length; item++) {
    		costs[item] = Float.POSITIVE_INFINITY;
    	}
    	
    	int[] bestPredecessor = new int[costs.length];

    	int height = energy.length;
		int width = energy[0].length;
		for (int item : successors[height * width]) {
			costs[item] = energy[0][item];
			//System.out.print(costs[item] + ",");
			bestPredecessor[item] = height * width;
			//System.out.print("{cost of [" + item + "] = " + costs[item] + "}");
		}
		//System.out.println();
		for (int row = 0; row < height; row++) {
			//System.out.print("{");
			if (row == height - 1) {
				break;
			}
    		for (int col = 0; col < width; col++) {
    			//System.out.print("{");
    			int stateID = getStateID(row, col, width);
    			for (int item : successors[stateID]) {
    				if (costs[item] > costs[stateID] + energy[row + 1][getCol(item, width)]) {
        				costs[item] = costs[stateID] + energy[row + 1][getCol(item, width)];
        				bestPredecessor[item] = stateID;
        				//System.out.print("{cost[" + item + "] = " + costs[item] + "}");
        			} 
    				
    			}    			
    			//System.out.print("}");
    		}
    		
    		//System.out.println("}");
		}
		int finalID = (width * height + 1);
		//costs[finalID] = Float.POSITIVE_INFINITY;
		for (int stateID = getStateID(height - 1, 0, width); stateID < finalID; stateID++) {
			if (costs[finalID] > costs[stateID]) {
				costs[finalID] = costs[stateID];
				bestPredecessor[finalID] = stateID;
				//System.out.print("{cost of [" + finalID + "] = " + costs[finalID] + "}");
				//System.out.println("{predecessor of [" + finalID + "] = " + bestPredecessor[finalID] + "}");
			}
			
		}
		int[] seam = path(bestPredecessor, costs, width * height, width * height + 1);
		return seam;
	}
	
    /**
     * Compute shortest path between {@code from} and {@code to}
     * @param successors adjacency list for all vertices
     * @param costs weight for all vertices
     * @param from first vertex
     * @param to last vertex
     * @return a sequence of vertices, or {@code null} if no path exists
     */
    public static int[] path(int[] bestPredecessor, float[] costs, int from, int to) {
        // TODO path
    	ArrayList<Integer> path = new ArrayList<Integer>();
    	int predecessor = to;
    	while (predecessor != from) {
    		path.add(bestPredecessor[predecessor]);
    		predecessor = bestPredecessor[predecessor];
    		//System.out.print(predecessor + ",");
    	} 
    	
    	int[] seam = new int[path.size() - 1];
    	int j = 0;
    	for(int i = path.size() - 2; i >= 0; i--, j++) {
    	    seam[j] = path.get(i);  
    	    //System.out.print(seam[j] + ",");
    	}
        return seam;
    }

    /**
     * Find best seam
     * @param energy weight for all pixels
     * @return a sequence of x-coordinates (the y-coordinate is the index)
     */
    public static int[] find(float[][] energy) {
        // TODO find
    	int[][] successors = successors(energy);
    	int[] path = costs(successors, energy);
    	
    	/*for (int item : seam) {
    		System.out.println(item);
    	}
    	*/
    	
    	int[] seam = new int[path.length];
    	for (int point : path) {
    		int col = getCol(point, energy[0].length);
    		int row = getRow(point, energy[0].length);
    	}
    
        return seam;
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
