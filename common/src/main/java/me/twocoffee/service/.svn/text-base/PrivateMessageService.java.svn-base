/**
 * 
 */
package me.twocoffee.service;

import java.util.List;

import me.twocoffee.entity.PrivateMessage;
import me.twocoffee.entity.PrivateMessageSession;
import me.twocoffee.exception.SendSameMessageInSessionException;
import me.twocoffee.service.entity.ResponseSession;

/**
 * 悄悄话业务逻辑
 * 
 * @author momo
 * 
 */
public interface PrivateMessageService {

	long countMessagesInSession(String sessionId);

	/**
	 * 获取指定用户的会话数目
	 * 
	 * @param accountId
	 * @return
	 */
	long countSessions(String accountId);

	/**
	 * 获取指定用户的指定内容的会话数目
	 * 
	 * @param accountId
	 * @param contentId
	 * @return
	 */
	long countSessions(String accountId, String contentId);

	void creatPrivateMessageSession(PrivateMessageSession pms);

	boolean deleteSession(String sessionId);

	/**
	 * 删除指定用户关于指定内容的悄悄话
	 * 
	 * @param owner
	 * @param target
	 * @param contentId
	 */
	void deleteSession(String owner, String target, String contentId);

	boolean deleteSessions(String accountId);

	List<PrivateMessage> getLatestMessagesInSession(String sessionId,
			String prevousId);

	List<PrivateMessage> getMessagesInSession(String sessionId);

	List<PrivateMessage> getMessagesInSession(String sessionId, int offset,
			int limit);

	/**
	 * 获取指定会话的消息列表
	 * 
	 * @param owner
	 *            会话拥有者id
	 * @param target
	 *            会话目标id
	 * @param contentId
	 *            内容id
	 * @param sinceId
	 *            指定消息id，返回id大于它的消息
	 * @return
	 */
	List<PrivateMessage> getMessagesInSession(String owner, String target,
			String contentId, String sinceId);

	PrivateMessageSession getSession(String sessionId);

	/**
	 * 获取指定帐户间关于某个内容的会话
	 * 
	 * @param ownerId
	 * @param accountId
	 * @param contentId
	 * @return
	 */
	PrivateMessageSession getSession(String ownerId, String accountId,
			String contentId);

	/**
	 * 获取指定用户的会话列表
	 * 
	 * @param accountId
	 * @return
	 */
	List<PrivateMessageSession> getSessions(String accountId);

	/**
	 * 获取指定用户的会话列表 分页
	 * 
	 * @param accountId
	 * @return
	 */
	List<PrivateMessageSession> getSessions(String accountId, int offset,
			int limit);

	/**
	 * 获取指定用户的指定内容的会话列表 分页
	 * 
	 * @param accountId
	 * @param contentId
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<PrivateMessageSession> getSessions(String accountId, String contentId,
			int offset, int limit);

	/**
	 * 发送消息。 发送者id、接受者id、内容id、消息内容必填；
	 * 
	 * @param message
	 *            消息实体
	 * @param contentId
	 *            内容ID
	 * @param sendCommentNotify
	 *            是否发送通知
	 * @return
	 */
	void sendMessage(PrivateMessage message, String contentId,
			boolean sendCommentNotify) throws SendSameMessageInSessionException;

	/**
	 * 发布通知事件
	 * 
	 * @param accountId
	 * @param targetAccountId
	 */
	// void sendMessageEvent(String accountId, String targetAccountId);

	public abstract long countMessage(String accountId, String targetId,
			String contentId);

	/**
	 * 获取某一内容下所有session
	 * @param accountId
	 * @param contentId
	 * @param offset
	 * @param limit
	 * @return
	 */
	public List<ResponseSession> getSessionsForContentId(String accountId,
			String contentId, int offset, int limit); 
	
}
