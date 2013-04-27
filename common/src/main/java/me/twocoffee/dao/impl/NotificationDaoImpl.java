package me.twocoffee.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.twocoffee.common.BaseDao;
import me.twocoffee.dao.NotificationDao;
import me.twocoffee.entity.Notification;
import me.twocoffee.entity.Notification.Status;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;

@org.springframework.stereotype.Repository
public class NotificationDaoImpl extends BaseDao<Notification> implements
		NotificationDao {

	@Override
	public int countUnRead(String accountId) {
		return (int) dataStore.createQuery(Notification.class)
				.field("accountId").equal(accountId).field("status")
				.equal(Notification.Status.normal).countAll();
	}

	@Override
	public List<Notification> getUnreadNotificationsByPrevousId(
			String accountId, String prevousId,String displayType) {
		List<String> keys = new ArrayList<String>();
		keys.add(Notification.CollapseKey.friend.toString());
		keys.add(Notification.CollapseKey.message.toString());
		keys.add(Notification.CollapseKey.RecommendByFriend.toString());
		keys.add(Notification.CollapseKey.Chat.toString());
		keys.add(Notification.CollapseKey.FriendMessage.toString());
		
		Query<Notification> q = this.dataStore.createQuery(Notification.class)
				.field("accountId").equal(accountId).field("status")
				.equal(Notification.Status.normal).field("id")
				.greaterThan(prevousId);
		if(!StringUtils.isBlank(displayType)){
			return q.filter("key in",keys).asList();
		}else{
			return q.asList();
		}
		
		
	}

	@Override
	public void save(Notification notification) {
		Query<Notification> q = this.dataStore.createQuery(Notification.class)
				.field("accountId").equal(notification.getAccountId())
				.disableValidation()
				.field("key").equal(notification.getKey().name());
		dataStore.delete(q);
		if (notification != null && notification.getId() == null) {
			notification.setId(new ObjectId().toString());
			notification.setDate(new Date());
			notification.setStatus(Status.normal);
		}
		dataStore.save(notification);
	}

	@Override
	public void updateStatus(String id, Status status) {
		UpdateOperations<Notification> u = this.dataStore
				.createUpdateOperations(Notification.class).set("status",
						status);
		Query<Notification> q = this.dataStore.createQuery(Notification.class)
				.field("id").equal(id);
		dataStore.update(q, u);
	}

	@Override
	public Notification findByAccountIdAndKey(String accountId, Notification.CollapseKey key) {
		return this.dataStore.createQuery(Notification.class).field("accountId").equal(accountId).field("key").equal(key.name()).get();
	}

}
