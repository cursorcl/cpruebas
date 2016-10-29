package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.NivelEvaluacionModel;
import cl.eos.persistence.models.NivelEvaluacion;

public class NivelEvaluacionController extends AController {
    public NivelEvaluacionController() {
    }

    @Override
    public void initialize() {
        model = new NivelEvaluacionModel();
        model.findAll(NivelEvaluacion.class, this);
    }

}
