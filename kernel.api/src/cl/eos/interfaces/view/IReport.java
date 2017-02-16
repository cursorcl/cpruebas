package cl.eos.interfaces.view;

import javafx.concurrent.Task;

public interface IReport {
  

  /**
   * Método que tiene que implementar la clase que hará de generador de reporte.
   */
  Task<Void> getReport();
}
