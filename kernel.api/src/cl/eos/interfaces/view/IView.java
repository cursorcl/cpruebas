package cl.eos.interfaces.view;

import java.awt.Dimension;
import java.util.List;

import cl.eos.interfaces.controller.IController;
import cl.eos.interfaces.entity.IEntity;

/**
 * Interface que debe implemetar todas las vistas.. Viene siendo una vista de
 * lectura.
 * 
 * @author eosorio
 */
public interface IView {

	/**
	 * Obtiene el nombre del la vista.
	 * 
	 * @return Nombre de la vista.
	 */
	String getName();

	/**
	 * Obtiene el titulo de la vista.
	 * 
	 * @return Titulo de la vista.
	 */
	String getTitle();

	/**
	 * Establece el titulo de la vista.
	 * 
	 * @param title
	 *            El titulo a establecer.
	 */
	void setTitle(String title);

	void addView(IView view);

	void removeView(IView view);

	List<IView> getViews();

	void setController(IController controller);

	IController getController();

	/**
	 * Notificacion de cambios de estado que puedan interesar a la HMI. La HMI
	 * espera uno o más estados especificos.
	 * 
	 * @param status
	 *            El estado informado.
	 */
	void onChangeStatus(Object status);

	/**
	 * Metodo llamado por el controlador cuando se ha enviado a grabar el
	 * objeto.
	 * 
	 * @param entity
	 *            El objeto que fue enviado a grabar.
	 */
	void onSaving(IEntity entity);

	/**
	 * Metodo llamado por el controlador cuando se completado la grabacion del
	 * objeto.
	 * 
	 * @param entity
	 *            El objeto que fue grabado.
	 */
	void onSaved(IEntity entity);

	/**
	 * Metodo llamado por el controlador cuando se ha enviado a eliminar el
	 * objeto.
	 * 
	 * @param entity
	 *            El objeto que fue enviado a eliminar.
	 */
	void onDeleting(IEntity entity);

	void onDeleted(IEntity entity);

	void onSelected(IEntity entity);
	
	
	/**
	 * Respuesta a la solicitud de busquda.
	 * 
	 * @param entity
	 */
	void onFound(IEntity entity);
	
	
	/**
	 * Donde el controlador notifica a las vistas que le ha llegado informacion desde 
	 * la persistencia.
	 * @param list
	 */
	void onDataArrived(List<IEntity> list);
	
	Dimension getPreferredSize();

	Object getPane();
}
