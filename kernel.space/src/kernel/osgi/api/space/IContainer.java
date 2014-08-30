package kernel.osgi.api.space;

import java.awt.Container;

/**
 * Es la interface que deben implementar todos aquellos que quieren ser un
 * contenedor de la aplicacion.
 * 
 * @author eosorio
 * 
 */
public interface IContainer {

	IMenuContainer getMenuContainer();

	Container getStatusContainer();

	IMainContainer getMainContainer();

}
