/**
 * 
 */
package me.twocoffee.dao.impl;

import java.util.List;

import me.twocoffee.common.BaseDao;
import me.twocoffee.dao.PrivateMessageDao;
import me.twocoffee.entity.PrivateMessage;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

/**
 * @author momo
 * 
 */
@Repository
public class PrivateMessageDaoImpl extends BaseDao<PrivateMessage> implements
		PrivateMessageDao {

	// private static final String SEQUECENAME = "private_message";

	// @Autowired
	// private SequenceDao sequenceDao;

	@Override
	public long countMessages(String sessionId) {

		return this.dataStore.createQuery(PrivateMessage.class)
				.filter("sessionId", sessionId)
				.countAll();
	}

	@Override
	public long countMessageWithUUID(String id, String uuid) {
		return this.dataStore.createQuery(PrivateMessage.class)
				.filter("sessionId", id).filter("uuid", uuid)
				.countAll();
	}

	@Override
	public void deleteMessage(String id) {
		this.dataStore.delete(dataStore.createQuery(PrivateMessage.class)
				.filter("sessionId", id));
	}

	@Override
	public List<PrivateMessage> getLatestMessagesBysessionId(String sessionId,
			String previousId) {

		return this.dataStore.createQuery(PrivateMessage.class)
				.filter("sessionId", sessionId)
				.field("_id").greaterThan(previousId)
				.asList();
	}

	@Override
	public List<PrivateMessage> getMessagesBysessionId(String sessionId) {

		return this.dataStore.createQuery(PrivateMessage.class)
				.filter("sessionId", sessionId)
				.order("-date").asList();
	}

	@Override
	public List<PrivateMessage> getMessagesBysessionId(String sessionId,
			int offset, int limit) {

		return this.dataStore.createQuery(PrivateMessage.class)
				.filter("sessionId", sessionId)
				.order("-date").limit(limit)
				.offset(offset).asList();
	}

	@Override
	public void save(PrivateMessage t) {
		if (t != null && t.getId() == null) {
			t.setId((new ObjectId()).toString());
		}
		super.save(t);
	}

	@Override
	public int getMessagesBySessionIdAndFriendId(
			String sessionId, String accountId) {
		return (int) this.dataStore.createQuery(PrivateMessage.class).filter("sessionId", sessionId).filter("toAccountId", accountId).field("message").notEqual("").countAll();
	}

}
