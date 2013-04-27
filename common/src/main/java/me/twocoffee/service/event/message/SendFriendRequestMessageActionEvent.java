package me.twocoffee.service.event.message;

import me.twocoffee.entity.Message;

import org.springframework.context.ApplicationEvent;

/**
 * 消息action：发送好友请求
 * 
 * @author chongf
 * 
 */
public class SendFriendRequestMessageActionEvent extends ApplicationEvent {

	private static final long serialVersionUID = -907495221449667891L;

	private Message message;

	public SendFriendRequestMessageActionEvent(Object source) {
		super(source);
	}

	public SendFriendRequestMessageActionEvent(Object source, Message message) {
		super(source);
		this.message = message;
	}

	public Message getMessage() {
		return message;
	}

}
