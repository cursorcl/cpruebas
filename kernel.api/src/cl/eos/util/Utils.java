package cl.eos.util;

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
				char dv = rut_dv[1].charAt(0);

				int m = 0, s = 1;
				for (; rut != 0; rut /= 10) {
					s = (s + rut % 10 * (9 - m++ % 6)) % 11;
				}
				resultado = (dv == (char) (s != 0 ? s + 47 : 75));
			}
		}
		return resultado;
	}
			
}
