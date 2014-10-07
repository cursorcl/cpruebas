package cl.eos.interfaces.view;


public interface IWindowManager {

  /**
   * Obtiene el elemento raiz principal que se gestiona.
   * 
   * @return E
   */
  Object getRoot();

  void setRoot(final Object root) throws Exception;

  Object getBreadcrumbBar();
  void setBreadcrumbBar(Object breadCrumb);
  /**
   * Agrega una ventana al visualizador
   * 
   * @param window la ventana que se va a agregar.
   */
  void show(final IView window);
  void setHomeView(final IView window);

  /**
   * Saca una ventana de la visualizaci�n.
   * 
   * @param window la ventana que se quiere sacar.
   */
  void hide(final IView window);

  /**
   * Cierra todas las ventanas.
   */
  void hideAll();



}
