/**
 * 
 */
package me.twocoffee.dao;

import java.util.List;

import me.twocoffee.entity.FriendMailConfig;

/**
 * 邮箱白名单管理
 * @author xuehui.miao
 *
 */
public interface FriendMailConfigDao {
	
	/**
	 * 保存一个邮箱白名单
	 * 
	 * @param friendMailConfig
	 */
	void saveFriendMailConfig(FriendMailConfig friendMailConfig);
	
	
	/**
	 * 保存一个用户所有好友的白名单
	 * 
	 * @param accountId
	 */
	void saveFriendMailConfig(String accountId);
	
	
	/**
	 * 获取一个好友的白名单
	 * 
	 * @param accountId
	 * @param friendId
	 * @return
	 */
	FriendMailConfig getFriendMailConfig(String accountId, String friendId);
	
	/**
	 * 获取一个用户所有的好友白名单列表
	 * 
	 * @param accountId
	 * @return
	 */
	List<FriendMailConfig> getFriendMailConfigList(String accountId);
	
	
	/**
	 * 获取一个用户所有阻止或不阻止的好友白名单列表
	 * 
	 * @param accountId
	 * @param isBlocked
	 * @return
	 */
	List<FriendMailConfig> getFriendMailConfigList(String accountId, boolean isBlocked);
	
	
	/**
	 * 更新一个好友白名单
	 * 
	 * @param friendMailConfig
	 */
	void updateFriendMailConfig(FriendMailConfig friendMailConfig);
	
	/**
	 * 删除一个好友白名单
	 * 
	 * @param friendMailConfig
	 */
	void removeFriendMailConfig(FriendMailConfig friendMailConfig);
	
	
	/**
	 * 删除一个好友白名单
	 * 
	 * @param accountId
	 * @param friendId
	 */
	void removeFriendMailConfig(String accountId, String friendId);
	
	
	/**
	 * 删除一个用户的所有好友白名单列表
	 * 
	 * @param accountId
	 */
	void removeFriendMailConfig(String accountId);
}
