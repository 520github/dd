package me.twocoffee.service.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.twocoffee.entity.Account;
import me.twocoffee.entity.Message;
import me.twocoffee.entity.Message.CatalogId;
import me.twocoffee.entity.Message.MessageAction;
import me.twocoffee.entity.Message.MessageType;
import me.twocoffee.entity.Notification;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.BadgeService;
import me.twocoffee.service.MessageService;
import me.twocoffee.service.NotificationService;
import me.twocoffee.service.PushService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 邀请好友监听器
 * 
 * @author chongf
 * 
 */
@Component
public class AcceptInviteListener implements
		ApplicationListener<AcceptInviteEvent> {
	private final static Logger logger = LoggerFactory
			.getLogger(AcceptInviteListener.class);

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

	@Override
	public void onApplicationEvent(AcceptInviteEvent event) {
		Account targetAccount = accountService.getById(event
				.getTargetAccountId());
		if (targetAccount == null) {
			logger.info("Target Account[{}] not found！",
					event.getTargetAccountId());
			return;
		}
		/**
		 * construct notification and save
		 */
		Notification notification = new Notification();// 生成系统通知并存储 notification
		notification.setKey(Notification.CollapseKey.friend);
		badgeService.increaseBadge(targetAccount.getId(),
				notification.getKey().getBadgeName());// 统计badge数字，相应字段badge数++
		int badge = badgeService.getBadge(
				targetAccount.getId(), notification.getKey()
						.getBadgeName());// badge 数字
		notification.setAccountId(targetAccount.getId());
		notification.setAction(notification.getKey().getAction());
		notification.setStatus(Notification.Status.normal);
		notification.setSubject(badge + "条好友邀请消息");
		notificationService.save(notification);
		/**
		 * construct Message and save
		 */

		Message message = new Message();
		message.setAccountId(targetAccount.getId());
		message.setCatalogId(CatalogId.Friend);
		message.setMessageType(MessageType.FriendSuggest);
		message.setSubject("加入了多多，是否现在就要加TA为好友?");
		Map<String, Object> attribute = new HashMap<String, Object>();
		attribute.put("id", event.getAccountId());
		message.setAttribute(attribute);
		Message.MessageAction accpetAction = new Message.MessageAction("+好友",
				Message.MessageAction.MessageActionValue.SendFriendRequest);
		Message.MessageAction ignoreAction = new Message.MessageAction("忽略",
				Message.MessageAction.MessageActionValue.IgnoreFriendRequest);
		List<MessageAction> actions = new ArrayList<MessageAction>(2);
		actions.add(accpetAction);
		actions.add(ignoreAction);
		message.setAction(actions);
		messageService.save(message);
		/**
		 * push notification
		 */
		pushService.push(notification, targetAccount.getId());// push消息
	}

}
