package me.twocoffee.service.impl;

import me.twocoffee.dao.FeedbackDao;
import me.twocoffee.entity.Feedback;
import me.twocoffee.service.FeedbackService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackServiceImpl implements FeedbackService {

	@Autowired
	private FeedbackDao feedbackDao;

	@Override
	public void saveFeedback(Feedback feedback) {
		this.feedbackDao.save(feedback);
	}

}
