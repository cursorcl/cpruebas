package cl.eos.view.ots.ejeevaluacion;

/**
 * Contiene tantos elementos en el arreglo como evaluaciones existen.
 * @author curso_000
 */
public class OTAcumulador {

  private int[] nroPersonas;

  public OTAcumulador() {

  }

  public int[] getNroPersonas() {
    return nroPersonas;
  }

  public void setNroPersonas(int[] nroPersonas) {
    this.nroPersonas = nroPersonas;
  }

  public int getAlumnos()
  {
      int suma = 0;
      if(nroPersonas != null &&  nroPersonas.length > 0)
      {
          for(int n = 0; n < nroPersonas.length; n++)
          {
              suma += nroPersonas[n];
          }
      }
      return suma;
  }
}
