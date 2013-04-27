package me.twocoffee.service.event;

import me.twocoffee.entity.Account;
import me.twocoffee.entity.Content;
import me.twocoffee.entity.Notification;
import me.twocoffee.entity.Notification.CollapseKey;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.BadgeService;
import me.twocoffee.service.ContentService;
import me.twocoffee.service.NotificationService;
import me.twocoffee.service.PushService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class RecommendedByFriendListener implements
		ApplicationListener<RecommendedByFriendEvent> {
	private final static Logger logger = LoggerFactory
			.getLogger(RecommendedByFriendListener.class);

	@Autowired
	private AccountService accountService;
	@Autowired
	private BadgeService badgeService;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private PushService pushService;
	@Autowired
	private ContentService contentService;

	@Override
	public void onApplicationEvent(RecommendedByFriendEvent event) {
		Account targetAccount = accountService.getById(event
				.getTargetAccountId());
		Account sourceAccount = accountService.getById(event.getAccountId());
		if (targetAccount == null || sourceAccount == null) {
			logger.info("Target Account[{}] not found！",
					event.getTargetAccountId());
			return;
		}

		Content content = contentService.getById(event.getContentId());
		Notification notification = notificationService.createAndSave(CollapseKey.RecommendByFriend, targetAccount, sourceAccount, content);
		pushService.push(notification, targetAccount.getId());// push消息
	}
}
