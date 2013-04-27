package me.twocoffee.dao;

import java.util.List;

import me.twocoffee.entity.Friend;
import me.twocoffee.entity.FriendLog;

public interface FriendDao {

	boolean addFriendLog(String accountId, String friendId, String remark,
			String postscript);

	boolean addFriendWithoutLog(String accountId, String friendId);

	int countByAccountId(String id);

	int countSharedByAccountId(String id);

	int countSharedTo(String accountId, String friendId);

	void favoriteFriend(String accountId, String friendId, String actionFavorite);

	List<Friend> findFavoriteFriends(String accountId);

	List<FriendLog> findFriendLog(String accountId);

	FriendLog findFriendLogById(String id);

	List<Friend> findFriends(String accountId);

	Friend getByAccountIdAndFriendId(String accountId, String friendId);

	FriendLog getLogByAccountIdAndFriendId(String accountId, String friendId);

	void modifyFriendLog(FriendLog friendLog);

	boolean removeFriend(String accountId, String friendId, String remark);

	void removeRepeatFriend(String accountId, String friendId, int index);

	boolean replyFriendLog(String friendLogId, String accountId, int reply);

	void update(Friend friend);

	public List<Friend> pageByAccountId(int offset, int limit, String accountId);
	
	void increasScore(Friend friend);
    
	List<Friend> getLatestFrequently(String accountId,int offset,int limit);
	
}
