package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.CursosModel;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.Ciclo;

public class CursosContoller extends AController {

	public CursosContoller() {
		
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		model = new CursosModel();
		model.findAll(Curso.class, this);
		model.findAll(Ciclo.class, this);
	}
}
