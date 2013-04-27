package me.twocoffee.dao.impl;

import java.util.Date;
import java.util.List;

import me.twocoffee.common.BaseDao;
import me.twocoffee.dao.TagDao;
import me.twocoffee.entity.StatisticTag;
import me.twocoffee.entity.Tag;
import me.twocoffee.entity.Tag.TagId;

import org.springframework.stereotype.Repository;

import com.google.code.morphia.query.Query;

@Repository
public class TagDaoImpl extends BaseDao<Tag> implements TagDao {

	@Override
	public Tag getById(TagId tagId) {
		return this.dataStore.createQuery(Tag.class).field("id").equal(tagId)
				.get();
	}

	@Override
	public List<Tag> getTagsByAccount(String accountId) {
		return this.dataStore.createQuery(Tag.class).field("id.accountId")
				.equal(accountId).order("date").asList();
	}

	@Override
	public void save(Tag tag) {
		if (tag != null && tag.getDate() == null) {
			tag.setDate(new Date());
		}
		super.save(tag);
	}
	
	@Override
	public void removeById(Tag.TagId tagId) {
		Query<Tag> query = this.dataStore.createQuery(Tag.class).field("id").equal(tagId);
		this.dataStore.delete(query);
	}
	
	@Override
	public List<Tag> getTopTags(String accountId, String order, int limit) {
		if(order == null || order.trim().length() <1)order = "date";
		return this.dataStore.createQuery(Tag.class).field("id.accountId")
		.equal(accountId).order(order).offset(0).limit(limit).asList();
	}

}
