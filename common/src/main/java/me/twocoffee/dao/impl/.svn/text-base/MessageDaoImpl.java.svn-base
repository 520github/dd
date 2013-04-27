package me.twocoffee.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import me.twocoffee.common.BaseDao;
import me.twocoffee.dao.MessageDao;
import me.twocoffee.entity.Message;
import me.twocoffee.entity.Message.CatalogId;
import me.twocoffee.entity.Message.MessageType;
import me.twocoffee.entity.Message.Status;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;

@org.springframework.stereotype.Repository
public class MessageDaoImpl extends BaseDao<Message> implements MessageDao {

	@Override
	public long countByAccountId(String accountId, CatalogId catalogId) {
		Query<Message> q = this.dataStore.createQuery(Message.class)
				.field("accountId")
				.equal(accountId).field("catalogId").equal(catalogId)
				.field("status").equal(Status.normal);
		return q.countAll();
	}

	@Override
	public long countNormalMessage(String accountId, MessageType type,
			Map<String, Object> attributes) {

		Query<Message> q = this.dataStore.createQuery(Message.class)
				.field("accountId")
				.equal(accountId).field("status").equal(Status.normal).filter("messageType", type);

		if (attributes != null) {

			for (String key : attributes.keySet()) {
				q = q.filter("attribute." + key, attributes.get(key));
			}
		}
		return q.countAll();
	}

	@Override
	public void deleteByAccountIdAndFriendId(String accountId, String friendId) {
		Query<Message> q = this.dataStore.createQuery(Message.class)
				.field("accountId").equal(accountId).field("attribute.id").equal(friendId);
				
		UpdateOperations<Message> u = this.dataStore
				.createUpdateOperations(Message.class).set("status",
						"deleted");
		
		dataStore.update(q,u);
	}

	@Override
	public void deleteByAccountIdAndType(String accountId, MessageType binding) {
		Query<Message> q = this.dataStore.createQuery(Message.class)
				.field("accountId").equal(accountId).field("messageType")
				.equal(binding);

		UpdateOperations<Message> u = this.dataStore
				.createUpdateOperations(Message.class).set("status",
						"deleted");

		dataStore.update(q, u);
	}

	
	public void deleteMessageForAttribute(String accountId, MessageType binding,Map<String,String> attribute){

		Query<Message> q = this.dataStore.createQuery(Message.class)
				.field("accountId").equal(accountId).field("messageType")
				.equal(binding);
		if(attribute==null){
			return;
		}
		for (String key : attribute.keySet()) {
			q.field("attribute."+key).equal(attribute.get(key));
		}

		UpdateOperations<Message> u = this.dataStore
				.createUpdateOperations(Message.class).set("status",
						"deleted");

		dataStore.update(q, u);
	
	}
	
	@Override
	public List<Message> getMessages(String accountId, CatalogId catalogId,String displayType,
			int limit, int offset) {
		Query<Message> q = this.dataStore.createQuery(Message.class)
				.field("accountId")
				.equal(accountId);
				//如果displayType为空则表示手机客户端请求
				if(StringUtils.isBlank(displayType) && catalogId.equals(Message.CatalogId.Friend)){
					q.filter("catalogId in",new String[]{catalogId.toString(),Message.CatalogId.Acknowledgment.toString()});
				}else{
					q.field("catalogId").equal(catalogId);
				};
				q.field("status").equal(Status.normal)
				.order("-date").offset(offset).limit(limit);
		return q.asList();
	}

	@Override
	public void save(Message message) {
		if (message != null && message.getId() == null) {
			message.setId(new ObjectId().toString());
			message.setDate(new Date());
			message.setStatus(Status.normal);
		}
		super.save(message);
	}

	@Override
	public void updateStatus(String id, Status status) {
		UpdateOperations<Message> u = this.dataStore
				.createUpdateOperations(Message.class).set("status",
						status);

		Query<Message> q = this.dataStore.createQuery(Message.class)
				.field("id").equal(id);

		dataStore.update(q, u);
	}

	@Override
	public void updateStatusByAccountId(String accountId, Status status) {
		UpdateOperations<Message> u = this.dataStore
				.createUpdateOperations(Message.class).set("status",
						status);
		Query<Message> q = this.dataStore.createQuery(Message.class)
				.field("accountId").equal(accountId);
		dataStore.update(q, u);
	}

}
