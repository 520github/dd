/**
 * 
 */
package me.twocoffee.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import me.twocoffee.common.BaseDao;
import me.twocoffee.dao.PublicRepositoryDao;
import me.twocoffee.entity.PublicRepository;
import me.twocoffee.entity.Repository;

import org.bson.types.ObjectId;

/**
 * @author momo
 * 
 */
@org.springframework.stereotype.Repository
public class PublicRepositoryDaoImpl extends BaseDao<PublicRepository>
		implements
		PublicRepositoryDao {

	@Override
	public void save(PublicRepository t) {

		if (t != null && (t.getId() == null || t.getId().equals(""))) {
			t.setId(new ObjectId().toString());
			t.setDate(new Date());
		}
		super.save(t);
	}

	@Override
	public PublicRepository getByContentIdAndAccountId(String contentId,
			String accountId) {

		return dataStore.createQuery(PublicRepository.class).
				field("contentId").equal(contentId).
				field("accountId").equal(accountId).get();
	}

	@Override
	public List<PublicRepository> getByAccountId(String accountId,String orderby, int PageSize,
			int PageNum) {
		
		return dataStore.createQuery(PublicRepository.class).field("accountId").equal(accountId).order(orderby).offset(
				PageSize * PageNum).limit(PageSize).asList();
	}
	
	@Override
	public int countByAccountId(String id) {
		return (int)this.dataStore.createQuery(PublicRepository.class)
				.filter("accountId =", id)
				.countAll();
	}

	@Override
	public List<String> getRepositoryIdsByAccountIdOrderBy(String accountId,
			String orderby, int limit, int offset) {


		List<PublicRepository> temp;
		if (null==orderby||"".equals(orderby)) {
			temp = dataStore.createQuery(PublicRepository.class).
					field("accountId").equal(accountId).offset(
							offset).limit(limit).asList();
		}else{
			temp = dataStore.createQuery(PublicRepository.class).
			field("accountId").equal(accountId).order(orderby).offset(
					offset).limit(limit).asList();
		}

		List<String> result = new ArrayList<String>();
		for (Iterator<PublicRepository> iterator = temp.iterator(); iterator.hasNext();) {
			PublicRepository publicrepository = iterator.next();
			result.add(publicrepository.getContentId());
		}
		return result;
	
	}
}
