package me.twocoffee.common.exception;

public class InitialException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8534182146185251482L;

    public InitialException(Exception e) {
    	super(e);
    }
    
    public InitialException(String message) {
        super(message);
    }
    
    public InitialException(String message, Throwable cause) {
    	super(message, cause);
    }
}
