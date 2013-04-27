package me.twocoffee.service.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.twocoffee.entity.Account;
import me.twocoffee.entity.Account.RoleType;
import me.twocoffee.entity.Account.Status;
import me.twocoffee.entity.Friend;
import me.twocoffee.entity.Message;
import me.twocoffee.entity.Message.CatalogId;
import me.twocoffee.entity.Message.MessageAction;
import me.twocoffee.entity.Message.MessageType;
import me.twocoffee.entity.Notification;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.BadgeService;
import me.twocoffee.service.FriendService;
import me.twocoffee.service.MessageService;
import me.twocoffee.service.NotificationService;
import me.twocoffee.service.PushService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class AcceptFriendListener implements
		ApplicationListener<AcceptFriendEvent> {
	private final static Logger logger = LoggerFactory
			.getLogger(AcceptFriendListener.class);

	@Autowired
	private AccountService accountService;
	@Autowired
	private BadgeService badgeService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private PushService pushService;
	@Autowired
	private FriendService friendService;

	@Override
	public void onApplicationEvent(AcceptFriendEvent event) {

		Account targetAccount = accountService.getById(event
				.getTargetAccountId());// 好友account
		Account sourceAccount = accountService.getById(event.getAccountId());
		if (targetAccount == null || sourceAccount == null) {
			logger.info("Target Account[{}] not found！",
					event.getTargetAccountId());
			return;
		}
		if (sourceAccount.hasRole(RoleType.Public)) {
			targetAccount.addStat(Status.KnowDuoduoPublic);
			accountService.save(targetAccount);
		}
		/**
		 * construct notification and save
		 */
		Notification notification = new Notification();// 生成系统通知并存储 notification
		notification.setKey(Notification.CollapseKey.FriendMessage);
		badgeService.increaseBadge(sourceAccount.getId(),
				notification.getKey().getBadgeName());// 统计badge数字，相应字段badge数++
		// int badge = badgeService.getBadge(
		// sourceAccount.getId(), notification.getKey()
		// .getBadgeName());// badge 数字
		notification.setAccountId(sourceAccount.getId());
		notification.setAction(notification.getKey().getAction());
		notification.setStatus(Notification.Status.normal);
		// notification.setSubject(badge + "条好友消息");
		notification.setSubject(targetAccount.getName() + "同意了你的好友申请!");
		notificationService.save(notification);
		/**
		 * construct Message and save
		 */

		Friend friend = friendService.getByAccountIdAndFriendId(
				event.getAccountId(), targetAccount.getId());

		Message message = new Message();
		message.setAccountId(sourceAccount.getId());
		message.setCatalogId(CatalogId.Friend);
		message.setMessageType(MessageType.FriendRequestFeedback);

		Map<String, Object> attribute = new HashMap<String, Object>();
		if (friend != null && friend.getRemarkName() != null) {
			attribute.put("alias", friend.getRemarkName());
		}

		message.setSubject("添加成功！与TA开始无限分享，赶快试试吧!");

		attribute.put("id", targetAccount.getId());
		attribute.put("friendId", sourceAccount.getId());
		message.setAttribute(attribute);
		Message.MessageAction ignoreAction = new Message.MessageAction("忽略",
				Message.MessageAction.MessageActionValue.IgnoreFriendRequest);
		List<MessageAction> actions = new ArrayList<MessageAction>(2);
		actions.add(ignoreAction);
		message.setAction(actions);
		messageService.deleteByAccountIdAndFriendId(message.getAccountId(),
				message.getAttribute().get("id").toString());
		messageService.save(message);
		/**
		 * push notification
		 */
		pushService.push(notification, sourceAccount.getId());// push消息

	}

}
