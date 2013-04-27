/**
 * 
 */
package me.twocoffee.dao.impl;

import me.twocoffee.common.BaseDao;
import me.twocoffee.dao.ThirdPartyContentSynchronizeLogDao;
import me.twocoffee.entity.ThirdPartyContentSynchronizeLog;
import me.twocoffee.entity.ThirdPartyProfile;

/**
 * @author wenjian
 * 
 */
@org.springframework.stereotype.Repository
public class ThirdPartyContentSynchronizeLogDaoImpl extends
		BaseDao<ThirdPartyContentSynchronizeLog> implements
		ThirdPartyContentSynchronizeLogDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.twocoffee.dao.ThirdPartyContentSynchronizeLogDao#saveContentSynchronizeLog
	 * (me.twocoffee.entity.ThirdPartyContentSynchronizeLog)
	 */
	@Override
	public void saveContentSynchronizeLog(
			ThirdPartyContentSynchronizeLog contentSynchronizeLog) {

		super.save(contentSynchronizeLog);
	}

	@Override
	public ThirdPartyContentSynchronizeLog findThirdPartyContentSynchronizeLog(
			ThirdPartyProfile thirdpartyProfile) {

		return createQuery()
				.filter("thirdPartyType", thirdpartyProfile.getAccountType())
				.filter("uid", thirdpartyProfile.getUserId())
				.filter("accountId", thirdpartyProfile.getAccountId()).get();
		
	}

}
