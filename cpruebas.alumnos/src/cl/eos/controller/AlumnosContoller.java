package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.AlumnosModel;
import cl.eos.persistence.models.Alumno;

public class AlumnosContoller extends AController {

	public AlumnosContoller() {
		
	}

	@Override
	public void initialize() {
		model = new AlumnosModel();
		model.findAll(Alumno.class, this);		
	}
}
