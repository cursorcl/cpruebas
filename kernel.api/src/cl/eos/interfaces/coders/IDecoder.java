package cl.eos.interfaces.coders;

import cl.eos.interfaces.entity.IEntity;

/**
 * Interface que deben cumplir las herramientas que realizan la codificacion y
 * decodificacion.
 *
 * @author eosorio
 */
public interface IDecoder {
    IEntity decode(Object bBuffer);

    String decodeTo();

}
