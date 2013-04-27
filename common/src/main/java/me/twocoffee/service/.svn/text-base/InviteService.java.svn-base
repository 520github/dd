package me.twocoffee.service;

import java.util.List;

import me.twocoffee.entity.Account.RoleType;
import me.twocoffee.entity.Invite;
import me.twocoffee.service.impl.InviteServiceImpl.InviteVerifyResult;

//TODO:SNS getInviteByAccount
public interface InviteService {
	public Invite getByOwnerId(String ownerId);

	/**
	 * 获取指定帐户的邀请码
	 * 
	 * @param accountId
	 * @param create
	 *            如果不存在，则创建
	 * @return
	 */
	public Invite getInviteByAccount(String accountId, boolean create);

	/**
	 * @param id
	 */
	public Invite getInviteById(String id);

	public List<Invite> getInvites(int number, RoleType role);

	public abstract String save(Invite invite);

	/**
	 * @param id
	 * @return
	 */
	public InviteVerifyResult verify(String id);
	
	public String covertinviteCode2AccountId(String inviteCode);
}
