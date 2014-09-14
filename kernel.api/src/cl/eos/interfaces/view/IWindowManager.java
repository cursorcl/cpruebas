package cl.eos.interfaces.view;


public interface IWindowManager {

  /**
   * Obtiene el elemento raiz principal que se gestiona.
   * @return E
   */
  Object getRoot();

  void setRoot(final Object root) throws Exception;

  /**
   * Agrega una ventana al visualizador
   * 
   * @param window la ventana que se va a agregar.
   */
  void show(final Object window);

  /**
   * Saca una ventana de la visualizaci�n.
   * 
   * @param window la ventana que se quiere sacar.
   */
  void hide(final Object window);

  /**
   * Cierra todas las ventanas.
   */
  void hideAll();

  /**
   * Muestra una ventana con la opci�n de cerrar el resto.
   * 
   * @param window Ventana a presentar.
   * @param closeOthers Verdadero si quiere cerrar el resto de ventanas.
   */
  void show(final Object window, boolean closeOthers);

  /**
   * Muestra window como hija de parent.
   * 
   * @param parent Contenedor de despliegue de window.
   * @param widnow Quien se quiere mostrar.
   */
  void show(final Object parent, final Object widnow);

  void show(final Object parent, final Object widnow, final boolean closeOthers);


}
