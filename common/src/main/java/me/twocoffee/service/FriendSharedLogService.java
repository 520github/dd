package me.twocoffee.service;

import me.twocoffee.entity.FriendSharedLog;

public interface FriendSharedLogService {
	
	/**
	 * 获取指定用户的上次分享给的人
	 * 
	 * @param accountId
	 * @return
	 */
	FriendSharedLog getFriendSharedLog(String accountId);
	
	void save(FriendSharedLog friendSharedLog);

}
