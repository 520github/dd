/**
 * 
 */
package me.twocoffee.dao;

import java.util.List;

import me.twocoffee.entity.PublicRepository;

/**
 * @author momo
 * 
 */
public interface PublicRepositoryDao {

	void save(PublicRepository repository);

	PublicRepository getByContentIdAndAccountId(String contentId,
			String accountId);
	public List<PublicRepository> getByAccountId(String accountId,String orderby, int PageSize,
			int PageNum);
	
	public List<String> getRepositoryIdsByAccountIdOrderBy(String accountId,String orderby, int PageSize,
			int PageNum);

	int countByAccountId(String id);

}
