package cl.eos.detection;

import georegression.struct.point.Point2D_I32;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import boofcv.alg.filter.binary.BinaryImageOps;
import boofcv.alg.filter.binary.Contour;
import boofcv.alg.filter.binary.ThresholdImageOps;
import boofcv.core.image.ConvertBufferedImage;
import boofcv.struct.ConnectRule;
import boofcv.struct.image.ImageFloat32;
import boofcv.struct.image.ImageSInt32;
import boofcv.struct.image.ImageUInt8;
import cl.cursor.card.Recognizer;
import cl.cursor.card.RecognizerFactory;

/**
 * Imagen escaneada en una resolucion de 300dpi. 1) 1.l) El primer rectangulo oscuro está en 58,1541
 * 1.2) La primera columna de circulo a leer está en 290,1528 y la segunda en 710, 1528 1.3) Los
 * circulos tienen una dimension de 44 pixeles 1.4) La separacion entre ciruclos de un bloque es 6
 * pixeles.
 * 
 * 2) 2.1) 58,1541 (+335) 2.2) 58,1876 (+334) 2.3) 58,2210 (+334) 2.4) 58,2544 (+333) 2.5) 58,2877
 *
 *
 */
public class ExtractorImagenesPruebas {

  /**
   * Delta x entre las primeras columnas de circulos.
   */
  public static int DELTA_X_FIRST_CIRCLES = 418;
  /**
   * Delta x entre el rectangulo y la primara corrida de circulos.
   */
  public static int DELTA_X = 232;
  /**
   * Delta y entre el rectangulo y la primara corrida de circulos.
   */

  public static int DELTA_Y_FIRST_CIRCLE_RESP = -17;
  public static int CIRCLE_SIZE = 44;
  public static int CIRCLE_Y_SPCAES = 6;
  public static int CIRCLE_X_SPCAES = 16;
  public static int BASE = 0;
  public static int NRO_PREG_POR_COLUMNA = 25;
  public static int GROUP_SIZE = 5;
  public static int prueba = 1;
  public static int XRUTREF = 145;

  private static String RESPUESTAS[] = {"A", "B", "C", "D", "E", "V", "F", "O", "M"};
  private static String RUT[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "K"};

  // Son las diferencias del inicio del círculo con inicio rectángulo
  public static int[] CIRCLE_X_RUT_DIFF = {286 - XRUTREF, 348 - XRUTREF, 418 - XRUTREF,
      477 - XRUTREF, 536 - XRUTREF, 611 - XRUTREF, 675 - XRUTREF, 736 - XRUTREF, 816 - XRUTREF};
  // Espaciamiento entre inicios de circulo del rut
  public static int CIRCLE_Y_RUT_DIFF = 48;



  /**
   * Constructor de la clase de extracción de información de una imagen prueba.
   * 
   * @throws IOException Error al leer las redes.
   */
  public ExtractorImagenesPruebas() throws IOException {
  }


  public OTResultadoScanner process(File archivo, int nroPreguntas) throws IOException {
    BufferedImage limage;
      limage = ImageIO.read(archivo);
      OTResultadoScanner resultado = new OTResultadoScanner();
      BufferedImage rotated = rectificarImagen(limage);
      Point[] pointsReference = obtenerPuntosReferencia(rotated);
      Point[] pRefRespuestas = Arrays.copyOfRange(pointsReference, 1, pointsReference.length);
      String respuestas = getRespuestas(pRefRespuestas, rotated, nroPreguntas);

       Point pRefRut = pointsReference[0];
       String rut = getRut(pRefRut, rotated);

      resultado.setForma(1);
      resultado.setRespuestas(respuestas);
      resultado.setRut(rut);
    return resultado;
  }
  
