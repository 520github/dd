package me.twocoffee.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.twocoffee.common.mq.MessageQueueSender;
import me.twocoffee.dao.AcknowledgmentDao;
import me.twocoffee.dao.ContentDao;
import me.twocoffee.dao.RepositoryDao;
import me.twocoffee.entity.Acknowledgment;
import me.twocoffee.entity.Content;
import me.twocoffee.entity.Repository;
import me.twocoffee.entity.Repository.FriendShare;
import me.twocoffee.service.AcknowledgmentService;
import me.twocoffee.service.event.AcknowledgmentEvent;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
@Service
public class AcknowledgmentServiceImpl implements AcknowledgmentService {

	@Autowired
	private AcknowledgmentDao acknowledgmentDao;
	
	@Autowired
	private RepositoryDao repostoryDao;
	
	@Autowired
	private ContentDao contentDao;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private MessageQueueSender queueSender;
	
	@Override
	public boolean sendAcknowledgment(String accountId,String contentId) {
		Acknowledgment akm = new Acknowledgment();
		akm.setFrom(accountId);
		akm.setContent(contentId);
		
       Repository repository = repostoryDao.getByContentIdAndAccountId(contentId, accountId);
       if(repository==null)
    	   return false;
       
       List<FriendShare> froms = repository.getFromFriends();
       if(froms==null)
    	   return false;
    
       Content content = contentDao.getById(contentId);
       for (FriendShare friendShare : froms) {
    	   if(friendShare.isAcknowledgment())
    		continue;
		friendShare.setAcknowledgment(true);
		akm.setAccountId(friendShare.getFriendId());
		akm.setDate(new Date());
		akm.setMessage("感谢你分享的:\""+content.getTitle()+"\"");
		akm.setSubject("感谢你分享的:\""+content.getTitle()+"\"");
		akm.setId(new ObjectId().toString());
		acknowledgmentDao.save(akm);
		//sendAcknowledgmentEvent(accountId,friendShare.getFriendId(),contentId);
	}
       repository.setLastModified(new Date());
       repostoryDao.save(repository);
       return true;
       
	}

	@Override
	public boolean Available(String accountId, String contentId) {
		 Repository repository = repostoryDao.getByContentIdAndAccountId(contentId, accountId);
		 if(repository==null){
			 return false;
		 }
		 List<FriendShare> froms = repository.getFromFriends();
		 if(froms==null){
			 return false;
		 }
		 for (FriendShare friendShare : froms) {
			if(!friendShare.isAcknowledgment()){
				return true;
			}
		}
		return false;
	}

	public void sendAcknowledgmentEvent(String accountId, String targetAccountId,
			String contentId) {

		AcknowledgmentEvent e = new AcknowledgmentEvent(this,
				accountId, targetAccountId,contentId);

		applicationContext.publishEvent(e);
	}

	@Override
	public int totalByAccountIdAndContent(String accountId, String contentId) {
		return acknowledgmentDao.totalByAccountIdAndContent(accountId, contentId);
	}

	@Override
	public List<Acknowledgment> findByAccountIdAndContentId(String accountId,
			String contentId, int limit, int offset) {
		return acknowledgmentDao.findByAccountIdAndContentId(accountId, contentId, limit, offset);
	}

	@Override
	public void delete(Acknowledgment acknowledgment) {
		acknowledgmentDao.delete(acknowledgment.getId());
		
	}

	@Override
	public boolean sendAcknowledgmentToQueue(String accountId, String contentId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("accountId", accountId);
	    map.put("contentId", contentId);
		queueSender.send("acknowledgmentQueue", map, 30000);
		return true;
	}
}
