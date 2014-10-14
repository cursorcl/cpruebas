/**
 * Oct 9, 2014 - ayachan
 */
package cl.cursor.card;

import java.io.File;
import java.io.IOException;

import cl.cursor.card.impl.NeuralNetworkRecognizer;

/**
 * @author ayachan
 */
public class RecognizerFactory
{

  /**
   * Create a recognizer based on the information stored in the given file.
   * 
   * @param file
   *          The file where the information to build a recognizer is stored.
   * @return The recognizer.
   * @throws IOException
   */
  static public Recognizer create(File file) throws IOException
  {
    return new NeuralNetworkRecognizer(file);
  }
}
