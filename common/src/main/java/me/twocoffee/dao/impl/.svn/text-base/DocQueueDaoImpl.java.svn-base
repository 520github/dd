package me.twocoffee.dao.impl;

import java.util.Date;

import me.twocoffee.common.BaseDao;
import me.twocoffee.dao.DocQueueDao;
import me.twocoffee.entity.DocQueue;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;

@Repository
public class DocQueueDaoImpl extends BaseDao<DocQueue> implements
		DocQueueDao {

	@Override
	public DocQueue peek(String serviceId) {
		Query<DocQueue> query = dataStore.createQuery(DocQueue.class)
				.field("processing").equal(false).order("date");
		UpdateOperations<DocQueue> ops = dataStore
				.createUpdateOperations(DocQueue.class)
				.set("fetcher", serviceId).set("processing", true);
		return this.dataStore.findAndModify(query, ops, false);
	}

	@Override
	public void resetQueueByServiceId(String serviceId) {
		Query<DocQueue> query = dataStore.createQuery(DocQueue.class)
				.field("fetcher").equal(serviceId)
				.field("processing").equal(true);
		UpdateOperations<DocQueue> ops = dataStore
				.createUpdateOperations(DocQueue.class)
				.set("processing", false);
		dataStore.update(query, ops);
	}

	@Override
	public void save(DocQueue docQueue) {
		if (docQueue != null && docQueue.getId() == null) {
			docQueue.setId(new ObjectId().toString());
			docQueue.setDate(new Date());
		}
		super.save(docQueue);
	}

	@Override
	public void delete(DocQueue doc) {
		dataStore.delete(doc);
	}
}
