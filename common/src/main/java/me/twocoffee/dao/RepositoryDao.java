package me.twocoffee.dao;

import java.util.List;

import me.twocoffee.common.search.PagedResult;
import me.twocoffee.entity.Repository;

public interface RepositoryDao {

	/**
	 * 删除某个收藏关系
	 * 
	 * @param id
	 */
	void delete(String id);

	List<Repository> findBySharedFriends(String accountId, String contentId,
			List<String> friendIds);

	/**
	 * 根据contentId和accountId获取一个Repository对象
	 * 
	 * @return
	 */
	Repository getByContentIdAndAccountId(String contentId, String accountId);

	/**
	 * 根据id获取收藏关系
	 * 
	 * @param id
	 * @return
	 */
	Repository getById(String id);

	Repository getLatestByAccount(String accountId);

	/**
	 * 根据accountId获取Repository列表
	 * 
	 * @param accountId
	 * @return
	 */
	List<Repository> getRepositoryByAccountId(String accountId);

	/**
	 * 根据AccountId及一个用户标签获取Repository列表
	 * 
	 * @param accountId
	 * @param tag
	 * @return
	 */
	List<Repository> getRepositoryByAccountIdAndTag(String accountId, String tag);

	/**
	 * 根据AccountId及多个用户标签获取Repository列表
	 * 
	 * @param accountId
	 * @param tagList
	 * @return
	 */
	List<Repository> getRepositoryByAccountIdAndTags(String accountId,
			List<String> tagList);
	
	
	/**
	 * 根据多个系统标签获取Repository列表
	 * 
	 * @param systemTagList
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<Repository> getRepositoryBySystemTags(List<String> systemTagList,int offset,int limit);
	
	/**
	 * 根据多个系统标签和多个不存在的系统标签获取Repository列表
	 * 
	 * @param systemTagList
	 * @param notSystemTagList
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<Repository> getRepositoryBySystemTags(List<String> systemTagList,List<String> notSystemTagList,int offset,int limit);

	/**
	 * 根据accountIdhe和orderBy获取Repository列表
	 * 
	 * @param accountId
	 * @param orderBy
	 *            排序字段
	 * @return
	 */
	List<Repository> getRepositoryByAccountIdOrderBy(String accountId,
			String orderBy, int PageNum, int PageSize);

	/**
	 * 根据contentId获取Repository列表
	 * 
	 * @param contentId
	 * @return
	 */
	List<Repository> getRepositoryByContentId(String contentId);

	/**
	 * 根据accountIdhe和orderBy获取Repository的ids列表
	 * 
	 * @param accountId
	 * @param orderBy
	 *            排序字段
	 * @return
	 */
	List<String> getRepositoryIdsByAccountIdOrderBy(String accountId,
			String orderBy, int PageNum, int PageSize);

	/**
	 * 保存
	 * 
	 * @param repository
	 */
	void save(Repository repository);

	void setPrivateMessageCount(String accountId, long l);
	

	/**
	 * 搜索相关内容关系
	 * @param accountId
	 * @param tag
	 * @param key
	 * @param userTag
	 * @param sortType
	 * @param language
	 * @param offset
	 * @param limit
	 * @return
	 */
	PagedResult search(String accountId, String tag, 
			String userTag,String firendId, int offset, int limit);


	List<Repository> getAllRepository();
	
	/**
	 * 获取公共内容数量
	 * @return
	 */
	long getPublicRepositoryNum();
}
