package me.twocoffee.service;

import me.twocoffee.entity.Device;
import me.twocoffee.entity.Notification;

public interface PushService {
	/**
	 * 给某个设备发送push消息
	 * 
	 * @param notification
	 *            通知实体
	 * @param deivice
	 *            设备
	 */
	public void push(Notification notification, Device deivice);

	/**
	 * 给某个账户发送push消息
	 * 
	 * @param notification
	 * @param accountId
	 */
	public void push(Notification notification, String accountId);
	/**
	 * 提供客户端测试推送消息
	 */
	
	public void testPush(Notification notification, String accountId,int badge);
}
