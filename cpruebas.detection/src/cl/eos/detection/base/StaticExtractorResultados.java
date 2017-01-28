package cl.eos.detection.base;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

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
import georegression.struct.point.Point2D_I32;

/**
 * Imagen escaneada en una resolucion de 300dpi. 1) 1.l) El primer rectangulo
 * oscuro está en 58,1541 1.2) La primera columna de circulo a leer está en
 * 290,1528 y la segunda en 710, 1528 1.3) Los circulos tienen una dimension de
 * 44 pixeles 1.4) La separacion entre ciruclos de un bloque es 6 pixeles.
 *
 * 2) 2.1) 58,1541 (+335) 2.2) 58,1876 (+334) 2.3) 58,2210 (+334) 2.4) 58,2544
 * (+333) 2.5) 58,2877
 *
 * Cambio para ver merge.
 */
public class StaticExtractorResultados {

    private static final Logger log = Logger.getLogger(StaticExtractorResultados.class.getName());
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

    protected static String RESPUESTAS[] = { "A", "B", "C", "D", "E", "O" };
    protected static String RUT[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "K" };

    public static int DELTA_Y_FIRST_CIRCLE_RESP = -17;
    public static int CIRCLE_SIZE = 44;
    public static int CIRCLE_Y_SPCAES = 6;
    public static int CIRCLE_X_SPCAES = 16;
    public static int BASE = 0;
    public static int NRO_PREG_POR_COLUMNA = 25;
    public static int GROUP_SIZE = 5;
    public static int prueba = 1;

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
    public static int[] CIRCLE_X_RUT_DIFF = { 203 - StaticExtractorResultados.XRUTREF,
            270 - StaticExtractorResultados.XRUTREF, 345 - StaticExtractorResultados.XRUTREF,
            408 - StaticExtractorResultados.XRUTREF, 471 - StaticExtractorResultados.XRUTREF,
            550 - StaticExtractorResultados.XRUTREF, 617 - StaticExtractorResultados.XRUTREF,
            680 - StaticExtractorResultados.XRUTREF, 764 - StaticExtractorResultados.XRUTREF };

    // Espaciamiento entre inicios de circulo del rut
    public static int CIRCLE_Y_RUT_DIFF = 48;
    protected static Recognizer recognizerRespustas;
    protected static Recognizer recognizerRut;

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
    static int nImage = 0;

    protected static final int CIRCLE_WIDTH = 45;

    protected static final int CIRCLE_HEIGHT = 48;

    protected static int[] X = { 2, 60, 119, 178, 239 };

    static int resC = 0;

    public static BufferedImage fillCirlces(BufferedImage bImage) {
        final Graphics2D g2d = (Graphics2D) bImage.getGraphics();
        g2d.setColor(Color.BLACK);
        final Ellipse2D ellipse = new Ellipse2D.Float();
        for (int n = 0; n < 5; n++) {
            ellipse.setFrame(0, 0, 45, 48);

            final int x = StaticExtractorResultados.X[n];
            final int y = 1;
            final int w = StaticExtractorResultados.CIRCLE_WIDTH;
            final int h = StaticExtractorResultados.CIRCLE_HEIGHT;

            final BufferedImage subImage = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
            final Graphics2D g = subImage.createGraphics();
            g.setColor(new Color(255, 255, 255, 0));
            g.fillRect(0, 0, w, h);
            g.setClip(ellipse);
            g.drawImage(bImage, -x, -y, null);
            g.dispose();
            final ImageFloat32 img32 = ConvertBufferedImage.convertFromSingle(subImage, null, ImageFloat32.class);
            final double value = ImageStatistics.mean(img32);
            g2d.drawOval(StaticExtractorResultados.X[n], 1, 45, 48);
            if (value < 180) {
                g2d.fillOval(StaticExtractorResultados.X[n], 1, 45, 48);
            }
        }
        g2d.dispose();
        return bImage;
    }

