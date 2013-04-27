package me.twocoffee.dao;

import java.util.List;

import me.twocoffee.entity.Notification;

public interface NotificationDao {
	/**
	 * 查询某用户的未读消息数
	 * 
	 * @param accountId
	 * @return
	 */
	public int countUnRead(String accountId);

	/**
	 * 根据id获取消息
	 * 
	 * @param notificationId
	 * @return
	 */
	public Notification getById(String notificationId);

	/**
	 * 查询某用户在指定id后的未读通知
	 * 
	 * @param accountId
	 * @param prevousId
	 * @return
	 */
	public List<Notification> getUnreadNotificationsByPrevousId(
			String accountId,
			String prevousId,
			String displayType
			);

	/**
	 * 保存消息
	 * 
	 * @param notification
	 */
	public void save(Notification notification);

	/**
	 * 更改消息状态
	 * 
	 * @param id
	 * @param status
	 */
	public void updateStatus(String id, Notification.Status status);
	
	/**
	 * 根据accountId,key 获取通知
	 * @param accountId
	 * @param key
	 * @return
	 */
	public Notification findByAccountIdAndKey(String accountId,Notification.CollapseKey key);
}
