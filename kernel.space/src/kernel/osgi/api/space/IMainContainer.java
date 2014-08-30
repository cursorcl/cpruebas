package kernel.osgi.api.space;

import cl.eos.interfaces.view.IViewContainer;

public interface IMainContainer {

	/**
	 * Agrega una vista al contenedor principal de la aplicacion.
	 * 
	 * @param view
	 *            Vista a ser agregada, esta vista es la vista principal de un
	 *            modulo.
	 */
	void add(IViewContainer view);

	/**
	 * Elimina vista del contenedor principal de la aplicacion.
	 * 
	 * @param view
	 *            Vista a ser eliminada.
	 */
	void remove(IViewContainer view);
}
