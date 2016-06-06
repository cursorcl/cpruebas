package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.NivelEvaluacionModel;

public class NivelEvaluacionControllerEdicion extends AController{

	public NivelEvaluacionControllerEdicion() {
	
	}
	
	@Override
	public void initialize() {
		model = new NivelEvaluacionModel();
	}
	

}
