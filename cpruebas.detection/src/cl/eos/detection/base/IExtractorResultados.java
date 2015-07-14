package cl.eos.detection.base;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import cl.eos.detection.OTResultadoScanner;
import cl.eos.exceptions.CPruebasException;

public interface IExtractorResultados {

	OTResultadoScanner process(File archivo, int nroPreguntas)
			throws IOException, CPruebasException;

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
	OTResultadoScanner process(BufferedImage image, int nroPreguntas);

}