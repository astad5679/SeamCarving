
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
	
	//The next two methods essentially do the opposite of the one just above. Given a certain state we wish to locate it as a pixel in the image, prompting us to call upon
	//the row and column
		//Remark: This is used primarily to locate the pixels of the calculated seam, so we can automatically assume (since we are reducing the width of the image) that each point on the
		//seam finds itself on the next row, so we only need to know the column number
	public static int getRow(int stateID, int maxCol) {
		int row = stateID / maxCol;
		return row;
	}
	
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
		successors[width * height + 1] = new int[] {};
		return successors;
	}
	
	//This method is currently calculating the cost it takes to get to a given pixel. It then stores the cheapest one in it's respective position in the bestPredecessor[] array
	//Since the default method had some restrictive parameters that I couldn't use, I just ended up calculating the best path and transforming back to its respective image coordinates
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
		
		ArrayList<Integer> path = new ArrayList<Integer>();
    	int predecessor = width * height + 1;
    	while (predecessor != width * height) {
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
		//int[] seam = path(bestPredecessor, costs, width * height, width * height + 1);
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
	//Basically, this method does nothing
    public static int[] path(int[][] successors, float[] costs, int from, int to) { //even though this works, I changed the parameters, so I need to figure this out another way
        // TODO path
    	
    	int[] seam = new int[successors.length];
    	
        return seam;
    }

    /**
     * Find best seam
     * @param energy weight for all pixels
     * @return a sequence of x-coordinates (the y-coordinate is the index)
     */
    //This method is also a misnomer, we're gonna have to work on the assignment more precisely
    public static int[] find(float[][] energy) {
        // TODO find
    	int[][] successors = successors(energy);
    	int[] path = costs(successors, energy);
    	
    	/*for (int item : seam) {
    		System.out.println(item);
    	}
    	*/
    	
    	int[] seam = new int[path.length];
    	int i = 0;
    	for (int point : path) {
    		seam[i] = getCol(point, energy[0].length);
    		i++;
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
