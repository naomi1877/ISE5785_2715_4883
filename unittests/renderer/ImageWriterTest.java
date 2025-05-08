package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

import static org.junit.jupiter.api.Assertions.*;
class ImageWriterTest {
    @Test
    void  testImageWriter() {
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
        int gridSize = 50;

        for (int i = 0; i < nX; i++) {
            for (int j = 0; j < nY; j++) {
                if (i % gridSize == 0 || j % gridSize == 0) {
                    imageWriter.writePixel(i, j, gridColor);
                }
            }
        }
        //saving the image to the file
        imageWriter.writeToImage(imageName);
    }
}