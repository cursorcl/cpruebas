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
 * Realiza el analisis de una imagen, obitiene el rut del alumno y las respuestas que el alumno ha
 * seleccionado. El pre-procesamiento se realiza a nivel de toda la imagen.
 */
public class ExtractorResultadosPrueba extends AExtractorResultados {

  private static ExtractorResultadosPrueba instance;

  /**
   * Constructor de la clase de extracción de información de una imagen prueba.
   * 
   * @throws IOException Error al leer las redes.
   */
  private ExtractorResultadosPrueba() throws IOException {
    recognizerRespustas = RecognizerFactory.create(new File("./res/red_2"));
    recognizerRut = RecognizerFactory.create(new File("./res/red_rut"));
  }

  public static ExtractorResultadosPrueba getInstance() throws IOException {
    if (instance == null) {
      instance = new ExtractorResultadosPrueba();
    }
    return instance;
  }

  public OTResultadoScanner process(File archivo, int nroPreguntas) throws IOException {
    return process(ImageIO.read(archivo), nroPreguntas);
  }

  /**
   * Metodo que realiza el procesamiento de una prueba, obtiene el rut y las respuestas. Por ahora
   * la forma ratorn 1.
   * 
   * @param image Imagen que contiene la prueba.
   * @param nroPreguntas Número de preguntas de la prueba.
   * @return {@link OTResultadoScanner} que contiene RUT, RESPUESTAS y FORMA de la prueba.
   */
  public OTResultadoScanner process(BufferedImage image, int nroPreguntas) {
    OTResultadoScanner resultado = new OTResultadoScanner();
    BufferedImage recImage = rectificarImagen(image);
    Point[] pointsReference = obtenerPuntosReferencia(recImage);
    Point[] pRefRespuestas = Arrays.copyOfRange(pointsReference, 1, pointsReference.length);
    recImage = preprocesarImagen(recImage);
    String respuestas = getRespuestas(pRefRespuestas, recImage, nroPreguntas);

    Point pRefRut = pointsReference[0];
    String rut = getRut(pRefRut, recImage);

    resultado.setForma(1);
    resultado.setRespuestas(respuestas);
    resultado.setRut(rut);
    return resultado;
  }

  static String[] fileNames = {"prueba_001.png", "prueba_002.png", "prueba_003.png",
      "prueba_004.png", "prueba_005.png", "prueba00.jpg", "prueba01.jpg", "prueba02.jpg",
      "prueba03.jpg"};

  public static void main(String args[]) throws IOException {
    try {
      ExtractorResultadosPrueba extractor = new ExtractorResultadosPrueba();

//      for (String fileName : fileNames) {
        String fileName = "prueba5.png";
        BufferedImage image = ImageIO.read(new File("./res/" + fileName));
        System.out.print(fileName + " ");
        System.out.println(extractor.process(image, 30));
//      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}