package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.ObjetivosModel;
import cl.eos.persistence.models.Objetivo;

public class ObjetivosContoller extends AController {

	public ObjetivosContoller() {
		
		
		
	}

	@Override
	public void initialize() {
		model = new ObjetivosModel();
		model.findAll(Objetivo.class, this);
	}
}
