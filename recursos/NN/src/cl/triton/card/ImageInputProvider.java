/**
 * Oct 14, 2014 - Turbo7
 */
package cl.triton.card;

import java.awt.Image;

/**
 * @author Turbo7
 */
public class ImageInputProvider implements InputProvider {
    static double colorToGrayscale(int rgb) {
        final double red = (rgb & 0x000000FF) / 255.0;
        final double green = ((rgb & 0x0000FF00) >> 8) / 255.0;
        final double blue = ((rgb & 0x00FF0000) >> 16) / 255.0;

        return 0.2126 * red + 0.7152 * green + 0.0722 * blue;
    }

    final Image image;
    final int width;

    final int height;

    final ImageInput imgi;

    public ImageInputProvider(Image image, int width, int height) {
        this.image = image;
        this.width = width;
        this.height = height;

        imgi = new ImageInput(width, height);
        imgi.map(image);
    }

    @Override
    public double getInput(int index) {
        final int col = index % width;
        final int row = index / width;

        final int rgb = (int) imgi.getValue(col, row);
        final double value = ImageInputProvider.colorToGrayscale(rgb);

        return 1 - value;
    }

    @Override
    public int getInputSize() {
        return width * height;
    }
}
