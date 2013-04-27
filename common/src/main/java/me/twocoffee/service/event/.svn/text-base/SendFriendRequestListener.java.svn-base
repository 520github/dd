package me.twocoffee.service.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.twocoffee.entity.Account;
import me.twocoffee.entity.Account.RoleType;
import me.twocoffee.entity.Account.Status;
import me.twocoffee.entity.FriendLog;
import me.twocoffee.entity.Message;
import me.twocoffee.entity.Message.CatalogId;
import me.twocoffee.entity.Message.MessageAction;
import me.twocoffee.entity.Message.MessageType;
import me.twocoffee.entity.Notification;
import me.twocoffee.entity.Notification.CollapseKey;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.BadgeService;
import me.twocoffee.service.FriendService;
import me.twocoffee.service.MessageService;
import me.twocoffee.service.NotificationService;
import me.twocoffee.service.PushService;
import me.twocoffee.service.event.message.AcceptFriendRequestMessageActionListener;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 发送好友请求监听器
 * 
 * @author chongf
 * 
 */
@Component
public class SendFriendRequestListener implements
		ApplicationListener<SendFriendRequestEvent> {

	private final static Logger logger = LoggerFactory
			.getLogger(AcceptFriendRequestMessageActionListener.class);

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
	public void onApplicationEvent(SendFriendRequestEvent event) {
		Account targetAccount = accountService.getById(event
				.getTargetAccountId());
		
		Account sourceAccount = accountService.getById(event.getAccountId());
		
		if (targetAccount == null || sourceAccount == null) {
			logger.info("Target Account[{}] not found！",
					event.getTargetAccountId());
		
			return;
		}
		
		if (targetAccount.hasRole(RoleType.Public)) {
			friendService.addFriendWithoutLog(sourceAccount.getId(), targetAccount.getId());
			sourceAccount.addStat(Status.KnowDuoduoPublic);
			accountService.save(sourceAccount);
			return;
		}
		/**
		 * construct notification and save
		 */
		Notification notification = new Notification();// 生成系统通知并存储 notification
		notification.setKey(Notification.CollapseKey.FriendMessage);
		badgeService.increaseBadge(targetAccount.getId(),
				notification.getKey().getBadgeName());// 统计badge数字，相应字段badge数++

		notification.setAccountId(targetAccount.getId());
		notification.setAction(notification.getKey().getAction());
		notification.setStatus(Notification.Status.normal);
		//notification.setSubject(badge + "条好友消息");
		notification.setSubject(sourceAccount.getName()+"申请加你为好友!");
		notificationService.save(notification);
		/**
		 * construct Message and save
		 */

		FriendLog friendLog = friendService.getLogByAccountIdAndFriendId(
				event.getAccountId(), targetAccount.getId());
		Message message = new Message();
		message.setAccountId(targetAccount.getId());
		message.setCatalogId(CatalogId.Friend);
		message.setMessageType(MessageType.FriendRequest);
		// message.setSubject(sourceAccount.getName() + " 加入了多多，是否现在就要加他为好友？");
		message.setSubject("申请加你为好友，是否同意？");
		Map<String, Object> attribute = new HashMap<String, Object>();
		attribute.put("id", event.getAccountId());
		if (friendLog != null) {
			attribute.put("message", friendLog.getPostscript());
		}
		message.setAttribute(attribute);
		Message.MessageAction accpetAction = new Message.MessageAction("同意",
				Message.MessageAction.MessageActionValue.AcceptFriendRequest);
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
		logger.warn("share to friend===== targetAccount"
				+ targetAccount.getId());

		pushService.push(notification, targetAccount.getId());// push消息
		String thirdPartyType = event.getThirdType();

		if (StringUtils.isNotBlank(thirdPartyType)) {
			Map<String, String> attributes = new HashMap<String, String>();
			attributes.put("thirdpartyType", thirdPartyType);
			accountService
					.handlerMessage(sourceAccount.getId(),
							Message.MessageType.FriendInvitation,
							CollapseKey.FriendMessage, attributes);
		}
	}
}
