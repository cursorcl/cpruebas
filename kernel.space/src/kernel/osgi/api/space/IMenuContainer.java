package kernel.osgi.api.space;

import cl.eos.interfaces.view.IMenu;
import cl.eos.interfaces.view.IMenuItem;


public interface IMenuContainer {

	/**
	 * Agrega un menu al final del contenedor:
	 * 
	 * @param menu
	 *            Menu a ser agregado.
	 */
	void addMenu(IMenu menu);

	/**
	 * Agrega un menu al final del parent:
	 * 
	 * @param menu
	 *            Menu a ser agregado.
	 */
	void addMenu(IMenu parent, IMenu child);

	/**
	 * Agrega un menutem al final del parent:
	 * 
	 * @param menu
	 *            MenuItem a ser agregado.
	 */

	void addMenuItem(IMenu parent, IMenuItem menuItem);

	void remove(IMenu menu);

	void remove(IMenu parent, IMenu child);

	void remove(IMenu parent, IMenuItem menuItem);
}
