package cl.eos.interfaces.controller;

import java.util.List;

import cl.eos.interfaces.entity.IEntity;
import cl.eos.interfaces.model.IModel;
import cl.eos.interfaces.view.IView;

public interface IController {

	/**
	 * Este metodo debe ser implementado por todos los controladores.
	 * Se puede utilizar para pedir la informacion desde el repositorio.
	 */
	void initialize();
	
	
	void add(IEntity entity);

	/**
	 * Metodo que envia al almacenamiento el objeto. El controlador debe liberar
	 * de inmediato el control y notificar que se encuetra en el estado grabando
	 * a todas sus vistas. El controlador queda a la espera del repositorio de
	 * la notificacion del resultado de la grabacion.
	 * 
	 * @param entity
	 *            Objeto a grabar.
	 */
	void save(IEntity entity);

	/**
	 * Metodo que elimina el objeto. El controlador debe liberar de inmediato el
	 * control y notificar que se encuetra en el estado eliminando a todas sus
	 * vistas. El controlador queda a la espera del repositorio de la
	 * notificacion del resultado del borrado.
	 * 
	 * @param entity
	 *            Objeto a grabar.
	 */
	void delete(IEntity entity);

	void delete(List<? extends IEntity> entity);
	
	/**
	 * Metodo que selecciona el objeto. El controlador debe notificar que el
	 * objeto esta seleccionado a todas la vistas.
	 * 
	 * @param entity
	 *            Objeto a grabar.
	 */

	void select(IEntity entity);

	/**
	 * Obtiene el modelo.
	 * 
	 * @return Modelo de la funcion.
	 */
	IModel getModel();

	/**
	 * Agrega una vista, no se valida si la vista esta repetida.
	 * 
	 * @param view
	 *            La vista a ser agregada.
	 */
	void addView(IView view);

	/**
	 * Elimina la vista.
	 * 
	 * @param view
	 *            Vista a ser eliminada.
	 */
	void removeView(IView view);

	/**
	 * Obtiene la lista
	 * 
	 * @return
	 */
	List<IView> getViews();

	void notifyFound(IEntity entity);

	void notifySaved(IEntity entity);

	void notifyDeleted(IEntity entity);
	
	void notifyChangeStatus(Object status);
}
