package me.twocoffee.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import me.twocoffee.common.BaseDao;
import me.twocoffee.common.search.PagedResult;
import me.twocoffee.dao.RepositoryDao;
import me.twocoffee.entity.Repository;
import me.twocoffee.entity.SystemTagEnum;

import org.bson.types.ObjectId;

import com.google.code.morphia.query.Criteria;
import com.google.code.morphia.query.Query;
import com.google.code.morphia.query.UpdateOperations;

@org.springframework.stereotype.Repository
public class RepositoryDaoImpl extends BaseDao<Repository> implements
		RepositoryDao {

	@Override
	public void delete(String id) {
		super.delete(id);
	}

	@Override
	public List<Repository> findBySharedFriends(String accountId,
			String contentId, List<String> friendIds) {
		return this.createQuery().filter("contentId =", contentId)
				.filter("accountId in", friendIds)
				.filter("fromFriends.friendId", accountId).asList();
	}

	@Override
	public List<Repository> getAllRepository() {
		return dataStore.find(Repository.class).asList();
	}

	@Override
	public Repository getByContentIdAndAccountId(String contentId,
			String accountId) {
		return dataStore.createQuery(Repository.class).field("contentId")
				.equal(contentId).field("accountId").equal(accountId).get();
	}

	@Override
	public Repository getLatestByAccount(String accountId) {
		return this.createQuery().filter("accountId =", accountId)
				.filter("tag exists", SystemTagEnum.Collect.toString())
				.order("date").get();
	}

	@Override
	public List<Repository> getRepositoryByAccountId(String accountId) {
		return dataStore.createQuery(Repository.class).field("accountId")
				.equal(accountId).asList();
	}

	@Override
	public List<Repository> getRepositoryByAccountIdAndTag(String accountId,
			String tag) {
		if (tag == null || tag.trim().length() < 1)
			return null;
		List<String> tagList = new ArrayList<String>();
		tagList.add(tag);
		return this.getRepositoryByAccountIdAndTags(accountId, tagList);
	}

	@Override
	public List<Repository> getRepositoryByAccountIdAndTags(String accountId,
			List<String> tagList) {
		return dataStore.createQuery(Repository.class).field("accountId")
				.equal(accountId).filter("userTag in", tagList).asList();
	}
	
	@Override
	public List<Repository> getRepositoryBySystemTags(List<String> systemTagList,int offset,int limit){
		Query<Repository> q = dataStore.createQuery(Repository.class);
		for(String tag:systemTagList) {
			q.filter("tag", tag);
		}
		q.offset(offset).limit(limit);
		return q.asList();
	}
	
	@Override
	public List<Repository> getRepositoryBySystemTags(List<String> systemTagList,List<String> notSystemTagList,int offset,int limit){
		Query<Repository> q = dataStore.createQuery(Repository.class);
		for(String tag:systemTagList) {
			q.filter("tag", tag);
		}
		if(notSystemTagList!=null && notSystemTagList.size() >0) {
			for(String notTag:notSystemTagList) {
				q.field("tag").notEqual(notTag);
			}
		}
		q.offset(offset).limit(limit);
		return q.asList();
	}

	@Override
	public List<Repository> getRepositoryByAccountIdOrderBy(String accountId,
			String orderBy, int PageNum, int PageSize) {
		return dataStore.createQuery(Repository.class).field("accountId")
				.equal(accountId).order(orderBy).offset(PageSize * PageNum)
				.asList();
	}

	@Override
	public List<Repository> getRepositoryByContentId(String contentId) {
		return dataStore.createQuery(Repository.class).field("contentId")
				.equal(contentId).asList();
	}

	@Override
	public List<String> getRepositoryIdsByAccountIdOrderBy(String accountId,
			String orderBy, int PageNum, int PageSize) {

		List<Repository> temp = dataStore.createQuery(Repository.class)
				.field("accountId").equal(accountId).order(orderBy)
				.offset(PageSize * PageNum).limit(PageSize).asList();

		List<String> result = new ArrayList<String>();
		for (Iterator<Repository> iterator = temp.iterator(); iterator
				.hasNext();) {
			Repository repository = iterator.next();
			result.add(repository.getId());
		}
		return result;
	}

	@Override
	public void save(Repository t) {
		if (t != null && (t.getId() == null || t.getId().equals(""))) {
			t.setId(new ObjectId().toString());

			if (t.getDate() == null) {
				t.setDate(new Date());
			}
		}
		super.save(t);
	}

	@Override
	public PagedResult search(String accountId, String tag, String userTag,
			String friendId, int offset, int limit) {
		Query<Repository> query = dataStore.createQuery(Repository.class)
				.field("accountId").equal(accountId);
		if (tag != null && !"".equals(tag)) {
			query.filter("tag in", tag.split(","));
		}
		if (userTag != null && !"".equals(userTag)) {
			query.filter("tag in", userTag.split(","));
		}
		if (friendId != null && !"".equals(friendId)) {
			query.filter("fromFriends.friendId", friendId);
		}
		List<Repository> rs = query.limit(limit).offset(offset).order("-date")
				.asList();

		PagedResult<String> result = new PagedResult<String>();
		if (rs != null) {
			result.setTotal(query.countAll());
			if (result.getTotal() != 0) {
				List<String> list = new ArrayList<String>();
				for (Repository r : rs) {
					list.add(r.getId());
				}
				result.setResult(list);
			}
		}
		return result;
	}

	@Override
	public void setPrivateMessageCount(String accountId, long l) {
		List<String> tagList = new ArrayList<String>();
		tagList.add(SystemTagEnum.Delete_Source_Friend.toString());
		tagList.add(SystemTagEnum.Source_Friend.toString());
		UpdateOperations<Repository> ops = dataStore.createUpdateOperations(
				Repository.class).set("fromFriends.badge", 0l);

		dataStore.update(
				dataStore.createQuery(Repository.class).field("accountId")
						.equal(accountId).filter("tag in", tagList), ops);

	}
	
	@Override
	public long getPublicRepositoryNum() {
		long num = 0;
		num = this.createQuery().filter("tag", SystemTagEnum.Public.toString()).countAll();
		return num;
	}
	
}
