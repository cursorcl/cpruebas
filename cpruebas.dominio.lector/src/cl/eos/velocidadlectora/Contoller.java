package cl.eos.velocidadlectora;

import cl.eos.imp.controller.AController;
import cl.eos.persistence.models.VelocidadLectora;

public class Contoller extends AController {

    public Contoller() {

    }

    @Override
    public void initialize() {
        model = new Model();
        model.findAll(VelocidadLectora.class, this);
    }
}
