package me.twocoffee.exception;

import java.util.Map;

public class RegisterFaildException extends Exception {

	private int errorCode;
	private Map fieldErrors;
	
	
	public RegisterFaildException(int errorCode,Map fieldErrors){
		this.errorCode=errorCode;
		this.fieldErrors=fieldErrors;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	
	public Map getFieldErrors() {
		return fieldErrors;
	}
}
