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
 * Imagen escaneada en una resolucion de 300dpi. 1) 1.l) El primer rectangulo
 * oscuro está en 58,1541 1.2) La primera columna de circulo a leer está en
 * 290,1528 y la segunda en 710, 1528 1.3) Los circulos tienen una dimension de
 * 44 pixeles 1.4) La separacion entre ciruclos de un bloque es 6 pixeles.
 * 
 * 2) 2.1) 58,1541 (+335) 2.2) 58,1876 (+334) 2.3) 58,2210 (+334) 2.4) 58,2544
 * (+333) 2.5) 58,2877
 *
 *
 */
public class ExtractorResultadosPruebasByItem extends AExtractorResultados {

	private static ExtractorResultadosPruebasByItem instance;

	/**
	 * Constructor de la clase de extracción de información de una imagen
	 * prueba.
	 * 
	 * @throws IOException
	 *             Error al leer las redes.
	 */
	private ExtractorResultadosPruebasByItem() throws IOException {
		recognizerRespustas = RecognizerFactory.create(new File(
				"./res/red_respuestas_26x72.red"));
		recognizerRut = RecognizerFactory.create(new File("./res/red_rut.red"));
	}

	public static ExtractorResultadosPruebasByItem getInstance()
			throws IOException {
		if (instance == null) {
			instance = new ExtractorResultadosPruebasByItem();
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
			rut = preprocesarImagen(rut);
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
				String respuesta = getRespuesta(img);
				resp.append(respuesta);
				pregunta++;
			}
		}
		return resp.toString();
	}

	public static void main(String args[]) throws IOException {
		try {
			ExtractorResultadosPruebasByItem extractor = new ExtractorResultadosPruebasByItem();

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
