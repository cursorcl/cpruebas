package cl.eos.util;

public class Utils {

	/**
	 * Método Estático que valida si un rut es válido Fuente :
	 */
	public static boolean validarRut(int rut, char dv) {
		int m = 0, s = 1;
		for (; rut != 0; rut /= 10) {
			s = (s + rut % 10 * (9 - m++ % 6)) % 11;
		}
		return dv == (char) (s != 0 ? s + 47 : 75);
	}
}
