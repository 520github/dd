/**
 * 
 */
package me.twocoffee.service.event.message;

import me.twocoffee.entity.Message;

import org.springframework.context.ApplicationEvent;

/**
 * @author momo
 * 
 */
public class ShowThirdpartyFriendActionEvent extends ApplicationEvent {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7525149125905585151L;
	/**
	 * 
	 */
	private Message message;

	public ShowThirdpartyFriendActionEvent(Object source) {
		super(source);
	}
	
	public ShowThirdpartyFriendActionEvent(Object source, Message message) {
		super(source);
		this.message = message;
	}
	
	public Message getMessage() {
		return message;
	}
}
