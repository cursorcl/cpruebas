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
    private SimpleBooleanProperty valid;

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
        File fRedPrueba = new File("res/red_2");
        
        File fRedRut = new File("res/red_rut");
        
        if (fRedPrueba.exists() && fRedRut.exists()) {
            try {
                recognizerRespustas = RecognizerFactory.create(fRedPrueba);
                recognizerRut = RecognizerFactory.create(fRedRut);
                
            } catch (IOException e) {
                log.error("Error al leer los archivos de redes neuronales:" + e.getLocalizedMessage());
                valid.set(false);
            }

        } else {
            log.error("red_rut existe = " + fRedRut.getAbsolutePath() + " " + fRedRut.exists());
            log.error("red_2 existe = " + fRedPrueba.getAbsolutePath() + " " + fRedPrueba.exists());
            valid.set(false);
        }

    }

    public static ExtractorResultadosPrueba getInstance() {
        if (instance == null) {
            instance = new ExtractorResultadosPrueba();
        }
        return instance;
    }

    public OTResultadoScanner process(File archivo, int nroPreguntas) throws IOException, CPruebasException {
        BufferedImage bImg = ImageIO.read(archivo);
        int h = bImg.getHeight();
        int w = bImg.getWidth();
        if (h < 3200 || w < 2480) {
            log.error(String.format("Dimensiones de la prueba son muy pequeñas %dx%d", w, h));
            throw new CPruebasException(String.format("Dimensiones de la prueba son muy pequeñas %dx%d", w, h));
        }
        writeIMG(bImg, "original");
        return process(bImg, nroPreguntas);
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
        OTResultadoScanner resultado = null;
        BufferedImage recImage = rectificarImagen(image);
        writeIMG(recImage, "rectificar");
        Point[] pointsReference = obtenerPuntosReferencia(recImage);
        if (pointsReference != null) {
            resultado =  new OTResultadoScanner();
            Point pRefRut = pointsReference[0];
            nRut = 0;
            String rut = getRut(pRefRut, recImage);
            Point[] pRefRespuestas = Arrays.copyOfRange(pointsReference, 1, pointsReference.length);
            recImage = preprocesarImagen(recImage);
            String respuestas = getRespuestas(pRefRespuestas, recImage, nroPreguntas);

            log.info(String.format("%s,%s", rut, respuestas));

            resultado.setForma(1);
            resultado.setRespuestas(respuestas);
            resultado.setRut(rut);
        }
        return resultado;
    }

    public final SimpleBooleanProperty validProperty() {
        return this.valid;
    }

    public final boolean isValid() {
        return this.validProperty().get();
    }

    public final void setValid(final boolean valid) {
        this.validProperty().set(valid);
    }

}
