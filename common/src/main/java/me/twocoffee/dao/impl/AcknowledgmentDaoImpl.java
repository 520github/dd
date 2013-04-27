package me.twocoffee.dao.impl;

import java.util.List;

import me.twocoffee.common.BaseDao;
import me.twocoffee.dao.AcknowledgmentDao;
import me.twocoffee.entity.Acknowledgment;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.google.code.morphia.query.Query;

@Repository
public class AcknowledgmentDaoImpl extends BaseDao<Acknowledgment> implements AcknowledgmentDao{

	@Override
	public int totalByAccountIdAndContent(String accountId, String contentId) {
		Query<Acknowledgment> query = dataStore.createQuery(Acknowledgment.class);
		if(StringUtils.isBlank(accountId)&&StringUtils.isBlank(contentId)){
			return 0;
		}
		if(!StringUtils.isBlank(accountId)){
			query.field("accountId").equal(accountId);
		}
		if(!StringUtils.isBlank(contentId)){
			query.field("content").equal(contentId);
		}
		
		return (int) query.countAll();
	}

	@Override
	public List<Acknowledgment> findByAccountIdAndContentId(String accountId,
			String contentId, int limit, int offset) {
		Query<Acknowledgment> query = dataStore.createQuery(Acknowledgment.class);
		if(StringUtils.isBlank(accountId)&&StringUtils.isBlank(contentId)){
			return null;
		}
		if(!StringUtils.isBlank(accountId)){
			query.field("accountId").equal(accountId);
		}
		if(!StringUtils.isBlank(contentId)){
			query.field("content").equal(contentId);
		}
		if(limit!=0){
			query.limit(limit);
		}
		if(offset!=0){
			query.offset(offset);
		}
		return query.order("-date").asList();
	}

	@Override
	public void delete(String id) {
		super.delete(id);
	}

    
}
