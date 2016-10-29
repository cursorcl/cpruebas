/**
 * Oct 9, 2014 - ayachan
 */
package cl.cursor.card;

import java.awt.Image;

import cl.sisdef.util.Pair;

/**
 * A pattern recognizer.
 *
 * @author ayachan
 */
public interface Recognizer {

    /**
     * Test the image and return the recognition result.
     * 
     * @param image
     *            The image to be recognized.
     * @return The recognition process output.
     */
    double[] match(Image image);

    /**
     * Recognize an image.<br />
     * If the recognition process resolve a solution into the given tolerance,
     * then that answer index is returned, else -1.
     * 
     * @param image
     *            The image to be recognized.
     * @param tolerance
     *            The tolerance to admit a solution. A value in percentage that
     *            should be done between the best solution versus the others.
     * @return The index of the best solution if into tolerance, -1 otherwise.
     */
    Pair<Integer, Pair<Double, Double>> recognize(Image image, double tolerance);
}
