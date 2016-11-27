package cl.eos.exception;

public class ExceptionImport extends Throwable {

    private static final long serialVersionUID = 1L;

    public ExceptionImport(String errores) {
        super(errores);
    }
}
