package cl.eos.detection;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import cl.cursor.card.RecognizerFactory;
import cl.eos.detection.base.AExtractorResultados;

/**
 * Realiza el analisis de una imagen, obitiene el rut del alumno y las
 * respuestas que el alumno ha seleccionado.
 * El pre-procesamiento se realiza a nivel de toda la imagen.
 */
public class ExtractorResultadosPrueba extends AExtractorResultados {

	private static ExtractorResultadosPrueba instance;

	/**
	 * Constructor de la clase de extracción de información de una imagen
	 * prueba.
	 * 
	 * @throws IOException
	 *             Error al leer las redes.
	 */
	private ExtractorResultadosPrueba() throws IOException {
		recognizerRespustas = RecognizerFactory.create(new File(
				"./res/red_respuestas.red"));
		recognizerRut = RecognizerFactory.create(new File("./res/red_rut.red"));
	}

	public static ExtractorResultadosPrueba getInstance() throws IOException {
		if (instance == null) {
			instance = new ExtractorResultadosPrueba();
		}
		return instance;
	}

	public OTResultadoScanner process(File archivo, int nroPreguntas)
			throws IOException {
		return process(ImageIO.read(archivo), nroPreguntas);
	}

	/**
	 * Metodo que realiza el procesamiento de una prueba, obtiene el rut y las
	 * respuestas. Por ahora la forma ratorn 1.
	 * 
	 * @param image
	 *            Imagen que contiene la prueba.
	 * @param nroPreguntas
	 *            Número de preguntas de la prueba.
	 * @return {@link OTResultadoScanner} que contiene RUT, RESPUESTAS y FORMA
	 *         de la prueba.
	 */
	public OTResultadoScanner process(BufferedImage image, int nroPreguntas) {
		OTResultadoScanner resultado = new OTResultadoScanner();
		BufferedImage recImage = rectificarImagen(image);
		Point[] pointsReference = obtenerPuntosReferencia(recImage);
		Point[] pRefRespuestas = Arrays.copyOfRange(pointsReference, 1,
				pointsReference.length);
		recImage = preprocesarImagen(recImage);
		String respuestas = getRespuestas(pRefRespuestas, recImage,
				nroPreguntas);

		Point pRefRut = pointsReference[0];
		String rut = getRut(pRefRut, recImage);

		resultado.setForma(1);
		resultado.setRespuestas(respuestas);
		resultado.setRut(rut);
		return resultado;
	}

	public static void main(String args[]) throws IOException {
		try {
			ExtractorResultadosPrueba extractor = new ExtractorResultadosPrueba();

			// for (int n = 0; n < 4; n++) {
			BufferedImage image = ImageIO
					.read(new File("./res/prueba_002.png"));
			System.out.println(extractor.process(image, 45));
			// }

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
