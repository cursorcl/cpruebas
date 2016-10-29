package cl.eos.exceptions;

/**
 * Excepciones para el proyecto.
 * 
 * @author curso_000
 */
public class CPruebasException extends Exception {

    private static final long serialVersionUID = 1L;

    public CPruebasException() {
        super();
    }

    public CPruebasException(String message) {
        super(message);
    }

    public CPruebasException(String message, Throwable cause) {
        super(message, cause);
    }

    public CPruebasException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public CPruebasException(Throwable cause) {
        super(cause);
    }

}
