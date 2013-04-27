package me.twocoffee.dao;

import java.util.List;
import java.util.Map;

import me.twocoffee.entity.Message;
import me.twocoffee.entity.Message.CatalogId;
import me.twocoffee.entity.Message.MessageType;

public interface MessageDao {
	/**
	 * 获取某个帐户的normal状态下的消息数
	 * 
	 * @param accountId
	 * @param catalogId
	 * @return
	 */
	public long countByAccountId(String accountId, CatalogId catalogId);

	public long countNormalMessage(String accountId, MessageType type,
			Map<String, Object> attributes);

	/**
	 * 删除重复消息
	 * 
	 * @param accountId
	 * @param friendId
	 */
	public void deleteByAccountIdAndFriendId(String accountId, String friendId);

	public void deleteByAccountIdAndType(String accountId, MessageType binding);

	/**
	 * 根据id查询message
	 * 
	 * @param messageId
	 * @return
	 */
	public Message getById(String messageId);

	/**
	 * 分页获取某个账户某个类型的message
	 * 
	 * @param accountId
	 * @param catalogId
	 * @param limit
	 *            每页数量
	 * @param offset
	 *            偏移量
	 * @return
	 */
	public List<Message> getMessages(String accountId, CatalogId catalogId,
			String displayType,int limit, int offset);

	/**
	 * 保存一个消息
	 * 
	 * @param message
	 */
	public void save(Message message);

	/**
	 * 更改一个状态
	 * 
	 * @param id
	 * @param status
	 */
	public void updateStatus(String id, Message.Status status);

	/**
	 * 根据accountId更改所有message状态
	 * 
	 * @param accountId
	 * @param status
	 */
	public void updateStatusByAccountId(String accountId, Message.Status status);
	
	/**
	 * 根据attribute删除消息
	 * @param accountId
	 * @param binding
	 * @param attribute
	 */
	public void deleteMessageForAttribute(String accountId, MessageType binding,Map<String,String> attribute);
}
