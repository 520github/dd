/**
 * 
 */
package me.twocoffee.dao.impl;

import java.util.List;

import me.twocoffee.common.BaseDao;
import me.twocoffee.dao.ContactDao;
import me.twocoffee.entity.Contact;
import me.twocoffee.entity.Contact.RelationType;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;

import com.google.code.morphia.query.Query;
import com.mongodb.WriteResult;

/**
 * @author momo
 * 
 */
@org.springframework.stereotype.Repository
public class ContactDaoImpl extends BaseDao<Contact> implements ContactDao {

	@Override
	public long count(String accountId, ThirdPartyType thirdPartyType) {
		Query<Contact> q = createQuery().filter("accountId",
				accountId)
				.filter("thirdPartyType", thirdPartyType);

		return q.countAll();
	}

	@Override
	public List<Contact> find(String accountId) {
		return createQuery().filter("accountId", accountId)
				.asList();
	}

	@Override
	public List<Contact> find(String accountId,
			List<ThirdPartyType> thirdPartyTypes, List<RelationType> relation,
			Integer limit, Integer offset) {

		Query<Contact> q = createQuery().filter("accountId",accountId);
				
		if(thirdPartyTypes != null && thirdPartyTypes.size() >0) {
			q = q.filter("thirdPartyType in", thirdPartyTypes);
		}
		if (relation != null && relation.size() > 0) {
			q = q.filter("relation in", relation);
		}

		if (limit > 0) {
			q = q.limit(limit);
		}

		if (offset >= 0) {
			q = q.offset(offset);
		}
		return q.asList();
	}

	@Override
	public List<Contact> find(String accountId, ThirdPartyType accountType,
			int limit, int offset) {

		Query<Contact> q = createQuery().filter("accountId",
				accountId)
				.filter("thirdPartyType", accountType);

		if (limit > 0) {
			q = q.limit(limit);
		}

		if (offset > 0) {
			q = q.offset(offset);
		}
		return q.asList();
	}
	
	@Override
	public List<Contact> find(String accountId, ThirdPartyType accountType,List<String> friendUids) {
		Query<Contact> q = createQuery().filter("accountId", accountId)
		                      .filter("thirdPartyType", accountType);
		if(friendUids != null && friendUids.size() >0) {
			q = q.filter("uid in", friendUids);
		}
		return q.asList();
	}

	@Override
	public Contact get(String accountId, ThirdPartyType type, String friendUid) {
		return createQuery().filter("accountId", accountId)
				.filter("thirdPartyType", type).filter("uid", friendUid).get();

	}

	@Override
	public Contact get(String id, ThirdPartyType thirdPartyType,
			String[] mobile) {

		return createQuery().filter("accountId", id)
				.filter("thirdPartyType", thirdPartyType)
				.filter("mobile in", mobile).get();

	}

	@Override
	public int remove(String accountId, ThirdPartyType thirdPartyType) {
		Query<Contact> q = createQuery().filter("accountId",
				accountId)
				.filter("thirdPartyType", thirdPartyType);

		WriteResult wr = this.dataStore.delete(q);
		return wr.getN();
	}

}
