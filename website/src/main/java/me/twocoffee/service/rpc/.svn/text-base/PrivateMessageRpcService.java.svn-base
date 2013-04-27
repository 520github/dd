/**
 * 
 */
package me.twocoffee.service.rpc;

import me.twocoffee.common.search.PagedResult;
import me.twocoffee.entity.PrivateMessage;
import me.twocoffee.entity.PrivateMessageSession;

/**
 * @author momo
 * 
 */
public interface PrivateMessageRpcService {

	int deleteSession(String token, String contentId, String accountId);

	PagedResult<PrivateMessageSession> getPrivateMessageSessions(String token,
			String contentId, int limit, int offset);

	PagedResult<PrivateMessage> getPrivateMessagesInSession(String token,
			String accountid,
			String contentId, String sinceId);

	int sendPrivateMessage(String token, String contentId, String accountId,
			String message, String uuid);
}
