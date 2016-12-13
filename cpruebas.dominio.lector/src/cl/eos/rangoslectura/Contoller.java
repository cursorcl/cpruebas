package cl.eos.rangoslectura;

import cl.eos.imp.controller.AController;
import cl.eos.persistence.models.SRangosLectura;

public class Contoller extends AController {

    public Contoller() {

    }

    @Override
    public void initialize() {
        model = new Model();
        model.findAll(SRangosLectura.class, this);
    }
}
