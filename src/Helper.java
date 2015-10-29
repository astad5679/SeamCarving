
import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Provide simple tools to read, write and show pictures. 
 * (This class was supplied by default because with our current programming knowledge we would be unable to implement it)
 */
public final class Helper {

    // Convert specified BufferedImage into an array
    private static int[][] fromBufferedImage(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] array = new int[height][width];
        for (int row = 0; row < height; ++row) {
            for (int col = 0; col < width; ++col) {
                array[row][col] = image.getRGB(col, row) & 0xffffff;
            }
        }
        return array;
    }

    // Convert specified array int a BufferedImage
    private static BufferedImage toBufferedImage(int[][] array) {
        int width = array[0].length;
        int height = array.length;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int row = 0; row < height; ++row) {
            for (int col = 0; col < width; ++col) {
                image.setRGB(col, row, array[row][col] | 0xff000000);
            }
        }
        return image;
    }

    /**
     * Reads specified image from disk.
     * @param path Input file path
     * @return HxW array of packed RGB colors, or <code>null</code> on failure
     * @see #write
     */
    public static int[][] read(String path) {
        try {
            BufferedImage image = ImageIO.read(new File(path));
            return fromBufferedImage(image);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Writes specified image to disk.
     * @param path Output file path
     * @param array HxW array of packed RGB colors
     * @return {@code true} if write operation was successful, {@code false} otherwise
     * @see #read
     */
    public static boolean write(String path, int[][] array) {

        // Convert array to Java image
        BufferedImage image = toBufferedImage(array);

        // Get desired file format
        int index = path.lastIndexOf('.');
        if (index < 0)
        	return false;
        String extension = path.substring(index + 1);

        // Export image
        try {
            return ImageIO.write(image, extension, new File(path));
        } catch (IOException e) {
            return false;
        }

    }

    /**
     * Shows specified image in a window.
     * @param array HxW array of packed RGB colors
     * @param title title to be displayed
     */
    public static void show(int[][] array, String title) {

        // Convert array to Java image
        final BufferedImage image = toBufferedImage(array);

        // Create a panel to render this image
        @SuppressWarnings("serial")
		JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, getWidth(), getHeight(), null, null);
            }
        };

        // Create a frame to hold this panel
        final JFrame frame = new JFrame(title);
        frame.add(panel);
        frame.pack();
        frame.setSize(image.getWidth(), image.getHeight());

        // Register closing event
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.setVisible(false);
                synchronized (frame) {
                    frame.notifyAll();
                }
            }
        });

        // Show this frame
        frame.setVisible(true);

        // Wait for close operation
        try {
            synchronized (frame) {
                while (frame.isVisible())
                    frame.wait();
            }
        } catch (InterruptedException e) {
            // Empty on purpose
        }
        frame.dispose();

    }

}
