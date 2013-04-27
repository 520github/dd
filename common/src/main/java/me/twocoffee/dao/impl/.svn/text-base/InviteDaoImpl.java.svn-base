package me.twocoffee.dao.impl;

import me.twocoffee.common.BaseDao;
import me.twocoffee.dao.InviteDao;
import me.twocoffee.entity.Invite;

@org.springframework.stereotype.Repository
public class InviteDaoImpl extends BaseDao<Invite> implements
		InviteDao {

	private static final String ID = "_id";

	@Override
	public Invite findById(String id) {
		return createQuery().filter(ID, id).get();
	}

	@Override
	public Invite getByOwnerId(String ownerId) {
		return createQuery().filter("ownerId", ownerId).get();
	}

}
