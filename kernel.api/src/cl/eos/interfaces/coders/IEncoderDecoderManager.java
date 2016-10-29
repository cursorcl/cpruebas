package cl.eos.interfaces.coders;

import java.util.List;

import cl.eos.exceptions.DecodeException;
import cl.eos.exceptions.EncodeException;
import cl.eos.interfaces.entity.IEntity;

/**
 * Administrador de codificacion y decodificacion de objetos.
 * <p>
 * Este administrador carga todos los codificadores y decodificadores que se
 * registran en el sistema.
 * <p>
 * Al ser requerida una codificacion/decodificacion este busca en su lista el
 * codificador/decodificador correspondiente.
 *
 * @author eosorio
 */
public interface IEncoderDecoderManager {
    void addDecoder(IDecoder decoder);

    void addDecoder(String clase, IDecoder decoder);

    void addEncoder(IEncoder encoder);

    void addEncoder(String clase, IEncoder encoder);

    /**
     * Decodifica una lista de objetos y los transforma en una lista de
     * entidades.
     * 
     * @param lstObject
     *            Lista de objetos a transformar.
     * @return Lista de IEntity transformados.
     * @throws DecodeException
     *             En el caso que falle una transformacion.
     */
    List<IEntity> decode(List<Object> lstObject) throws DecodeException;

    /**
     * Decodifica la tira de bytes, retornando el objeto asociado a ésta.
     * 
     * @param object
     *            Objeto que contiene la informacion a transformar.
     * @return Objeto asociado a la tira de bytes.
     * @throws DecodeException
     *             En el caso que falle la transformacion.
     */
    IEntity decode(Object object) throws DecodeException;

    /**
     * Codifica un objeto y lo transforma en una tira de bytes.
     *
     * @param object
     *            Objeto a ser codificado.
     * @return Tira de bytes asociada a la codificacion.
     * @throws EncodeException
     *             En el caso que falle la transformacion.
     */
    Object encode(IEntity object) throws EncodeException;

    /**
     * Codifica una lista de entidades y las transforma en una lista de objetos.
     * 
     * @param lstObject
     *            Lista de IEntoty a transformar.
     * @return Lista de objetos transformados.
     * @throws EncodeException
     *             En el caso que falle una transformacion.
     */
    List<Object> encode(List<IEntity> lstObject) throws EncodeException;

    void removeDecoder(IDecoder decoder);

    void removeEncoder(IEncoder encoder);
}
