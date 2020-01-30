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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import boofcv.alg.filter.binary.BinaryImageOps;
import boofcv.alg.filter.binary.Contour;
import boofcv.alg.filter.binary.GThresholdImageOps;
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
import cl.eos.exceptions.CPruebasException;
import cl.eos.util.Utils;
import cl.sisdef.util.Pair;
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
 *
 */
public abstract class AExtractorResultados implements IExtractorResultados {

    private static final Logger log = Logger.getLogger(AExtractorResultados.class.getName());

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

    protected static String RUT[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "K" };
    // Inicio rectangulo.
    public static int XRUTREF = 53;

    // Son las diferencias del inicio del círculo con inicio rectángulo
    public static int[] CIRCLE_X_RUT_DIFF = { 0, 64, 135, 196, 256, 332, 397, 456, 536 };

    // Espaciamiento entre inicios de circulo del rut
    public static int CIRCLE_Y_RUT_DIFF = 48;
    protected static final int CIRCLE_WIDTH = 45;
    protected static final int CIRCLE_HEIGHT = 48;
    protected static int[] X = { 2, 60, 119, 178, 239 };

    static int resC = 0;

    /**
     * Comparador creado especificamente para ordenar los pares de valores que
     * se obtienen al procesar el RUT. Ordena por el primer item que en este
     * caso representa el valor menor de la corrdenada X.
     * 
     * @return El valor correspondiente indicando si es menor, igual o mayor el
     *         primero par con respecto al segundo.
     */
    public static Comparator<? super Pair<Integer, Integer>> compararPairsInteger() {
        return (pairSource, pairTarget) -> pairSource.getFirst().compareTo(pairTarget.getFirst());
    }

