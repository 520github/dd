package me.twocoffee.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.twocoffee.common.util.InviteCodeUtils;
import me.twocoffee.dao.InviteDao;
import me.twocoffee.dao.SequenceDao;
import me.twocoffee.entity.Account.RoleType;
import me.twocoffee.entity.Invite;
import me.twocoffee.service.InviteService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
// TODO:SNS getInviteByAccount
public class InviteServiceImpl implements InviteService {
	public static enum InviteVerifyResult {
		OK,
		NotExists,
		Expired
	}

	@Autowired
	private InviteDao inviteDao;

	@Autowired
	private SequenceDao sequenceDao;

	private Invite createAndReturn(RoleType role) {
		Invite invite = new Invite();
		invite.setCreateTime(new Date());

		if (role != null) {
			Set<RoleType> roles = new HashSet<RoleType>();
			roles.add(role);
			invite.setRoles(roles);
		}
		invite.setId(save(invite));
		return invite;
	}

	@Override
	public Invite getByOwnerId(String ownerId) {
		return inviteDao.getByOwnerId(ownerId);
	}

	@Override
	public Invite getInviteByAccount(String accountId, boolean create) {
		Invite invite = getByOwnerId(accountId);

		if (invite == null) {
			Invite it = new Invite();
			it.setOwnerId(accountId);
			it.setCreateTime(new Date());
			save(it);
			invite = it;
		}
		return invite;
	}

	@Override
	public Invite getInviteById(String id) {
		return inviteDao.findById(id);
	}

	@Override
	public List<Invite> getInvites(int number, RoleType role) {
		List<Invite> invites = new ArrayList<Invite>();

		for (int i = 0; i < number; i++) {

			try {
				invites.add(createAndReturn(role));

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return invites;
	}

	@Override
	public String save(Invite invite) {

		if (StringUtils.isBlank(invite.getId())) {
			invite.setId(InviteCodeUtils.createInviteCode(sequenceDao.next()));
		}
		return inviteDao.saveAndReturnId(invite);
	}

	public void setInviteDao(InviteDao inviteDao) {
		this.inviteDao = inviteDao;
	}

	@Override
	public InviteVerifyResult verify(String id) {
		Invite invite = getInviteById(id);
		if (invite == null) {
			return InviteVerifyResult.NotExists;
		}
		// if (invite.getExpiredTime().before(new Date())) {
		// return InviteVerifyResult.Expired;
		// }
		return InviteVerifyResult.OK;
	}

	@Override
	public String covertinviteCode2AccountId(String inviteCode) {
		// TODO SNS
		String accountId = "";
		return accountId;
	}
}