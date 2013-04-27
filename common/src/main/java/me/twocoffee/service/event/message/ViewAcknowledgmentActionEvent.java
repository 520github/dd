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
public class ViewAcknowledgmentActionEvent extends ApplicationEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1923044548625992692L;
	/**
	 * 
	 */
	private Message message;

	public ViewAcknowledgmentActionEvent(Object source) {
		super(source);
	}
	
	public ViewAcknowledgmentActionEvent(Object source, Message message) {
		super(source);
		this.message = message;
	}
	
	public Message getMessage() {
		return message;
	}
}
