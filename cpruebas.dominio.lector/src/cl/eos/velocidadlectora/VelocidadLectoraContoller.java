package cl.eos.velocidadlectora;

import cl.eos.imp.controller.AController;
import cl.eos.restful.tables.R_DL_VelocidadLectora;

public class VelocidadLectoraContoller extends AController {

    public VelocidadLectoraContoller() {

    }

    @Override
    public void initialize() {
        model = new Model();
        model.findAll(R_DL_VelocidadLectora.class, this);
    }
}
