package cl.eos.calidadlectora;

import cl.eos.imp.controller.AController;
import cl.eos.restful.tables.R_DL_CalidadLectora;

public class CalidadLectoraController extends AController {

    public CalidadLectoraController() {

    }

    @Override
    public void initialize() {
        model = new Model();
        model.findAll(R_DL_CalidadLectora.class, this);
    }
}
