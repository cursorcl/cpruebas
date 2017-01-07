package cl.eos.controller;

import cl.eos.imp.controller.AController;
import cl.eos.model.HabilidadesModel;
import cl.eos.restful.tables.R_Habilidad;

public class HabilidadesContoller extends AController {

    public HabilidadesContoller() {

    }

    @Override
    public void initialize() {
        model = new HabilidadesModel();
        model.findAll(R_Habilidad.class, this);
    }
}
