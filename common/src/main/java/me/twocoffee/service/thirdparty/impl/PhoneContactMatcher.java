/**
 * 
 */
package me.twocoffee.service.thirdparty.impl;

import me.twocoffee.entity.Account;
import me.twocoffee.entity.Contact;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;

import org.springframework.stereotype.Component;

/**
 * @author momo
 * 
 */
@Component
public class PhoneContactMatcher extends AbstractContactMatcher {

	public Account findFriend(Contact contact) {

		for (String mobile : contact.getMobile()) {
			Account account = thirdpartyService.getByThirdParty(
					getThirdPartyType(), mobile.replaceAll("\\(|\\)|-", ""), true);

			if (account != null) {
				return account;
			}
		}
		return null;
	}

	@Override
	protected Contact getFriendContact(String friendId, Contact contact) {
		return thirdpartyService.getContact(friendId, getThirdPartyType(),
				contact.getMobile());

	}

	@Override
	protected ThirdPartyType getThirdPartyType() {
		return ThirdPartyType.Phone;
	}
}
