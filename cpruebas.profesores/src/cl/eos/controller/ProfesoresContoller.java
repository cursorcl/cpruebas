package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.ProfesoresModel;
import cl.eos.restful.tables.R_Profesor;

public class ProfesoresContoller extends AController {

    public ProfesoresContoller() {

    }

    @Override
    public void initialize() {
        model = new ProfesoresModel();
        model.findAll(R_Profesor.class, this);
    }
}
