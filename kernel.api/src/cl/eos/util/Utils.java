package cl.eos.util;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

import org.apache.log4j.Logger;


public class Utils {

	public static float MAX_PUNTAJE = 340f;
	private static Logger log = Logger.getLogger(Utils.class);

	/**
	 * Metodo Estatico que valida si un rut es valido
	 */
	public static boolean validarRut(String strRut) {
		boolean resultado = false;
		if (strRut.length() > 0) {
			// Creamos un arreglo con el rut y el digito verificador
			String[] rut_dv = strRut.split("-");
			// Las partes del rut (numero y dv) deben tener una longitud
			// positiva
			if (rut_dv.length == 2) {
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
				ImageInputStream stream = new FileImageInputStream(new File(
						path));
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

	public static Float getNota(int nroPreguntas, float porcDificultad,
			float correctas, float notaMinima) {
		float puntajeCorte = ((float) porcDificultad / 100f)
				* ((float) nroPreguntas);
		float factorBajoCorte = (4f - notaMinima) / puntajeCorte;
		float factorSobreCorte = 3f / (nroPreguntas - puntajeCorte);
		float nota = 0f;
		if (correctas <= puntajeCorte) {
			nota = correctas * factorBajoCorte + notaMinima;
		} else {
			nota = factorBajoCorte * puntajeCorte + (correctas - puntajeCorte)
					* factorSobreCorte + notaMinima;
		}

		return nota;
	}

	/**
	 * Obtiene el puntaje simulado de acuerdo a las pruebas SIMCE.
	 * 
	 * @param nota
	 *            La nota calculada del alumno.
	 * @return Valor del puntaje
	 */
	public static int getPuntaje(float nota) {

		float[] pjes = getPuntajes4Plus(nota);
		if (nota < 4f) {
			pjes = getPuntajes4Minus(nota);
		}
		return Math.round((pjes[0] + pjes[1] + pjes[2]) / 3f);
	}

	public static float getPorcenta(float nota) {
		return ((float) getPuntaje(nota) / MAX_PUNTAJE) * 100f;
	}
	
	

	/**
	 * Solo vale cuando es superior a 4.
	 * 
	 * @param nota
	 * @return
	 */
	private static float[] getPuntajes4Plus(float nota) {
		float v1 = (MAX_PUNTAJE - 215f) / 31f; // 4.03225806
		float v2 = (MAX_PUNTAJE - 210f) / 30f; // 4.33333333
		float v3 = (MAX_PUNTAJE - 215f) / 33f; // 3.78787879
		float fNota = ((int) (nota * 10f)) / 10f;

		int n = (int) Math.round(((7.0f - fNota) * 10f));
		float max = MAX_PUNTAJE;

		float[] pjes = { max - v1 * n, max - v2 * n, max - v3 * n };

		return pjes;
	}

	private static float[] getPuntajes4Minus(float nota) {
		float[] pjes = getPuntajes4Plus(4f);
		float v1 = ((pjes[0] - 120f) / 39f) * 2f; // 5
		float v2 = ((pjes[1] - 120f) / 38f) * 1f; // 2
		float v3 = ((pjes[2] - 120f) / 41f) * 3f; // 8
		float fNota = ((int) (nota * 10f)) / 10f;

		int n = (int) Math.round(((4.0f - fNota) * 10f));
		float[] res = { pjes[0] - v1 * n, pjes[1] - v2 * n, pjes[2] - v3 * n };
		return res;
	}

	public static Float getCorrectas(String respuestas, String respEsperadas) {
		float correctas = 0;
		for (int n = 0; n < respEsperadas.length(); n++) {
			correctas += respEsperadas.charAt(n) == respuestas.charAt(n) ? 1
					: 0;
		}
		return correctas;
	}

	public static String getFileExtension(File file) {
		String fileName = file.getName();
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		else
			return "";
	}

	public static void main(String[] args) {
		System.out.println(Math.round(getPuntaje(6.2f)));
		System.out.println(Math.round(getPuntaje(6.43f)));
		System.out.println(Math.round(getPuntaje(6.545f)));
		System.out.println(Math.round(getPuntaje(4.2f)));
	}

	public static char getDecimalSeparator() {
		DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance();
		DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
		char sep = symbols.getDecimalSeparator();
		return sep;
	}

	public static float redondeo2Decimales(float parametro) {
		return Math.round(parametro * 100f) / 100f;
	}

	public static double redondeo2Decimales(double parametro) {
		return Math.round(parametro * 100d) / 100d;
	}

	   public static float redondeo1Decimal(float parametro) {
	        return Math.round(parametro * 10f) / 10f;
	    }

	    public static double redondeo1Decimal(double parametro) {
	        return Math.round(parametro * 10d) / 10d;
	    }
	
	public static boolean isNumeric(String s) {
		return s.matches("[-+]?\\d*\\.?\\d+");
	}

	public static boolean isInteger(String s) {
		return s.matches("[-+]?\\d+");
	}

	/**
	 * Obtiene el directorio donde se almacenan todos los archivos. Retorna
	 * uset.home directorio.
	 * 
	 * @return
	 */
	public static File getDefaultDirectory() {
		String path = System.getProperty("user.home") + File.separator
				+ "Documents";
		path += File.separator + "CPruebas";
		File customDir = new File(path);
		if (customDir.exists()) {
			//log.debug(customDir + " ya existe.");
		} else if (customDir.mkdirs()) {
			//log.debug(customDir + " fue creado.");
		} else {
			log.error(customDir + " no fue creado.");
			path = System.getProperty("user.home");
			customDir = new File(path);
		}
		return customDir;
	}

}
