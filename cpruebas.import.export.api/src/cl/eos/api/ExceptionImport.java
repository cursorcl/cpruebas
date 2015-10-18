package cl.eos.api;

public class ExceptionImport extends Exception {

	private static final long serialVersionUID = 1L;

	public ExceptionImport() {
		super();
	}

	public ExceptionImport(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ExceptionImport(String message, Throwable cause) {
		super(message, cause);
	}

	public ExceptionImport(String message) {
		super(message);
	}

	public ExceptionImport(Throwable cause) {
		super(cause);
	}
	
}
