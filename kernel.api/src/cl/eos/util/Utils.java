package cl.eos.util;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

import org.apache.log4j.Logger;

public class Utils {

    private static final String LASTKEY = "lastkey";
    public static float MAX_PUNTAJE = 340f;
    private static Logger log = Logger.getLogger(Utils.class);

    public static Float getCorrectas(String respuestas, String respEsperadas) {
        float correctas = 0;
        for (int n = 0; n < respEsperadas.length(); n++) {
            correctas += respEsperadas.charAt(n) == respuestas.charAt(n) ? 1 : 0;
        }
        return correctas;
    }

    public static char getDecimalSeparator() {
        final DecimalFormat format = (DecimalFormat) NumberFormat.getInstance();
        final DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
        final char sep = symbols.getDecimalSeparator();
        return sep;
    }

    /**
     * Obtiene el directorio donde se almacenan todos los archivos. Retorna
     * uset.home directorio.
     * 
     * @return
     */
    public static File getDefaultDirectory() {
        String path = System.getProperty("user.home") + File.separator + "Documents";
        path += File.separator + "CPruebas";
        File customDir = new File(path);
        if (customDir.exists()) {
            // log.debug(customDir + " ya existe.");
        } else if (customDir.mkdirs()) {
            // log.debug(customDir + " fue creado.");
        } else {
            Utils.log.error(customDir + " no fue creado.");
            path = System.getProperty("user.home");
            customDir = new File(path);
        }
        return customDir;
    }

    public static String getFileExtension(File file) {
        final String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        else
            return "";
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

    public static Dimension getImageDim(final String path) {
        Dimension result = null;
        final String suffix = Utils.getFileSuffix(path);
        final Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
        if (iter.hasNext()) {
            final ImageReader reader = iter.next();
            try {
                final ImageInputStream stream = new FileImageInputStream(new File(path));
                reader.setInput(stream);
                final int width = reader.getWidth(reader.getMinIndex());
                final int height = reader.getHeight(reader.getMinIndex());
                result = new Dimension(width, height);
            } catch (final IOException e) {
                System.out.println(e.getMessage());
            } finally {
                reader.dispose();
            }
        } else {
            System.out.println("No reader found for given format: " + suffix);
        }
        return result;
    }

    public static Float getNota(int nroPreguntas, float porcDificultad, float correctas, float notaMinima) {
        final float puntajeCorte = porcDificultad / 100f * nroPreguntas;
        final float factorBajoCorte = (4f - notaMinima) / puntajeCorte;
        final float factorSobreCorte = 3f / (nroPreguntas - puntajeCorte);
        float nota = 0f;
        if (correctas <= puntajeCorte) {
            nota = correctas * factorBajoCorte + notaMinima;
        } else {
            nota = factorBajoCorte * puntajeCorte + (correctas - puntajeCorte) * factorSobreCorte + notaMinima;
        }

        return nota;
    }

    public static float getPorcenta(float nota) {
        return Utils.getPuntaje(nota) / Utils.MAX_PUNTAJE * 100f;
    }

    /**
     * Obtiene el puntaje simulado de acuerdo a las pruebas SIMCE.
     * 
     * @param nota
     *            La nota calculada del alumno.
     * @return Valor del puntaje
     */
    public static int getPuntaje(float nota) {

        float[] pjes = Utils.getPuntajes4Plus(nota);
        if (nota < 4f) {
            pjes = Utils.getPuntajes4Minus(nota);
        }
        return Math.round((pjes[0] + pjes[1] + pjes[2]) / 3f);
    }

    private static float[] getPuntajes4Minus(float nota) {
        final float[] pjes = Utils.getPuntajes4Plus(4f);
        final float v1 = (pjes[0] - 120f) / 39f * 2f; // 5
        final float v2 = (pjes[1] - 120f) / 38f * 1f; // 2
        final float v3 = (pjes[2] - 120f) / 41f * 3f; // 8
        final float fNota = (int) (nota * 10f) / 10f;

        final int n = Math.round((4.0f - fNota) * 10f);
        final float[] res = { pjes[0] - v1 * n, pjes[1] - v2 * n, pjes[2] - v3 * n };
        return res;
    }

    /**
     * Solo vale cuando es superior a 4.
     * 
     * @param nota
     * @return
     */
    private static float[] getPuntajes4Plus(float nota) {
        final float v1 = (Utils.MAX_PUNTAJE - 215f) / 31f; // 4.03225806
        final float v2 = (Utils.MAX_PUNTAJE - 210f) / 30f; // 4.33333333
        final float v3 = (Utils.MAX_PUNTAJE - 215f) / 33f; // 3.78787879
        final float fNota = (int) (nota * 10f) / 10f;

        final int n = Math.round((7.0f - fNota) * 10f);
        final float max = Utils.MAX_PUNTAJE;

        final float[] pjes = { max - v1 * n, max - v2 * n, max - v3 * n };

        return pjes;
    }

    public static boolean isInteger(String s) {
        return s.matches("[-+]?\\d+");
    }

    public static boolean isNumeric(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }

    public static void main(String[] args) {
        System.out.println(Math.round(Utils.getPuntaje(6.2f)));
        System.out.println(Math.round(Utils.getPuntaje(6.43f)));
        System.out.println(Math.round(Utils.getPuntaje(6.545f)));
        System.out.println(Math.round(Utils.getPuntaje(4.2f)));
    }

    public static double redondeo1Decimal(double parametro) {
        return Math.round(parametro * 10d) / 10d;
    }

    public static float redondeo1Decimal(float parametro) {
        return Math.round(parametro * 10f) / 10f;
    }

    public static double redondeo2Decimales(double parametro) {
        return Math.round(parametro * 100d) / 100d;
    }

    public static float redondeo2Decimales(float parametro) {
        return Math.round(parametro * 100f) / 100f;
    }

    /**
     * Metodo Estatico que valida si un rut es valido
     */
    public static boolean validarRut(String strRut) {
        boolean resultado = false;
        if (strRut.length() > 0) {
            // Creamos un arreglo con el rut y el digito verificador
            final String[] rut_dv = strRut.split("-");
            // Las partes del rut (numero y dv) deben tener una longitud
            // positiva
            if (rut_dv.length == 2) {
                int rut = Integer.parseInt(rut_dv[0]);
                final char dv = rut_dv[1].toUpperCase().charAt(0);

                int m = 0, s = 1;
                for (; rut != 0; rut /= 10) {
                    s = (s + rut % 10 * (9 - m++ % 6)) % 11;
                }
                resultado = dv == (char) (s != 0 ? s + 47 : 75);
            }
        }
        return resultado;
    }

    public static long getLastIndex()
    {
        Preferences p = Preferences.userRoot();
        long result  = p.getLong(LASTKEY, 10000);
        p.putLong(LASTKEY, ++result);
        try {
            p.flush();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
        return result;
    }
}
