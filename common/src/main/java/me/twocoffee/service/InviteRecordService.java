package me.twocoffee.service;

import java.util.List;

import me.twocoffee.entity.InviteRecord;

public interface InviteRecordService {

	/**
	 * 保持纪录
	 * 
	 * @param record
	 */
	void save(InviteRecord record);

	/**
	 * 根据邀请者查找
	 * 
	 * @param invitor
	 * @return
	 */
	List<InviteRecord> findByInvitor(String invitor, int PageSize,
			int PageNum);

	/**
	 * 根据受邀者查找
	 * 
	 * @param invitees
	 * @return
	 */
	List<InviteRecord> findByInvitees(String invitees);

	/**
	 * 根据邀请者和受邀者查找
	 * 
	 * @param Invitor
	 * @param invitees
	 * @return
	 */
	InviteRecord findByInvitorAndInvitees(String invitor, String invitees);

	/**
	 * 根据邀请者和邀请码查找
	 * 
	 * @param invitor
	 * @param code
	 * @return
	 */
	InviteRecord findByInvitorAndCode(String invitor, String code);

	/**
	 * 根据邀请者查找
	 * 
	 * @param invitor
	 * @return
	 */
	InviteRecord findByInvitor(String invitor);

	/**
	 * 确认是否是邀请的注册用户
	 * 
	 * @param loginName
	 */
	void verifyInvitation(String loginName);
}
