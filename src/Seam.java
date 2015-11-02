
/**
 * @author Andres Stadelmann
 */
import java.util.ArrayList;

public final class Seam {
	
	//This method calculates and returns the state ID of any given pixel. The ID will be subsequently used to more easily store and access the potential successors of each state
	public static int getStateID(int row, int col, int maxCol) {
		int stateID = (row * maxCol) + col;
		return stateID;
	}
	
	//This method essentially does the opposite of the one just above. Given a certain state we wish to locate it as a pixel in the image, prompting us to call upon
	//the row and column
		//Remark: This is used primarily to locate the pixels of the calculated seam, so we can automatically assume (since we are reducing the width of the image) that each point on the
		//seam finds itself on the next row, so we only need to know the column number (thus the absence of a getRow)
	public static int getCol(int stateID, int maxCol) {
		int col = stateID % maxCol;
		return col;
	}
	
	
	//The entire point of this method is to exclusively store the "successors" of each pixel, namely all the possibilities of branching out (limited to a distance of 1 going downwards)
	//This can be drastically improved especially because the link between the image and each individual state isn't very smooth
	public static int[][] successors(float[][] energy) {
		int height = energy.length;
		int width = energy[0].length;
		int[][] successors = new int[height * width + 2][];
		successors[height * width] = new int[width];
		
		for (int row = 0; row < height; row++) {
    		for (int col = 0; col < width; col++) {
    			int stateID = getStateID(row, col, width);
    			
    			if (row == 0) {
    				successors[height * width][col] = stateID;
    			} else if (row == height - 1) {
    				successors[stateID] = new int[] {width * height + 1};
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
        			}
    			}
    		}
    	}
		successors[width * height + 1] = new int[] {};
		return successors;
	}
	
	//Transforms the energy array into a more accessible (single layer) costs array which corresponds to the IDs of the successor array
	public static float[] costs(float[][] energy) {
		float[] costs = new float[energy.length * energy[0].length];
		int i = 0;
		for (int row = 0; row < energy.length; row++) {
			for (int col = 0; col < energy[row].length; col++, i++) {
				costs[i] = energy[row][col];
			}
		}
		return costs;
	}
	
	
    /**
     * Compute shortest path between {@code from} and {@code to}
     * @param successors adjacency list for all vertices
     * @param costs weight for all vertices
     * @param from first vertex
     * @param to last vertex
     * @return a sequence of vertices, or {@code null} if no path exists
     */
	//This method is currently calculating the cost it takes to get to a given pixel. It then stores the cheapest one in it's respective position in the bestPredecessor[] array
    public static int[] path(int[][] successors, float[] costs, int from, int to) { 
        // TODO path
    	float[] paths = new float[successors.length];
    	for (int item = 0; item < paths.length; item++) {
    		paths[item] = Float.POSITIVE_INFINITY;
    	}
    	
    	int[] bestPredecessor = new int[paths.length]; //Used to store the current best predecessor (which will be replaced if out-dated)

		for (int item : successors[from]) { //Stores all the best predecessors for the first line because it is a special case
			paths[item] = costs[item];
			bestPredecessor[item] = from;
		}
		
		for (int stateID = 0; stateID < from; stateID++) {
			for (int successor : successors[stateID]) {
				try { 
					if (paths[successor] > paths[stateID] + costs[successor]) {
						paths[successor] = paths[stateID] + costs[successor];
						bestPredecessor[successor] = stateID;
    				}	
				} catch (Exception e) { //this catches the case in which we have reached the last line of the array, so we start comparing the cheapest paths
					if (paths[to] > paths[stateID]) {
						paths[to] = paths[stateID];
						bestPredecessor[to] = stateID;
					}
				}
			}    			
		}
		
		ArrayList<Integer> path = new ArrayList<Integer>(); //I use an ArrayList here for simplicity, because I don't have the height value of the matrix
    	int predecessor = to;
    	while (predecessor != from) { //store the vertices of each respective position (starting from the bottom) until we reach the from position
    		path.add(bestPredecessor[predecessor]); 
    		predecessor = bestPredecessor[predecessor];
    	} 
    	
    	int[] seam = new int[path.size() - 1]; 	//we then need to flip the list while cutting off the last term, this array will contain the stateID's but not the column positions
    	int j = 0;								//this is actually the reason why testSeamPath fails, because I will calculate the columns in the Seam.java (for reasons which I will explain below)
    	for(int i = path.size() - 2; i >= 0; i--, j++) {
    	    seam[j] = path.get(i);  
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
    	float[] costs = costs(energy);
    	int[] path = path(successors, costs, costs.length, costs.length + 1);
    	
    	int[] seam = new int[path.length];
    	int i = 0;
    	for (int point : path) {
    		seam[i] = getCol(point, energy[0].length); //To get the column position I use the getCol() method, and I need to pass the width of the line as an argument.
    		i++;									   //Since I constructed it this way, it was just way simpler to return the actual seam at this point and not earlier, thus the failure of the JUnit test.
    	}
    
        return seam;
    }

    //From this point on this is all default code (no fancy additions just yet...maybe in the future when I actually have time :D)
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
