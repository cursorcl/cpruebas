package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.ProfesoresModel;
import cl.eos.persistence.models.Profesor;

public class ProfesoresContoller extends AController {

    public ProfesoresContoller() {

    }

    @Override
    public void initialize() {
        model = new ProfesoresModel();
        model.findAll(Profesor.class, this);
    }
}
