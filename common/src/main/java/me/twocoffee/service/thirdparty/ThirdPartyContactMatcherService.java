/**
 * 
 */
package me.twocoffee.service.thirdparty;

import java.util.List;

import me.twocoffee.entity.Contact;
import me.twocoffee.entity.Contact.RelationType;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;

/**
 * @author momo
 * 
 */
// TODO:SNS new
public interface ThirdPartyContactMatcherService {

	List<Contact> getMatchedThirdPartyFriend(
			List<ThirdPartyType> thirdPartyTypes, String accountId,
			List<RelationType> rtypes, Integer limit, Integer offset);

	void matchThirdPartyFriend(List<Contact> contacts,
			ThirdPartyType thirdPartyType, String accountId);

	void matchThirdPartyFriend(List<Contact> contacts,
			ThirdPartyType thirdPartyType, String accountId, boolean first);

	public void rematchThirdpartyFriends(String accountId, String friendId);
}
