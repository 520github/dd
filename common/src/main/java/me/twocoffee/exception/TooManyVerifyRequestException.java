package me.twocoffee.exception;

public class TooManyVerifyRequestException extends Exception {
	private int retryAfterInSeconds=60;
	
	/**
	 * 两次验证之间最小时间间隔
	 */
	private int minVerifyIntervalInMinutes = 1;

	/**
	 * 在某一时间段(verifyTimesLimitCycleInHours)内验证的次数限制
	 */
	private int maxVerifyTimes = 10;

	
	/**
	 * 验证次数限制的时间段
	 */
	private int verifyTimesLimitCycleInHours = 24;

	
	public TooManyVerifyRequestException(){
		
	}

	public int getMinVerifyIntervalInMinutes() {
		return minVerifyIntervalInMinutes;
	}

	public void setMinVerifyIntervalInMinutes(int minVerifyIntervalInMinutes) {
		this.minVerifyIntervalInMinutes = minVerifyIntervalInMinutes;
	}

	public int getMaxVerifyTimes() {
		return maxVerifyTimes;
	}

	public void setMaxVerifyTimes(int maxVerifyTimes) {
		this.maxVerifyTimes = maxVerifyTimes;
	}

	public int getVerifyTimesLimitCycleInHours() {
		return verifyTimesLimitCycleInHours;
	}

	public void setVerifyTimesLimitCycleInHours(int verifyTimesLimitCycleInHours) {
		this.verifyTimesLimitCycleInHours = verifyTimesLimitCycleInHours;
	}

	public TooManyVerifyRequestException(int retryAfterInSeconds) {
		this.retryAfterInSeconds = retryAfterInSeconds;
	}

	public int getRetryAfterInSeconds() {
		return retryAfterInSeconds;
	}
}
