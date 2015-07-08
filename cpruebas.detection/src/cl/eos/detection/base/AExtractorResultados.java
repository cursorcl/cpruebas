package cl.eos.detection.base;

import georegression.struct.point.Point2D_I32;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import boofcv.alg.filter.binary.BinaryImageOps;
import boofcv.alg.filter.binary.Contour;
import boofcv.alg.filter.binary.ThresholdImageOps;
import boofcv.alg.misc.ImageStatistics;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.gui.binary.VisualizeBinaryData;
import boofcv.struct.ConnectRule;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageSInt32;
import boofcv.struct.image.ImageUInt8;
import cl.cursor.card.Recognizer;
import cl.eos.detection.ExtractorResultadosPrueba;
import cl.eos.detection.OTResultadoScanner;
import cl.eos.util.Utils;
import cl.sisdef.util.Pair;

import com.sun.istack.internal.logging.Logger;

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
public abstract class AExtractorResultados implements IExtractorResultados {

	private static final Logger log = Logger
			.getLogger(AExtractorResultados.class);
	/**
	 * Delta x entre las primeras columnas de circulos.
	 */
	// public static int DELTA_X_FIRST_CIRCLES = 418; // impresion desde PDF
	public static int DELTA_X_FIRST_CIRCLES = 440; // impresion desde SISTEMA
	/**
	 * Delta x entre el rectangulo y la primara corrida de circulos.
	 */
	// public static int DELTA_X = 232; // Impresion desde PDF
	public static int DELTA_X = 238; // Impresion desde SISTEMA
	/**
	 * Delta y entre el rectangulo y la primara corrida de circulos.
	 */

	// Las posiciones de los circulos con respecto a los puntos de referencia en
	// Y son:
	/*
	 * Primer circulo es PREF - 15(DELTA_Y_FIRST_CIRCLE_RESP) Segundo circulo es
	 * PREF - 15 + 54 Tercer circulo es PREF - 15 + 54 * 2 Cuarto circulo es
	 * PREF - 15 + 54 * 3 Quinto circulo es PREF - 15 + 54 * 4
	 */
	public static int DELTA_Y_FIRST_CIRCLE_RESP = -15;
	public static int CIRCLE_SIZE = 44;
	public static int CIRCLE_Y_SPCAES = 6;
	public static int CIRCLE_X_SPCAES = 16;
	public static int BASE = 0;
	public static int NRO_PREG_POR_COLUMNA = 25;
	public static int GROUP_SIZE = 5;
	public static int prueba = 1;

	protected static String RESPUESTAS[] = { "A", "B", "C", "D", "E", "O" };
	protected static String RUT[] = { "0", "1", "2", "3", "4", "5", "6", "7",
			"8", "9", "K" };

	/*
	 * //Inicio rectangulo. public static int XRUTREF = 145; // Son las
	 * diferencias del inicio del círculo con inicio rectángulo public static
	 * int[] CIRCLE_X_RUT_DIFF = {286 - XRUTREF, 348 - XRUTREF, 418 - XRUTREF,
	 * 477 - XRUTREF, 536 - XRUTREF, 611 - XRUTREF, 675 - XRUTREF, 736 -
	 * XRUTREF, 816 - XRUTREF};
	 */

	// Inicio rectangulo.
	public static int XRUTREF = 53;
	// Son las diferencias del inicio del círculo con inicio rectángulo
	public static int[] CIRCLE_X_RUT_DIFF = { 203 - XRUTREF, 270 - XRUTREF,
			345 - XRUTREF, 408 - XRUTREF, 471 - XRUTREF, 550 - XRUTREF,
			617 - XRUTREF, 680 - XRUTREF, 764 - XRUTREF };

	// Espaciamiento entre inicios de circulo del rut
	public static int CIRCLE_Y_RUT_DIFF = 48;
	protected Recognizer recognizerRespustas;
	protected Recognizer recognizerRut;

	/*
	 * (non-Javadoc)
	 * 
	 * @see cl.eos.detection.IExtractorResultados#process(java.io.File, int)
	 */
	@Override
	public OTResultadoScanner process(File archivo, int nroPreguntas)
			throws IOException {
		return process(ImageIO.read(archivo), nroPreguntas);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cl.eos.detection.IExtractorResultados#process(java.awt.image.BufferedImage
	 * , int)
	 */
	@Override
	public abstract OTResultadoScanner process(BufferedImage image,
			int nroPreguntas);

	/**
	 * Procesa la seccion rut para obtnener el rut de quien resolvio la prueba.
	 * 
	 * @param pRefRut
	 *            Punto de referencia para la lectura del rut.
	 * @param image
	 *            Imagen de la prueba que se procesa.
	 * @return String con el rut que viene referenciado en la prueba.
	 */
	
