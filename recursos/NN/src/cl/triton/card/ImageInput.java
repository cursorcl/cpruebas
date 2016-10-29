/**
 * Aug 9, 2014 - Turbo7
 */
package cl.triton.card;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * An entity that map any image to a grid.
 *
 * @author Turbo7
 */
public class ImageInput {

    final int width;
    final int height;
    final BufferedImage gridImage;

    public ImageInput(int width, int height) {
        this.width = width;
        this.height = height;

        gridImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public int getHeight() {
        return height;
    }

    public double getValue(int x, int y) {
        return gridImage.getRGB(x, y);
    }

    public int getWidth() {
        return width;
    }

    public void map(Image image) {
        final Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        final Graphics2D grph = gridImage.createGraphics();
        grph.drawImage(scaledImage, 0, 0, null);
        grph.dispose();
    }
}
