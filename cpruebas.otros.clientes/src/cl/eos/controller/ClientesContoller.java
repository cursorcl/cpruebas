package cl.eos.controller;

import java.util.HashMap;
import java.util.Map;

import cl.eos.imp.controller.AController;
import cl.eos.model.ClientesModel;
import cl.eos.persistence.models.Clientes;

public class ClientesContoller extends AController {

    public void buscarPorDireccion(String direccion) {
        final Map<String, Object> param = new HashMap<String, Object>();
        param.put("dato", direccion);
        model.find("Alumno.preguntita", param);
    }

    @Override
    public void initialize() {
        model = new ClientesModel();
        model.findAll(Clientes.class, this);
    }

}
