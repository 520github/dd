package me.twocoffee.dao.impl;

import me.twocoffee.common.BaseDao;
import me.twocoffee.dao.FriendSharedLogDao;
import me.twocoffee.entity.FriendSharedLog;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

@Repository
public class FriendSharedLogDaoImpl extends
		BaseDao<FriendSharedLog>
		implements
		FriendSharedLogDao {

	@Override
	public FriendSharedLog findByAccountId(String accountId) {

		return createQuery().filter("accountId", accountId)
				.get();
	}
	
	@Override
	public void save(FriendSharedLog friendSharedLog) {
		if (friendSharedLog != null && (friendSharedLog.getId() == null || friendSharedLog.getId().equals(""))) {
			friendSharedLog.setId(new ObjectId().toString());
		}
		super.save(friendSharedLog);
	}

}
