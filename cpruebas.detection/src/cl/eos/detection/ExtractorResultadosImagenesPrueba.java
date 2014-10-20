package cl.eos.detection;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import cl.cursor.card.RecognizerFactory;
import cl.eos.detection.base.AExtractorResultados;
import cl.sisdef.util.Pair;

/**
 * Realiza el analisis de una imagen, obtiene el rut del alumno y las respuestas
 * que el alumno ha seleccionado. Generando una imagen de cada una de los
 * elementos que son procesados. El pre-procesamiento se realiza a nivel de toda
 * la imagen.
 */
public class ExtractorResultadosImagenesPrueba extends AExtractorResultados {

	private static ExtractorResultadosImagenesPrueba instance;
	/**
	 * La carpeta donde quedará almacenado el resultado.
	 */
	private String output;
	/**
	 * Constructor de la clase de extracción de información de una imagen
	 * prueba.
	 * 
	 * @throws IOException
	 *             Error al leer las redes.
	 */
	public ExtractorResultadosImagenesPrueba() throws IOException {
		recognizerRespustas = RecognizerFactory.create(new File(
				"./res/red_respuestas.red"));
		recognizerRut = RecognizerFactory.create(new File("./res/red_rut.red"));
	}

	public static ExtractorResultadosImagenesPrueba getInstance()
			throws IOException {
		if (instance == null) {
			instance = new ExtractorResultadosImagenesPrueba();
		}
		return instance;
	}

	public OTResultadoScanner process(File archivo, int nroPreguntas)
			throws IOException {
		BufferedImage limage;
		limage = ImageIO.read(archivo);
		return process(limage, nroPreguntas);
	}

	/**
	 * Metodo que realiza el procesamiento de una prueba, obtiene el rut y las
	 * respuestas. Por ahora la forma ratorn 1.
	 * 
	 * @param limage
	 *            Imagen que contiene la prueba.
	 * @param nroPreguntas
	 *            Número de preguntas de la prueba.
	 * @return {@link OTResultadoScanner} que contiene RUT, RESPUESTAS y FORMA
	 *         de la prueba.
	 */
	public OTResultadoScanner process(BufferedImage limage, int nroPreguntas) {
		OTResultadoScanner resultado = new OTResultadoScanner();
		BufferedImage rotated = rectificarImagen(limage);
		Point[] pointsReference = obtenerPuntosReferencia(rotated);
		Point[] pRefRespuestas = Arrays.copyOfRange(pointsReference, 1,
				pointsReference.length);
		String respuestas = getRespuestas(pRefRespuestas, rotated, nroPreguntas);

		Point pRefRut = pointsReference[0];
		String rut = getRut(pRefRut, rotated);

		resultado.setForma(1);
		resultado.setRespuestas(respuestas);
		resultado.setRut(rut);
		return resultado;
	}

	/**
	 * Procesa la seccion rut para obtnener el rut de quien resolvio la prueba.
	 * 
	 * @param pRefRut
	 *            Punto de referencia para la lectura del rut.
	 * @param image
	 *            Imagen de la prueba que se procesa.
	 * @return String con el rut que viene referenciado en la prueba.
	 */
	protected String getRut(Point pRefRut, BufferedImage image) {
		int x = pRefRut.x;
		StringBuffer strRut = new StringBuffer("");
		for (int n = 0; n < CIRCLE_X_RUT_DIFF.length; n++) {
			int y = pRefRut.y;
			BufferedImage rut = image.getSubimage(x + CIRCLE_X_RUT_DIFF[n] - 2,
					y - 2, 49, 48 * 11);
			try {
				ImageIO.write(rut, "png",
						new File(String.format("./res/rut%d.png", n)));
			} catch (IOException e) {
				e.printStackTrace();
			}

			Pair<Integer, Pair<Double, Double>> result = recognizerRut
					.recognize(rut, 0.75);
			int idx = result.getFirst();
			if (idx != -1) {
				strRut.append(RUT[idx]);
			}
		}
		return strRut.toString();
	}

	/**
	 * Procesa la seccion de respuestas para obtener el string asociado a todas
	 * las respuetas.
	 * 
	 * @param pRefRespuestas
	 *            Puntos de referencia de las marcas de respuestas.
	 * @param image
	 *            La imagen de la prueba escaneada.
	 * @param nroPreguntas
	 *            Número de preguntas de la prueba.
	 * @return String con las respuestas rescatadas de la prueba.
	 */
	protected String getRespuestas(Point[] pRefRespuestas, BufferedImage image,
			int nroPreguntas) {
		int pregunta = 1;
		StringBuffer resp = new StringBuffer();
		for (int idx = 1; idx <= nroPreguntas; idx = idx + GROUP_SIZE) {
			// Con esto se calcula el nro del punto de referencia.
			int pIdx = ((pregunta % NRO_PREG_POR_COLUMNA) - 1) / GROUP_SIZE;

			int x = pRefRespuestas[pIdx].x;
			int y = pRefRespuestas[pIdx].y;

			int col = (pregunta - 1) / NRO_PREG_POR_COLUMNA;

			for (int n = 0; n < GROUP_SIZE; n++) {
				int left = x + DELTA_X - 4 + col * DELTA_X_FIRST_CIRCLES;
				int top = y + DELTA_Y_FIRST_CIRCLE_RESP + BASE + n * 52;
				BufferedImage img = image.getSubimage(left, top, CIRCLE_SIZE
						* 5 + CIRCLE_X_SPCAES * 4 + 4, CIRCLE_SIZE + 8);
				img = preprocesarImagen(img);
				try {
					ImageIO.write(
							img,
							"png",
							new File(String.format("./res/respuesta%d.png",
									pregunta)));
				} catch (IOException e) {
					e.printStackTrace();
				}
				String respuesta = getRespuesta(img);
				resp.append(respuesta);
				pregunta++;
			}
		}
		return resp.toString();
	}

	public static void main(String args[]) {
		try {
			ExtractorResultadosImagenesPrueba extractor = new ExtractorResultadosImagenesPrueba();

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
