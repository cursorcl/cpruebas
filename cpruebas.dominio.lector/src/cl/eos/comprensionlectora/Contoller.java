package cl.eos.comprensionlectora;

import cl.eos.imp.controller.AController;
import cl.eos.persistence.models.ComprensionLectora;

public class Contoller extends AController {

	public Contoller() {
		
	}

	@Override
	public void initialize() {
		model = new Model();
		model.findAll(ComprensionLectora.class, this);
	}
}
