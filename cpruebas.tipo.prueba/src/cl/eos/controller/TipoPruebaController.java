package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.TipoPruebaModel;
import cl.eos.restful.tables.R_TipoPrueba;

public class TipoPruebaController extends AController {

    public TipoPruebaController() {
    }

    @Override
    public void initialize() {
        model = new TipoPruebaModel();
        model.findAll(R_TipoPrueba.class, this);
    }
}
