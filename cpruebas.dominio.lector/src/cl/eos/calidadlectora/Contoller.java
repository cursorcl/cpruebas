package cl.eos.calidadlectora;

import cl.eos.imp.controller.AController;
import cl.eos.persistence.models.CalidadLectora;

public class Contoller extends AController {

	public Contoller() {
		
	}

	@Override
	public void initialize() {
		model = new Model();
		model.findAll(CalidadLectora.class, this);
	}
}
