package cl.eos.comprensionlectora;

import cl.eos.imp.controller.AController;
import cl.eos.persistence.models.SComprensionLectora;

public class Contoller extends AController {

    public Contoller() {

    }

    @Override
    public void initialize() {
        model = new Model();
        model.findAll(SComprensionLectora.class, this);
    }
}
