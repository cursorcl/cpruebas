/**
 * Aug 10, 2014 - Turbo7
 */
package cl.triton.card;

/**
 * The input provider for a given layer, provided by their previous layer in the
 * chain (the network input itself for the first layer).<br />
 * Each layer <b>is</b> an input provider, the same for all their neurons.
 *
 * @author Turbo7
 */
public interface InputProvider {

    /**
     * Retrieve the given input in this provider.
     * 
     * @param index
     *            The index of the input.
     * @return The index input of this provider.
     */
    double getInput(int index);

    /**
     * @return The number of inputs in this provider.<br />
     *         For layers is the number of neurons (an their outputs).
     */
    int getInputSize();
}
