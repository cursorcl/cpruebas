package cl.eos.view.ots.ejeevaluacion;

import java.util.Arrays;

/**
 * Contiene tantos elementos en el arreglo como evaluaciones existen.
 * 
 * @author curso_000
 */
public class OTAcumulador {

  private int[] nroPersonas;

  public OTAcumulador() {

  }

  public OTAcumulador(int nroRangos)
  {
    nroPersonas =  new int[nroRangos];
    Arrays.fill(nroPersonas, 0);
  }
  public int getAlumnos() {
    int suma = 0;
    if (nroPersonas != null && nroPersonas.length > 0) {
      for (final int nroPersona : nroPersonas) {
        suma += nroPersona;
      }
    }
    return suma;
  }

  public int[] getNroPersonas() {
    return nroPersonas;
  }

  public void setNroPersonas(int[] nroPersonas) {
    this.nroPersonas = nroPersonas;
  }
}
