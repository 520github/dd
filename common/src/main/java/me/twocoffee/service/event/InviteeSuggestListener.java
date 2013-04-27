/**
 * 
 */
package me.twocoffee.service.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.twocoffee.common.constant.SystemConstant;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.Message;
import me.twocoffee.entity.Message.CatalogId;
import me.twocoffee.entity.Message.MessageAction;
import me.twocoffee.entity.Message.MessageFrom;
import me.twocoffee.entity.Message.MessageType;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author momo
 * 
 */
@Component
public class InviteeSuggestListener implements
		ApplicationListener<InviteeSuggestEvent> {

	@Autowired
	private MessageService messageService;
	@Autowired
	private AccountService accountService;

	@Override
	public void onApplicationEvent(InviteeSuggestEvent event) {
		Account a = (Account) event.getSource();
		Account target = accountService.getById(a.getInviter());

		if (target == null) {
			return;
		}
		/*
		 * Notification notification = new Notification();// 生成系统通知并存储 //
		 * notification
		 * notification.setKey(Notification.CollapseKey.FriendMessage);
		 * badgeService.increaseBadge(a.getId(),
		 * notification.getKey().getBadgeName());// 统计badge数字，相应字段badge数++
		 * 
		 * int badge = badgeService.getBadge( a.getId(), notification.getKey()
		 * .getBadgeName());// badge 数字
		 * 
		 * notification.setAccountId(a.getId());
		 * notification.setAction(notification.getKey().getAction());
		 * notification.setStatus(Notification.Status.normal);
		 * notification.setSubject(badge + "条好友消息");
		 * notificationService.save(notification);
		 */
		/**
		 * construct Message and save
		 */
		Message message = new Message();
		Message.MessageFrom from = new MessageFrom();
		Account recommand = accountService.getRecommendAccount();

		if (recommand != null) {
			from.setAccountId(recommand.getId());
			from.setAvatar(recommand.getAvatarForMap());
			from.setName(recommand.getName());

		} else {
			Map<String, String> avatarMap = new HashMap<String, String>();
			String path = "http://" + SystemConstant.domainName + "/"
					+ LocaleContextHolder.getLocale().toString()
					+ "/images/";

			avatarMap.put("small", path + "avatar-35px.png");
			avatarMap.put("medium", path + "avatar-100px.png");
			avatarMap.put("large", path + "avatar-150px.png");
			from.setAvatar(avatarMap);
			from.setName("多多助手");
		}
		message.setFrom(from);
		message.setAccountId(a.getId());
		message.setCatalogId(CatalogId.Friend);
		message.setMessageType(MessageType.InviteeSuggest);
		// message.setSubject(sourceAccount.getName() +
		// " 加入了多多，是否现在就要加他为好友？");
		message.setSubject("Hi，欢迎加入“多多”，你的邀请者是“" + target.getName()
				+ "”，要加他为多多好友么，添加好友后就可以彼此很方便的分享有用的东东啦。");

		Map<String, Object> attribute = new HashMap<String, Object>();
		attribute.put("accountId", target.getId());
		attribute.put("avatar", target.getAvatarForMap());
		attribute.put("name", target.getName());
		message.setAttribute(attribute);
		Message.MessageAction accpetAction = new Message.MessageAction("同意",
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
		// pushService.push(notification, a.getId());// push消息
	}

}
