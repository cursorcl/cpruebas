/**
 * Aug 25, 2014 - Turbo7
 */
package cl.triton.card;

import org.neuroph.nnet.MultiLayerPerceptron;

/**
 * @author Turbo7
 */
public class TestNeuroph {

    static final int DEFAULT_WIDTH = 18;
    static final int DEFAULT_HEIGHT = 24;

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        // create multi layer perceptron network
        // ... inner cells = (input + output)/2 + 1
        final int innerCellsCount = (TestNeuroph.DEFAULT_WIDTH * TestNeuroph.DEFAULT_HEIGHT + 10) / 2 + 1;
        new MultiLayerPerceptron(TestNeuroph.DEFAULT_WIDTH * TestNeuroph.DEFAULT_HEIGHT, innerCellsCount, 10);

        // create training set
    }

}
