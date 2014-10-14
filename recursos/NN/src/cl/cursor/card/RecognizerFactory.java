/**
 * Oct 9, 2014 - ayachan
 */
package cl.cursor.card;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import cl.cursor.card.impl.NeuralNetworkRecognizer;

/**
 * @author ayachan
 */
public class RecognizerFactory {

	/**
	 * Create a recognizer based on the information stored in the given file.
	 * 
	 * @param file
	 *            The file where the information to build a recognizer is
	 *            stored.
	 * @return The recognizer.
	 * @throws IOException
	 */
	static public Recognizer create(File file) throws IOException {
		String dimension = file.getAbsoluteFile() + ".properties";
		File fProp = new File(dimension);
		int width = 64;
		int height = 24;
		if(fProp.exists())
		{
			FileReader reader = new FileReader(fProp);
			Properties props = new Properties();
			props.load(reader);
			width =  Integer.parseInt(props.getProperty("width", "64"));
			height =  Integer.parseInt(props.getProperty("height", "24"));
		}
		return new NeuralNetworkRecognizer(file, width, height);
	}
}
