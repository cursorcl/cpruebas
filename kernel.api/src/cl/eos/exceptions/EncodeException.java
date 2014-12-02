package cl.eos.exceptions;

/**
 * @author eosorio
 */
public class EncodeException extends Exception
{

    /**
     * Atributo de la clase de nombre EncodeException.java que contiene el valor de .....
     */
    private static final long serialVersionUID = 3577511396909895976L;

    /**
     * Constructor de la clase EncodeException.
     */
    public EncodeException()
    {
    }

    /**
     * Constructor de la clase EncodeException.
     */
    public EncodeException( final String message, final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace )
    {
        super( message, cause, enableSuppression, writableStackTrace );
    }

    /**
     * Constructor de la clase EncodeException.
     */
    public EncodeException( final String message, final Throwable cause )
    {
        super( message, cause );
    }

    /**
     * Constructor de la clase EncodeException.
     */
    public EncodeException( final String message )
    {
        super( message );
    }

    /**
     * Constructor de la clase EncodeException.
     */
    public EncodeException( final Throwable cause )
    {
        super( cause );
    }

}
