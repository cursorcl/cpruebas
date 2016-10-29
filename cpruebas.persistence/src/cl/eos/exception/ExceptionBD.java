package cl.eos.exception;

public class ExceptionBD extends Exception {

    private static final long serialVersionUID = -1804527174324506930L;

    public ExceptionBD() {
        super();
    }

    public ExceptionBD(String mensaje) {
        super(mensaje);
    }

    public ExceptionBD(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public ExceptionBD(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

    public ExceptionBD(Throwable arg0) {
        super(arg0);
    }

}
