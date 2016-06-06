package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.EvaluacionEjeTematicoModel;
import cl.eos.persistence.models.EvaluacionEjeTematico;

public class EvaluacionEjeTematicoController extends AController {

	public EvaluacionEjeTematicoController() {
		
	}

	@Override
	public void initialize() {
		model = new EvaluacionEjeTematicoModel();
		model.findAll(EvaluacionEjeTematico.class, this);
	}
}
