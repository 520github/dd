/**
 * 
 */
package me.twocoffee.service.impl;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.twocoffee.dao.PrivateMessageDao;
import me.twocoffee.dao.PrivateMessageSessionDao;
import me.twocoffee.entity.Account;
import me.twocoffee.entity.PrivateMessage;
import me.twocoffee.entity.PrivateMessageSession;
import me.twocoffee.entity.Repository;
import me.twocoffee.exception.SendSameMessageInSessionException;
import me.twocoffee.service.AccountService;
import me.twocoffee.service.ContentSearcher;
import me.twocoffee.service.PrivateMessageService;
import me.twocoffee.service.RepositoryService;
import me.twocoffee.service.entity.ContentAndAccount;
import me.twocoffee.service.entity.ContentDetail.Avatar;
import me.twocoffee.service.entity.ResponseSession;
import me.twocoffee.service.event.RemovePrivateMessageEvent;
import me.twocoffee.service.event.SendPrivateMessageEvent;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * @author momo
 * 
 */
@Service
public class PrivateMessageServiceImpl implements PrivateMessageService {

	private static final Logger logger = LoggerFactory
			.getLogger(PrivateMessageServiceImpl.class);
	
	@Autowired
	private PrivateMessageDao messageDao;

	// private static final String SEQUECENAME = "private_message";
	//
	// @Autowired
	// private SequenceDao sequenceDao;

	@Autowired
	private PrivateMessageSessionDao messageSessionDao;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private ContentSearcher contentSearcher;

	private void resetUnreadBadge(String sessionId) {
		messageSessionDao.retsetUnread(sessionId);
	}

	private boolean sendMore(PrivateMessageSession mySession,
			PrivateMessage message) {

		if (StringUtils.isBlank(message.getUuid())) {
			return false;
		}
		return messageDao.countMessageWithUUID(mySession.getId(),
				message.getUuid()) > 0;

	}

	@SuppressWarnings("unused")
	private void sendRemoveMessage(String owner, String target, String contentId) {
		ContentAndAccount contentAndAccount = new ContentAndAccount(contentId,
				owner, target);

		List<ContentAndAccount> list = new ArrayList<ContentAndAccount>();
		list.add(contentAndAccount);
		RemovePrivateMessageEvent event = new RemovePrivateMessageEvent(list);
		applicationContext.publishEvent(event);
	}

	@Override
	public long countMessage(String accountId, String targetId, String contentId) {
		PrivateMessageSession session = messageSessionDao.find(accountId,
				targetId, contentId);

		if (session == null) {
			return 0;
		}
		return session.getMessageCount();
	}

	@Override
	public long countMessagesInSession(String sessionId) {
		long count = 0;
		count = messageDao.countMessages(sessionId);
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.twocoffee.service.PrivateMessageService#countSessions(java.lang.String
	 * )
	 */
	@Override
	public long countSessions(String accountId) {
		long count = 0;
		count = messageSessionDao.countSessions(accountId);
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.twocoffee.service.PrivateMessageService#countSessions(java.lang.String
	 * , java.lang.String)
	 */
	@Override
	public long countSessions(String accountId, String contentId) {
		long count = 0;
		count = messageSessionDao.countSessions(accountId, contentId);
		return count;
	}

	@Override
	public void creatPrivateMessageSession(PrivateMessageSession pms) {
		messageSessionDao.save(pms);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.twocoffee.service.PrivateMessageService#deleteSession(java.lang.String
	 * , java.lang.String)
	 */
	@Override
	public boolean deleteSession(String sessionId) {
		messageSessionDao.deleteSession(sessionId);
		return true;
	}

