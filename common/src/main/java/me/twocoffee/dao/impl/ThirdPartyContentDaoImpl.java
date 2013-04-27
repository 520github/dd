/**
 * 
 */
package me.twocoffee.dao.impl;

import java.util.Date;
import java.util.List;

import me.twocoffee.common.BaseDao;
import me.twocoffee.dao.ThirdPartyContentDao;
import me.twocoffee.entity.ThirdPartyContent;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;

/**
 * @author momo
 * 
 */
@org.springframework.stereotype.Repository
public class ThirdPartyContentDaoImpl extends BaseDao<ThirdPartyContent>
		implements ThirdPartyContentDao {

	@Override
	public List<ThirdPartyContent> findAfter(
			ThirdPartyType accountType,
			String userId, Date syncDate) {

		return createQuery().filter("thirdPartyType", accountType)
				.filter("uid", userId).filter("favoriteTime >", syncDate)
				.asList();
	}

	@Override
	public ThirdPartyContent getLatestContent(
			ThirdPartyType accountType,
			String userId) {

		return createQuery().filter("thirdPartyType", accountType)
				.filter("uid", userId).order("-favoriteTime").limit(1).get();

	}

}
