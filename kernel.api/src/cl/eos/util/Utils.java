package cl.eos.util;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;


public class Utils {

	/**
	 * Método Estático que valida si un rut es válido Fuente :
	 */
	public static boolean validarRut(String strRut) 
	{
		boolean resultado= false; 		
		if (strRut.length() > 0) {
			// Creamos un arreglo con el rut y el digito verificador
			String[] rut_dv = strRut.split("-");
			// Las partes del rut (numero y dv) deben tener una longitud
			// positiva
			if (rut_dv.length == 2) 
			{
				int rut = Integer.parseInt(rut_dv[0]);
				char dv = rut_dv[1].toUpperCase().charAt(0);

				int m = 0, s = 1;
				for (; rut != 0; rut /= 10) {
					s = (s + rut % 10 * (9 - m++ % 6)) % 11;
				}
				resultado = (dv == (char) (s != 0 ? s + 47 : 75));
			}
		}
		return resultado;
	}
		
	
	public static Dimension getImageDim(final String path) {
	    Dimension result = null;
	    String suffix = getFileSuffix(path);
	    Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
	    if (iter.hasNext()) {
	        ImageReader reader = iter.next();
	        try {
	            ImageInputStream stream = new FileImageInputStream(new File(path));
	            reader.setInput(stream);
	            int width = reader.getWidth(reader.getMinIndex());
	            int height = reader.getHeight(reader.getMinIndex());
	            result = new Dimension(width, height);
	        } catch (IOException e) {
	            System.out.println(e.getMessage());
	        } finally {
	            reader.dispose();
	        }
	    } else {
	    	System.out.println("No reader found for given format: " + suffix);
	    }
	    return result;
	}
	
	private static String getFileSuffix(final String path) {
	    String result = null;
	    if (path != null) {
	        result = "";
	        if (path.lastIndexOf('.') != -1) {
	            result = path.substring(path.lastIndexOf('.'));
	            if (result.startsWith(".")) {
	                result = result.substring(1);
	            }
	        }
	    }
	    return result;
	}
	
	public static Float getNota(int nroPreguntas, float porcDificultad, String respuestas, String respEsperadas, float  notaMinima)
	{
		float puntajeCorte = ((float)porcDificultad / 100f) * ((float)nroPreguntas);
		float factorBajoCorte = (4f - notaMinima) /  puntajeCorte;
		float factorSobreCorte = 3f /  (nroPreguntas  - puntajeCorte);
		
		float nota = 0f;
		
		float correctas = getCorrectas(respuestas, respEsperadas);
		
		if(correctas <= puntajeCorte)
		{
			nota = correctas * factorBajoCorte + notaMinima;
		}
		else
		{
			nota = factorBajoCorte * puntajeCorte + (correctas - puntajeCorte) * factorSobreCorte + notaMinima;
		}
		
		return nota;
	}
	
	public static Float getCorrectas(String respuestas, String respEsperadas)
	{
		float correctas = 0;
		for(int n = 0; n < respEsperadas.length(); n++)
		{
			correctas += respEsperadas.charAt(n) == respuestas.charAt(n) ? 1: 0; 
		}
		return correctas;
	}
}
