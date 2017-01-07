package cl.eos.comprensionlectora;

import cl.eos.imp.controller.AController;
import cl.eos.restful.tables.R_DL_ComprensionLectora;

public class ComprensionLectoraController extends AController {

    public ComprensionLectoraController() {

    }

    @Override
    public void initialize() {
        model = new Model();
        model.findAll(R_DL_ComprensionLectora.class, this);
    }
}
