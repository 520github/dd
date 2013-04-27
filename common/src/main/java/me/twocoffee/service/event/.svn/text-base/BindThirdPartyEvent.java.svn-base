/**
 * 
 */
package me.twocoffee.service.event;

import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;

import org.springframework.context.ApplicationEvent;

/**
 * @author momo
 * 
 */
public class BindThirdPartyEvent extends ApplicationEvent {

	private ThirdPartyType type;
	
	public ThirdPartyType getType() {
		return type;
	}

	public void setType(ThirdPartyType type) {
		this.type = type;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BindThirdPartyEvent(Object source) {
		super(source);
	}

	public BindThirdPartyEvent(Object account, ThirdPartyType type) {
		super(account);
		this.type = type;
	}

}
