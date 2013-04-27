package me.twocoffee.service.event.message;

import me.twocoffee.entity.FriendLog;
import me.twocoffee.entity.FriendMailConfig;
import me.twocoffee.entity.Message;
import me.twocoffee.service.FriendMailConfigService;
import me.twocoffee.service.FriendService;
import me.twocoffee.service.MessageService;
import me.twocoffee.service.thirdparty.ThirdPartyContactMatcherService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 监听 消息action：接受好友请求
 * 
 * @author chongf
 * 
 */
@Component
public class AcceptFriendRequestMessageActionListener implements
		ApplicationListener<AcceptFriendRequestMessageActionEvent> {

	@Autowired
	private FriendService friendService;
	@Autowired
	private MessageService messageService;

	@Autowired
	private FriendMailConfigService friendMailConfigService;

	@Autowired
	private ThirdPartyContactMatcherService contactMatcherService;

	@Override
	public void onApplicationEvent(AcceptFriendRequestMessageActionEvent event) {
		Message message = event.getMessage();
		String friendId = message.getAccountId();
		String accountId = message.getAttribute().get("id").toString();

		FriendLog log = friendService.getLogByAccountIdAndFriendId(accountId,
				friendId);
		if (log == null) {
			messageService.delete(message.getId());
			return;
		}

		friendService.acceptFriendRequest(log.getId(), friendId);

		// 增加好友白名单
		FriendMailConfig friendMailConfig = new FriendMailConfig();
		friendMailConfig.setBlocked(false);
		friendMailConfig.setAccountId(accountId);
		friendMailConfig.setFriendId(friendId);
		friendMailConfigService.saveFriendMailConfig(friendMailConfig);

		friendMailConfig.setId(null);
		friendMailConfig.setAccountId(friendId);
		friendMailConfig.setFriendId(accountId);
		friendMailConfigService.saveFriendMailConfig(friendMailConfig);

		// 删除这个消息
		messageService.delete(message.getId());
		contactMatcherService.rematchThirdpartyFriends(accountId, friendId);
	}

}
