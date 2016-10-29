/**
 * Aug 9, 2014 - Turbo7
 */
package cl.triton.card;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.Random;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import cl.sisdef.util.UtilForm;

/**
 * A panel that can map any image to their grid size.
 *
 * @author Turbo7
 */
public class ImagePanel extends JPanel implements InputProvider {
    private static final long serialVersionUID = 1L;

    static final Logger log = Logger.getLogger(ImagePanel.class.getName());

    static final int DEFAULT_WIDTH = 18;
    static final int DEFAULT_HEIGHT = 24;

    static final int RANDOM_RANGE = 10;

    static double colorToGrayscale(int rgb) {
        final double red = (rgb & 0x000000FF) / 255.0;
        final double green = ((rgb & 0x0000FF00) >> 8) / 255.0;
        final double blue = ((rgb & 0x00FF0000) >> 16) / 255.0;

        return 0.2126 * red + 0.7152 * green + 0.0722 * blue;
    }

    int width = ImagePanel.DEFAULT_WIDTH;

    int height = ImagePanel.DEFAULT_HEIGHT;

    JLabel[][] pixels = null;

    ImageInput imgi = null;

    final Random random = new Random(System.currentTimeMillis());

    public ImagePanel() {
        super(new BorderLayout());
        setBorder(UtilForm.createBorder("Input Pattern", null, BevelBorder.LOWERED));
        initialize();
    }

    void draw() {
        for (int col = 0; col < width; col++)
            for (int row = 0; row < height; row++) {
                final int rgb = (int) imgi.getValue(col, row);
                final Color color = new Color(rgb);
                // System.out.printf("painting color %s\n", color);
                pixels[col][row].setBackground(color);
            }
    }

    @Override
    public double getInput(int index) {
        final int col = index % width;
        final int row = index / width;

        final int currentColor = pixels[col][row].getBackground().getRGB();
        final double value = ImagePanel.colorToGrayscale(currentColor);

        // double value = colorToGrayscale( (int) imgi.getValue(col, row));
        //// log.info(String.format("(%3d): %f", index, value));
        return 1 - value;
    }

    public int getInputHeight() {
        return height;
    }

    @Override
    public int getInputSize() {
        return width * height;
    }

    public int getInputWidth() {
        return width;
    }

    final void initialize() {
        removeAll();

        pixels = new JLabel[width][height];

        final JPanel pixelsPanel = new JPanel(new GridLayout(height, width, 2, 2));
        for (int row = 0; row < height; row++)
            for (int col = 0; col < width; col++) {
                pixels[col][row] = new JLabel();
                pixels[col][row].setOpaque(true);
                pixelsPanel.add(pixels[col][row]);
            }
        add(pixelsPanel, BorderLayout.CENTER);

        validate();
    }

    public void noise() {
        for (int col = 0; col < width; col++)
            for (int row = 0; row < height; row++) {
                final Color color = pixels[col][row].getBackground();
                final Color noiseColor = new Color(pixelNoise(color.getRed()), pixelNoise(color.getGreen()),
                        pixelNoise(color.getBlue()));
                pixels[col][row].setBackground(noiseColor);
            }
    }

    // @Override
    // public double getValue(int x, int y)
    // {
    // return imgi.getValue(x, y);
    // }

    int pixelNoise(int value) {
        final int r = random.nextInt(2 * ImagePanel.RANDOM_RANGE) - ImagePanel.RANDOM_RANGE;
        value = value + r;
        if (value < 0)
            value = 0;
        else if (value > 255)
            value = 255;
        else
            ;
        return value;
    }

    public void resetSize(int iwidth, int iheight) {
        width = iwidth;
        height = iheight;

        initialize();
    }

    public void setHint(int col, int row, String hint) {
        pixels[col][row].setToolTipText(hint);
    }

    public void setImage(Image image) {
        imgi = new ImageInput(width, height);
        imgi.map(image);
        draw();
    }

    public void setImage(ImageInput ii) {
        imgi = ii;
        draw();
    }
}
