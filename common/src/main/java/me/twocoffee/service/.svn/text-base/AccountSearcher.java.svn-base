package me.twocoffee.service;

import java.util.List;

import me.twocoffee.common.search.PagedResult;
import me.twocoffee.entity.Account;

/**
 * 帐号搜索
 * @author drizzt
 *
 */
public interface AccountSearcher {
	/**
	 * 添加帐号索引
	 * @param accountId
	 */
	void addIndex(String accountId);

	/**
	 * 添加帐号索引
	 * @param a
	 */
	void addIndex(Account a);
	
	/**
	 * 按昵称搜索 
	 * @param name
	 * @param offset
	 * @param pageSize
	 * @return 返回帐号ID列表
	 */
	PagedResult searchByName(String name, int offset, int pageSize);
	
	/**
	 * 按登录名搜索
	 * @param loginName
	 * @param offset
	 * @param pageSize
	 * @return 返回帐号ID列表
	 */
	PagedResult searchByLoginName(String loginName, int offset, int pageSize);
	
	/**
	 * 按关键字搜索
	 * @param key
	 * @param sort
	 * @param offset
	 * @param pageSize
	 * @return 返回帐号ID列表
	 */
	PagedResult search(String key, int sort, int offset, int pageSize);
}
