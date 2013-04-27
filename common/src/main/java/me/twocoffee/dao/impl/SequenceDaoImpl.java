/**
 * 
 */
package me.twocoffee.dao.impl;

import me.twocoffee.common.BaseDao;
import me.twocoffee.dao.SequenceDao;
import me.twocoffee.entity.Sequence;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * @author momo
 * 
 */
@org.springframework.stereotype.Repository
public class SequenceDaoImpl extends BaseDao<Sequence> implements
		SequenceDao {

	private String name = "inviteCode";

	public String getName() {
		return name;
	}

	@Override
	public Long next() {
		return next(name);
	}

	public Long next(String name) {
		DBObject obj = dataStore
				.getDB()
				.getCollection("sequence")
				.findAndModify(
						new BasicDBObject("_id", name),
						new BasicDBObject(),
						new BasicDBObject(),
						false,
						new BasicDBObject("$inc", new BasicDBObject("next", 1L)),
						true,
						true);

		return (Long) obj.get("next");
	}

	public void setName(String name) {
		this.name = name;
	}

}
