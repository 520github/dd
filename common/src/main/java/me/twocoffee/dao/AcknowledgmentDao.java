package me.twocoffee.dao;

import java.util.List;

import me.twocoffee.entity.Acknowledgment;

public interface AcknowledgmentDao {
 
	void save(Acknowledgment akm);
	
	int totalByAccountIdAndContent(String accountId,String contentId);
	
	List<Acknowledgment> findByAccountIdAndContentId(String accountId,
			String contentId, int limit, int offset);
	
	void delete(String id);
}
