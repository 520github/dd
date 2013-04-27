package me.twocoffee.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.twocoffee.common.BaseDao;
import me.twocoffee.common.ScoreAlgorithm;
import me.twocoffee.common.util.Pinyin4jUtils;
import me.twocoffee.dao.FriendDao;
import me.twocoffee.entity.ContentAction;
import me.twocoffee.entity.ContentAction.ActionType;
import me.twocoffee.entity.Friend;
import me.twocoffee.entity.Friend.FriendType;
import me.twocoffee.entity.FriendLog;
import me.twocoffee.entity.FriendLog.LogType;
import me.twocoffee.entity.Repository;
import me.twocoffee.entity.SystemTagEnum;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.google.code.morphia.query.Query;

@Service
public class FriendDaoImpl extends BaseDao<Friend> implements FriendDao {
	private void saveFriend(Friend f) {
		if (f.getId() == null || f.getId().equals("")) {
			f.setId(new ObjectId().toString());
		}

		dataStore.save(f);
	}

	private void saveLog(FriendLog log) {
		if (log.getId() == null || log.getId().equals("")) {
			log.setId(new ObjectId().toString());
		}

		dataStore.save(log);
	}

	@Override
	public boolean addFriendLog(String accountId, String friendId,
			String remark, String postscript) {
		FriendLog log = new FriendLog();
		log.setAccountId(accountId);
		log.setCreateTime(new Date());
		log.setFriendId(friendId);
		log.setLogType(LogType.Adding);
		log.setRemarkName(remark);
		log.setPostscript(postscript);

		saveLog(log);

		return log.getId() != null;
	}

	public boolean addFriendWithoutLog(String accountId, String friendId) {
		Date dtNow = new Date();

		Friend f = new Friend();
		f.setAccountId(accountId);
		f.setFriendId(friendId);
		f.setCreated(dtNow);
		f.setHot(0);
		saveFriend(f);

		f = new Friend();
		f.setAccountId(friendId);
		f.setFriendId(accountId);
		f.setCreated(dtNow);
		f.setHot(0);
		saveFriend(f);
		return f.getId() != null;
	}

	@Override
	public int countByAccountId(String id) {
		return (int) createQuery().filter("accountId =", id).countAll();
	}

	@Override
	public int countSharedByAccountId(String id) {
		return (int) dataStore.createQuery(ContentAction.class)
				.filter("accountId =", id)
				.filter("action =", ActionType.Friend).countAll();
	}

	@Override
	public int countSharedTo(String accountId, String friendId) {
		List<String> accounts = new ArrayList<String>();
		accounts.add(friendId);
		accounts.add(accountId);
		return (int) dataStore.createQuery(Repository.class)
				.filter("accountId in", accounts)
				.filter("fromFriends.friendId in", accounts)
				.filter("tag nin", SystemTagEnum.Delete)
				.countAll();
	}

	@Override
	public void favoriteFriend(String accountId, String friendId,
			String actionFavorite) {
		Friend f = getByAccountIdAndFriendId(accountId, friendId);
		if (f != null) {
			if ("Favorite".equals(actionFavorite)) {
				f.setFriendType(FriendType.Favorite);
			} else if ("Normal".equals(actionFavorite)) {
				f.setFriendType(FriendType.Normal);
			}
			this.save(f);
		}

		FriendLog log = new FriendLog();
		log.setAccountId(accountId);
		log.setCreateTime(new Date());
		log.setFriendId(friendId);
		// TODO YUXJ FriendLog该如何记
		log.setLogType(LogType.Removed);

		saveLog(log);
	}

	public List<Friend> findByAccountIdAndFriendId(String accountId,
			String friendId) {
		return createQuery().filter("accountId =", accountId)
				.filter("friendId =", friendId)
				.asList();
	}

	@Override
	public List<Friend> findFavoriteFriends(String accountId) {
		return createQuery().filter("accountId =", accountId)
				.filter("friendType =", "Favorite").order("hot, -created")
				.asList();
	}

	@Override
	public List<FriendLog> findFriendLog(String accountId) {
		Query<FriendLog> q = dataStore.createQuery(FriendLog.class);
		q.or(
				q.criteria("accountId").equal(accountId),
				q.criteria("friendId").equal(accountId)
				);
		return q.order("-createTime").asList();
	}

	@Override
	public FriendLog findFriendLogById(String id) {
		return dataStore.createQuery(FriendLog.class)
				.filter("id =", id).get();
	}

