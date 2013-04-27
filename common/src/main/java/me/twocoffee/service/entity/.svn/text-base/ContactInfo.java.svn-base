/**
 * 
 */
package me.twocoffee.service.entity;

import me.twocoffee.entity.Account;
import me.twocoffee.entity.Contact;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author momo
 * 
 */
// TODO:SNS new
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ContactInfo extends Contact {

	private Account account;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
}
