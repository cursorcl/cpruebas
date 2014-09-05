package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.AlumnosModel;
import cl.eos.persistence.models.Alumno;
import cl.eos.persistence.models.Colegio;
import cl.eos.persistence.models.Curso;

public class AlumnosContoller extends AController {

	public AlumnosContoller() {
		
	}

	@Override
	public void initialize() {
		model = new AlumnosModel();
		model.findAll(Alumno.class, this);
		model.findAll(Colegio.class, this);
		model.findAll(Curso.class, this);		
	}
}
