package me.twocoffee.dao.impl;

import me.twocoffee.common.BaseDao;
import me.twocoffee.dao.FeedbackDao;
import me.twocoffee.entity.Feedback;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

@org.springframework.stereotype.Repository
public class FeedbackDaoImpl extends BaseDao<Feedback> implements FeedbackDao {

	@Override
	public void save(Feedback feedback) {

		if (StringUtils.isBlank(feedback.getId())) {
			feedback.setId(new ObjectId().toString());
		}
		super.save(feedback);
	}
}
