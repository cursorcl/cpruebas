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

    void addView(IView view);

    IController getController();

    /**
     * Obtiene el nombre del la vista.
     * 
     * @return Nombre de la vista.
     */
    String getName();

    Object getPanel();

    Dimension getPreferredSize();

    /**
     * Obtiene el titulo de la vista.
     * 
     * @return Titulo de la vista.
     */
    String getTitle();

    List<IView> getViews();

    /**
     * Notificacion de cambios de estado que puedan interesar a la HMI. La HMI
     * espera uno o m�s estados especificos.
     * 
     * @param status
     *            El estado informado.
     */
    void onChangeStatus(Object status);

    /**
     * Donde el controlador notifica a las vistas que le ha llegado informacion
     * desde la persistencia.
     * 
     * @param list
     */
    void onDataArrived(List<Object> list);

    void onDeleted(IEntity entity);

    /**
     * Metodo llamado por el controlador cuando se ha enviado a eliminar el
     * objeto.
     * 
     * @param entity
     *            El objeto que fue enviado a eliminar.
     */
    void onDeleting(IEntity entity);

    void onError(String error);

    /**
     * Respuesta a la solicitud de busquda.
     * 
     * @param entity
     */
    void onFound(IEntity entity);

    /**
     * Metodo llamado por el controlador cuando se completado la grabacion del
     * objeto.
     * 
     * @param entity
     *            El objeto que fue grabado.
     */
    void onSaved(IEntity entity);

    /**
     * Metodo llamado por el controlador cuando se ha enviado a grabar el
     * objeto.
     * 
     * @param entity
     *            El objeto que fue enviado a grabar.
     */
    void onSaving(IEntity entity);

    void onSelected(IEntity entity);

    void removeView(IView view);

    void setController(IController controller);

    void setPanel(Object panel);

    /**
     * Establece el titulo de la vista.
     * 
     * @param title
     *            El titulo a establecer.
     */
    void setTitle(String title);
    

}
