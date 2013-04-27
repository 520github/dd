/**
 * 
 */
package me.twocoffee.dao.impl;

import java.util.Date;
import java.util.List;

import me.twocoffee.common.BaseDao;
import me.twocoffee.dao.ThirdPartyProfileDao;
import me.twocoffee.entity.ThirdPartyProfile;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;

import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;

/**
 * @author momo
 * 
 */
@org.springframework.stereotype.Repository
public class ThirdProfileDaoImpl extends BaseDao<ThirdPartyProfile> implements
		ThirdPartyProfileDao {

	@Override
	public List<ThirdPartyProfile> findByAccountId(String accountId) {
		return createQuery().filter("accountId", accountId).asList();
	}

	@Override
	public ThirdPartyProfile findByTypeAndAccountId(ThirdPartyType type,
			String accountId) {

		return createQuery().filter("accountId", accountId)
				.filter("accountType", type).get();

	}

	@Override
	public ThirdPartyProfile findByTypeAndId(ThirdPartyType type, String userId) {
		Query<ThirdPartyProfile> q = createQuery().filter("userId", userId);

		if (type != null) {
			q = q.filter("accountType", type);
		}
		return q.get();
	}

	@Override
	public ThirdPartyProfile findByTypeAndId(ThirdPartyType type,
			String userId, boolean bind) {

		Query<ThirdPartyProfile> q = createQuery().filter("userId", userId);

		if (type != null) {
			q = q.filter("accountType", type);
		}
		q = q.filter("bind", bind);
		return q.get();
	}

	@Override
	public ThirdPartyProfile getByTypeAndID(ThirdPartyType type, String userId,
			boolean b) {

		Query<ThirdPartyProfile> q = createQuery().filter("userId", userId).filter("accountType", type)
				.filter("login", b);

		return q.get();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.twocoffee.dao.ThirdPartyProfileDao#findByTypeAndAccountId(me.twocoffee
	 * .entity.ThirdPartyProfile.ThirdPartyType, boolean, java.util.Date,
	 * java.util.Date, int, int)
	 */
	@Override
	public List<ThirdPartyProfile> findByTypeAndAccountId(ThirdPartyType type,
			boolean bind, Date max, Date min, int limit, int offset) {

		return createQuery().filter("accountType", type)
				.filter("expiredDate >", min).filter("expiredDate <", max).filter("bind", bind)
				.limit(limit).offset(offset).asList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.twocoffee.dao.ThirdPartyProfileDao#updateToken(me.twocoffee.entity
	 * .ThirdPartyProfile)
	 */
	@Override
	public void updateToken(ThirdPartyProfile thirdpartyProfile) {

		if (thirdpartyProfile != null && thirdpartyProfile.getToken() != null
				&& !"".equals(thirdpartyProfile.getToken())) {

			UpdateOperations<ThirdPartyProfile> update = dataStore
					.createUpdateOperations(ThirdPartyProfile.class);

			update.set("token", thirdpartyProfile.getToken());
			update.set("expiredDate", thirdpartyProfile.getExpiredDate());
			update.set("refreshToken", thirdpartyProfile.getRefreshToken());
			dataStore.update(
					createQuery().filter(
							"userId", thirdpartyProfile.getUserId()).filter("accountType",
							thirdpartyProfile.getAccountType()), update);
		}
	}

	@Override
	public void delete(ThirdPartyProfile p) {
		dataStore.delete(p);
	}

	@Override
	public List<ThirdPartyProfile> getByTypes(List<ThirdPartyType> types,
			int limit, int offset) {

		return createQuery().filter("accountType in ", types).offset(offset)
				.limit(limit).asList();
		
	}

}
