/**
 * 
 */
package me.twocoffee.dao;

import java.util.List;

import me.twocoffee.entity.Contact;
import me.twocoffee.entity.Contact.RelationType;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;

/**
 * @author momo
 * 
 */
// TODO:SNS new
public interface ContactDao {

	long count(String accountId, ThirdPartyType accountType);

	List<Contact> find(String accountId);

	List<Contact> find(String accountId, List<ThirdPartyType> thirdPartyTypes,
			List<RelationType> relation, Integer limit, Integer offset);

	List<Contact> find(String accountId, ThirdPartyType accountType, int limit,
			int offset);
	
	List<Contact> find(String accountId, ThirdPartyType accountType,List<String> friendUids);

	Contact get(String accountId, ThirdPartyType type, String friendUid);

	Contact get(String id, ThirdPartyType thirdPartyType, String[] mobile);

	int remove(String accountId, ThirdPartyType thirdPartyType);

	void save(List<Contact> contacts);

}
