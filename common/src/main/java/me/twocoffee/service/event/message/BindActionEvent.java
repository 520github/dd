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
public class BindActionEvent extends ApplicationEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6183832098393987788L;
	
	/**
	 * 
	 */
	private Message message;
	
	public BindActionEvent(Object source) {
		super(source);
	}

	public BindActionEvent(Object source, Message message) {
		super(source);
		this.message = message;
	}

	public Message getMessage() {
		return message;
	}



}
