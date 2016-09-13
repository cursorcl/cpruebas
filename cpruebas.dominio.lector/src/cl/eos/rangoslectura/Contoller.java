package cl.eos.rangoslectura;

import cl.eos.imp.controller.AController;
import cl.eos.persistence.models.RangosLectura;

public class Contoller extends AController {

	public Contoller() {
		
	}

	@Override
	public void initialize() {
		model = new Model();
		model.findAll(RangosLectura.class, this);
	}
}
