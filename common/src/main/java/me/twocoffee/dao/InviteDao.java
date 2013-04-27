package me.twocoffee.dao;

import me.twocoffee.entity.Invite;

public interface InviteDao {

	public Invite findById(String id);

	public void save(Invite invite);

	public String saveAndReturnId(Invite invite);

	public Invite getByOwnerId(String ownerId);
}
