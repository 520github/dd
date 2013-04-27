package me.twocoffee.exception;

/**
 * 帐户验证过于频繁
 * 
 */
public class TooShortVerifyIntervalException extends Exception {
	
	private int retryAfterInSeconds;
	
	public TooShortVerifyIntervalException(int retryAfterInSeconds){
		this.retryAfterInSeconds=retryAfterInSeconds;
	}

	public int getRetryAfterInSeconds() {
		return retryAfterInSeconds;
	}
}
