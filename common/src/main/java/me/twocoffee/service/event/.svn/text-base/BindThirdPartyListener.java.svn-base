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
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.BadgeService;
import me.twocoffee.service.MessageService;
import me.twocoffee.service.NotificationService;
import me.twocoffee.service.PushService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author momo
 * 
 */
@Component
public class BindThirdPartyListener implements
	ApplicationListener<BindThirdPartyEvent> {

    @Autowired
    private MessageService messageService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private BadgeService badgeService;

    @Autowired
    private PushService pushService;

    @Override
    public void onApplicationEvent(BindThirdPartyEvent event) {
	Account account = (Account) event.getSource();

	if (event.getType() == null) {
	    bindWeibo(account);
	    bindRenren(account);

	} else if (event.getType() == ThirdPartyType.Weibo) {
	    bindWeibo(account);

	} else if (event.getType() == ThirdPartyType.Renren) {
	    bindRenren(account);

	} else if (event.getType() == ThirdPartyType.Tencent) {
	    bindTencent(account);
	}
    }

    private void bindRenren(Account account) {
	String renren_displayActionName = "绑定人人";
	String renren_subject = "绑定人人，可以将人人收藏导入“多多”中永久保存，还可以添加人人好友为好友，与朋友一起分享有用的东东。";
	String renren_href = "iicoffee://view/thirdparty/renren/authority";
	bindThirdPartyWithoutEvent(account, false, renren_subject,
		renren_displayActionName, renren_href, ThirdPartyType.Renren);
    }

    private void bindTencent(Account account) {
	String renren_displayActionName = "绑定腾讯微博";
	String renren_subject = "绑定腾讯微博，可以将腾讯微博收藏导入“多多”中永久保存，还可以添加腾讯微博互听好友为好友，与朋友一起分享有用的东东。";
	String renren_href = "iicoffee://view/thirdparty/tencent/authority";
	bindThirdPartyWithoutEvent(account, false, renren_subject,
		renren_displayActionName, renren_href, ThirdPartyType.Tencent);
    }

    private void bindWeibo(Account account) {
	String displayActionName = "绑定微博";
	String subject = "绑定新浪微博，可以将微博收藏导入“多多”中永久保存，还可以添加微博互粉好友为好友，与朋友一起分享有用的东东。";
	String href = "iicoffee://view/thirdparty/weibo/authority";
	bindThirdPartyWithoutEvent(account, false, subject, displayActionName,
		href, ThirdPartyType.Weibo);
    }

    /**
     * @param account
     * @param weibo
     * @param href
     */
    public void bindThirdPartyWithoutEvent(Account account, boolean ispush,
	    String subject, String displayActionName, String href,
	    ThirdPartyType weibo) {
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
		    + LocaleContextHolder.getLocale().toString() + "/images/";

	    avatarMap.put("small", path + "avatar-35px.png");
	    avatarMap.put("medium", path + "avatar-100px.png");
	    avatarMap.put("large", path + "avatar-150px.png");
	    from.setAvatar(avatarMap);
	    from.setName("多多助手");
	}
	message.setFrom(from);
	message.setAccountId(account.getId());
	message.setCatalogId(CatalogId.Friend);
	message.setMessageType(MessageType.Binding);
	message.setSubject(subject);
	Map<String, Object> attribute = new HashMap<String, Object>();
	attribute.put("thirdpartyType", weibo);
	message.setAttribute(attribute);
	Message.MessageAction bindMessage = new Message.MessageAction(
		displayActionName,
		Message.MessageAction.MessageActionValue.Bind);

	bindMessage.setHref(href);
	List<MessageAction> actions = new ArrayList<MessageAction>(1);
	actions.add(bindMessage);
	message.setAction(actions);
	messageService.save(message);

	if (ispush) {
	    Notification notification = new Notification();// 生成系统通知并存储
	    notification.setKey(Notification.CollapseKey.FriendMessage);
	    badgeService.increaseBadge(account.getId(), notification.getKey()
		    .getBadgeName());// 统计badge数字，相应字段b int badge =

	    badgeService.getBadge(account.getId(), notification.getKey()
		    .getBadgeName());// badge 数字

	    notification.setAccountId(account.getId());
	    notification.setAction(notification.getKey().getAction());
	    notification.setStatus(Notification.Status.normal);
	    notification.setSubject(subject);
	    notificationService.save(notification);
	    pushService.push(notification, account.getId());
	}
    }
}
