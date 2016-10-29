package cl.eos.exceptions;

/**
 * @author eosorio
 */
public class DecodeException extends Exception {

    /**
     * Atributo de la clase de nombre DecodeException.java que contiene el valor
     * de .....
     */
    private static final long serialVersionUID = 8405208673433883591L;

    /**
     * Constructor de la clase DecodeException.
     */
    public DecodeException() {
    }

    /**
     * Constructor de la clase DecodeException.
     */
    public DecodeException(final String message) {
        super(message);
    }

    /**
     * Constructor de la clase DecodeException.
     */
    public DecodeException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor de la clase DecodeException.
     */
    public DecodeException(final String message, final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * Constructor de la clase DecodeException.
     */
    public DecodeException(final Throwable cause) {
        super(cause);
    }

}