	@Override
	public void deleteSession(String owner, String target, String contentId) {

		if (StringUtils.isBlank(contentId) && StringUtils.isBlank(target)) {
			messageSessionDao.deleteSessions(owner);
			// sendRemoveMessage(owner, target, contentId);
			return;
		}
		PrivateMessageSession session = messageSessionDao.find(owner, target,
				contentId);

		if (session != null) {
			messageSessionDao.deleteSession(owner, target, contentId);
			messageDao.deleteMessage(session.getId());
			// sendRemoveMessage(owner, target, contentId);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.twocoffee.service.PrivateMessageService#deleteSessions(java.lang.String
	 * )
	 */
	@Override
	public boolean deleteSessions(String accountId) {
		messageSessionDao.deleteSessions(accountId);
		return true;
	}

	@Override
	public List<PrivateMessage> getLatestMessagesInSession(String sessionId,
			String sinceId) {

		if (StringUtils.isBlank(sinceId)) {
			sinceId = "";
		}
		resetUnreadBadge(sessionId);
		return messageDao.getLatestMessagesBysessionId(sessionId, sinceId);
	}

	public PrivateMessageDao getMessageDao() {
		return messageDao;
	}

	public PrivateMessageSessionDao getMessageSessionDao() {
		return messageSessionDao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.twocoffee.service.PrivateMessageService#getMessagesInSession(java.
	 * lang.String)
	 */
	@Override
	public List<PrivateMessage> getMessagesInSession(String sessionId) {
		return messageDao.getMessagesBysessionId(sessionId);
	}

	@Override
	public List<PrivateMessage> getMessagesInSession(String sessionId,
			int offset, int limit) {
		return messageDao.getMessagesBysessionId(sessionId, offset, limit);
	}

	@Override
	public List<PrivateMessage> getMessagesInSession(String owner,
			String target, String contentId, String sinceId) {

		if (StringUtils.isBlank(sinceId)) {
			sinceId = "";
		}
		PrivateMessageSession session = messageSessionDao.find(owner, target,
				contentId);

		if (session == null) {
			return new ArrayList<PrivateMessage>(0);
		}
		return messageDao
				.getLatestMessagesBysessionId(session.getId(), sinceId);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.twocoffee.service.PrivateMessageService#getSession(java.lang.String)
	 */
	@Override
	public PrivateMessageSession getSession(String sessionId) {
		return messageSessionDao.findById(sessionId);
	}

	@Override
	public PrivateMessageSession getSession(String ownerId, String accountId,
			String contentId) {

		return this.messageSessionDao.find(ownerId, accountId, contentId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.twocoffee.service.PrivateMessageService#getSessions(java.lang.String)
	 */
	@Override
	public List<PrivateMessageSession> getSessions(String accountId) {
		return messageSessionDao.getSessions(accountId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.twocoffee.service.PrivateMessageService#getSessions(java.lang.String)
	 */
	@Override
	public List<PrivateMessageSession> getSessions(String accountId,
			int offset, int limit) {
		List<PrivateMessageSession> list = messageSessionDao
				.getMessageSessionsByAccountId(accountId, offset, limit);

		List<PrivateMessageSession> result = new ArrayList<PrivateMessageSession>();
		for (PrivateMessageSession session : list) {

			int count = messageDao.getMessagesBySessionIdAndFriendId(
					session.getId(), session.getOwneraccountId());
			if (count > 0) {
				result.add(session);
			}

		}
		return result;
	}

	@Override
	public List<PrivateMessageSession> getSessions(String accountId,
			String contentId, int offset, int limit) {

		if (offset < 0) {
			offset = 0;
		}

		if (limit <= 0) {
			return null;
		}

		List<PrivateMessageSession> list = messageSessionDao
				.getMessageSessionsByAccountIdAndContentId(accountId,
						contentId, offset, limit);
		List<PrivateMessageSession> result = new ArrayList<PrivateMessageSession>();
		for (PrivateMessageSession session : list) {

			int count = messageDao.getMessagesBySessionIdAndFriendId(
					session.getId(), session.getOwneraccountId());
			if (count > 0) {
				result.add(session);
			}

		}
		return result;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.twocoffee.service.PrivateMessageService#sendMessage(me.twocoffee.entity
	 * .PrivateMessage)
	 */
	// @Override
	// public void sendMessage(PrivateMessage message, String contentId) {
	// sendMessage(message, contentId, true);
	// }

	@Override
	public List<ResponseSession> getSessionsForContentId(String accountId,
			String contentId, int offset, int limit) {
		List<ResponseSession> rs = new ArrayList<ResponseSession>();
		if (offset < 0) {
			offset = 0;
		}

		if (limit <= 0) {
			return null;
		}
		List<PrivateMessageSession> sessions = messageSessionDao
				.getMessageSessionsByAccountIdAndContentId(accountId,
						contentId, offset, limit);
		ResponseSession responseSession = null;
		for (PrivateMessageSession session : sessions) {
			int count = messageDao.getMessagesBySessionIdAndFriendId(
					session.getId(), session.getOwneraccountId());
			if (count > 0) {
				responseSession = new ResponseSession();
				responseSession.setSession(session);
				Account account = accountService
						.getById(session.getAccountId());
				if (account == null)
					continue;
				responseSession.setAccount(account);
				Avatar avatar = new Avatar();
				avatar.setLarge(account.getPhotos().get(Account.PHOTO_SIZE_BIG));
				avatar.setMedium(account.getPhotos().get(
						Account.PHOTO_SIZE_MIDDLE));
				avatar.setSmall(account.getPhotos().get(
						Account.PHOTO_SIZE_SMALL));
				responseSession.setAvatar(avatar);
				rs.add(responseSession);

			}

		}

		return rs;

	}

	@Override
	public void sendMessage(PrivateMessage message, String contentId,
			boolean sendCommentNotify) throws SendSameMessageInSessionException {

		if (message == null
				|| StringUtils.isBlank(message.getAccountId())
				|| StringUtils.isBlank(message.getToAccountId())
				|| StringUtils.isBlank(contentId)) {

			throw new InvalidParameterException("invalid message!");
		}
		// 获取所有者的会话
		PrivateMessageSession mySession = null;

		if (StringUtils.isNotBlank(message.getSessionId())) {
			mySession = messageSessionDao.findById(message
					.getSessionId());

			if (mySession == null) {
				throw new InvalidParameterException(
						"invalid message! session is not exist!");

			}

		} else {
			mySession = messageSessionDao.find(
					message.getAccountId(), message.getToAccountId(),
					contentId);

		}
		boolean isMessageNotNull = true;	//	判断分享者的分享语是否为空 若为空 则分享者一方的 PrivateMessageSession不存
		if(StringUtils.isBlank(message.getMessage())){
//			return;
			message.setMessage("我觉得这个东东可能对你有帮助，希望你喜欢哦!");
			isMessageNotNull = false;
		}
		String ownerid = (new ObjectId()).toString();
		String targetid = (new ObjectId()).toString();
		// 悄悄话接收人的session
		PrivateMessageSession oppsite_session = messageSessionDao.find(
				message.getToAccountId(), message.getAccountId(),
				contentId);

		message.setId(ownerid);
		message.setDate(new Date());
		PrivateMessageSession.LastUpdate lastupdate1 = new PrivateMessageSession.LastUpdate();
		lastupdate1.setMessage(message.getMessage());
		lastupdate1.setDate(message.getDate());
		lastupdate1.setAccountId(message.getAccountId());
		lastupdate1.setId(targetid);

		// 如果mySession为空，则创建session；不为空则更新session
		if (mySession == null) {
			mySession = new PrivateMessageSession();
			mySession.setAccountId(message.getToAccountId());
			mySession.setContentId(contentId);
			mySession.setMessageCount(1);
			mySession.setOwneraccountId(message.getAccountId());
			mySession.setId(new ObjectId().toString());
			// mySession.setUnread(1);

		} else {
			// 判断是否重复发送
			if (sendMore(mySession, message)) {
				throw new SendSameMessageInSessionException();
			}
			
			//	只计对方给自己的回复数 所以注掉下面一行
//			mySession.setMessageCount(mySession.getMessageCount() + 1);	
//			mySession.setLastUpdate(myLastupdate);
			// mySession.setUnread(mySession.getUnread() + 1);
		}
		// 如果oppsite_session为空，则创建session；否则更新session
		if (oppsite_session == null) {
			oppsite_session = mySession.changeDirection();
			oppsite_session.setId(new ObjectId().toString());
			oppsite_session.setMessageCount(1);
			oppsite_session.setUnread(1);

		} else {
			oppsite_session.setMessageCount(oppsite_session
					.getMessageCount() + 1);

			oppsite_session.setUnread(oppsite_session.getUnread() + 1);
		}
		oppsite_session.setLastUpdate(lastupdate1);
		// 保存session和message
		List<PrivateMessageSession> sessions = new ArrayList<PrivateMessageSession>();
		sessions.add(oppsite_session);
//		if(isMessageNotNull){
//			sessions.add(mySession);
//		}
		messageSessionDao.save(sessions);

		message.setSessionId(mySession.getId());
		PrivateMessage oppsite_msg = new PrivateMessage();
		oppsite_msg.setDate(message.getDate());
		oppsite_msg.setAccountId(message.getAccountId());
		oppsite_msg.setMessage(message.getMessage());
		oppsite_msg.setId(lastupdate1.getId());
		oppsite_msg.setSessionId(oppsite_session.getId());
		oppsite_msg.setToAccountId(message.getToAccountId());
		oppsite_msg.setUuid(message.getUuid());
		List<PrivateMessage> messages = new ArrayList<PrivateMessage>();
		messages.add(oppsite_msg);
		if(isMessageNotNull){	//	不加此判断 那么这个message会成为孤儿 因为对应的PrivateMessageSession没有存。 当然也不会出问题
			messages.add(message);
		}
		messageDao.save(messages);
		Repository ry = repositoryService.getRepositoryByContentIdAndAccountId(
				contentId, message.getAccountId());
		ry.setLastModified(new Date());
		repositoryService.save(ry);
		contentSearcher.updateIndex(ry.getId());
		
		try{
		sendMessageEvent(message.getAccountId(), message.getToAccountId(),
					mySession, oppsite_session, sendCommentNotify);

		} catch(Exception e) {
			logger.error("send private message error! cannot push message!", e);
			
		}
	}

	public void sendMessageEvent(String accountId, String targetAccountId,
			PrivateMessageSession source, PrivateMessageSession target,
			boolean sendCommentNotify) {

		SendPrivateMessageEvent e = new SendPrivateMessageEvent(this,
				accountId, targetAccountId, source, target, sendCommentNotify);

		applicationContext.publishEvent(e);
	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {

		this.applicationContext = applicationContext;
	}

	public void setMessageDao(PrivateMessageDao messageDao) {
		this.messageDao = messageDao;
	}

	public void setMessageSessionDao(PrivateMessageSessionDao messageSessionDao) {
		this.messageSessionDao = messageSessionDao;
	}

}
