package me.twocoffee.service;

import java.util.List;
import java.util.Map;

import me.twocoffee.entity.Message;
import me.twocoffee.entity.Message.MessageAction;
import me.twocoffee.entity.Message.MessageType;

public interface MessageService {

	/**
	 * 执行一个消息
	 * 
	 * @param messageId
	 * @param action
	 */
	public void actionMessage(String messageId, MessageAction action);

	/**
	 * 查询某用户的总消息数
	 * 
	 * @param accountId
	 * @param catalogId
	 *            消息类型
	 * @return
	 */
	public long countMessageByAccountId(String accountId,
			Message.CatalogId catalogId);

	public long countNormalMessage(String accountId, Message.MessageType type,
			Map<String, Object> attributes);

	/**
	 * 删除消息
	 * 
	 * @param messageId
	 */
	public void delete(String messageId);

	public void delete(String accountId, MessageType binding);

	/**
	 * 删除全部消息
	 * 
	 * @param accountId
	 */
	public void deleteAll(String accountId);

	/**
	 * 删除重复消息
	 * 
	 * @param accountId
	 * @param friendId
	 */
	public void deleteByAccountIdAndFriendId(String accountId, String friendId);

	/**
	 * 获取消息列表
	 * 
	 * @param accountId
	 * @param catalogId
	 *            消息类型
	 * @return
	 */
	public List<Message> getMessages(String accountId,
			Message.CatalogId catalogId,String displayType,int limit, int offset);

	/**
	 * 增加一个消息
	 * 
	 * @param message
	 */
	public void save(Message message);
	
	
	public void deleteForAttribute(String accountId,MessageType binding,Map<String,String> attribute);
}
