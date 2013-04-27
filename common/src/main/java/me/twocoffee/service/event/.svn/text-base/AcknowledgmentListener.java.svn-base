package me.twocoffee.service.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.twocoffee.entity.Account;
import me.twocoffee.entity.Content;
import me.twocoffee.entity.Message;
import me.twocoffee.entity.Message.CatalogId;
import me.twocoffee.entity.Message.MessageAction;
import me.twocoffee.entity.Message.MessageFrom;
import me.twocoffee.entity.Message.MessageType;
import me.twocoffee.entity.Notification;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.BadgeService;
import me.twocoffee.service.ContentService;
import me.twocoffee.service.MessageService;
import me.twocoffee.service.NotificationService;
import me.twocoffee.service.PushService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class AcknowledgmentListener implements
		ApplicationListener<AcknowledgmentEvent> {
	private final static Logger logger = LoggerFactory
			.getLogger(AcknowledgmentListener.class);
	@Autowired
	AccountService accountService;

	@Autowired
	BadgeService badgeService;

	@Autowired
	NotificationService notificationService;

	@Autowired
	MessageService messageService;

	@Autowired
	PushService pushService;

	@Autowired
	ContentService contentService;

	@Override
	public void onApplicationEvent(AcknowledgmentEvent event) {

		Account targetAccount = accountService.getById(event
				.getTargetAccountId());// 好友account
		Account sourceAccount = accountService.getById(event.getAccountId());
		if (targetAccount == null || sourceAccount == null) {
			logger.info("Target Account[{}] not found！",
					event.getTargetAccountId());
			return;
		}

		Content content = contentService.getById(event.getContentId());
		Notification notification = new Notification();// 生成系统通知并存储 notification
		notification.setKey(Notification.CollapseKey.Acknowledgment);
		badgeService.increaseBadge(targetAccount.getId(),
				notification.getKey().getBadgeName());// 统计badge数字，相应字段badge数++
		// int badge = badgeService.getBadge(
		// targetAccount.getId(), notification.getKey()
		// .getBadgeName());// badge 数字
		notification.setAccountId(targetAccount.getId());
		notification.setAction(notification.getKey().getAction());
		notification.setStatus(Notification.Status.normal);
		// notification.setSubject(badge + "条好友消息");
		notification.setSubject(sourceAccount.getName() + "感谢你分享的:\""
				+ content.getTitle() + "\"");
		notificationService.save(notification);

		Message message = new Message();
		message.setAccountId(targetAccount.getId());
		message.setCatalogId(CatalogId.Acknowledgment);
		message.setMessageType(MessageType.Acknowledgment);
		message.setSubject("感谢你分享的:\"" + content.getTitle() + "\"");
		Map<String, Object> attribute = new HashMap<String, Object>();
		attribute.put("id", event.getAccountId());
		message.setAttribute(attribute);
		MessageFrom from = new MessageFrom();
		from.setAccountId(sourceAccount.getId());
		from.setName(sourceAccount.getName());
		from.setAvatar(sourceAccount.getAvatarForMap());
		message.setFrom(from);

		Message.MessageAction viewAction = new Message.MessageAction("查看",
				Message.MessageAction.MessageActionValue.ViewAcknowledgment);
		viewAction.setHref(Notification.CollapseKey.Acknowledgment.getAction());
		Message.MessageAction ignoreAction = new Message.MessageAction("忽略",
				Message.MessageAction.MessageActionValue.IgnoreFriendRequest);
		List<MessageAction> actions = new ArrayList<MessageAction>(2);
		actions.add(ignoreAction);
		actions.add(viewAction);
		message.setAction(actions);
		messageService.save(message);
		pushService.push(notification, targetAccount.getId());// push消息

	}

}
