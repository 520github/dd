package me.twocoffee.service;

import java.util.List;

import me.twocoffee.entity.Account;
import me.twocoffee.entity.Friend;
import me.twocoffee.entity.FriendLog;
import me.twocoffee.entity.FriendSharedLog;

/**
 * 好友服务
 * 
 * @author drizzt
 * 
 */
public interface FriendService {
	/**
	 * 同意添加好友
	 * 
	 * @param friendLogId
	 *            好友邀请记录ID
	 * @param accoundId
	 *            回复的帐号ID
	 */
	boolean acceptFriendRequest(String friendLogId, String accountId);

	/**
	 * 添加好友申请
	 * 
	 * @param accountId
	 *            用户ID
	 * @param friendId
	 *            好友ID
	 * @param remark
	 *            备注
	 * @param thirdpartyType
	 */
	int addFriend(String accountId, String friendId, String remark,
			String postscript, String thirdpartyType);

	/**
	 * 直接添加好友
	 * 
	 * @param id
	 * @param id2
	 * @return
	 */
	boolean addFriendWithoutLog(String id, String id2);

	/**
	 * 得到朋友数
	 * 
	 * @param id
	 * @return
	 */
	int countByAccountId(String id);

	/**
	 * 得到分享数
	 * 
	 * @param id
	 * @return
	 */
	int countSharedByAccountId(String id);

	/**
	 * 得到朋友分享给我的文章数(从friendId分享到accountId)
	 * 
	 * @param accountId
	 * @param friendId
	 * @return
	 */
	int countSharedTo(String accountId, String friendId);

	/**
	 * 处理常用好友逻辑
	 * 
	 * @param ownerId
	 * @param targetId
	 * @param actionFavorite
	 * @return
	 */
	int dealFavorite(String ownerId, String targetId, String actionFavorite);

	/**
	 * 查找当前用户的常用好友
	 * 
	 * @param accountId
	 * @return
	 */
	List<Account> findFavoriteFriends(String accountId);

	/**
	 * 查找挡墙好友，并返回好友信息
	 * 
	 * @param accountId
	 * @return
	 */
	List<Friend> findFriendInfos(String accountId);

	/**
	 * 返回好友申请记录
	 * 
	 * @param accountId
	 * @return
	 */
	List<FriendLog> findFriendLog(String accountId);

	/**
	 * 查找当前用户的好友
	 * 
	 * @param accountId
	 * @return
	 */
	List<Account> findFriends(String accountId);

	/**
	 * 得到此文章共享信息
	 * 
	 * @param accountId
	 * @param repositoryId
	 * @param friendIds
	 * @return
	 */
	List<Boolean> findSharedInfo(String accountId, String contentId,
			List<String> friendIds);

	/**
	 * 得到朋友信息
	 * 
	 * @param accountId
	 * @param friendId
	 * @return
	 */
	Friend getByAccountIdAndFriendId(String accountId, String friendId);

	/**
	 * 从账户id和好友id得到添加申请的记录
	 * 
	 * @param accountId
	 * @param friendId
	 * @return
	 */
	FriendLog getLogByAccountIdAndFriendId(String accountId, String friendId);

	List<String> pageByAccountId(int offset, int limit, String accountId);

	/**
	 * 拒绝添加好友
	 * 
	 * @param friendLogId
	 *            好友邀请记录ID
	 * @param accoundId
	 *            回复的帐号ID
	 */
	boolean rejectFriendRequest(String friendLogId, String accountId);

	/**
	 * 删除好友
	 * 
	 * @param accountId
	 *            帐号ID
	 * @param friendId
	 *            好友ID
	 * @param remark
	 *            备注
	 */
	boolean removeFriend(String accountId, String friendId, String remark);

	/**
	 * 共享文章给好友
	 * 
	 * @param accountId
	 *            当前用户ID
	 * @param repositoryId
	 *            文章ID
	 * @param friends
	 *            共享的好友列表
	 * @param grade
	 *            评分，为负数，则不评分
	 * @param commend
	 *            评论
	 * @return 是否成功
	 */
        // TODO 评分可以清理掉了。这个Feature已经没有了
	boolean shareToFriends(String accountId, String repositoryId,
			List<String> friends, int grade, String commend);

	/**
	 * 共享文章给好友
	 * 
	 * @param accountId
	 *            当前用户ID
	 * @param repositoryId
	 *            文章ID
	 * @param friends
	 *            共享的好友列表
	 * @param grade
	 *            评分，为负数，则不评分
	 * @param commend
	 *            评论
	 * @return 是否成功
	 */
	// TODO 评分可以清理掉了。这个Feature已经没有了
	boolean shareToFriendsWriteDB(String accountId, String repositoryId,
			List<String> friends, int grade, String comment);

	/**
	 * 更新好友信息
	 * 
	 * @param friend
	 */
	void update(Friend friend);

	/**
	 * 获取最近联系人列表
	 * 
	 * @param accountId
	 * @param direction
	 *            排序方式
	 * @param offset
	 * @param limit
	 * @return
	 */
	public List<Account> getLatestFrequently(String accountId, int offset,
			int limit);

	/**
	 * 更新重力算法结果
	 * 
	 * @param friend
	 */
	public void increasScore(Friend friend);
	
	
	/**
	 * 保存好友分享记录
	 * @param friendSharedLog
	 */
	public void saveFriendSharedLog(FriendSharedLog friendSharedLog) ;
	
	
	/**
	 * 获取好友分享记录
	 * @param accountId
	 * @return
	 */
	public FriendSharedLog getFriendSharedLogByAccountId(String accountId);
}
