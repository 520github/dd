/**
 * 
 */
package me.twocoffee.rest.entity;

import me.twocoffee.entity.Contact;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author momo
 * 
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ContactResult extends Contact {

    private BasicAccount account;

    public BasicAccount getAccount() {
	return account;
    }

    public void init(Contact contact) {
	setAvatar(contact.getAvatar());
	setMobile(contact.getMobile());
	setName(contact.getName());
	setThirdPartyType(contact.getThirdPartyType());
	setUid(contact.getUid());
	setProfile(contact.getProfile());
    }

    public void setAccount(BasicAccount account) {
	this.account = account;
    }
}
