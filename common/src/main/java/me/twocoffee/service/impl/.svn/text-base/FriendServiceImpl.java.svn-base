package me.twocoffee.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.twocoffee.common.mq.MessageQueueSender;
import me.twocoffee.dao.AccountDao;
import me.twocoffee.dao.FriendDao;
import me.twocoffee.dao.FriendSharedLogDao;
import me.twocoffee.dao.PrivateMessageDao;
import me.twocoffee.dao.PrivateMessageSessionDao;
import me.twocoffee.dao.RepositoryDao;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.Content;
import me.twocoffee.entity.ContentAction;
import me.twocoffee.entity.ContentAction.ActionType;
import me.twocoffee.entity.Friend;
import me.twocoffee.entity.FriendLog;
import me.twocoffee.entity.FriendSharedLog;
import me.twocoffee.entity.PrivateMessage;
import me.twocoffee.entity.Repository;
import me.twocoffee.entity.Repository.FriendShare;
import me.twocoffee.entity.SystemTagEnum;
import me.twocoffee.service.ContentSearcher;
import me.twocoffee.service.ContentService;
import me.twocoffee.service.FriendService;
import me.twocoffee.service.PrivateMessageService;
import me.twocoffee.service.RepositoryService;
import me.twocoffee.service.TagService;
import me.twocoffee.service.event.AcceptFriendEvent;
import me.twocoffee.service.event.RecommendedByFriendEvent;
import me.twocoffee.service.event.SendFriendRequestEvent;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class FriendServiceImpl implements FriendService,
	ApplicationContextAware {
    protected static final Logger LOGGER = LoggerFactory
	    .getLogger(FriendServiceImpl.class);
    @Autowired
    private FriendDao friendDao;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private RepositoryDao repositoryDao;

    @Autowired
    private TagService tagService;

    @Autowired
    private ContentSearcher contentSearcher;

    @Autowired
    private ContentService contentService;

    @Autowired
    private PrivateMessageSessionDao privateMessageSessionDao;

    @Autowired
    private PrivateMessageDao privateMessageDao;

    @Autowired
    private PrivateMessageService privateMessageService;

    @Autowired
    private FriendSharedLogDao friendSharedLogDao;

    @Autowired
    private MessageQueueSender queueSender;

    @Autowired
    private RepositoryService repositoryService;

    private final SystemTagEnum[] includeTags = new SystemTagEnum[] {
	    SystemTagEnum.Type_Web, SystemTagEnum.Type_HtmlClip,
	    SystemTagEnum.Type_Image,
	    SystemTagEnum.Type_Product, SystemTagEnum.Type_Video,
	    SystemTagEnum.Type_File, SystemTagEnum.Original_None,
    };

    private ApplicationContext applicationContext;

    private void acceptFriendRequestEvent(String accountId, String friendId) {
	AcceptFriendEvent e = new AcceptFriendEvent(this, accountId, friendId);
	applicationContext.publishEvent(e);
    }

    private void addFriendMessageEvent(String accountId, String friendId,
	    String thirdType) {
	SendFriendRequestEvent e = new SendFriendRequestEvent(this, accountId,
		friendId, thirdType);
	applicationContext.publishEvent(e);
    }

    private void addFriendTag(Repository dr, List<String> tag) {
	dr.getTag().add(
		tagService.getSystemTagName(SystemTagEnum.Source_Friend));
	if (!hasFriendTag(tag, SystemTagEnum.Unread)) {
	    dr.getTag().add(tagService.getSystemTagName(SystemTagEnum.Unread));
	}
    }

    private List<String> createFriendIdList(List<Friend> list) {
	List<String> ids = new ArrayList<String>();
	for (Friend f : list) {
	    ids.add(f.getFriendId());
	}
	return ids;
    }

    private List<String> createShareTags(List<String> oldTags) {
	List<String> tags = new ArrayList<String>();
	tags.add(tagService.getSystemTagName(SystemTagEnum.Source_Friend));
	tags.add(tagService.getSystemTagName(SystemTagEnum.Unread));
	for (SystemTagEnum t : includeTags) {
	    for (String ts : oldTags) {
		String s = tagService.getSystemTagName(t);
		if (ts.equals(s)) {
		    tags.add(s);
		    break;
		}
	    }
	}
	return tags;
    }

    private FriendShare findFriend(List<FriendShare> fromFriends,
	    String accountId) {
	if (fromFriends == null || fromFriends.size() < 1) {
	    return null;
	}
	for (FriendShare f : fromFriends) {
	    if (f.getFriendId().equals(accountId)) {
		return f;
	    }
	}
	return null;
    }

    private boolean hasFriendTag(List<String> tag, SystemTagEnum friendTag) {
	if (tag == null || tag.size() < 1) {
	    return true;
	}
	String shared = tagService.getSystemTagName(friendTag);
	for (String t : tag) {
	    if (t.equals(shared)) {
		return true;
	    }
	}
	return false;
    }

    private boolean isFirstShared(List<String> tag) {
	if (tag == null || tag.size() < 1) {
	    return true;
	}
	String shared = tagService.getSystemTagName(SystemTagEnum.My_Recommend);
	for (String t : tag) {
	    if (t.equals(shared)) {
		return false;
	    }
	}
	return true;
    }

    private void removeRepeatFriends(List<Friend> list, List<Account> friends) {
	List<String> ids = new ArrayList<String>();
	for (Friend f : list) {
	    boolean find = false;
	    if (friends != null) {
		for (Account a : friends) {
		    if (a.getId().equals(f.getFriendId())) {
			find = true;
			break;
		    }
		}
		if (!find) {
		    friendDao.removeRepeatFriend(f.getAccountId(),
			    f.getFriendId(), 0);
		}
	    }
	    find = false;
	    for (String id : ids) {
		if (id.equals(f.getFriendId())) {
		    find = true;
		    break;
		}
	    }
	    if (!find) {
		ids.add(f.getFriendId());
	    } else {
		friendDao.removeRepeatFriend(f.getAccountId(), f.getFriendId(),
			1);
	    }
	}
    }

    @Override
    public boolean acceptFriendRequest(String friendLogId, String accountId) {
	FriendLog log = friendDao.findFriendLogById(friendLogId);
	boolean flag = friendDao.replyFriendLog(friendLogId, accountId, 1);
	acceptFriendRequestEvent(log.getAccountId(), log.getFriendId());
	return flag;

    }

    @Override
    public int addFriend(String accountId, String friendId, String remark,
	    String postscript, String thirdpartyType) {
	FriendLog friendLog = null;
	if ((friendLog = friendDao.getLogByAccountIdAndFriendId(accountId,
		friendId)) != null) {
	    friendLog.setRemarkName(remark);
	    friendLog.setPostscript(postscript);
	    friendDao.modifyFriendLog(friendLog);
	    return -2;
	}
	if (friendDao.getByAccountIdAndFriendId(accountId, friendId) != null) {
	    return -3;
	}
	int r = friendDao.addFriendLog(accountId, friendId, remark, postscript) ? 0
		: -1;
	if (r == 0) {
	    addFriendMessageEvent(accountId, friendId, thirdpartyType);
	}
	return r;
    }

    @Override
    public boolean addFriendWithoutLog(String accountId, String friendId) {
	return friendDao.addFriendWithoutLog(accountId, friendId);
    }

    @Override
    public int countByAccountId(String id) {
	return friendDao.countByAccountId(id);
    }

    @Override
    public int countSharedByAccountId(String id) {
	return friendDao.countSharedByAccountId(id);
    }

    @Override
    public int countSharedTo(String accountId, String friendId) {
	return friendDao.countSharedTo(accountId, friendId);
    }

    @Override
    public int dealFavorite(String ownerId, String targetId,
	    String actionFavorite) {
	friendDao.favoriteFriend(ownerId, targetId, actionFavorite);
	return 0;
    }

    @Override
    public List<Account> findFavoriteFriends(String accountId) {
	List<Friend> list = friendDao.findFavoriteFriends(accountId);
	if (list == null || list.size() < 1) {
	    return null;
	}

	List<String> ids = createFriendIdList(list);
	List<Account> friends = accountDao.findByAccountIdList(ids);
	if (friends == null || friends.size() != list.size()) {
	    removeRepeatFriends(list, friends);
	}
	return friends;
    }

    @Override
    public List<Friend> findFriendInfos(String accountId) {
	List<Friend> list = friendDao.findFriends(accountId);
	if (list == null || list.size() < 1) {
	    return null;
	}

	return list;
    }

    @Override
    public List<FriendLog> findFriendLog(String accountId) {
	return friendDao.findFriendLog(accountId);
    }

    @Override
    public List<Account> findFriends(String accountId) {
	List<Friend> list = friendDao.findFriends(accountId);
	if (list == null || list.size() < 1) {
	    return null;
	}

	List<String> ids = createFriendIdList(list);
	List<Account> friends = accountDao.findByAccountIdList(ids);
	if (friends == null || friends.size() != list.size()) {
	    removeRepeatFriends(list, friends);
	}
	return friends;
    }

    @Override
    public List<Boolean> findSharedInfo(String accountId, String contentId,
	    List<String> friendIds) {
	if (friendIds == null || friendIds.size() < 1) {
	    return null;
	}
	List<Repository> list = repositoryDao.findBySharedFriends(accountId,
		contentId, friendIds);
	List<Boolean> fes = new ArrayList<Boolean>();
	for (String f : friendIds) {
	    boolean b = false;
	    if (list != null) {
		for (Repository r : list) {
		    if (r.getAccountId().equals(f)) {
			b = true;
			break;
		    }
		}
	    }
	    fes.add(b);
	}
	return fes;
    }

    @Override
    public Friend getByAccountIdAndFriendId(String accountId, String friendId) {
	return friendDao.getByAccountIdAndFriendId(accountId, friendId);
    }

    @Override
    public List<Account> getLatestFrequently(String accountId, int offset,
	    int limit) {
	List<Friend> list = friendDao.getLatestFrequently(accountId,
		offset, limit);
	List<Account> accountList = new ArrayList<Account>();
	if (list != null) {
	    for (Friend friend : list) {
		Account account = accountDao.getById(friend.getFriendId());
		accountList.add(account);
	    }
	}

	return accountList;
    }

    @Override
    public FriendLog getLogByAccountIdAndFriendId(String accountId,
	    String friendId) {
	return friendDao.getLogByAccountIdAndFriendId(accountId, friendId);
    }

    @Override
    public void increasScore(Friend friend) {
	friendDao.increasScore(friend);
    }

    @Override
    public List<String> pageByAccountId(int offset, int limit, String accountId) {
	List<Friend> list = friendDao.pageByAccountId(offset, limit, accountId);
	List<String> ids = new ArrayList<String>();
	if (list != null) {
	    for (Friend f : list) {
		ids.add(f.getFriendId());
	    }
	}
	return ids;
    }

    @Override
    public boolean rejectFriendRequest(String friendLogId, String accountId) {
	return friendDao.replyFriendLog(friendLogId, accountId, 2);
    }

    @Override
    public boolean removeFriend(String accountId, String friendId, String remark) {
	return friendDao.removeFriend(accountId, friendId, remark);
    }

    @Override
    public void setApplicationContext(ApplicationContext c)
	    throws BeansException {
	applicationContext = c;
    }

    @Override
    public boolean shareToFriends(String accountId, String repositoryId,
	    List<String> friends, int grade, String comment) {
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("accountId", accountId);
	map.put("repositoryId", repositoryId);
	map.put("friends", friends);
	map.put("grade", grade);
	map.put("comment", comment);
	queueSender.send("friendShareQueue", map, 30000);
	return true;
    }

    @Override
    public boolean shareToFriendsWriteDB(String accountId, String repositoryId,
	    List<String> friends, int grade, String comment) {
	LOGGER.warn("Share to friend.accountId=" + accountId + ",repositoryId="
		+ repositoryId);

	if (friends == null || friends.size() < 1) {
	    return false;
	}

	Repository r = repositoryDao.getById(repositoryId);
	if (r == null) {
	    return false;
	}

	if (isFirstShared(r.getTag())) {
	    r.getTag().add(
		    tagService.getSystemTagName(SystemTagEnum.My_Recommend));
	    r.setLastModified(new Date());
	    repositoryDao.save(r);
	}
	Date dtNow = new Date();
	List<String> ids = new ArrayList<String>();
	String sid = r.getId();
	// need send comment notify
	boolean sendCommentNotify = false;

	for (String fid : friends) {
	    Repository dr = repositoryDao.getByContentIdAndAccountId(
		    r.getContentId(), fid);
	    // 更新friend表中最后分享时间以及分享次数
	    Friend friend = friendDao.getByAccountIdAndFriendId(accountId, fid);

	    if (friend == null) {
		return false;
	    }
	    friend.setLastModify(new Date());
	    int shareCount = friend.getShareCount();
	    friend.setShareCount(++shareCount);
	    friendDao.update(friend);

	    if (dr == null) {
		dr = r;
		dr.setId(null);
		dr.setAccountId(fid);
		dr.setFromFriends(new ArrayList<FriendShare>());
		Repository.FriendShare fs = new Repository.FriendShare();
		fs.setFriendId(accountId);
		fs.setShareTime(dtNow);
		fs.setComment(comment);
		fs.setScore(grade);
		dr.getFromFriends().add(fs);
		dr.setTag(createShareTags(r.getTag()));
		dr.setUserTag(null);
		dr.setDate(dtNow);

	    } else {
		dr.setDate(new Date());
		dr.setLastModified(dr.getDate());
		if (dr.getFromFriends() == null) {
		    dr.setFromFriends(new ArrayList<FriendShare>());
		}
		if (!hasFriendTag(dr.getTag(), SystemTagEnum.Source_Friend)) {
		    addFriendTag(dr, dr.getTag());
		}
		Repository.FriendShare fs = findFriend(dr.getFromFriends(),
			accountId);
		if (fs == null) {
		    fs = new Repository.FriendShare();
		    fs.setFriendId(accountId);
		    fs.setShareTime(dtNow);
		    fs.setComment(comment);
		    fs.setScore(grade);
		    dr.getFromFriends().add(fs);
		} else {
		    fs.setShareTime(dtNow);
		    fs.setComment(comment);
		    fs.setScore(grade);
		}
	    }

	    if (StringUtils.isBlank(dr.getShareLink())) {
		repositoryService.updateShareLink(dr);
	    }

	    // 添加TA和我的分享TAG TAG类型为LinkA:B
	    if (!repositoryService.isContainTag(dr.getTag(), "Link_"
		    + accountId + "_" + fid)) {
		dr.getTag().add("Link_" + accountId + "_" + fid);
	    }
	    repositoryDao.save(dr);

	    ids.add(dr.getId());

	    ContentAction ca = new ContentAction();
	    ca.setAccountId(accountId);
	    ca.setToId(fid);
	    ca.setContentId(dr.getContentId());
	    ca.setAction(ActionType.Friend);
	    ca.setActionTime(dtNow);
	    ca.setRating(grade);
	    ca.setRemark(comment);

	    contentService.saveContentAction(ca);

	    RecommendedByFriendEvent e = new RecommendedByFriendEvent(this,
		    accountId, fid, dr.getContentId());
	    try {
		applicationContext.publishEvent(e);
	    } catch (Exception ee) {
		LOGGER.error("Friend Share PublishEvent exception:", ee);
	    }

	    PrivateMessage pm = new PrivateMessage();
	    pm.setDate(dtNow);
	    pm.setAccountId(accountId);
	    pm.setMessage(comment);
	    pm.setToAccountId(fid);
	    // 不会发送评论通知
	    privateMessageService.sendMessage(pm, r.getContentId(),
		    sendCommentNotify);
	}

	ids.add(sid);

	Content c = contentService.getById(r.getContentId());
	int count = 0;
	if (c.getCounter() != null) {
	    count = c.getCounter().getShare();
	}

	count += friends.size();
	if (c.getCounter() == null) {
	    c.setCounter(new Content.Counter());
	}
	c.getCounter().setShare(count);
	contentService.save(c);
	contentSearcher.addIndexs(ids.toArray(new String[0]));

	// 保存分享日志

	FriendSharedLog friendSharedLog = getFriendSharedLogByAccountId(accountId);
	if (friendSharedLog == null) {
	    friendSharedLog = new FriendSharedLog();
	}
	friendSharedLog.setFriendId(friends);
	friendSharedLog.setSharedTime(new Date());
	friendSharedLog.setAccountId(accountId);

	saveFriendSharedLog(friendSharedLog);

	return true;
    }

    @Override
    public void saveFriendSharedLog(FriendSharedLog friendSharedLog) {
	friendSharedLogDao.save(friendSharedLog);
    }

    @Override
    public FriendSharedLog getFriendSharedLogByAccountId(String accountId) {
	return friendSharedLogDao.findByAccountId(accountId);
    }

    @Override
    public void update(Friend friend) {
	friendDao.update(friend);
    }

}
