package cl.eos.imp;

import cl.eos.api.IImporter;

/**
 * Clase abstracta que implementa metodos de la interface por defecto. 
 * @author cursor
 *
 */
public abstract class AImporter implements IImporter {

	protected Object source;

	@Override
	public void setSource(Object source) {
		this.source = source;
	}

	
	
}
