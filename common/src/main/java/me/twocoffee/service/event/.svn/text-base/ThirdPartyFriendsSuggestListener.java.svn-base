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
import me.twocoffee.entity.Notification;
import me.twocoffee.entity.ThirdPartyProfile;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.BadgeService;
import me.twocoffee.service.MessageService;
import me.twocoffee.service.NotificationService;
import me.twocoffee.service.PushService;
import me.twocoffee.service.thirdparty.ThirdpartyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author momo
 * 
 */
@Component
public class ThirdPartyFriendsSuggestListener implements
	ApplicationListener<ThirdPartyFriendsSuggestEvent> {

    @Autowired
    private BadgeService badgeService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private final AccountService accountService = null;
    @Autowired
    private PushService pushService;

    @Autowired
    private ThirdpartyService thirdpartyService;

    @Override
    public void onApplicationEvent(ThirdPartyFriendsSuggestEvent event) {
	String aid = (String) event.getSource();
	ThirdPartyType type = event.getType();
	Account target = event.getTarget();
	Account account = accountService.getById(aid);
	ThirdPartyProfile profile = thirdpartyService.getByAccountId(aid, type);

	if (target == null || account == null) {
	    return;
	}
	Notification notification = new Notification();// 生成系统通知并存储
						       // notification
	notification.setKey(Notification.CollapseKey.FriendMessage);
	badgeService.increaseBadge(target.getId(),
		notification.getKey().getBadgeName());// 统计badge数字，相应字段badge数++

	// int badge = badgeService.getBadge(
	// target.getId(), notification.getKey()
	// .getBadgeName());// badge 数字

	notification.setAccountId(target.getId());
	notification.setAction(notification.getKey().getAction());
	notification.setStatus(Notification.Status.normal);
	// notification.setSubject(badge + "条好友消息");
	switch (type) {
	case Phone:
	    notification.setSubject("你的通讯录好友，\"" + profile.getNickName()
		    + "\"刚刚加入了多多 。点击进入消息列表。");
	    break;
	case Weibo:
	    notification.setSubject("你的新浪微博互粉好友，\"" + profile.getNickName()
		    + "\"刚刚加入了多多 。点击进入消息列表。");
	    break;
	case Renren:
	    notification.setSubject("你的人人好友，\"" + profile.getNickName()
		    + "\"刚刚加入了多多 。点击进入消息列表。");
	    break;
	case Tencent:
	    notification.setSubject("你的腾讯微博好友，\"" + profile.getNickName()
		    + "\"刚刚加入了多多 。点击进入消息列表。");
	default:
	    break;
	}
	notificationService.save(notification);
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
	message.setAccountId(target.getId());
	message.setCatalogId(CatalogId.Friend);
	message.setMessageType(MessageType.ThirdpartyFriendSuggest);
	// message.setSubject(sourceAccount.getName() +
	// " 加入了多多，是否现在就要加他为好友？");
	StringBuffer buffer = new StringBuffer();
	switch (type) {
	case Phone:
	    buffer.append("你的通讯录好友\"");
	    break;
	case Weibo:
	    buffer.append("你的新浪微博互粉好友\"");
	    break;
	case Renren:
	    buffer.append("你的人人好友\"");
	    break;
	case Tencent:
	    buffer.append("你的腾讯微博好友\"");
	default:
	    break;
	}
	buffer.append(profile.getNickName()).append("\"刚刚加入了多多，要加TA为多多好友么？");
	message.setSubject(buffer.toString());
	Map<String, Object> attribute = new HashMap<String, Object>();
	attribute.put("accountId", aid);
	attribute.put("avatar", account.getAvatarForMap());
	attribute.put("name", account.getName());
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
	pushService.push(notification, target.getId());// push消息
    }

}
