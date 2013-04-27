/**
 * 
 */
package me.twocoffee.dao.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.code.morphia.query.Query;

import me.twocoffee.common.BaseDao;
import me.twocoffee.dao.FriendDao;
import me.twocoffee.dao.FriendMailConfigDao;
import me.twocoffee.entity.Friend;
import me.twocoffee.entity.FriendMailConfig;

/**
 * 邮箱白名单管理
 * 
 * @author xuehui.miao
 *
 */
@Service
public class FriendMailConfigDaoImpl extends BaseDao<FriendMailConfig> implements
		FriendMailConfigDao {
	
	@Autowired
	private FriendDao friendDao;
	
	@Override
	public FriendMailConfig getFriendMailConfig(String accountId, String friendId) {
		return this.createQuery().
	        filter("accountId", accountId).
	        filter("friendId", friendId).
	        get();
	}
	/* (non-Javadoc)
	 * @see me.twocoffee.dao.FriendMailConfigDao#getFriendMailConfigList(java.lang.String)
	 */
	@Override
	public List<FriendMailConfig> getFriendMailConfigList(String accountId) {
		return this.createQuery().
            filter("accountId", accountId).
            asList();
	}

	/* (non-Javadoc)
	 * @see me.twocoffee.dao.FriendMailConfigDao#getFriendMailConfigList(java.lang.String, boolean)
	 */
	@Override
	public List<FriendMailConfig> getFriendMailConfigList(String accountId,
			boolean isBlocked) {
		return this.createQuery().
            filter("accountId", accountId).
            filter("isBlocked", isBlocked).
            asList();
	}

	/* (non-Javadoc)
	 * @see me.twocoffee.dao.FriendMailConfigDao#removeFriendMailConfig(me.twocoffee.entity.FriendMailConfig)
	 */
	@Override
	public void removeFriendMailConfig(FriendMailConfig friendMailConfig) {
		if(friendMailConfig == null)return ;
		if(friendMailConfig.getId() == null || friendMailConfig.getId().trim().length() < 1) {
			return ;
		}
		
		dataStore.delete(friendMailConfig);
	}

	/* (non-Javadoc)
	 * @see me.twocoffee.dao.FriendMailConfigDao#removeFriendMailConfig(java.lang.String, java.lang.String)
	 */
	@Override
	public void removeFriendMailConfig(String accountId, String friendId) {
		Query<FriendMailConfig> query = this.createQuery().
		    filter("accountId", accountId).
		    filter("friendId", friendId);
		dataStore.delete(query);
	}

	/* (non-Javadoc)
	 * @see me.twocoffee.dao.FriendMailConfigDao#removeFriendMailConfig(java.lang.String)
	 */
	@Override
	public void removeFriendMailConfig(String accountId) {
		if(accountId == null)return;
		
		Query<FriendMailConfig> query = this.createQuery().filter("accountId", accountId);
		dataStore.delete(query);
	}

	/* (non-Javadoc)
	 * @see me.twocoffee.dao.FriendMailConfigDao#saveFriendMailConfig(me.twocoffee.entity.FriendMailConfig)
	 */
	@Override
	public void saveFriendMailConfig(FriendMailConfig friendMailConfig) {
		if(friendMailConfig == null)return ;
		if(friendMailConfig.getId() == null || friendMailConfig.getId().trim().length() < 1) {
			friendMailConfig.setId(new ObjectId().toString());
		}
		
		super.save(friendMailConfig);

	}

	/* (non-Javadoc)
	 * @see me.twocoffee.dao.FriendMailConfigDao#saveFriendMailConfig(java.lang.String)
	 */
	@Override
	public void saveFriendMailConfig(String accountId) {
		if(accountId == null)return;
		List<Friend> friendList = friendDao.findFriends(accountId);
		if(friendList == null || friendList.size() < 1) {
			return ;
		}
		
		for (int i = 0; i < friendList.size(); i++) {
			Friend friend = friendList.get(i);
			if(friend == null)continue;
			String friendId = friend.getFriendId();
			if(friendId == null || friendId.trim().length() < 1)continue;
			
			FriendMailConfig friendMailConfig = new FriendMailConfig();
			
			friendMailConfig.setAccountId(accountId);
			friendMailConfig.setFriendId(friendId);
			friendMailConfig.setBlocked(false);
			
			this.saveFriendMailConfig(friendMailConfig);
		}
	}

	/* (non-Javadoc)
	 * @see me.twocoffee.dao.FriendMailConfigDao#updateFriendMailConfig(me.twocoffee.entity.FriendMailConfig)
	 */
	@Override
	public void updateFriendMailConfig(FriendMailConfig friendMailConfig) {
		if(friendMailConfig == null) return;
		if(friendMailConfig.getId() == null || friendMailConfig.getId().trim().length() < 1) {
			return ;
		}
		super.save(friendMailConfig);
	}

}
