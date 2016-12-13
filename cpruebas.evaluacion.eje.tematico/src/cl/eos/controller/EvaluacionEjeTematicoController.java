package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.EvaluacionEjeTematicoModel;
import cl.eos.persistence.models.SEvaluacionEjeTematico;

public class EvaluacionEjeTematicoController extends AController {

    public EvaluacionEjeTematicoController() {

    }

    @Override
    public void initialize() {
        model = new EvaluacionEjeTematicoModel();
        model.findAll(SEvaluacionEjeTematico.class, this);
    }
}
