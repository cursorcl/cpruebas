package cl.eos.rangoslectura;

import cl.eos.imp.controller.AController;
import cl.eos.persistence.models.SRangosLectura;

public class RangosLecturaContoller extends AController {

    public RangosLecturaContoller() {

    }

    @Override
    public void initialize() {
        model = new Model();
        model.findAll(SRangosLectura.class, this);
    }
}