    /**
     * Obtiene los contornos de las marcas establecidas en la impresión.
     * 
     * @param limage
     *            Imagen completa de la prueba.
     * @return Lista de los contornos de las imagenes.
     */
    public static final List<Contour> getContours(BufferedImage limage) {

        final BufferedImage image = limage.getSubimage(0, 0, 150, 3200);
        final ImageFloat32 input = ConvertBufferedImage.convertFromSingle(image, null, ImageFloat32.class);
        final ImageUInt8 binary = new ImageUInt8(input.width, input.height);
        final ImageSInt32 label = new ImageSInt32(input.width, input.height);
        ThresholdImageOps.threshold(input, binary, 190, true);
        ImageUInt8 filtered = BinaryImageOps.erode8(binary, 9, null);
        filtered = BinaryImageOps.dilate8(filtered, 9, null);
        VisualizeBinaryData.renderBinary(filtered, null);
        // writeIMG(bImage, "contornos" + (nImage++));
        return BinaryImageOps.contour(filtered, ConnectRule.EIGHT, label);
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
    public static String getRespuestas(Point[] pRefRespuestas, BufferedImage image, int nroPreguntas) {
        int pregunta = 1;
        new StringBuffer();
        for (int idx = 1; idx <= nroPreguntas; idx = idx + StaticExtractorResultados.GROUP_SIZE) {
            // Con esto se calcula el nro del punto de referencia.
            final int pIdx = (pregunta % StaticExtractorResultados.NRO_PREG_POR_COLUMNA - 1)
                    / StaticExtractorResultados.GROUP_SIZE;

            final int x = pRefRespuestas[pIdx].x;
            final int y = pRefRespuestas[pIdx].y;

            final int col = (pregunta - 1) / StaticExtractorResultados.NRO_PREG_POR_COLUMNA;

            for (int n = 0; n < StaticExtractorResultados.GROUP_SIZE; n++) {
                final int left = x + StaticExtractorResultados.DELTA_X - 4
                        + col * StaticExtractorResultados.DELTA_X_FIRST_CIRCLES;
                final int top = y + StaticExtractorResultados.DELTA_Y_FIRST_CIRCLE_RESP + StaticExtractorResultados.BASE
                        + n * 52;
                BufferedImage img = image.getSubimage(left, top,
                        StaticExtractorResultados.CIRCLE_SIZE * 5 + StaticExtractorResultados.CIRCLE_X_SPCAES * 4 + 4,
                        StaticExtractorResultados.CIRCLE_SIZE + 8);
                img = StaticExtractorResultados.fillCirlces(img);

                // try {
                // ImageIO.write(img, "png", new File("/res/PR_res" + pregunta +
                // ".png"));
                // } catch (IOException e) {
                // e.printStackTrace();
                // }
                pregunta++;
            }
        }
        return "";
    }

    /**
     * Obtiene la rotación con las marcas establecidas en impresion.
     * 
     * @param contours
     *            Lista de contornos que con los cuales se quiere obtener el
     *            angulo de rotacion.
     * @return Angulo de rotación.
     */
    public static final double getRotationAngle(List<Contour> contours) {

        final int[] x = new int[contours.size() - 1];
        final int[] y = new int[contours.size() - 1];
        int n = 0;
        for (int idx = 1; idx < contours.size(); idx++) {
            final Contour contour = contours.get(idx);
            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            for (final Point2D_I32 point : contour.external) {
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
        final int dx = x[n - 1] - x[0];
        final int dy = y[n - 1] - y[0];
        final double angle = Math.PI / 2f - Math.atan2(dy, dx);
        log.fine(String.format("Angulo: %f[rad] %f[°]", angle, angle / Math.PI * 180.0));
        return angle;
    }

    public static String getRut(Point pRefRut, BufferedImage image) {
        StaticExtractorResultados.nRut++;
        final int x = pRefRut.x;
        for (final int element : StaticExtractorResultados.CIRCLE_X_RUT_DIFF) {
            final int y = pRefRut.y;
            final BufferedImage rut = image.getSubimage(x + element - 2, y - 2, 51, 555);

            try {
                ImageIO.write(rut, "png", new File("/res/ruts/RUT_" + StaticExtractorResultados.nRut % 9 + ".png"));
                StaticExtractorResultados.nRut++;
            } catch (final IOException e) {
                e.printStackTrace();
            }

        }
        return "";
    }

    public static void main(String[] args) {

        BufferedImage limage = null;
        final int n = 8;
        // for (n = 0; n < 37; n++)
        {
            try {
                limage = ImageIO.read(new File(String.format("/res/5A_CN_Independencia/prueba%02d.JPG", n)));
                limage = StaticExtractorResultados.rectificarImagen(limage);
                final Point[] pointsReference = StaticExtractorResultados.obtenerPuntosReferencia(limage);
                final Point pRefRut = pointsReference[0];
                StaticExtractorResultados.getRut(pRefRut, limage);
                final Point[] pRefRespuestas = Arrays.copyOfRange(pointsReference, 1, pointsReference.length);
                limage = StaticExtractorResultados.preprocesarImagen(limage);
                StaticExtractorResultados.getRespuestas(pRefRespuestas, limage, 35);

                StaticExtractorResultados.getContours(limage);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Se obtienen todos los puntos de referencia en base a los contornos
     * generados de las marcas de impresion.
     * 
     * @param image
     *            La imagen que tiene las figuras de referencia.
     * @return Puntos de referencia para obtener las respuestas.
     */
    public static final Point[] obtenerPuntosReferencia(BufferedImage image) {
        final List<Contour> contours = StaticExtractorResultados.getContours(image);
        final Point[] points = new Point[contours.size()];
        int n = 0;
        for (int idx = 0; idx < contours.size(); idx++) {
            final Contour contour = contours.get(idx);
            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            for (final Point2D_I32 point : contour.external) {
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

    public static final BufferedImage preprocesarImagen(BufferedImage image) {
        final ImageFloat32 input = ConvertBufferedImage.convertFromSingle(image, null, ImageFloat32.class);
        final ImageUInt8 binary = new ImageUInt8(input.width, input.height);
        ThresholdImageOps.threshold(input, binary, 180, false);
        ImageUInt8 output = BinaryImageOps.erode4(binary, 2, null);
        output = BinaryImageOps.dilate4(output, 7, null);
        output = BinaryImageOps.erode4(output, 2, null);
        output = BinaryImageOps.dilate4(output, 5, null);
        output = BinaryImageOps.erode4(output, 8, null);
        final BufferedImage bImage = VisualizeBinaryData.renderBinary(output, null);

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
    public static final BufferedImage rectificarImagen(BufferedImage limage) {
        List<Contour> contours = StaticExtractorResultados.getContours(limage);
        BufferedImage image = limage;
        if (contours.size() < 6) {
            image = StaticExtractorResultados.rotate(Math.PI, limage);
            // writeIMG(image, "rotada1_" + (nImage++));
            contours = StaticExtractorResultados.getContours(image);
        }
        final double anlge = StaticExtractorResultados.getRotationAngle(contours);
        image = StaticExtractorResultados.rotate(anlge, image);
        // writeIMG(image, "rotada2_" + (nImage++));
        return image;
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
    public static final BufferedImage rotate(double angle, BufferedImage image) {
        final int drawLocationX = 0;
        final int drawLocationY = 0;

        // Valores para la rotación.
        final double rotationRequired = angle;
        final double locationX = image.getWidth() / 2;
        final double locationY = image.getHeight() / 2;
        final AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
        final AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BICUBIC);
        final BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        final Graphics2D g2d = (Graphics2D) result.getGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
        // Se dibuja la imagen rotada.
        g2d.drawImage(op.filter(image, null), drawLocationX, drawLocationY, null);
        return result;
    }

    public static void writeIMG(BufferedImage image, String name) {
        try {
            ImageIO.write(image, "png", new File("/res/ruts/" + name + ".png"));
        } catch (final IOException e) {
            e.printStackTrace();
        }
        ;
    }

}
