package cl.eos.rangoslectura;

import cl.eos.imp.controller.AController;
import cl.eos.restful.tables.R_DL_RangoLecturas;

public class RangosLecturaContoller extends AController {

    public RangosLecturaContoller() {

    }

    @Override
    public void initialize() {
        model = new Model();
        model.findAll(R_DL_RangoLecturas.class, this);
    }
}
