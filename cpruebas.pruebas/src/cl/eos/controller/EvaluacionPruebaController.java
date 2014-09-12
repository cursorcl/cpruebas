package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.EvaluacionPruebaModel;
import cl.eos.persistence.models.EvaluacionPrueba;

public class EvaluacionPruebaController extends AController {

	@Override
	public void initialize() {
		model = new EvaluacionPruebaModel();
		model.findAll(EvaluacionPrueba.class, this);
	}

}
