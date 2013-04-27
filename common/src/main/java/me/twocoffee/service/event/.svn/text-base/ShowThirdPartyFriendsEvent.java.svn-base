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
public class ShowThirdPartyFriendsEvent extends ApplicationEvent {

	private final ThirdPartyType thirdPartyType;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ShowThirdPartyFriendsEvent(Object source, ThirdPartyType type) {
		super(source);
		thirdPartyType = type;
	}

	public ThirdPartyType getThirdPartyType() {
		return thirdPartyType;
	}

}
