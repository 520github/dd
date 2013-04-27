/**
 * 
 */
package me.twocoffee.exception;

import java.util.Map;

/**
 * @author momo
 * 
 */
public class UpdateAccountFailedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final int errorCode;

	private final Map<String, String> fieldErrors;

	public UpdateAccountFailedException(int errorCode,
			Map<String, String> fieldErrors) {
		this.errorCode = errorCode;
		this.fieldErrors = fieldErrors;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public Map<String, String> getFieldErrors() {
		return fieldErrors;
	}

}
