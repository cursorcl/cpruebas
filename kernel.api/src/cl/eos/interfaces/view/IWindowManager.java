package cl.eos.interfaces.view;

public interface IWindowManager {

    Object getBreadcrumbBar();

    /**
     * Obtiene el elemento raiz principal que se gestiona.
     * 
     * @return E
     */
    Object getRoot();

    /**
     * Saca una ventana de la visualizaci�n.
     * 
     * @param window
     *            la ventana que se quiere sacar.
     */
    void hide(final IView window);

    /**
     * Cierra todas las ventanas.
     */
    void hideAll();

    void setBreadcrumbBar(Object breadCrumb);

    void setHomeView(final IView window);

    void setRoot(final Object root) throws Exception;

    /**
     * Agrega una ventana al visualizador
     * 
     * @param window
     *            la ventana que se va a agregar.
     */
    void show(final IView window);

    void showOver(IView window);
    
    void repaint();

}
