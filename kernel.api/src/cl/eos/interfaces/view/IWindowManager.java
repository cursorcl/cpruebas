package cl.eos.interfaces.view;

import javafx.scene.Parent;

public interface IWindowManager {

	/**
	 * Agrega una ventana al visualizador
	 * @param window la ventana que se va a agregar.
	 */
	void show(final Parent window );
	
	/**
	 * Saca una ventana de la visualización. 
	 * @param window la ventana que se quiere sacar.
	 */
	void hide(final Parent window);
	
	/**
	 * Cierra todas las ventanas.
	 */
	void hideAll();

	/**
	 * Muestra una ventana con la opción de cerrar el resto.
	 * @param window  Ventana a presentar.
	 * @param closeOthers Verdadero si quiere cerrar el resto de ventanas.
	 */
	void show(final Parent window, boolean closeOthers);
}
