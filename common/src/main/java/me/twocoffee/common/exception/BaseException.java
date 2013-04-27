package me.twocoffee.common.exception;


/**
 * Basic class of all exception thrown by the system.
 * @author laiss
 *
 */
public class BaseException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a new BaseException. 
	 */
	public BaseException(String message) {
		super(message);
	}

	/**
	 * Constructs a new BaseException.
	 * @param cause root exception.
	 */
	public BaseException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Constructs a new BaseException.
	 * @param message description of current exception
	 * @param cause root exception.
	 */
	public BaseException(String message, Throwable cause) {
		super(message, cause);
	}

}
