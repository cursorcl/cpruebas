package cl.eos.util;

public class Utils {

	/**
	 * M�todo Est�tico que valida si un rut es v�lido Fuente :
	 */
	public static boolean validarRut(int rut, char dv) {
		int m = 0, s = 1;
		for (; rut != 0; rut /= 10) {
			s = (s + rut % 10 * (9 - m++ % 6)) % 11;
		}
		return dv == (char) (s != 0 ? s + 47 : 75);
	}
}
