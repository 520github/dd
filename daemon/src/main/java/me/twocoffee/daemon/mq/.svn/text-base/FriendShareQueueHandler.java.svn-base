package me.twocoffee.daemon.mq;

import java.util.List;
import java.util.Map;

import me.twocoffee.daemon.MessageQueueDisposer;
import me.twocoffee.daemon.MessageQueueManager;
import me.twocoffee.service.FriendService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class FriendShareQueueHandler implements MessageQueueDisposer {
    protected static final Logger LOGGER = LoggerFactory
	    .getLogger(MessageQueueManager.class);

    @Autowired
    private FriendService friendService;

    @Override
    public String getQueueName() {
	return "friendShareQueue";
    }

    @Override
    public boolean dispose(Object data) {
	try {
	    Map<String, Object> map = (Map) data;
	    String accountId = map.get("accountId").toString();
	    String repositoryId = map.get("repositoryId").toString();
	    List<String> friends = (List<String>) map.get("friends");
	    int grade = Integer.valueOf(map.get("grade").toString());
	    String comment = "";
	    if (map.get("comment") != null) {
		comment = map.get("comment").toString();
	    }

	    LOGGER.info("FriendShareQueueHandler dispose.accountId="
		    + accountId + ",repositoryId=" + repositoryId);
	    return friendService.shareToFriendsWriteDB(accountId, repositoryId,
		    friends, grade, comment);
	} catch (Exception e) {
	    LOGGER.error("FriendShareQueueHandler dispose error.", e);
	    throw new RuntimeException(e);
	}
    }

}
