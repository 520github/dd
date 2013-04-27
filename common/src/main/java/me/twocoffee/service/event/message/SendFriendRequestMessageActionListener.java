package me.twocoffee.service.event.message;

import me.twocoffee.entity.Message;
import me.twocoffee.service.FriendService;
import me.twocoffee.service.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 监听 消息action：发送好友请求
 * 
 * @author chongf
 * 
 */
@Component
public class SendFriendRequestMessageActionListener implements
		ApplicationListener<SendFriendRequestMessageActionEvent> {

	@Autowired
	private FriendService friendService;

	@Autowired
	private MessageService messageService;

	@Override
	public void onApplicationEvent(SendFriendRequestMessageActionEvent event) {
		Message message = event.getMessage();
		String accountId = message.getAccountId();

		if (message.getAttribute().get("id") != null
				&& !"".equals(message.getAttribute().get("id"))) {
			String friendId = message.getAttribute().get("id").toString();

			friendService.addFriend(accountId, friendId, "", "", "");
			messageService.delete(message.getId());
		} else if (message.getAttribute().get("accountId") != null
				&& !"".equals(message.getAttribute().get("accountId"))) {

			String friendId = message.getAttribute().get("accountId")
					.toString();
			friendService.addFriend(accountId, friendId, "", "", "");
			messageService.delete(message.getId());
		}
	}

}
