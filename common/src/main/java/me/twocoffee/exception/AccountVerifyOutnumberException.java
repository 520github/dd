package me.twocoffee.exception;

/**
 * 帐号验证超过最大次数
 * 
 */
public class AccountVerifyOutnumberException extends Exception {
	private int retryAfterInSeconds;
	
	public AccountVerifyOutnumberException(int retryAfterInSeconds){
		this.retryAfterInSeconds=retryAfterInSeconds;
	}

	public int getRetryAfterInSeconds() {
		return retryAfterInSeconds;
	}
}
