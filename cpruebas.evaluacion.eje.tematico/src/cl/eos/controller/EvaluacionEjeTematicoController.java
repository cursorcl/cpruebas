package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.EvaluacionEjeTematicoModel;
import cl.eos.restful.tables.R_EvaluacionEjetematico;

public class EvaluacionEjeTematicoController extends AController {

    public EvaluacionEjeTematicoController() {

    }

    @Override
    public void initialize() {
        model = new EvaluacionEjeTematicoModel();
        model.findAll(R_EvaluacionEjetematico.class, this);
    }
}
