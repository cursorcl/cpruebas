package cl.eos.detection;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import cl.cursor.card.RecognizerFactory;
import cl.eos.detection.base.AExtractorResultados;
import cl.eos.exceptions.CPruebasException;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Realiza el analisis de una imagen, obitiene el rut del alumno y las
 * respuestas que el alumno ha seleccionado. El pre-procesamiento se realiza a
 * nivel de toda la imagen.
 */
public class ExtractorResultadosPrueba extends AExtractorResultados {

    static final Logger log = Logger.getLogger(ExtractorResultadosPrueba.class);
    private static ExtractorResultadosPrueba instance;

    public static ExtractorResultadosPrueba getInstance() {
        if (ExtractorResultadosPrueba.instance == null) {
            ExtractorResultadosPrueba.instance = new ExtractorResultadosPrueba();
        }
        return ExtractorResultadosPrueba.instance;
    }

    private final SimpleBooleanProperty valid;

    /**
     * Constructor de la clase de extracción de información de una imagen
     * prueba.
     *
     * @throws CPruebasException
     *
     * @throws IOException
     *             Error al leer las redes.
     */
    private ExtractorResultadosPrueba() {
        valid = new SimpleBooleanProperty(true);
        final File fRedPrueba = new File("res/red_2");

        final File fRedRut = new File("res/red_rut");

        if (fRedPrueba.exists() && fRedRut.exists()) {
            try {
                recognizerRespustas = RecognizerFactory.create(fRedPrueba);
                recognizerRut = RecognizerFactory.create(fRedRut);

            } catch (final IOException e) {
                ExtractorResultadosPrueba.log
                        .error("Error al leer los archivos de redes neuronales:" + e.getLocalizedMessage());
                valid.set(false);
            }

        } else {
            ExtractorResultadosPrueba.log
                    .error("red_rut existe = " + fRedRut.getAbsolutePath() + " " + fRedRut.exists());
            ExtractorResultadosPrueba.log
                    .error("red_2 existe = " + fRedPrueba.getAbsolutePath() + " " + fRedPrueba.exists());
            valid.set(false);
        }

    }

    public final boolean isValid() {
        return validProperty().get();
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
    @Override
    public OTResultadoScanner process(BufferedImage image, int nroPreguntas) {
        OTResultadoScanner resultado = null;
        BufferedImage recImage = rectificarImagen(image);
        AExtractorResultados.writeIMG(recImage, "rectificar");
        final Point[] pointsReference = obtenerPuntosReferencia(recImage);
        if (pointsReference != null) {
            resultado = new OTResultadoScanner();
            final Point pRefRut = pointsReference[0];
            nRut = 0;
            final String rut = getRut(pRefRut, recImage);
            final Point[] pRefRespuestas = Arrays.copyOfRange(pointsReference, 1, pointsReference.length);
            recImage = preprocesarImagen(recImage);
            final String respuestas = getRespuestas(pRefRespuestas, recImage, nroPreguntas);

            ExtractorResultadosPrueba.log.info(String.format("%s,%s", rut, respuestas));

            resultado.setForma(1);
            resultado.setRespuestas(respuestas);
            resultado.setRut(rut);
        }
        return resultado;
    }

    @Override
    public OTResultadoScanner process(File archivo, int nroPreguntas) throws IOException, CPruebasException {
        final BufferedImage bImg = ImageIO.read(archivo);
        final int h = bImg.getHeight();
        final int w = bImg.getWidth();
        if (h < 3200 || w < 2480) {
            ExtractorResultadosPrueba.log.error(String.format("Dimensiones de la prueba son muy pequeñas %dx%d", w, h));
            throw new CPruebasException(String.format("Dimensiones de la prueba son muy pequeñas %dx%d", w, h));
        }
        AExtractorResultados.writeIMG(bImg, "original");
        return process(bImg, nroPreguntas);
    }

    public final void setValid(final boolean valid) {
        validProperty().set(valid);
    }

    public final SimpleBooleanProperty validProperty() {
        return valid;
    }

}
