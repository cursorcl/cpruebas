package cl.eos.interfaces.view;

import cl.eos.interfaces.entity.IEntity;

/**
 * Extiende a IView. Este tipo de vistas permiten realizar cambios a los
 * objetos. Viene siendo una vista de lectura / escritura.
 * 
 * @author eosorio
 * 
 */
public interface IFormView extends IView {

	void save(IEntity otObject);

	void delete(IEntity otObject);

	void select(IEntity otObject);

	boolean validate(IEntity otObject);
}
