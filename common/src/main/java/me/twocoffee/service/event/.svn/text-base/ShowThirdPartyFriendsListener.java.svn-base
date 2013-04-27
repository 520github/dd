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
import me.twocoffee.entity.ThirdPartyProfile;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;
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
public class ShowThirdPartyFriendsListener implements
	ApplicationListener<ShowThirdPartyFriendsEvent> {

    @Autowired
    private MessageService messageService;

    @Autowired
    private AccountService accountService;

    @Override
    public void onApplicationEvent(ShowThirdPartyFriendsEvent event) {

	if (event.getThirdPartyType() != null) {

	    switch (event.getThirdPartyType()) {
	    case Phone:
		Account account = (Account) event.getSource();
		String subject = "你还没有添加通讯录好友，添加通讯录好友，就可以与好友一起分享有用的东东，畅玩多多啦，马上去添加吧。";
		String displayAction = "去添加";
		String href = "iicoffee://view/thirdparty/phone/friend";
		sendEvent(account.getId(), event.getThirdPartyType(), subject,
			displayAction, href);
		break;
	    case Weibo:
		ThirdPartyProfile thirdPartyProfile_weibo = (ThirdPartyProfile) event
			.getSource();

		String subject_weibo = "你还没有添加微博互粉好友，添加微博互粉好友，就可以与好友一起分享有用的东东，畅玩多多啦，马上去添加吧。";
		String displayAction_weibo = "去添加";
		String href_weibo = "iicoffee://view/thirdparty/weibo/friend";
		sendEvent(thirdPartyProfile_weibo.getAccountId(),
			event.getThirdPartyType(), subject_weibo,
			displayAction_weibo, href_weibo);
		break;
	    case Renren:
		ThirdPartyProfile thirdPartyProfile_renren = (ThirdPartyProfile) event
			.getSource();

		String subject_renren = "你还没有添加人人好友，添加人人好友，就可以与好友一起分享有用的东东，畅玩多多啦，马上去添加吧。";
		String displayAction_renren = "去添加";
		String href_renren = "iicoffee://view/thirdparty/renren/friend";
		sendEvent(thirdPartyProfile_renren.getAccountId(),
			event.getThirdPartyType(), subject_renren,
			displayAction_renren, href_renren);
		break;
	    case Tencent:
		ThirdPartyProfile thirdPartyProfile_tencent = (ThirdPartyProfile) event
			.getSource();

		String subject_tencent = "你还没有添加腾讯微博好友，添加腾讯微博好友，就可以与好友一起分享有用的东东，畅玩多多啦，马上去添加吧。";
		String displayAction_tencent = "去添加";
		String href_tencent = "iicoffee://view/thirdparty/tencent/friend";
		sendEvent(thirdPartyProfile_tencent.getAccountId(),
			event.getThirdPartyType(), subject_tencent,
			displayAction_tencent, href_tencent);
		break;
	    default:
		break;
	    }
	}
    }

    private void sendEvent(String accountId, ThirdPartyType type,
	    String subject, String displayAction, String href) {
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
	message.setAccountId(accountId);
	message.setCatalogId(CatalogId.Friend);
	message.setMessageType(MessageType.FriendInvitation);
	Map<String, Object> attribute = new HashMap<String, Object>();
	attribute.put("thirdpartyType", type);
	message.setAttribute(attribute);
	message.setSubject(subject);
	Message.MessageAction showFriendMessage = new Message.MessageAction(
		displayAction,
		Message.MessageAction.MessageActionValue.ShowThirdpartyFriend);

	showFriendMessage.setHref(href);
	List<MessageAction> actions = new ArrayList<MessageAction>(1);
	actions.add(showFriendMessage);
	message.setAction(actions);
	messageService.save(message);
    }

}
