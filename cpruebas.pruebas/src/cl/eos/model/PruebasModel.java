package cl.eos.model;

import java.util.HashMap;
import java.util.Map;

import cl.eos.imp.model.AModel;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.Prueba;
import cl.eos.provider.persistence.PersistenceServiceFactory;

public class PruebasModel extends AModel {

	public PruebasModel() {
	}

  @Override
  public void update(IEntity entity) {

    if (entity instanceof Prueba) {
      Prueba prueba = (Prueba) entity;
      Map<String, Object> parameters = new HashMap<String, Object>();
      parameters.put("pruebaId", prueba.getId());
      PersistenceServiceFactory.getPersistenceService().executeUpdate("Formas.deleteByPrueba",
          parameters);
      PersistenceServiceFactory.getPersistenceService().executeUpdate(
          "RespuestasEsperadasPrueba.deleteByPrueba", parameters);
    }
    super.update(entity);
  }
	
	
	
}
