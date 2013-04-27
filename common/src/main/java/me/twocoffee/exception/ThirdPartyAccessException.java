/**
 * 
 */
package me.twocoffee.exception;

/**
 * @author momo
 * 
 */
// TODO:SNS new
public class ThirdPartyAccessException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ThirdPartyAccessException() {
		super();
	}

	public ThirdPartyAccessException(Exception e) {
		super(e);
	}

	public ThirdPartyAccessException(String msg) {
		super(msg);
	}

	public ThirdPartyAccessException(String msg, Exception e) {
		super(msg, e);
	}

}
