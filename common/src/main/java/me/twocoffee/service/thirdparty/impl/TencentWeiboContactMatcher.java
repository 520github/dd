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
@Component("tencentWeiboContactMatcher")
public class TencentWeiboContactMatcher extends AbstractContactMatcher {

    @Override
    protected Account findFriend(Contact contact) {
	return thirdpartyService.getByThirdParty(contact.getThirdPartyType(),
		contact.getUid().toLowerCase(), true);
    }

    @Override
    protected Contact getFriendContact(String friendId, Contact contact) {
	return thirdpartyService.getContact(friendId, getThirdPartyType(),
		contact.getUid().toLowerCase());

    }

    @Override
    protected ThirdPartyType getThirdPartyType() {
	return ThirdPartyType.Tencent;
    }
}
