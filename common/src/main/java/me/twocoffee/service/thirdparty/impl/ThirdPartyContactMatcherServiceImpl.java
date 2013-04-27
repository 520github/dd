/**
 * 
 */
package me.twocoffee.service.thirdparty.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.twocoffee.entity.Account;
import me.twocoffee.entity.Contact;
import me.twocoffee.entity.Contact.RelationType;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.thirdparty.FriendMatcher;
import me.twocoffee.service.thirdparty.ThirdPartyContactMatcherService;
import me.twocoffee.service.thirdparty.ThirdpartyService;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author momo
 * 
 */
public class ThirdPartyContactMatcherServiceImpl implements
		ThirdPartyContactMatcherService {

	@Autowired
	private ThirdpartyService thirdpartyService;

	@Autowired
	private AccountService accountService;

	private Map<ThirdPartyType, FriendMatcher> matchers = new HashMap<ThirdPartyType, FriendMatcher>();

	private void setContactResult(List<Contact> contacts) {

		if (contacts != null) {

			for (Contact c : contacts) {

				switch (c.getRelation()) {
				case DuoduoAccount:
				case DuoduoFriend:

					if (c.getRelation().equals(RelationType.DuoduoAccount)) {
						c.setMatchedAccount(accountService.getById(c
								.getFriendId()));

					}
					break;

				default:
					break;
				}
			}
		}
	}

	public AccountService getAccountService() {
		return accountService;
	}

	@Override
	public List<Contact> getMatchedThirdPartyFriend(
			List<ThirdPartyType> thirdPartyTypes, String accountId,
			List<RelationType> relations, Integer limit, Integer offset) {

		List<Contact> contacts = thirdpartyService.getContacts(accountId,
				thirdPartyTypes, relations, limit, offset);

		setContactResult(contacts);
		return contacts;
	}

	public Map<ThirdPartyType, FriendMatcher> getMatchers() {
		return matchers;
	}

	public ThirdpartyService getThirdpartyService() {
		return thirdpartyService;
	}

	@Override
	public void matchThirdPartyFriend(List<Contact> contacts,
			ThirdPartyType thirdPartyType, String accountId) {

		boolean first = false;

		if (thirdpartyService.removeContact(thirdPartyType, accountId) == 0) {
			first = true;
		}
		matchThirdPartyFriend(contacts, thirdPartyType, accountId, first);
	}

	@Override
	public void matchThirdPartyFriend(List<Contact> contacts,
			ThirdPartyType thirdPartyType, String accountId, boolean first) {

		if (contacts != null && !contacts.isEmpty()) {
			FriendMatcher matcher = matchers.get(thirdPartyType);

			if (matcher == null) {
				return;
			}
			matcher.doMatch(contacts, accountId, first);
		}
	}

	@Override
	public void rematchThirdpartyFriends(String accountId, String friendId) {
		Account currentAccount = accountService.getById(accountId);
		Account friendAccount = accountService.getById(friendId);
		
		if (currentAccount.getAccountType() == friendAccount.getAccountType()) {
			ThirdPartyType type = ThirdPartyType.forName(currentAccount.getAccountType().toString());
			
			if (type != ThirdPartyType.Unknown) {
				FriendMatcher matcher = matchers.get(type);
				matcher.rematch(currentAccount, friendAccount);
			}
		}
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	public void setMatchers(Map<ThirdPartyType, FriendMatcher> matchers) {
		this.matchers = matchers;
	}

	public void setThirdpartyService(ThirdpartyService thirdpartyService) {
		this.thirdpartyService = thirdpartyService;
	}
}
