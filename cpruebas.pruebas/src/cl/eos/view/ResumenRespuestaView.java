package cl.eos.view;

import cl.eos.imp.view.AFormView;
import cl.eos.interfaces.entity.IEntity;
import cl.eos.persistence.models.EvaluacionPrueba;

public class ResumenRespuestaView extends AFormView {

	public ResumenRespuestaView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onFound(IEntity entity) {
		if (entity instanceof EvaluacionPrueba) {
			EvaluacionPrueba evaluacionPrueba = (EvaluacionPrueba) entity;
		}
	}
}
