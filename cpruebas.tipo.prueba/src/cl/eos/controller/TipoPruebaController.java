package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.TipoPruebaModel;
import cl.eos.persistence.models.TipoPrueba;


public class TipoPruebaController extends AController  {

	
	@Override
	public void initialize() {
		model = new TipoPruebaModel();
		model.findAll(TipoPrueba.class, this);
	}
}
