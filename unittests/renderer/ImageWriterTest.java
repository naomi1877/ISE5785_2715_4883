package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

/**
 * Unit tests for the {@link renderer.ImageWriter} class.
 */
class ImageWriterTest {
    /**
     * default constructor
     */
    public ImageWriterTest() {
    }

    /**
     * Test method for {@link renderer.ImageWriter#ImageWriter(int, int)}.
     */
    @Test
    void testImageWriter() {
        //arrange
        String imageName = "grid_test";
        int nX = 800;
        int nY = 500;
        ImageWriter imageWriter = new ImageWriter(nX, nY);

        //background color
        Color backgroundColor = new Color(255d, 255d, 0d);

        //coloring the all pixels with the background color
        for (int i = 0; i < nX; i++) {
            for (int j = 0; j < nY; j++) {
                imageWriter.writePixel(i, j, backgroundColor);
            }
        }

        //creating a grid - red color
        Color gridColor = new Color(255d, 0d, 0d);
        int ySize = 500 / 10;
        int xSize = 800 / 16;

        for (int i = 0; i < nX; i++) {
            for (int j = 0; j < nY; j++) {
                if (i % xSize == 0 || j % ySize == 0) {
                    imageWriter.writePixel(i, j, gridColor);
                }
            }
        }
        //saving the image to the file
        imageWriter.writeToImage(imageName);
    }
}