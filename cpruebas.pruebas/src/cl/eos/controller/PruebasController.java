package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.PruebasModel;
import cl.eos.persistence.models.Prueba;

public class PruebasController extends AController {

	public PruebasController() {
		
	}

	@Override
	public void initialize() {
		model = new PruebasModel();
		model.findAll(Prueba.class, this);
	}

	
	
}
