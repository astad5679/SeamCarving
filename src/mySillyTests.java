
public class mySillyTests {
	public static void main(String[] args) {
		/*float test1 = 0b100000;
		int test = 255;
		int test2 = 255;
		
		String test1 = Integer.toBinaryString(255);
		String test = Integer.toBinaryString(0);
		String test2 = Integer.toBinaryString(255);
		test = test1 + test + test2;
		int t = 0b11111111011111111 & 0b111110011111111;
		//System.out.println(Integer.toBinaryString(t));
		//double test2 = 3.5;
		//double test3 = (double) test1;
		//System.out.println(test1/255.0f);
		//System.out.println(Integer.toBinaryString(255));
		//System.out.println(Integer.toBinaryString(255));
		int thing = 0x9f;
		//System.out.println(thing);
		//System.out.println(159.0/255.0);
		
		
		int grayN = (int) (159.0f);
        //String grayC = Integer.toBinaryString(grayN);
		int rgb = 159;
		rgb = (rgb << 8) + 159;
		rgb = (rgb << 8) + 159;
        System.out.println(Integer.toHexString(rgb));
        //*/
		
		float[][] testArray = {
				{0.3f,0.8f,0.9f},
				{0.2f,0.4f,0.3f},
				{0.1f,0.5f,0.2f}
		};
		float pos = Filter.at(testArray, -1, -1);
		System.out.println(pos); 
		
		
		float[][] kernel = {
				{0.1f,0.1f,0.1f},
				{0.1f,0.2f,0.1f},
				{0.1f,0.1f,0.1f}
		};
		
		float[][] gray = Filter.filter(testArray, kernel);
		
		for (int i = 0; i < 3; i++) {
			System.out.print("{");
			for (int j = 0; j < 3; j++) {
				System.out.print(gray[i][j] + ",");
			}
			System.out.println("}");
		}
		
	 }

}
