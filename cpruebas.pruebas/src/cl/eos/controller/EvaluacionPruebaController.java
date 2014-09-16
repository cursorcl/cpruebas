package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.EvaluacionPruebaModel;
import cl.eos.persistence.models.Prueba;

public class EvaluacionPruebaController extends AController {

	@Override
	public void initialize() {
		model = new EvaluacionPruebaModel();
		model.findAll(Prueba.class, this);
	}
	

}
