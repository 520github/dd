/**
 * 
 */
package me.twocoffee.dao.impl;

import java.util.List;

import me.twocoffee.common.BaseDao;
import me.twocoffee.dao.PrivateMessageSessionDao;
import me.twocoffee.entity.PrivateMessageSession;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;

/**
 * @author momo
 * 
 */
@Repository
public class PrivateMessageSessionDaoImpl extends
		BaseDao<PrivateMessageSession>
		implements
		PrivateMessageSessionDao {

	@Override
	public long countSessions(String accountId) {
		Query<PrivateMessageSession> q = this.dataStore
				.createQuery(PrivateMessageSession.class)
				.field("owneraccountId")
				.equal(accountId);
		return q.countAll();
	}

	@Override
	public long countSessions(String accountId, String contentId) {
		Query<PrivateMessageSession> q = this.dataStore
				.createQuery(PrivateMessageSession.class)
				.field("owneraccountId")
				.equal(accountId)
				.field("contentId")
				.equal(contentId);
		return q.countAll();
	}

	@Override
	public void deleteSession(String sessionId) {
		this.delete(sessionId);

	}

	@Override
	public String deleteSession(String owner, String target, String contentId) {
		Query<PrivateMessageSession> q = createQuery().filter("owneraccountId",
				owner)
				.filter("accountId", target);

		if (StringUtils.isNotBlank(contentId)) {
			q = q.filter("contentId", contentId);
		}
		return (String) this.dataStore.delete(q).getField("id");
	}

	@Override
	public void deleteSessions(String accountId) {
		Query<PrivateMessageSession> q = this.dataStore
				.createQuery(PrivateMessageSession.class)
				.field("owneraccountId")
				.equal(accountId);
		this.dataStore.delete(q);

	}

	@Override
	public PrivateMessageSession find(String fromAccountId, String toAccountId,
			String contentId) {

		return createQuery().filter("owneraccountId", fromAccountId)
				.filter("contentId", contentId)
				.filter("accountId", toAccountId).get();
	}

	@Override
	public PrivateMessageSession findById(String sessionId) {
		return createQuery().filter("id", sessionId).get();
	}

	@Override
	public List<PrivateMessageSession> getMessageSessionsByAccountId(
			String accountId, int offset, int limit) {
		return this.dataStore.createQuery(PrivateMessageSession.class)
				.filter("owneraccountId", accountId)
				.order("-lastUpdate.date").limit(limit)
				.offset(offset).asList();

	}

	@Override
	public List<PrivateMessageSession> getMessageSessionsByAccountIdAndContentId(
			String accountId, String contentId, int offset, int limit) {

		return this.dataStore.createQuery(PrivateMessageSession.class)
				.filter("owneraccountId", accountId)
				.filter("contentId", contentId)
				.order("-lastUpdate.date").limit(limit)
				.offset(offset).asList();

	}

	@Override
	public List<PrivateMessageSession> getSessions(String accountId) {
		Query<PrivateMessageSession> q = this.dataStore
				.createQuery(PrivateMessageSession.class)
				.field("owneraccountId")
				.equal(accountId);
		return q.asList();
	}

	@Override
	public void retsetUnread(String sessionId) {
		UpdateOperations<PrivateMessageSession> ops = dataStore
				.createUpdateOperations(PrivateMessageSession.class);

		ops.set("unread", 0);

		dataStore.update(dataStore.createQuery(PrivateMessageSession.class)
				.filter("id", sessionId), ops);

	}

	@Override
	public void save(PrivateMessageSession t) {
		if (t != null && (t.getId() == null || t.getId().equals(""))) {
			t.setId(new ObjectId().toString());
		}
		super.save(t);
	}

}
