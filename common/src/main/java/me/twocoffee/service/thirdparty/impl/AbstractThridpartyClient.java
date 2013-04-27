package me.twocoffee.service.thirdparty.impl;

import me.twocoffee.common.auth.AuthClient;
import me.twocoffee.dao.ContactDao;
import me.twocoffee.dao.ThirdPartyContentDao;
import me.twocoffee.dao.ThirdPartyProfileDao;
import me.twocoffee.entity.Contact;
import me.twocoffee.entity.ThirdPartyContent;
import me.twocoffee.entity.ThirdPartyProfile;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;
import me.twocoffee.service.entity.ThirdPartyPostMessage;
import me.twocoffee.service.thirdparty.ThirdPartyClient;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractThridpartyClient implements ThirdPartyClient {

    @Autowired
    protected ThirdPartyContentDao thirdPartyContentDao;

    @Autowired
    protected ThirdPartyProfileDao thirdProfileDao;

    @Autowired
    protected ContactDao contactDao;

    @Autowired
    protected AuthClient authClient;

    @Override
    public int getFriendNumber(ThirdPartyProfile profile) {
	return 1;
    }

    @Override
    public String getContentDisplay(ThirdPartyContent content) {
	// String vm =
	// getTemplate("src/test/resources/thirdparty_content/weibo/favorite.vm");
	String vm = "<div><img src=\"$avatar\">$nickName </div><div><p>$text</p></div><div><img src=\"$image\"></div>";
	vm = vm.replace("$avatar", content.getContentUser().getAvatar());
	vm = vm.replace("$nickName", content.getContentUser().getName());

	if (content.getContent() != null) {
	    vm = vm.replace("$text", content.getContent());
	}

	if (content.getAttachments() != null
		&& content.getAttachments().size() > 0) {

	    if (StringUtils.isNotBlank(content.getAttachments().get(0)
		    .getNormalImage())) {

		vm = vm.replace("$image", content.getAttachments().get(0)
			.getNormalImage());

	    } else {
		vm = vm.replace("$image", "");
	    }

	} else {
	    vm = vm.replace("$image", "");
	}
	String replyvm = "<div>$reply_nickName</div><div><p>$reply_text</p></div><div><img src=\"$reply_image\"></div>";

	if (content.getReply() != null) {
	    replyvm = replyvm.replace("$reply_nickName", content.getReply()
		    .getContentUser().getName());

	    replyvm = replyvm.replace("$reply_text", content.getReply()
		    .getContent());

	    if (content.getReply().getAttachments() != null
		    && content.getReply().getAttachments().size() > 0) {

		if (StringUtils.isNotBlank(content.getReply().getAttachments()
			.get(0).getNormalImage())) {

		    replyvm = replyvm.replace("$reply_image", content
			    .getReply().getAttachments().get(0)
			    .getNormalImage());

		} else {
		    replyvm = replyvm.replace("$reply_image", "");
		}

	    } else {
		replyvm = replyvm.replace("$reply_image", "");
	    }
	    vm += replyvm;
	}
	return vm;
    }

    @Override
    public String getSummaryDisplay(ThirdPartyContent content) {
	// String vm =
	// getTemplate("src/test/resources/thirdparty_content/weibo/favorite.vm");
	String vm = "<div>$nickName </div><div><p>$text</p></div>";
	vm = vm.replace("$nickName", content.getContentUser().getName());
	vm = vm.replace("$text", content.getContent());
	String replyvm = "<div>$reply_nickName</div> <div><p>$reply_text</p></div>";

	if (content.getReply() != null) {
	    replyvm = replyvm.replace("$reply_nickName", content.getReply()
		    .getContentUser().getName());

	    replyvm = replyvm.replace("$reply_text", content.getReply()
		    .getContent());

	    vm += replyvm;
	}
	return vm;
    }

    protected String appandUser(ThirdPartyProfile profile,
	    ThirdPartyPostMessage message) {

	StringBuffer buffer = new StringBuffer(message.getContent());

	if (message.getUserId() != null && message.getUserId().size() > 0) {

	    for (String t : message.getUserId()) {
		Contact c = getThirdPartyFriend(profile.getAccountId(),
			profile.getAccountType(), t);

		if (c != null) {
		    buffer.insert(0, "@").insert(1, c.getName() + " ");
		}
	    }
	}
	return buffer.toString();
    }

    protected Contact getThirdPartyFriend(String accountId,
	    ThirdPartyType type,
	    String friendUid) {

	return contactDao.get(accountId, type, friendUid);
    }

}
