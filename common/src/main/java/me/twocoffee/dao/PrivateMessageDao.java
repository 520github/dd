/**
 * 
 */
package me.twocoffee.dao;

import java.util.List;

import me.twocoffee.entity.PrivateMessage;

/**
 * @author momo
 * 
 */
public interface PrivateMessageDao {

	long countMessageWithUUID(String id, String uuid);

	void deleteMessage(String id);

	void save(List<PrivateMessage> messages);

	void save(PrivateMessage message);

	public long countMessages(String sessionId);

	public List<PrivateMessage> getLatestMessagesBysessionId(String sessionId,
			String startId);

	public List<PrivateMessage> getMessagesBysessionId(String sessionId);

	public List<PrivateMessage> getMessagesBysessionId(String sessionId,
			int offset, int limit);

	public int getMessagesBySessionIdAndFriendId(String sessionId,String friendId);

}
