package me.twocoffee.service.event.message;

import me.twocoffee.entity.FriendLog;
import me.twocoffee.entity.Message;
import me.twocoffee.service.FriendService;
import me.twocoffee.service.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 监听 消息action：忽略好友请求
 * 
 * @author chongf
 * 
 */
@Component
public class IgnoreFriendRequestMessageActionListener implements
		ApplicationListener<IgnoreFriendRequestMessageActionEvent> {

	@Autowired
	private FriendService friendService;

	@Autowired
	private MessageService messageService;

	@Override
	public void onApplicationEvent(IgnoreFriendRequestMessageActionEvent event) {
		Message message = event.getMessage();
		String friendId = message.getAccountId();

		if (message.getAttribute().get("id") != null
				&& !"".equals(message.getAttribute().get("id"))) {

			String accountId = message.getAttribute().get("id").toString();
			messageService.delete(message.getId());
			FriendLog log = friendService.getLogByAccountIdAndFriendId(
					accountId,
					friendId);

			if (log == null)
				return;

			friendService.rejectFriendRequest(log.getId(), friendId);

		} else if (message.getAttribute().get("accountId") != null
				&& !"".equals(message.getAttribute().get("accountId"))) {

			String accountId = message.getAttribute().get("accountId")
					.toString();

			messageService.delete(message.getId());
			FriendLog log = friendService.getLogByAccountIdAndFriendId(
					accountId,
					friendId);

			if (log == null)
				return;

			friendService.rejectFriendRequest(log.getId(), friendId);
		}

	}

}
