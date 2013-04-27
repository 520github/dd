package me.twocoffee.dao.impl;

import me.twocoffee.common.BaseDao;
import me.twocoffee.dao.ContentDao;
import me.twocoffee.entity.Content;
import me.twocoffee.entity.Content.ContentType;
import me.twocoffee.entity.ContentAction;

import org.bson.types.ObjectId;

@org.springframework.stereotype.Repository
public class ContentDaoImpl extends BaseDao<Content> implements ContentDao {

	@Override
	public Content getByUrl(String url) {
		return dataStore.createQuery(Content.class).field("url").equal(url)
				.get();
	}

	@Override
	public Content getByUrlAndContentType(String url, ContentType type) {
		return dataStore.createQuery(Content.class).field("url").equal(url)
				.field("contentType").equal(type)
				.get();
	}

	@Override
	public void save(Content t) {
		if (t != null && (t.getId() == null || t.getId().equals(""))) {
			t.setId(new ObjectId().toString());
		}
		super.save(t);
	}

	@Override
	public void saveContentAction(ContentAction ca) {
		if (ca != null && (ca.getId() == null || ca.getId().equals(""))) {
			ca.setId(new ObjectId().toString());
		}

		this.dataStore.save(ca);
	}

	@Override
	public void updateContent(Content content) {

		if ((content != null) && (content.getId() != null)
				&& (!content.getId().equals(""))) {
			super.save(content);
		}

	}
}
