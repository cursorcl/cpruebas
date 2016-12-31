package cl.eos.comprensionlectora;

import cl.eos.imp.controller.AController;
import cl.eos.persistence.models.SComprensionLectora;

public class ComprensionLectoraController extends AController {

    public ComprensionLectoraController() {

    }

    @Override
    public void initialize() {
        model = new Model();
        model.findAll(SComprensionLectora.class, this);
    }
}
