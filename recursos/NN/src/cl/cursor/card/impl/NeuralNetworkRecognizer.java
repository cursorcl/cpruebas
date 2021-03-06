/**
 * Oct 9, 2014 - ayachan
 */
package cl.cursor.card.impl;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

import org.neuroph.core.NeuralNetwork;

import cl.cursor.card.Recognizer;
import cl.sisdef.util.Pair;
import cl.triton.card.ImageInputProvider;

/**
 * @author ayachan
 */
public class NeuralNetworkRecognizer implements Recognizer {
    static final Logger log = Logger.getLogger(NeuralNetworkRecognizer.class.getName());

    final NeuralNetwork<?> network;
    final int width;
    final int height;

    public NeuralNetworkRecognizer(File file) throws IOException {
        // the (first) file with the grid size
        NeuralNetworkRecognizer.log.info(String.format("Reading red %s", file.getName()));
        final BufferedReader brdr = new BufferedReader(new FileReader(file));
        final String[] grdsz = brdr.readLine().split(" ");
        final String nnfilename = brdr.readLine();
        brdr.close();
        width = Integer.parseInt(grdsz[0]);
        height = Integer.parseInt(grdsz[1]);

        NeuralNetworkRecognizer.log.info(String.format("Grid is read as %d x %d", width, height));

        final File nnFile = new File(file.getParent(), nnfilename);
        network = NeuralNetwork.createFromFile(nnFile);

        NeuralNetworkRecognizer.log.info(String.format("Grid is readed"));
    }

    @Override
    public double[] match(Image image) {
        // set the image
        final ImageInputProvider iip = new ImageInputProvider(image, width, height);

        // the input
        final double[] input = new double[iip.getInputSize()];
        for (int n = 0; n < iip.getInputSize(); n++)
            input[n] = iip.getInput(n);

        // the evaluation
        network.setInput(input);
        network.calculate();
        return network.getOutput();
    }

    @Override
    public Pair<Integer, Pair<Double, Double>> recognize(Image image, double tolerance) {
        final double[] output = match(image);

        // StringBuffer sb = new StringBuffer();
        // for (int n=0; n<output.length; n++)
        // {
        // if (sb.length() > 0)
        // sb.append(", ");
        // sb.append(String.format("[%2d]: %.3f", n, output[n]));
        // }
        // log.info(sb.toString());

        int bestIndex = -1;
        double bestValue = Double.NEGATIVE_INFINITY;
        double bestOther = Double.NEGATIVE_INFINITY;

        for (int n = 0; n < output.length; n++) {
            if (output[n] > bestValue) {
                // (keep the second place value)
                if (bestValue > bestOther)
                    bestOther = bestValue;

                bestIndex = n;
                bestValue = output[n];
            } else if (output[n] > bestOther)
                bestOther = output[n];
            else
                ;
        }

        final double outputTolerance = Math.abs(bestValue - bestOther) / bestValue;
        // log.info(String.format("tolerance between %.3f and %.3f is %.2f",
        // bestValue, bestOther, outputTolerance));

        final int result = outputTolerance > tolerance ? bestIndex : -1;

        // Pair<Double, Double> second = new Pair<Double, Double>(bestValue,
        // outputTolerance);
        return new Pair<Integer, Pair<Double, Double>>(result, new Pair<Double, Double>(bestValue, outputTolerance));
    }
}
