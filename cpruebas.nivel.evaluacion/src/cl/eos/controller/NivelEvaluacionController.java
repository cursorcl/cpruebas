package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.NivelEvaluacionModel;
import cl.eos.restful.tables.R_NivelEvaluacion;

public class NivelEvaluacionController extends AController {
    public NivelEvaluacionController() {
    }

    @Override
    public void initialize() {
        model = new NivelEvaluacionModel();
        model.findAll(R_NivelEvaluacion.class, this);
    }

}
