package me.twocoffee.service;

import java.util.List;

import me.twocoffee.entity.Account;
import me.twocoffee.entity.Content;
import me.twocoffee.entity.Notification;
import me.twocoffee.entity.Notification.CollapseKey;

public interface NotificationService {

	/**
	 * 查询未读数（作为push badge）
	 * 
	 * @param accountId
	 * @return
	 */
	public int countUnread(String accountId);

	/**
	 * 根据accountId,消息类型删除通知，供每个模块调用
	 */
	public void deleteByAccountIdAndKey(String accountId,
			Notification.CollapseKey key);

	public void deleteOneByAccountIdAndKey(String accountId,
			CollapseKey friendmessage);

	/**
	 * 获取从指定id后的通知
	 * 
	 * @param accountId
	 * @param prevousId
	 * @return
	 */
	public List<Notification> getLatestNotifications(String accountId,
			String prevousId, String displayType);

	/**
	 * 根据ID获取通知
	 * 
	 * @param id
	 * @return
	 */
	public Notification getNotificationById(String id);

	/**
	 * 置为已读
	 * 
	 * @param notificationId
	 */
	public void read(String accountId, String notificationId);

	/**
	 * 保存一个通知 保证accountId+collapseKey是唯一的： 如果已经存在accountId+collapseKey相同的记录，
	 * 则会把之前的删掉并新建。
	 * 
	 * @param notification
	 */
	public void save(Notification notification);

	/**
	 * 创建并保存通知
	 * 
	 * @param collapseKey
	 * @param targetAccountId
	 * @param account
	 * @param content
	 * @return 
	 */
	Notification createAndSave(CollapseKey collapseKey, Account targetAccount,
			Account account, Content content);
	
}
