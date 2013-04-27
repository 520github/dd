/**
 * 
 */
package me.twocoffee.service.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author momo
 * 
 */
// TODO:SNS new
public class ThirdPartyUserAuthorizationEvent extends ApplicationEvent {

	private boolean first;
	
	private boolean refreshToken = true;

	/**
	 * @return the refreshToken
	 */
	public boolean isRefreshToken() {
		return refreshToken;
	}

	/**
	 * @param refreshToken the refreshToken to set
	 */
	public void setRefreshToken(boolean refreshToken) {
		this.refreshToken = refreshToken;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ThirdPartyUserAuthorizationEvent(Object source, boolean first) {
		super(source);
		this.first = first;
		this.refreshToken = true;
	}
	
	public ThirdPartyUserAuthorizationEvent(Object source, boolean first, boolean refreshToken) {
		super(source);
		this.first = first;
		this.refreshToken = refreshToken;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

}
