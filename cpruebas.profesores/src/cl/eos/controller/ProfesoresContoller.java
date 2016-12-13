package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.ProfesoresModel;
import cl.eos.persistence.models.SProfesor;

public class ProfesoresContoller extends AController {

    public ProfesoresContoller() {

    }

    @Override
    public void initialize() {
        model = new ProfesoresModel();
        model.findAll(SProfesor.class, this);
    }
}
