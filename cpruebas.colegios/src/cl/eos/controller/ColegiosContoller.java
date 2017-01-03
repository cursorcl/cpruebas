package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.ColegiosModel;
import cl.eos.restful.tables.R_Colegio;
import cl.eos.restful.tables.R_TipoColegio;

public class ColegiosContoller extends AController {

    public ColegiosContoller() {

    }

    @Override
    public void initialize() {
        model = new ColegiosModel();
        model.findAll(R_Colegio.class, this);
        model.findAll(R_TipoColegio.class, this);
    }
}
