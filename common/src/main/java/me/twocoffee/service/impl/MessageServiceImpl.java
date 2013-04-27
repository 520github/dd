package me.twocoffee.service.impl;

import java.util.List;
import java.util.Map;

import me.twocoffee.dao.MessageDao;
import me.twocoffee.entity.Message;
import me.twocoffee.entity.Message.MessageAction;
import me.twocoffee.entity.Message.MessageType;
import me.twocoffee.entity.Message.Status;
import me.twocoffee.service.MessageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService,
		ApplicationContextAware {

	private static final Logger logger = LoggerFactory
			.getLogger(MessageServiceImpl.class);

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private MessageDao messageDao;

	@Override
	public void actionMessage(String messageId, MessageAction action) {
		Message message = messageDao.getById(messageId);

		if (message == null) {
			logger.debug("No message[{}] found,I decided not to do anything!",
					messageId);

			return;
		}
		ApplicationEvent event = action.getAction()
				.getActionEvent(this, message);
		this.applicationContext.publishEvent(event);
	}

	@Override
	public long countMessageByAccountId(String accountId,
			Message.CatalogId catalogId) {
		return messageDao.countByAccountId(accountId, catalogId);
	}

	@Override
	public long countNormalMessage(String accountId, Message.MessageType type,
			Map<String, Object> attributes) {

		return messageDao.countNormalMessage(accountId, type, attributes);
	}

	@Override
	public void delete(String messageId) {
		messageDao.updateStatus(messageId, Status.deleted);
	}

	@Override
	public void delete(String accountId, MessageType binding) {
		messageDao.deleteByAccountIdAndType(accountId, binding);
	}

	@Override
	public void deleteAll(String accountId) {
		messageDao.updateStatusByAccountId(accountId, Status.deleted);
	}

	@Override
	public void deleteByAccountIdAndFriendId(String accountId, String friendId) {
		messageDao.deleteByAccountIdAndFriendId(accountId, friendId);
	}

	@Override
	public List<Message> getMessages(String accountId,
			Message.CatalogId catalogId,String displayType,int limit, int offset) {
		return messageDao.getMessages(accountId, catalogId, displayType,limit, offset);
	}

	@Override
	public void save(Message message) {
		messageDao.save(message);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void deleteForAttribute(String accountId, MessageType binding,
			Map<String, String> attribute) {
		messageDao.deleteMessageForAttribute(accountId, binding, attribute);
		
	}

}
