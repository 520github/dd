package me.twocoffee.dao.impl;

import java.util.List;

import me.twocoffee.common.BaseDao;
import me.twocoffee.dao.InviteRecordDao;
import me.twocoffee.entity.InviteRecord;

import org.bson.types.ObjectId;

@org.springframework.stereotype.Repository
public class InviteRecordDaoImpl extends BaseDao<InviteRecord> implements
		InviteRecordDao {
	@Override
	public void save(InviteRecord t) {
		if (t != null && t.getId() == null) {
			t.setId(new ObjectId().toString());
		}
		super.save(t);
	}

	@Override
	public List<InviteRecord> findByInvitor(String invitor, int PageSize,
			int PageNum) {
		return this.createQuery().filter("invitor", invitor).offset(
				PageSize * PageNum).limit(PageSize).asList();
	}

	@Override
	public List<InviteRecord> findByInvitees(String invitees) {
		return createQuery().filter("invitees ", invitees).asList();
	}

	@Override
	public InviteRecord findByInvitorAndInvitees(String invitor, String invitees) {
		return createQuery().filter("invitor =", invitor)
				.filter("invitees =", invitees)
				.get();
	}

	@Override
	public InviteRecord findByInvitorAndCode(String invitor, String code) {
		return createQuery().filter("invitor =", invitor)
				.filter("code =", code)
				.get();
	}

	@Override
	public InviteRecord findByInvitor(String invitor) {
		return createQuery().filter("invitor =", invitor)
				.get();
	}

}
