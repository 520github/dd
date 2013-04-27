package me.twocoffee.service.rpc;

import java.util.List;

import me.twocoffee.common.search.PagedResult;
import me.twocoffee.rest.entity.AccountInfo;
import me.twocoffee.rest.entity.FriendLogInfo;
import me.twocoffee.rest.generic.JsonObject;

public interface FriendRpcService {
	boolean accept(String token, String friendId);

	boolean add(String token, String friendId);

	boolean add(String token, String friendId, String alias, String message,
			String thirdType);

	List<AccountInfo> findFavoriteFriend(String token);

	List<AccountInfo> findFriend(String token);

	List<AccountInfo> findFriendLastTimeUsed(String token);

	List<FriendLogInfo> findFriendLog(String token);

	PagedResult<JsonObject> getFriendMessage(String authToken, String catalog,
			int limit,
			int offset);

	boolean reject(String token, String friendId);

	boolean removeFriend(String token, String friendId);

}
