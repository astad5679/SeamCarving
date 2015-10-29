
/**
 * @author Andres Stadelmann
 */
public final class Main {

    public static void main(String[] args) {

        // Load image
        System.out.println("Load image...");
        int[][] image = Helper.read("tower.jpg"); 	//You can change this .jpg to any one you see fit provided it is within the project folder 
        Helper.show(image, "Original");						//(for the purpose of this project however, the width of the image has to be greater than the height)

        // Convert to grayscale
        System.out.println("Convert to grayscale...");
        float[][] gray = Color.toGray(image);
        Helper.show(Color.toRGB(gray), "Grayscale");	//Color.toRGB() is required to create the corresponding rgb values for the floats in the array "gray"
        												//However, float[][] gray is still kept as all the filters need to be applied on the vanilla float values
        // Smooth it
        System.out.println("Smooth image...");
        float[][] smooth = Filter.smooth(gray);
        Helper.show(Color.toRGB(smooth), "Smooth");
        
        // Apply Sobel
        System.out.println("Compute Sobel filter...");
        float[][] sobel = Filter.sobel(smooth);
        Helper.show(Color.toRGB(sobel), "Sobel");
        
        // Find best seam
        System.out.println("Find best seam...");			//Never mind, I fixed that. However, I have completely neglected a method that was supplied by default, so I'm gonna have to
        int[] seam = Seam.find(sobel);						//use that somehow, because that would be technically required
        Helper.show(Seam.merge(image, seam), "Best seam");

        // Shrink until it is a square
        int count = image[0].length - image.length;							//This could be manipulated to dynamically accept user input values, such as resizing height as well as width
        System.out.println("Shrink by removing " + count + " seams...");	//(To not just recreate squares)
        for (int i = 0; i < count; ++i) {
            sobel = Filter.sobel(Filter.smooth(Color.toGray(image)));
            seam = Seam.find(sobel);
            image = Seam.shrink(image, seam);
            System.out.println("Seam " + (i + 1) + "/" + count);
        }
        System.out.println("Done");
        Helper.show(image, "Shrink");

        // Save result
        Helper.write("hiroshige.shrink.jpg", image);

    }

}
