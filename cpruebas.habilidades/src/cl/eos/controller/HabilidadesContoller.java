package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.HabilidadesModel;
import cl.eos.persistence.models.Habilidad;

public class HabilidadesContoller extends AController {

	public HabilidadesContoller() {
		
	}

	@Override
	public void initialize() {
		model = new HabilidadesModel();
		model.findAll(Habilidad.class, this);
	}
}
