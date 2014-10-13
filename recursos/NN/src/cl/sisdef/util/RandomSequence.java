/**
 * Aug 10, 2014 - Turbo7
 */
package cl.sisdef.util;

import java.util.Random;

/**
 * @author Turbo7
 */
public class RandomSequence
{
  static final Random random = new Random(System.currentTimeMillis());

  /**
   * Generate a random sequence of the first <i>length</i> integers.
   * 
   * @param length
   *          The length to generate.
   * @return An array of random integers in the range.
   */
  static public int[] generateRandomSequence(int length)
  {
    int[] result = new int[length];
    for (int n=0; n<length; n++)
    {
      int p = random.nextInt(n + 1);
      if (p != n)
        result[n] = result[p];
      result[p] = n;
    }
    return result;
  }
}