	@Override
	public List<Friend> findFriends(String accountId) {
		return createQuery().filter("accountId =", accountId)
				.order("hot, -created").asList();
	}

	public Friend getByAccountIdAndFriendId(String accountId, String friendId) {
		return createQuery().filter("accountId =", accountId)
				.filter("friendId =", friendId)
				.get();
	}

	@Override
	public FriendLog getLogByAccountIdAndFriendId(String accountId,
			String friendId) {
		return dataStore.createQuery(FriendLog.class)
				.filter("accountId =", accountId)
				.filter("friendId =", friendId)
				.filter("logType =", LogType.Adding)
				.get();
	}

	@Override
	public void modifyFriendLog(FriendLog friendLog) {
		saveLog(friendLog);
	}

	@Override
	public List<Friend> pageByAccountId(int offset, int limit, String accountId) {
		return dataStore.createQuery(Friend.class)
				.filter("accountId", accountId).offset(offset).limit(limit)
				.asList();
	}

	@Override
	public boolean removeFriend(String accountId, String friendId, String remark) {
		Friend f = getByAccountIdAndFriendId(accountId, friendId);
		if (f != null) {
			delete(f.getId());
		}
		f = getByAccountIdAndFriendId(friendId, accountId);
		if (f != null) {
			delete(f.getId());
		}

		FriendLog log = new FriendLog();
		log.setAccountId(accountId);
		log.setCreateTime(new Date());
		log.setFriendId(friendId);
		log.setLogType(LogType.Removed);

		saveLog(log);
		return log.getId() != null;
	}

	@Override
	public void removeRepeatFriend(String accountId, String friendId, int index) {
		List<Friend> list = findByAccountIdAndFriendId(accountId, friendId);
		if (list != null && list.size() > index) {
			int len = list.size();
			for (int i = index; i < len; i++) {
				delete(list.get(i).getId());
			}
		}

		list = findByAccountIdAndFriendId(friendId, accountId);
		if (list != null && list.size() > index) {
			int len = list.size();
			for (int i = index; i < len; i++) {
				delete(list.get(i).getId());
			}
		}
	}

	@Override
	public boolean replyFriendLog(String friendLogId, String accountId,
			int reply) {
		FriendLog log = dataStore.createQuery(FriendLog.class)
				.filter("id =", friendLogId).get();
		if (log == null || !log.getFriendId().equals(accountId)) {
			return false;
		}

		if (log.getLogType() == LogType.AddFinish) {
			return true;
		}

		Friend f = getByAccountIdAndFriendId(log.getAccountId(),
				log.getFriendId());
		if (f != null) {
			return true;
		}

		Date dtNow = new Date();
		log.setFinishTime(dtNow);
		if (reply == 2) {
			log.setLogType(LogType.AddRefuse);
			saveLog(log);
			return log.getId() != null;
		}

		log.setLogType(LogType.AddFinish);
		saveLog(log);

		f = new Friend();
		f.setAccountId(log.getAccountId());
		f.setFriendId(log.getFriendId());
		f.setCreated(dtNow);
		f.setHot(0);
		f.setRemarkName(log.getRemarkName());
		f.setRemarkNameInPinyin(Pinyin4jUtils.getPinYin(log.getRemarkName()));
		saveFriend(f);

		f = new Friend();
		f.setAccountId(log.getFriendId());
		f.setFriendId(log.getAccountId());
		f.setCreated(dtNow);
		f.setHot(0);
		// 备注只是单方面的，不能双方都一样
		// f.setRemarkName(log.getRemarkName());
		// f.setRemarkNameInPinyin(Pinyin4jUtils.getPinYin(log.getRemarkName()));
		saveFriend(f);
		return log.getId() != null;
	}

	
	
	
	@Override
	public void update(Friend friend) {
		this.save(friend);
	}

	@Override
	public void increasScore(Friend friend) {
		double score = 0;
		if(friend!=null){
		Date date = friend.getLastModify();
		if(date==null){
			date = new Date();
		}
		score = ScoreAlgorithm.getScore(friend.getShareCount(),date.getTime());
		}
		friend.setScore(score);
		this.save(friend);
	}

	@Override
	public List<Friend> getLatestFrequently(String accountId,int offset,int limit) {
		Query<Friend> query = dataStore.createQuery(Friend.class).field("accountId").equal(accountId);
		return query.offset(offset).limit(limit).order("-score").asList();
	}

}
