package cl.eos.calidadlectora;

import cl.eos.imp.controller.AController;
import cl.eos.persistence.models.SCalidadLectora;

public class CalidadLectoraController extends AController {

    public CalidadLectoraController() {

    }

    @Override
    public void initialize() {
        model = new Model();
        model.findAll(SCalidadLectora.class, this);
    }
}
