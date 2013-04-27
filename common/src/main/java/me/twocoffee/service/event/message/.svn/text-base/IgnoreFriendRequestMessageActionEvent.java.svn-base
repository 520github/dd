package me.twocoffee.service.event.message;

import me.twocoffee.entity.Message;

import org.springframework.context.ApplicationEvent;

/**
 * 消息action：忽略好友请求
 * 
 * @author chongf
 * 
 */
public class IgnoreFriendRequestMessageActionEvent extends ApplicationEvent {

	private static final long serialVersionUID = -7362508126189154056L;

	private Message message;

	public IgnoreFriendRequestMessageActionEvent(Object source) {
		super(source);
	}

	public IgnoreFriendRequestMessageActionEvent(Object source, Message message) {
		super(source);
		this.message = message;
	}

	public Message getMessage() {
		return message;
	}

}
