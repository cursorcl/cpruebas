/**
 * Aug 9, 2014 - Turbo7
 */
package cl.triton.card;

import java.awt.Image;
import java.util.logging.Logger;

/**
 * A panel that can map any image to their grid size.
 * 
 * @author Turbo7
 */
public class ImageInputProvider implements InputProvider {

  static final Logger log = Logger.getLogger(ImageInputProvider.class.getName());

  static final int DEFAULT_WIDTH = 18;
  static final int DEFAULT_HEIGHT = 24;

  int width = DEFAULT_WIDTH;
  int height = DEFAULT_HEIGHT;

  ImageInput imgi = null;

  public ImageInputProvider() {}

  public void resetSize(int iwidth, int iheight) {
    this.width = iwidth;
    this.height = iheight;
  }

  public void setImage(ImageInput ii) {
    imgi = ii;
  }

  public void setImage(Image image) {
    imgi = new ImageInput(width, height);
    imgi.map(image);
  }

  static final int RANDOM_RANGE = 10;

  public int getInputWidth() {
    return width;
  }

  public int getInputHeight() {
    return height;
  }

  @Override
  public int getInputSize() {
    return width * height;
  }

  static double colorToGrayscale(int rgb) {
    double red = (rgb & 0x000000FF) / 255.0;
    double green = ((rgb & 0x0000FF00) >> 8) / 255.0;
    double blue = ((rgb & 0x00FF0000) >> 16) / 255.0;

    return 0.2126 * red + 0.7152 * green + 0.0722 * blue;
  }

  @Override
  public double getInput(int index) {
    int col = index % width;
    int row = index / width;

    int currentColor = (int) imgi.getValue(col, row);
    double value = colorToGrayscale(currentColor);

    return 1 - value;
  }
}