	static int nRut = 0;
	protected String getRut(Point pRefRut, BufferedImage image) {
		nRut++;
		int x = pRefRut.x;
		StringBuffer strRut = new StringBuffer("");
		writeIMG(image, "RUT");
		int y = pRefRut.y;
		
		BufferedImage firstRut = image.getSubimage(x + CIRCLE_X_RUT_DIFF[0] - 5, y - 2, 66, 48);
		Point pointFirstRut = getPointReferenciaRut(firstRut);
		x = pRefRut.x - 10 + pointFirstRut.x;

		for (int n = 0; n < CIRCLE_X_RUT_DIFF.length; n++) {
			y = pRefRut.y;
			BufferedImage rut = image.getSubimage(x + CIRCLE_X_RUT_DIFF[n] - 2,
					y - 2, 51, 555);
			writeIMG(rut, "RUT_" + (nRut));
			nRut++;
			Pair<Integer, Pair<Double, Double>> result = recognizerRut
					.recognize(rut, 0.50);
			int idx = result.getFirst();
			if (idx != -1) {
				strRut.append(RUT[idx]);
			}
		}
		return strRut.toString();
	}
	
	/**
	 * Obtiene los contornos de las marcas establecidas en la impresión.
	 * 
	 * @param limage
	 *            Imagen completa de la prueba.
	 * @return Lista de los contornos de las imagenes.
	 */
	protected final Point getPointReferenciaRut(BufferedImage limage) {

		writeIMG(limage, "PedazoRut");
		ImageFloat32 input = ConvertBufferedImage.convertFromSingle(limage,
				null, ImageFloat32.class);

		ImageUInt8 binary = new ImageUInt8(input.width, input.height);
		ImageSInt32 label = new ImageSInt32(input.width, input.height);
		ThresholdImageOps.threshold(input, binary, (float) 190, true);
		writeIMG(VisualizeBinaryData.renderBinary(binary, null), "thresholdRut");
		ImageUInt8 filtered = BinaryImageOps.dilate8(binary, 2, null);
		writeIMG(VisualizeBinaryData.renderBinary(filtered, null), "dilated8-2Rut");
		filtered = BinaryImageOps.erode8(filtered, 3, null);
		writeIMG(VisualizeBinaryData.renderBinary(filtered, null), "eroded8-3Rut");
		filtered = BinaryImageOps.dilate8(filtered, 2, null);
		writeIMG(VisualizeBinaryData.renderBinary(filtered, null), "dilate8-5Rut");
		BufferedImage bImage = VisualizeBinaryData.renderBinary(filtered, null);
		writeIMG(bImage, "contornosPedazoRut");
		
		
		List<Contour> contours =  BinaryImageOps.contour(filtered, ConnectRule.EIGHT, label);
		
		Point[] points = new Point[contours.size()];
		
		int n = 0;
		for (int idx = 0; idx < contours.size(); idx++) {
			Contour contour = contours.get(idx);
			int minX = Integer.MAX_VALUE;
			int minY = Integer.MAX_VALUE;
			for (Point2D_I32 point : contour.external) {
				if (point.x < minX) {
					minX = point.x;
				}
				if (point.y < minY) {
					minY = point.y;
				}
			}
			points[n] = new Point(minX, minY);
			n++;
		}
		return points[0];
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

			for (int n = 0; n < GROUP_SIZE && (n + idx) <= nroPreguntas; n++) {
				int left = x + DELTA_X + col * DELTA_X_FIRST_CIRCLES; // - 4;
				int top = y + DELTA_Y_FIRST_CIRCLE_RESP + BASE + n * 54;
				BufferedImage img = image.getSubimage(left, top, CIRCLE_SIZE
						* 5 + CIRCLE_X_SPCAES * 4 + 4, CIRCLE_SIZE + 8);
				writeIMG(img, "resp_" + pregunta);
				img = fillCirlces(img);
				writeIMG(img, "fresp_" + pregunta);
				String respuesta = getRespuesta(img);

				resp.append(respuesta);
				pregunta++;
			}
		}
		return resp.toString();
	}

	protected final String getRespuesta(BufferedImage img) {
		String resp = "O";
		Pair<Integer, Pair<Double, Double>> result = recognizerRespustas
				.recognize(img, 0.9); // Red
										// pequeña
										// 0.8
		int idx = result.getFirst();
		if (idx != -1) {
			resp = RESPUESTAS[idx];
		} else {
			resp = "M";
		}
		return resp;
	}

	protected final BufferedImage preprocesarImagen(BufferedImage image) {
		ImageFloat32 input = ConvertBufferedImage.convertFromSingle(image,
				null, ImageFloat32.class);
		ImageUInt8 binary = new ImageUInt8(input.width, input.height);
		ThresholdImageOps.threshold(input, binary, (float) 185, false); // 170,180
		ImageUInt8 output = BinaryImageOps.erode4(binary, 3, null); // 2,4
		output = BinaryImageOps.dilate4(output, 7, null); // 7,9
		output = BinaryImageOps.erode4(output, 2, null);
		output = BinaryImageOps.dilate4(output, 5, null); // 5
		output = BinaryImageOps.erode4(output, 8, null);
		BufferedImage bImage = VisualizeBinaryData.renderBinary(output, null);

		return bImage;
	}

	/**
	 * Rotacion de la imagen. Este metodo realiza la corrección de la imagen. En
	 * esta version solamente enderza la imagen.
	 * 
	 * @param limage
	 *            Imagen a rotar
	 * @return
	 */
	protected final BufferedImage rectificarImagen(BufferedImage limage) {
		List<Contour> contours = getContours(limage);
		BufferedImage image = limage;
		if (contours.size() < 5) {
			image = rotate(Math.PI, limage);
			contours = getContours(image);
		}
		double anlge = getRotationAngle(contours);
		image = rotate(anlge, image);
		return image;
	}

	/**
	 * Se obtienen todos los puntos de referencia en base a los contornos
	 * generados de las marcas de impresion.
	 * 
	 * @param image
	 *            La imagen que tiene las figuras de referencia.
	 * @return Puntos de referencia para obtener las respuestas.
	 */
	protected final Point[] obtenerPuntosReferencia(BufferedImage image) {
		List<Contour> contours = getContours(image);
		Point[] points = new Point[contours.size()];
		int n = 0;
		for (int idx = 0; idx < contours.size(); idx++) {
			Contour contour = contours.get(idx);
			int minX = Integer.MAX_VALUE;
			int minY = Integer.MAX_VALUE;
			for (Point2D_I32 point : contour.external) {
				if (point.x < minX) {
					minX = point.x;
				}
				if (point.y < minY) {
					minY = point.y;
				}
			}
			points[n] = new Point(minX, minY);
			n++;
		}
		return points;
	}

	/**
	 * Obtiene la rotación con las marcas establecidas en impresion.
	 * 
	 * @param contours
	 *            Lista de contornos que con los cuales se quiere obtener el
	 *            angulo de rotacion.
	 * @return Angulo de rotación.
	 */
	protected final double getRotationAngle(List<Contour> contours) {

		int[] x = new int[contours.size() - 1];
		int[] y = new int[contours.size() - 1];
		int n = 0;
		for (int idx = 1; idx < contours.size(); idx++) {
			Contour contour = contours.get(idx);
			int minX = Integer.MAX_VALUE;
			int minY = Integer.MAX_VALUE;
			for (Point2D_I32 point : contour.external) {
				if (point.x < minX) {
					minX = point.x;
				}
				if (point.y < minY) {
					minY = point.y;
				}
			}
			x[n] = minX;
			y[n] = minY;
			n++;
		}
		int dx = x[n - 1] - x[0];
		int dy = y[n - 1] - y[0];
		double angle = Math.PI / 2f - Math.atan2(dy, dx);
		// log.info(String.format("Angulo: %f[rad] %f[°]", angle, angle /
		// Math.PI * 180.0));
		return angle;
	}

	/**
	 * Obtiene los contornos de las marcas establecidas en la impresión.
	 * 
	 * @param limage
	 *            Imagen completa de la prueba.
	 * @return Lista de los contornos de las imagenes.
	 */
	protected final List<Contour> getContours(BufferedImage limage) {

		BufferedImage image = limage.getSubimage(0, 0, 120, 3200);
		writeIMG(image, "subimage");
		ImageFloat32 input = ConvertBufferedImage.convertFromSingle(image,
				null, ImageFloat32.class);

		ImageUInt8 binary = new ImageUInt8(input.width, input.height);
		ImageSInt32 label = new ImageSInt32(input.width, input.height);
		ThresholdImageOps.threshold(input, binary, (float) 190, true);
		writeIMG(VisualizeBinaryData.renderBinary(binary, null), "threshold");
		ImageUInt8 filtered = BinaryImageOps.dilate8(binary, 2, null);
		filtered = BinaryImageOps.erode8(filtered, 9, null);
		filtered = BinaryImageOps.dilate8(filtered, 10, null);
		BufferedImage bImage = VisualizeBinaryData.renderBinary(filtered, null);
		writeIMG(bImage, "contornos");
		return BinaryImageOps.contour(filtered, ConnectRule.EIGHT, label);
	}

	/**
	 * Rota image en el angulo dado en radianes.
	 * 
	 * @param angle
	 *            Angulo de rotacion con respecto al centro.
	 * @param image
	 *            Imagen que se quiere rotar.
	 * @return Nueva imagen rotada.
	 */
	protected final BufferedImage rotate(double angle, BufferedImage image) {
		int drawLocationX = 0;
		int drawLocationY = 0;

		// Valores para la rotación.
		double rotationRequired = angle;
		double locationX = image.getWidth() / 2;
		double locationY = image.getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(
				rotationRequired, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx,
				AffineTransformOp.TYPE_BICUBIC);
		BufferedImage result = new BufferedImage(image.getWidth(),
				image.getHeight(), image.getType());
		Graphics2D g2d = (Graphics2D) result.getGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
		// Se dibuja la imagen rotada.
		g2d.drawImage(op.filter(image, null), drawLocationX, drawLocationY,
				null);
		return result;
	}

	protected static final int CIRCLE_WIDTH = 45;
	protected static final int CIRCLE_HEIGHT = 48;

	protected static int[] X = { 2, 60, 119, 178, 239 };

	static int resC = 0;

	protected BufferedImage fillCirlces(BufferedImage bImage) {
		Graphics2D g2d = (Graphics2D) bImage.getGraphics();
		g2d.setColor(Color.BLACK);
		Ellipse2D ellipse = new Ellipse2D.Float();
		for (int n = 0; n < 5; n++) {
			ellipse.setFrame(0, 0, 45, 48);

			int x = X[n];
			int y = 1;
			int w = CIRCLE_WIDTH;
			int h = CIRCLE_HEIGHT;

			BufferedImage subImage = new BufferedImage(w, h,
					BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g = subImage.createGraphics();
			g.setColor(new Color(255, 255, 255, 0));
			g.fillRect(0, 0, w, h);
			g.setClip(ellipse);
			g.drawImage(bImage, -x, -y, null);
			g.dispose();
			ImageFloat32 img32 = ConvertBufferedImage.convertFromSingle(
					subImage, null, ImageFloat32.class);
			double value = ImageStatistics.mean(img32);
			g2d.drawOval(X[n], 1, 45, 48);
			if (value < 165) {
				g2d.setColor(Color.BLACK);
				g2d.fillOval(X[n], 1, 45, 48);
			} else {
				g2d.setColor(Color.WHITE);
				g2d.fillOval(X[n], 1, 45, 48);
			}
		}
		g2d.dispose();
		return bImage;
	}

	public static void main(String[] args) {

		BufferedImage limage = null;
		ExtractorResultadosPrueba extractor = ExtractorResultadosPrueba
				.getInstance();

		int n = 37;
		// for (n = 0; n < 37; n++)
		{
			try {
				limage = ImageIO.read(new File(String.format(
						"/res/5A_CN_Independencia/prueba%02d.JPG", n)));

				// writeIMG(limage, "0fuente");
				// ImageFloat32 input =
				// ConvertBufferedImage.convertFromSingle(limage, null,
				// ImageFloat32.class);
				// ImageUInt8 binary = new ImageUInt8(input.width,
				// input.height);
				// ThresholdImageOps.threshold(input, binary, (float) 185,
				// false);
				// BufferedImage bImage =
				// VisualizeBinaryData.renderBinary(binary, null);
				// writeIMG(bImage, "1threshold");
				// ImageUInt8 output = BinaryImageOps.erode4(binary, 3, null);
				// bImage = VisualizeBinaryData.renderBinary(output, null);
				// writeIMG(bImage, "2erode4_1x2");
				// output = BinaryImageOps.dilate4(output, 7, null);
				// bImage = VisualizeBinaryData.renderBinary(output, null);
				// writeIMG(bImage, "3dilate4_1x7");
				// output = BinaryImageOps.erode4(output, 2, null);
				// bImage = VisualizeBinaryData.renderBinary(output, null);
				// writeIMG(bImage, "4erode4_2x2");
				// output = BinaryImageOps.dilate4(output, 5, null);
				// bImage = VisualizeBinaryData.renderBinary(output, null);
				// writeIMG(bImage, "5dialte4_2x5");
				// output = BinaryImageOps.erode4(output, 8, null);
				// bImage = VisualizeBinaryData.renderBinary(output, null);
				// writeIMG(bImage, "6erode4_2x8_finish");
				extractor.process(limage, 35);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void writeIMG(BufferedImage image, String name) {
		String debug = System.getProperty("DEBUG");
		if (debug != null && "TRUE".equals(debug)) {
			try {
				ImageIO.write(image, "png",
						new File(Utils.getDefaultDirectory() + "/" + name
								+ ".png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			;
		}
	}

}
