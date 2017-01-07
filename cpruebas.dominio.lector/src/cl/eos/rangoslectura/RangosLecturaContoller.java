package cl.eos.rangoslectura;

import cl.eos.imp.controller.AController;
import cl.eos.imp.model.RangosLectura;

public class RangosLecturaContoller extends AController {

    public RangosLecturaContoller() {

    }

    @Override
    public void initialize() {
        model = new Model();
        model.findAll(RangosLectura.class, this);
    }
}
