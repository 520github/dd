/**
 * 
 */
package me.twocoffee.service.event;

import me.twocoffee.entity.Account;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;

import org.springframework.context.ApplicationEvent;

/**
 * @author momo
 * 
 */
public class ThirdPartyFriendsSuggestEvent extends ApplicationEvent {

	private ThirdPartyType type;

	private Account target;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ThirdPartyFriendsSuggestEvent(Object source, ThirdPartyType type,
			Account target) {

		super(source);
		this.type = type;
		this.target = target;
	}

	public Account getTarget() {
		return target;
	}

	public ThirdPartyType getType() {
		return type;
	}

	public void setTarget(Account target) {
		this.target = target;
	}

	public void setType(ThirdPartyType type) {
		this.type = type;
	}

}
