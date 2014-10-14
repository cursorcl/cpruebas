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
import cl.triton.card.ImagePanel;

/**
 * @author ayachan
 */
public class CopyOfNeuralNetworkRecognizer implements Recognizer
{
  static final Logger log =
      Logger.getLogger(CopyOfNeuralNetworkRecognizer.class.getName());

  final NeuralNetwork<?> network;
  final ImagePanel imgp = new ImagePanel();

  public CopyOfNeuralNetworkRecognizer(File file) throws IOException
  {
    // the (first) file with the grid size
    BufferedReader brdr = new BufferedReader(new FileReader(file));
    String[] grdsz = brdr.readLine().split(" ");
    String nnfilename = brdr.readLine();

    int width = Integer.parseInt(grdsz[0]);
    int height = Integer.parseInt(grdsz[1]);
    imgp.resetSize(width, height);

    log.info(String.format("grid is read as %d x %d", width, height));

    File nnFile = new File(file.getParent(), nnfilename);
    network = NeuralNetwork.createFromFile(nnFile);

//    imgp.resetSize(64, 24);
  }

  @Override
  public int recognize(Image image, double tolerance)
  {
    double[] output = match(image);

    StringBuffer sb = new StringBuffer();
    for (int n=0; n<output.length; n++)
    {
      if (sb.length() > 0)
        sb.append(", ");
      sb.append(String.format("[%2d]: %.3f", n, output[n]));
    }
    log.info(sb.toString());

    int bestIndex = -1;
    double bestValue = Double.MIN_VALUE;
    double bestOther = Double.MIN_VALUE;

    for (int n=0; n<output.length; n++)
    {
      if (output[n] > bestValue)
      {
        // (keep the second place value)
        if (bestValue > bestOther)
          bestOther = bestValue;

        bestIndex = n;
        bestValue = output[n];
      }
    }

    double outputTolerance = Math.abs(bestValue - bestOther) / bestValue;
    log.info(String.format("tolerance between %.3f and %.3f is %.2f",
        bestValue, bestOther, outputTolerance));

    return (outputTolerance > tolerance) ? bestIndex : -1;
  }

  @Override
  public double[] match(Image image)
  {
    // set the image
    imgp.setImage(image);

    // the input
    double[] input = new double[imgp.getInputSize()];
    for (int n=0; n<imgp.getInputSize(); n++)
      input[n] = imgp.getInput(n);

    // the evaluation
    network.setInput(input);
    network.calculate();
    return network.getOutput();
  }
}
