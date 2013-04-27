/**
 * 
 */
package me.twocoffee.service;

import java.util.List;

import me.twocoffee.entity.FriendMailConfig;
import me.twocoffee.service.entity.FriendMail;

/**
 * 白名单管理服务
 * @author xuehui.miao
 *
 */
public interface FriendMailConfigService {
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
	 * 根据一个用户的好友ID获取好友白名单对象
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
	 * 获取好友白名单邮箱列表
	 * 
	 * @param accountId
	 * @return
	 */
	List<FriendMail> getFriendMailList(String accountId);
	
	
	/**
	 * 更新一个好友白名单
	 * 
	 * @param friendMailConfig
	 */
	void updateFriendMailConfig(FriendMailConfig friendMailConfig);
	
	
	/**
	 * 删除一个好友白名单
	 * 
	 * @param accountId
	 * @param friendId
	 */
	void removeFriendMailConfig(String accountId, String friendId);
}
