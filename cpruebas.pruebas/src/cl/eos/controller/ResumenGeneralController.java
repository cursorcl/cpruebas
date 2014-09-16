package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.ResumenGeneralModel;
import cl.eos.persistence.models.EvaluacionPrueba;

public class ResumenGeneralController extends AController {

	@Override
	public void initialize() {
		model = new ResumenGeneralModel();
		model.findAll(EvaluacionPrueba.class, this);
	}
	

}
