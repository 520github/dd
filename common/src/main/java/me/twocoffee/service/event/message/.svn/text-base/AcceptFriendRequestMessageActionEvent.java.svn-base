package me.twocoffee.service.event.message;

import me.twocoffee.entity.Message;

import org.springframework.context.ApplicationEvent;

/**
 * 消息action：接受好友请求
 * 
 * @author chongf
 * 
 */
public class AcceptFriendRequestMessageActionEvent extends ApplicationEvent {

	private static final long serialVersionUID = -8157675639166680819L;

	private Message message;

	public AcceptFriendRequestMessageActionEvent(Object source) {
		super(source);
	}

	public AcceptFriendRequestMessageActionEvent(Object source, Message message) {
		super(source);
		this.message = message;
	}

	public Message getMessage() {
		return message;
	}

}
