package me.twocoffee.service.impl;

import me.twocoffee.dao.FriendSharedLogDao;
import me.twocoffee.entity.FriendSharedLog;
import me.twocoffee.service.FriendSharedLogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendSharedLogServiceImpl implements FriendSharedLogService {

	@Autowired
	private FriendSharedLogDao friendSharedLogDao;

	@Override
	public FriendSharedLog getFriendSharedLog(String accountId) {
		return friendSharedLogDao.findByAccountId(accountId);
	}

	@Override
	public void save(FriendSharedLog friendSharedLog) {
		friendSharedLogDao.save(friendSharedLog);
		
	}

}
