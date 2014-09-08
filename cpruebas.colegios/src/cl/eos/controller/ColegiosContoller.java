package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.ColegiosModel;
import cl.eos.persistence.models.Colegio;

public class ColegiosContoller extends AController {

	public ColegiosContoller() {
		
	}

	@Override
	public void initialize() {
		model = new ColegiosModel();
		model.findAll(Colegio.class, this);
	}
}
