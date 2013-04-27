/**
 * 
 */
package me.twocoffee.service.thirdparty.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import me.twocoffee.entity.Account;
import me.twocoffee.entity.Contact;
import me.twocoffee.entity.ThirdPartyProfile;
import me.twocoffee.entity.Contact.RelationType;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;
import me.twocoffee.service.EventService;
import me.twocoffee.service.FriendService;
import me.twocoffee.service.event.ThirdPartyFriendsSuggestEvent;
import me.twocoffee.service.thirdparty.FriendMatcher;
import me.twocoffee.service.thirdparty.ThirdpartyService;

/**
 * @author wenjian
 * 
 */
public abstract class AbstractContactMatcher implements FriendMatcher {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractContactMatcher.class);

	@Autowired
	protected ThirdpartyService thirdpartyService;

	@Autowired
	protected EventService eventService;

	@Autowired
	protected FriendService friendService;

	protected void notifyExistDuoDuoAccount(String accountId, Contact c,
			Account account) {

		if (c.getRelation().equals(RelationType.DuoduoAccount)) {
			ThirdPartyFriendsSuggestEvent e = new ThirdPartyFriendsSuggestEvent(
					accountId, getThirdPartyType(), account);

			eventService.send(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.twocoffee.service.impl.FriendMatcher#findFriend(me.twocoffee.entity
	 * .Contact)
	 */
	protected abstract Account findFriend(Contact c);

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.twocoffee.service.impl.FriendMatcher#doMatch(java.util.List,
	 * me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType, java.lang.String,
	 * boolean)
	 */
	@Override
	public void doMatch(List<Contact> contacts, String accountId, boolean first) {
		thirdpartyService.removeContact(getThirdPartyType(), accountId);

		for (Contact contact : contacts) {

			try {
				Account account = findFriend(contact);
				setBasicContactInfo(accountId, contact);

				if (account != null) {
					setMatchedContactInfo(contact, account);

					if (first) {
						updateAndNotifyFriendContact(accountId, first, contact,
								account);

					}

				} else {
					contact.setRelation(RelationType.NotDuoduoAccount);
					LOGGER.debug("current account {} do {} match! find notduoduoaccount {}", new Object[]{contact.getAccountId(), getThirdPartyType(), contact.getId()});
				}

			} catch (Exception e) {
				LOGGER.error("error when matcher friends!accountID "
						+ accountId + " contact " + contact.getId(), e);

			}
		}
		thirdpartyService.saveContacts(contacts);
	}

	private void updateAndNotifyFriendContact(String accountId, boolean first,
			Contact contact, Account account) {

		Contact friendContact = getFriendContact(account.getId(), contact);

		if (friendContact != null
				&& !friendContact.getRelation().equals(contact.getRelation())) {

			friendContact.setRelation(contact.getRelation());

			if (contact.getRelation() == RelationType.DuoduoFriend) {
				friendContact.setFriendId(accountId);
			}
		}
		notifyExistDuoDuoAccount(accountId, contact, account);
	}

	protected void setMatchedContactInfo(Contact contact, Account account) {

		if (friendService.getByAccountIdAndFriendId(contact.getAccountId(),
				account.getId()) != null) {

			contact.setFriendId(account.getId());
			contact.setRelation(RelationType.DuoduoFriend);
			LOGGER.debug("current account {} do {} match! find duoduofriend {} account {} ", new Object[]{contact.getAccountId(), getThirdPartyType(), contact.getId(), account.getId()});

		} else {
			contact.setFriendId(account.getId());
			contact.setRelation(RelationType.DuoduoAccount);
			LOGGER.debug("current account {} do {} match! find duoduoaccount {} account {} ", new Object[]{contact.getAccountId(), getThirdPartyType(), contact.getId(), account.getId()});
		}
		contact.setMatchedAccount(account);
	}

	private void setBasicContactInfo(String accountId, Contact contact) {
		contact.setAccountId(accountId);
		contact.setThirdPartyType(getThirdPartyType());
		contact.setId(new ObjectId().toString());
		contact.setCreateDate(new Date());
	}

	protected abstract Contact getFriendContact(String friendId, Contact contact);

	protected abstract ThirdPartyType getThirdPartyType();

	@Override
	public void rematch(Account account, Account friend) {
		Contact friendContact = findAndSetContact(account, friend);
		Contact currentContact = findAndSetContact(friend, account);
		List<Contact> contacts = new ArrayList<Contact>(2);
		contacts.add(friendContact);
		contacts.add(currentContact);
		thirdpartyService.saveContacts(contacts);
	}

	private Contact findAndSetContact(Account account, Account friend) {
		ThirdPartyProfile currentProfile = thirdpartyService.getByAccountId(
				account.getId(), getThirdPartyType());

		Contact friendContact = thirdpartyService.getContact(friend.getId(),
				getThirdPartyType(), currentProfile.getUserId());

		friendContact.setFriendId(account.getId());
		friendContact.setRelation(RelationType.DuoduoFriend);
		friendContact.setMatchedAccount(account);
		return friendContact;
	}
}