  /**
   * Metodo que realiza el procesamiento de una prueba, obtiene el rut y las respuestas. Por ahora
   * la forma ratorn 1.
   * 
   * @param limage Imagen que contiene la prueba.
   * @param nroPreguntas Número de preguntas de la prueba.
   * @return {@link OTResultadoScanner} que contiene RUT, RESPUESTAS y FORMA de la prueba.
   */
  public OTResultadoScanner process(BufferedImage limage, int nroPreguntas) {
    OTResultadoScanner resultado = new OTResultadoScanner();
    BufferedImage rotated = rectificarImagen(limage);
    Point[] pointsReference = obtenerPuntosReferencia(rotated);
    Point[] pRefRespuestas = Arrays.copyOfRange(pointsReference, 1, pointsReference.length);
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
   * @param pRefRut Punto de referencia para la lectura del rut.
   * @param image Imagen de la prueba que se procesa.
   * @return String con el rut que viene referenciado en la prueba.
   */
  private String getRut(Point pRefRut, BufferedImage image) {
    int x = pRefRut.x;
    for (int n = 0; n < CIRCLE_X_RUT_DIFF.length; n++) {
      int y = pRefRut.y;
      BufferedImage rut = image.getSubimage(x + CIRCLE_X_RUT_DIFF[n] - 2, y - 2, 49, 48 * 11);
      try {
		ImageIO.write(rut, "png", new File("./res/rut" + n + ".png"));
	} catch (IOException e) {
	}
    }
    return "";
  }

  /**
   * Procesa la seccion de respuestas para obtener el string asociado a todas las respuetas.
   * 
   * @param pRefRespuestas Puntos de referencia de las marcas de respuestas.
   * @param image La imagen de la prueba escaneada.
   * @param nroPreguntas Número de preguntas de la prueba.
   * @return String con las respuestas rescatadas de la prueba.
   */
  private String getRespuestas(Point[] pRefRespuestas, BufferedImage image, int nroPreguntas) {
    int pregunta = 1;
    for (int idx = 1; idx <= nroPreguntas; idx = idx + GROUP_SIZE) {
      // Con esto se calcula el nro del punto de referencia.
      int pIdx = ((pregunta % NRO_PREG_POR_COLUMNA) - 1) / GROUP_SIZE;

      int x = pRefRespuestas[pIdx].x;
      int y = pRefRespuestas[pIdx].y;

      int col = (pregunta - 1) / NRO_PREG_POR_COLUMNA;

      for (int n = 0; n < GROUP_SIZE; n++) {
        int left = x + DELTA_X - 4 + col * DELTA_X_FIRST_CIRCLES;
        int top = y + DELTA_Y_FIRST_CIRCLE_RESP  + BASE + n * 52;
        BufferedImage img =
            image
                .getSubimage(left, top, CIRCLE_SIZE * 5 + CIRCLE_X_SPCAES * 4 + 4, CIRCLE_SIZE + 8);
        try {
			ImageIO.write(img, "png", new File("./res/resp" + pregunta + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        pregunta++;
      }
    }
    return "";
  }


/**
   * Rotacion de la imagen. Este metodo realiza la corrección de la imagen. En esta version
   * solamente enderza la imagen.
   * 
   * @param limage Imagen a rotar
   * @return
   */
  public BufferedImage rectificarImagen(BufferedImage limage) {
    List<Contour> contours = getContours(limage);
    double anlge = getRotationAngle(contours);
    BufferedImage image = rotate(anlge, limage);
    return image;
  }

  /**
   * Se obtienen todos los puntos de referencia en base a los contornos generados de las marcas de
   * impresion.
   * 
   * @param image La imagen que tiene las figuras de referencia.
   * @return Puntos de referencia para obtener las respuestas.
   */
  private Point[] obtenerPuntosReferencia(BufferedImage image) {
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
   * @param contours Lista de contornos que con los cuales se quiere obtener el angulo de rotacion.
   * @return Angulo de rotación.
   */
  private double getRotationAngle(List<Contour> contours) {
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

    return Math.PI / 2f - Math.atan2(dy, dx);
  }

  /**
   * Obtiene los contornos de las marcas establecidas en la impresión.
   * 
   * @param limage Imagen completa de la prueba.
   * @return Lista de los contornos de las imagenes.
   */
  public List<Contour> getContours(BufferedImage limage) {
    BufferedImage image = limage.getSubimage(0, 0, 200, 3200);
    ImageFloat32 input = ConvertBufferedImage.convertFromSingle(image, null, ImageFloat32.class);

    ImageUInt8 binary = new ImageUInt8(input.width, input.height);
    ImageSInt32 label = new ImageSInt32(input.width, input.height);
    ThresholdImageOps.threshold(input, binary, (float) 145, true);
    ImageUInt8 filtered = BinaryImageOps.erode8(binary, 8, null);
    filtered = BinaryImageOps.dilate8(filtered, 8, null);
    return BinaryImageOps.contour(filtered, ConnectRule.EIGHT, label);
  }


  /**
   * Rota image en el angulo dado en radianes.
   * 
   * @param angle Angulo de rotacion con respecto al centro.
   * @param image Imagen que se quiere rotar.
   * @return Nueva imagen rotada.
   */
  public BufferedImage rotate(double angle, BufferedImage image) {
    int drawLocationX = 0;
    int drawLocationY = 0;

    // Valores para la rotación.
    double rotationRequired = angle;
    double locationX = image.getWidth() / 2;
    double locationY = image.getHeight() / 2;
    AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BICUBIC);
    BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
    Graphics2D g2d = (Graphics2D) result.getGraphics();
    g2d.setColor(Color.WHITE);
    g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
    // Se dibuja la imagen rotada.
    g2d.drawImage(op.filter(image, null), drawLocationX, drawLocationY, null);
    return result;
  }


  public static void main(String args[]) {
    try {
      ExtractorImagenesPruebas extractor = new ExtractorImagenesPruebas();

//      for (int n = 0; n < 4; n++) {
        BufferedImage image = ImageIO.read(new File("./res/prueba_003.png"));
        System.out.println(extractor.process(image, 45));
//      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
