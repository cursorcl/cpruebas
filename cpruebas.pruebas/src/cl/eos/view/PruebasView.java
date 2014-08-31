package cl.eos.view;

import cl.eos.imp.view.AView;
import cl.eos.interfaces.model.IModel;

public class PruebasView extends AView {

	
	private IModel model;

	public PruebasView() {
		model = getController().getModel();
	}
}
