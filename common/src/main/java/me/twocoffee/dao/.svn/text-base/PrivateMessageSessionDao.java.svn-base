/**
 * 
 */
package me.twocoffee.dao;

import java.util.List;

import me.twocoffee.entity.PrivateMessageSession;

/**
 * @author momo
 * 
 */
public interface PrivateMessageSessionDao {

	long countSessions(String accountId);

	long countSessions(String accountId, String contentId);

	void deleteSession(String sessionId);

	String deleteSession(String owner, String target, String contentId);

	void deleteSessions(String accountId);

	PrivateMessageSession find(String fromAccountId, String toAccountId,
			String contentId);

	PrivateMessageSession findById(String sessionId);

	List<PrivateMessageSession> getSessions(String accountId);

	void retsetUnread(String sessionId);

	void save(List<PrivateMessageSession> sessions);

	void save(PrivateMessageSession session);

	public List<PrivateMessageSession> getMessageSessionsByAccountId(
			String accountId, int offset, int limit);

	public List<PrivateMessageSession> getMessageSessionsByAccountIdAndContentId(
			String accountId, String contentId, int offset, int limit);

}
