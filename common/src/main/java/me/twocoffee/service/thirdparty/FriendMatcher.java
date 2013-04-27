/**
 * 
 */
package me.twocoffee.service.thirdparty;

import java.util.List;

import me.twocoffee.entity.Account;
import me.twocoffee.entity.Contact;

/**
 * @author momo
 * 
 */
public interface FriendMatcher {

	void doMatch(List<Contact> contacts, String accountId, boolean first);

	public void rematch(Account account, Account friend);

}
