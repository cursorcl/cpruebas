package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.PruebasModel;
import cl.eos.persistence.models.Asignatura;
import cl.eos.persistence.models.Curso;
import cl.eos.persistence.models.NivelEvaluacion;
import cl.eos.persistence.models.Profesor;
import cl.eos.persistence.models.Prueba;
import cl.eos.persistence.models.TipoPrueba;

public class PruebasController extends AController {

	public PruebasController() {
		
	}

	@Override
	public void initialize() {
		model = new PruebasModel();
		model.findAll(Prueba.class, this);
		model.findAll(TipoPrueba.class, this);
		model.findAll(Profesor.class, this);
		model.findAll(Curso.class, this);
		model.findAll(Asignatura.class, this);
		model.findAll(NivelEvaluacion.class, this);
	}

	
	
}
