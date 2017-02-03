package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.EjesTematicosModel;
import cl.eos.restful.tables.R_Asignatura;
import cl.eos.restful.tables.R_TipoPrueba;

public class EjesTematicosContoller extends AController {

    public EjesTematicosContoller() {

    }

    @Override
    public void initialize() {
        model = new EjesTematicosModel();
        model.findAll(R_TipoPrueba.class, this);
        model.findAll(R_Asignatura.class, this);
    }
}
