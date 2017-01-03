package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.AsignaturasModel;
import cl.eos.restful.tables.R_Asignatura;

public class AsignaturasContoller extends AController {

    public AsignaturasContoller() {

    }

    @Override
    public void initialize() {
        model = new AsignaturasModel();
        model.findAll(R_Asignatura.class, this);
    }
}
