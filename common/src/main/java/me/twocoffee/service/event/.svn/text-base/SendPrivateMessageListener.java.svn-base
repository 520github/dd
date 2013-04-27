package me.twocoffee.service.event;

import me.twocoffee.entity.Account;
import me.twocoffee.entity.Content;
import me.twocoffee.entity.Notification;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.BadgeService;
import me.twocoffee.service.ContentService;
import me.twocoffee.service.NotificationService;
import me.twocoffee.service.PushService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class SendPrivateMessageListener implements
		ApplicationListener<SendPrivateMessageEvent> {

	private final static Logger logger = LoggerFactory
			.getLogger(SendPrivateMessageListener.class);

	@Autowired
	private AccountService accountService;
	@Autowired
	private BadgeService badgeService;
	// @Autowired
	// private MessageService messageService;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private PushService pushService;
	@Autowired
	private ContentService contentService;

	// @Autowired
	// private RepositoryService repositoryService;
	@Override
	public void onApplicationEvent(final SendPrivateMessageEvent event) {

		if (event.isSendCommentNotify()) {
			Account targetAccount = accountService.getById(event
					.getTargetAccountId());

			Account sourceAccount = accountService
					.getById(event.getAccountId());

			if (targetAccount == null || sourceAccount == null) {
				logger.info("Target Account[{}] not found！",
						event.getTargetAccountId());

				return;
			}

			String contentId = event.getSourceSession().getContentId();
			Content content = contentService.getById(contentId);

			/**
			 * construct notification and save
			 */
			Notification notification = new Notification();// 生成系统通知并存储
															// notification
			notification.setKey(Notification.CollapseKey.Chat);
			badgeService.increaseBadge(targetAccount.getId(), notification
					.getKey().getBadgeName());// 统计badge数字，相应字段badge数++

			notification.setAccountId(targetAccount.getId());
			notification.setAction(notification.getKey().getAction()
					+ event.getSourceSession().getContentId() + ":"
					+ event.getAccountId());
			notification.setStatus(Notification.Status.normal);
			// notification.setSubject(badge + "条新评论");
			String subject = "";
			
			if (StringUtils.isBlank(content.getTitle())) {
				subject = sourceAccount.getName() + "评论了内容，点击查看详情!";
			
			} else { 
				subject = sourceAccount.getName() + "评论了《"
					+ content.getTitle() + "》，点击查看详情!";
			
			}
			notification.setSubject(subject);
			notificationService.save(notification);
			pushService.push(notification, targetAccount.getId());// push消息}
		}
	}
}