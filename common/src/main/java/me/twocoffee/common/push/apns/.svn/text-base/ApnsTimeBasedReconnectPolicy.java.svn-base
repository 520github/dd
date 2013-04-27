package me.twocoffee.common.push.apns;

import java.util.concurrent.TimeUnit;

import com.notnoop.apns.ReconnectPolicy;

/**
 * Apns的重连策略
 * 
 * @author chongf
 * 
 */
public class ApnsTimeBasedReconnectPolicy implements ReconnectPolicy {

	private long lastRunning = 0;
	private final long period;

	public ApnsTimeBasedReconnectPolicy() {
		period = 15;
	}

	public ApnsTimeBasedReconnectPolicy(long period, TimeUnit timeUnit) {
		this.period = timeUnit.convert(period, TimeUnit.SECONDS);
	}

	@Override
	public ReconnectPolicy copy() {
		return null;
	}

	public long getPeriod() {
		return period;
	}

	@Override
	public void reconnected() {
		lastRunning = System.currentTimeMillis();
	}

	@Override
	public boolean shouldReconnect() {
		return System.currentTimeMillis() - lastRunning > period;
	}

}