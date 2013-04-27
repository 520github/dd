package me.twocoffee.daemon.mq;

import java.util.Map;

import me.twocoffee.daemon.MessageQueueDisposer;
import me.twocoffee.service.AcknowledgmentService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class acknowledgmentQueueHandler implements MessageQueueDisposer {
	protected static final Logger LOGGER = LoggerFactory.getLogger(acknowledgmentQueueHandler.class);
	
	@Autowired
	private AcknowledgmentService acknowledgmentService;
	
	@Override
	public String getQueueName() {
		return "acknowledgmentQueue";
	}

	@Override
	public boolean dispose(Object data) {
		
	   Map<String,Object> map = (Map<String, Object>) data;
       String contentId = map.get("contentId").toString();
       String accountId = map.get("accountId").toString();
       LOGGER.info("acknowledgmentQueueHandler dispose.accountId=" + accountId + ",Id=" + contentId);
       return acknowledgmentService.sendAcknowledgment(accountId, contentId);
	}

}