    public static void main(String[] args) {

        BufferedImage limage = null;
        final ExtractorResultadosPrueba extractor = ExtractorResultadosPrueba.getInstance();

        {
            try {
                limage = ImageIO.read(
                        new File("/home/cursor/Documents/CPruebas/scan 4.jpg"));
                final ImageFloat32 input = ConvertBufferedImage.convertFromSingle(limage, null, ImageFloat32.class);
                final ImageUInt8 binary = new ImageUInt8(input.width, input.height);
                double threshold = 185;
                threshold = GThresholdImageOps.computeOtsu(input, 0, 256);
                ThresholdImageOps.threshold(input, binary, (float) threshold, false); // 170,180
                ImageUInt8 output = BinaryImageOps.erode4(binary, 3, null); // 2,4
                output = BinaryImageOps.dilate4(output, 7, null); // 7,9
                output = BinaryImageOps.erode4(output, 2, null);
                output = BinaryImageOps.dilate4(output, 5, null); // 5
                output = BinaryImageOps.erode4(output, 3, null);
                final BufferedImage bImage = VisualizeBinaryData.renderBinary(output, null);

                AExtractorResultados.writeIMG(bImage, "ejemplo");
                extractor.process(limage, 35);

            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeIMG(BufferedImage image, String name) {
        final String debug = System.getProperty("DEBUG");
        if (debug != null && "TRUE".equalsIgnoreCase(debug)) {
            try {
                ImageIO.write(image, "png", new File(Utils.getDefaultDirectory() + "/" + name + ".png"));
            } catch (final IOException e) {
                e.printStackTrace();
            }
            ;
        }
    }

    protected int nRut = 0;

    protected Recognizer recognizerRespustas;

    protected Recognizer recognizerRut;

    protected BufferedImage fillCirlces(BufferedImage bImage) {
        final Graphics2D g2d = (Graphics2D) bImage.getGraphics();
        g2d.setColor(Color.BLACK);
        final Ellipse2D ellipse = new Ellipse2D.Float();
        for (int n = 0; n < 5; n++) {
            ellipse.setFrame(0, 0, 45, 48);

            final int x = AExtractorResultados.X[n];
            final int y = 1;
            final int w = AExtractorResultados.CIRCLE_WIDTH;
            final int h = AExtractorResultados.CIRCLE_HEIGHT;

            final BufferedImage subImage = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
            final Graphics2D g = subImage.createGraphics();
            g.setColor(new Color(255, 255, 255, 0));
            g.fillRect(0, 0, w, h);
            g.setClip(ellipse);
            g.drawImage(bImage, -x, -y, null);
            g.dispose();
            final ImageFloat32 img32 = ConvertBufferedImage.convertFromSingle(subImage, null, ImageFloat32.class);
            final double value = ImageStatistics.mean(img32);
            g2d.drawOval(AExtractorResultados.X[n], 1, 45, 48);
            if (value < 165) {
                g2d.setColor(Color.BLACK);
                g2d.fillOval(AExtractorResultados.X[n], 1, 45, 48);
            } else {
                g2d.setColor(Color.WHITE);
                g2d.fillOval(AExtractorResultados.X[n], 1, 45, 48);
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
    protected final List<Contour> getContours(BufferedImage limage) {

        final int h = Math.min(3200, limage.getHeight());
        int w = 160;
        List<Contour> contours = null;
        while (w < 230) {
            final BufferedImage image = limage.getSubimage(0, 0, w, h);
            AExtractorResultados.writeIMG(image, "subimage_contours");
            final ImageFloat32 input = ConvertBufferedImage.convertFromSingle(image, null, ImageFloat32.class);

            final ImageUInt8 binary = new ImageUInt8(input.width, input.height);
            final ImageSInt32 label = new ImageSInt32(input.width, input.height);
            double threshold = 190;
            threshold = GThresholdImageOps.computeOtsu(input, 0, 256);
            ThresholdImageOps.threshold(input, binary, (float) threshold, true);
            AExtractorResultados.writeIMG(VisualizeBinaryData.renderBinary(binary, null), "threshold_contours");
            ImageUInt8 filtered = BinaryImageOps.dilate8(binary, 2, null);
            filtered = BinaryImageOps.erode8(filtered, 9, null);
            filtered = BinaryImageOps.dilate8(filtered, 10, null);
            final BufferedImage bImage = VisualizeBinaryData.renderBinary(filtered, null);
            AExtractorResultados.writeIMG(bImage, "contornos_contours");

            contours = BinaryImageOps.contour(filtered, ConnectRule.EIGHT, label);
            if (contours != null && contours.size() == 6 ) {
                break;
            }
            w = w + 60;
        }
        return contours;
    }

    /**
     * Obtiene los contornos de las marcas establecidas en la impresión.
     * 
     * @param limage
     *            Imagen completa de la prueba.
     * @return Lista de los contornos de las imagenes.
     */
    protected final List<Contour> getContoursFullImage(BufferedImage limage) {

        List<Contour> contours = null;
        final BufferedImage image = limage;
        final ImageFloat32 input = ConvertBufferedImage.convertFromSingle(image, null, ImageFloat32.class);

        final ImageUInt8 binary = new ImageUInt8(input.width, input.height);
        final ImageSInt32 label = new ImageSInt32(input.width, input.height);
        double threshold = 190;
        threshold = GThresholdImageOps.computeOtsu(input, 0, 256);
        ThresholdImageOps.threshold(input, binary, (float) threshold, true);

        AExtractorResultados.writeIMG(VisualizeBinaryData.renderBinary(binary, null), "thresholfullimage");
        ImageUInt8 filtered = BinaryImageOps.dilate8(binary, 2, null);
        filtered = BinaryImageOps.erode8(filtered, 9, null);
        filtered = BinaryImageOps.dilate8(filtered, 10, null);
        contours = BinaryImageOps.contour(filtered, ConnectRule.EIGHT, label);
        return contours;
    }

    private Pair<Integer, Integer> getMinMaxX(Contour contour) {
        Pair<Integer, Integer> result = null;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        if (contour.external != null && contour.external.size() > 0) {
            for (int n = 0; n < contour.external.size(); n++) {
                if (contour.external.get(n).x < min) {
                    min = contour.external.get(n).x;
                }
                if (contour.external.get(n).x > max) {
                    max = contour.external.get(n).x;
                }
            }
            result = new Pair<Integer, Integer>(min, max);
        }

        return result;
    }

    /**
     * Obtiene los contornos de las marcas establecidas en la impresión.
     * 
     * @param limage
     *            Imagen completa de la prueba.
     * @return Lista de los contornos de las imagenes.
     */
    protected final Point getPointReferenciaRut(BufferedImage limage) {

        AExtractorResultados.writeIMG(limage, "PedazoRut");
        final ImageFloat32 input = ConvertBufferedImage.convertFromSingle(limage, null, ImageFloat32.class);

        final ImageUInt8 binary = new ImageUInt8(input.width, input.height);
        final ImageSInt32 label = new ImageSInt32(input.width, input.height);
        double threshold = 185;
        threshold = GThresholdImageOps.computeOtsu(input, 0, 256);
        ThresholdImageOps.threshold(input, binary, (float) threshold, true);
        AExtractorResultados.writeIMG(VisualizeBinaryData.renderBinary(binary, null), "thresholdRut");
        ImageUInt8 filtered = BinaryImageOps.dilate8(binary, 2, null);
        filtered = BinaryImageOps.erode8(filtered, 3, null);
        filtered = BinaryImageOps.dilate8(filtered, 2, null);
        final BufferedImage bImage = VisualizeBinaryData.renderBinary(filtered, null);
        AExtractorResultados.writeIMG(bImage, "contornosPedazoRut");

        final List<Contour> contours = BinaryImageOps.contour(filtered, ConnectRule.EIGHT, label);

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
        return points[0];
    }

    protected final String getRespuesta(BufferedImage img) {
        String resp = "O";
        final Pair<Integer, Pair<Double, Double>> result = recognizerRespustas.recognize(img, 0.9); // Red
        // pequeña
        // 0.8
        final int idx = result.getFirst();
        if (idx != -1) {
            resp = AExtractorResultados.RESPUESTAS[idx];
        } else {
            resp = "M";
        }
        return resp;
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
    protected String getRespuestas(Point[] pRefRespuestas, BufferedImage image, int nroPreguntas) {
        int pregunta = 1;

        final StringBuffer resp = new StringBuffer();
        for (int idx = 1; idx <= nroPreguntas; idx = idx + AExtractorResultados.GROUP_SIZE) {
            // Con esto se calcula el nro del punto de referencia.
            final int pIdx = (pregunta % AExtractorResultados.NRO_PREG_POR_COLUMNA - 1)
                    / AExtractorResultados.GROUP_SIZE;

            final int x = pRefRespuestas[pIdx].x;
            final int y = pRefRespuestas[pIdx].y;

            final int col = (pregunta - 1) / AExtractorResultados.NRO_PREG_POR_COLUMNA;

            for (int n = 0; n < AExtractorResultados.GROUP_SIZE && n + idx <= nroPreguntas; n++) {
                final int left = x + AExtractorResultados.DELTA_X + col * AExtractorResultados.DELTA_X_FIRST_CIRCLES; // -
                                                                                                                      // 4;
                final int top = y + AExtractorResultados.DELTA_Y_FIRST_CIRCLE_RESP + AExtractorResultados.BASE + n * 54;
                BufferedImage img = image.getSubimage(left, top,
                        AExtractorResultados.CIRCLE_SIZE * 5 + AExtractorResultados.CIRCLE_X_SPCAES * 4 + 4,
                        AExtractorResultados.CIRCLE_SIZE + 8);
                AExtractorResultados.writeIMG(img, "resp_" + pregunta);
                img = fillCirlces(img);
                AExtractorResultados.writeIMG(img, "fresp_" + pregunta);
                final String respuesta = getRespuesta(img);

                resp.append(respuesta);
                pregunta++;
            }
        }
        log.fine("Respuesras obtenidas:" + resp != null ? resp.toString() : "--");
        return resp.toString();
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

        final int[] x = new int[contours.size()];
        final int[] y = new int[contours.size()];
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
            x[n] = minX;
            y[n] = minY;
            n++;
        }
        final int dx = x[contours.size() - 1] - x[0];
        final int dy = y[contours.size() - 1] - y[0];
        final double angle = Math.PI / 2f - Math.atan2(dy, dx);
        log.fine(String.format("Angulo: %f[rad] %f[°]", angle, angle / Math.PI * 180.0));
        return angle;
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
        nRut++;
        int x = pRefRut.x;
        final StringBuffer strRut = new StringBuffer("");
        int y = pRefRut.y;

        final BufferedImage firstRut = image.getSubimage(x + 100, y - 2, 62, 45);
        AExtractorResultados.writeIMG(firstRut, "FIRST_RUT");
        final Point pointFirstRut = getPointReferenciaRut(firstRut);
        
        final BufferedImage nrut = image.getSubimage(x + 100 , y - 2, x + 600, 545);
        AExtractorResultados.writeIMG(nrut, "RUT");
        
        
        x = pRefRut.x + 100 + pointFirstRut.x;
        y = y + pointFirstRut.y + 2;
        BufferedImage sectorRut = image.getSubimage(x, y, x + 600, 545);
        sectorRut = preprocesarImagenRut(sectorRut);
        AExtractorResultados.writeIMG(sectorRut, "SECTOR_RUT");
        final List<Contour> contours = getContoursFullImage(sectorRut);

        final List<Pair<Integer, Integer>> sortedPairs = sortContoursByX(contours);

        for (int n = 0; n < sortedPairs.size(); n++) {
            final Pair<Integer, Integer> minMax = sortedPairs.get(n);
            y = pRefRut.y;
            final int start = minMax.getFirst().intValue();
            final int width = minMax.getSecond().intValue() - minMax.getFirst().intValue();
            final BufferedImage rut = sectorRut.getSubimage(start, 0, width, 545);

            AExtractorResultados.writeIMG(rut, "RUT_" + nRut);
            nRut++;
            final Pair<Integer, Pair<Double, Double>> result = recognizerRut.recognize(rut, 0.40);
            final int idx = result.getFirst();
            if (idx != -1) {
                strRut.append(AExtractorResultados.RUT[idx]);
            }
        }
        log.fine("Rut obtenido:" + strRut != null ? strRut.toString() : "--");
        return strRut.toString();
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
        final List<Contour> contours = getContours(image);
        Point[] points = null;
        if (contours != null) {
            points = new Point[contours.size()];
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
            final List<Point> result = new ArrayList<Point>();
            for (n = 0; n < points.length; n++) {
                result.add(points[n]);
            }
            Collections.sort(result, (p1, p2) -> p1.x - p2.x);
            while (result.size() > 6) {
                result.remove(result.size() - 1);
            }
            Collections.sort(result, (p1, p2) -> p1.y - p2.y);

            try {
                for (n = 0; n < 6; n++) {
                    points[n] = result.get(n);
                }
            } catch (final IndexOutOfBoundsException e) {
                log.severe("Problemas al obtnener puntos de referencia: " + e.getLocalizedMessage());
                points = null;
            }
        }
        return points;
    }

    protected final BufferedImage preprocesarImagen(BufferedImage image) {
        final ImageFloat32 input = ConvertBufferedImage.convertFromSingle(image, null, ImageFloat32.class);
        final ImageUInt8 binary = new ImageUInt8(input.width, input.height);
        double threshold = 185;
        threshold = GThresholdImageOps.computeOtsu(input, 0, 256);
        ThresholdImageOps.threshold(input, binary, (float) threshold, false); // 170,180
        ImageUInt8 output = BinaryImageOps.erode4(binary, 3, null); // 2,4
        output = BinaryImageOps.dilate4(output, 7, null); // 7,9
        output = BinaryImageOps.erode4(output, 2, null);
        output = BinaryImageOps.dilate4(output, 5, null); // 5
        output = BinaryImageOps.erode4(output, 8, null);
        final BufferedImage bImage = VisualizeBinaryData.renderBinary(output, null);

        return bImage;
    }

    protected final BufferedImage preprocesarImagenRut(BufferedImage image) {
        final ImageFloat32 input = ConvertBufferedImage.convertFromSingle(image, null, ImageFloat32.class);
        final ImageUInt8 binary = new ImageUInt8(input.width, input.height);
        double threshold = 185;
        threshold = GThresholdImageOps.computeOtsu(input, 0, 256);
        ThresholdImageOps.threshold(input, binary, (float) threshold, false); // 170,180
        ImageUInt8 output = BinaryImageOps.erode4(binary, 3, null); // 2,4
        output = BinaryImageOps.dilate4(output, 7, null); // 7,9
        output = BinaryImageOps.erode4(output, 2, null);
        output = BinaryImageOps.dilate4(output, 5, null); // 5
        output = BinaryImageOps.erode4(output, 3, null);
        final BufferedImage bImage = VisualizeBinaryData.renderBinary(output, null);

        return bImage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see cl.eos.detection.IExtractorResultados#process(java.awt.image.
     * BufferedImage , int)
     */
    @Override
    public abstract OTResultadoScanner process(BufferedImage image, int nroPreguntas);

    /*
     * (non-Javadoc)
     * 
     * @see cl.eos.detection.IExtractorResultados#process(java.io.File, int)
     */
    @Override
    public OTResultadoScanner process(File archivo, int nroPreguntas) throws IOException, CPruebasException {
        return process(ImageIO.read(archivo), nroPreguntas);
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
        if (contours != null) {
            if (contours.size() < 2) {
                image = rotate(Math.PI, limage);
                contours = getContours(image);
            }
            final double anlge = getRotationAngle(contours);
            image = rotate(anlge, image);
        }
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
    protected final BufferedImage rotate(double angle, BufferedImage image) {
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

    /**
     * Ordena los contornos en base al valor minimo de la X de cada contorno.
     * 
     * @param contours
     *            Contornos a ordenar.
     * @return Lista de Pares ordenados de minimo-maximo.
     */
    private List<Pair<Integer, Integer>> sortContoursByX(List<Contour> contours) {
        final List<Pair<Integer, Integer>> result = new ArrayList<Pair<Integer, Integer>>(contours.size());
        for (final Contour contour : contours) {
            final Pair<Integer, Integer> minMax = getMinMaxX(contour);
            result.add(minMax);
        }

        Collections.sort(result, AExtractorResultados.compararPairsInteger());

        return result;
    }

}
