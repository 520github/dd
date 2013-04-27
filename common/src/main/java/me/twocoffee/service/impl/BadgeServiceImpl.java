package me.twocoffee.service.impl;

import me.twocoffee.dao.BadgeDao;
import me.twocoffee.entity.Badge;
import me.twocoffee.entity.Badge.BadgeName;
import me.twocoffee.service.BadgeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BadgeServiceImpl implements BadgeService {

	@Autowired
	private BadgeDao badgeDao;

	@Override
	public Badge getBadge(String accountId) {
		return badgeDao.getBadgeByAccountId(accountId);
	}

	@Override
	public int getBadge(String accountId, BadgeName badgeName) {
		return badgeDao.getBadgeByAccountId(accountId, badgeName);
	}

	@Override
	public void increaseBadge(String accountId, BadgeName badgeName) {
		badgeDao.increaseBadge(accountId, badgeName);
	}

	@Override
	public void resetBadge(String accountId, BadgeName badgeName) {
		badgeDao.resetBadge(accountId, badgeName);
	}

}
