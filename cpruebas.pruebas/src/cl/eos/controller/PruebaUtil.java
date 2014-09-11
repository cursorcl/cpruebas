package cl.eos.controller;

/**
 * Esta clase contiene los metodos para realizar los calculos de las pruebas.
 * @author eosorio
 */
public class PruebaUtil {

	public static Float getNota(int nroPreguntas, float porcDificultad, String respuestas, String respEsperadas, float  notaMinima)
	{
		float puntajeCorte = ((float)porcDificultad / 100f) * ((float)nroPreguntas);
		float factorBajoCorte = (4f - notaMinima) /  puntajeCorte;
		float factorSobreCorte = 3f /  (nroPreguntas  - puntajeCorte);
		
		float nota = 0f;
		
		float correctas = 0;
		for(int n = 0; n < respEsperadas.length(); n++)
		{
			correctas += respEsperadas.charAt(n) == respuestas.charAt(n) ? 1: 0; 
		}
		
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
	
	public static void main(String[] args) {
		System.out.println(PruebaUtil.getNota(30, 60, "123456789012345678901234567U90", "123456789012345678901234567890", 1));
	}
}
