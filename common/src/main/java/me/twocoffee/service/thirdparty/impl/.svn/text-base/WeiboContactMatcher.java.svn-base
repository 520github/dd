/**
 * 
 */
package me.twocoffee.service.thirdparty.impl;

import me.twocoffee.entity.Account;
import me.twocoffee.entity.Contact;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;

import org.springframework.stereotype.Component;

/**
 * @author wenjian
 * 
 */
@Component
public class WeiboContactMatcher extends AbstractContactMatcher {

	public Account findFriend(Contact contact) {
		return thirdpartyService.getByThirdParty(contact.getThirdPartyType(),
				contact.getUid(), true);
	}

	@Override
	protected Contact getFriendContact(String friendId, Contact contact) {
		return thirdpartyService.getContact(friendId, getThirdPartyType(),
				contact.getUid());

	}

	@Override
	protected ThirdPartyType getThirdPartyType() {
		return ThirdPartyType.Weibo;
	}
}
