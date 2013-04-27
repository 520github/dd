package me.twocoffee.dao;

import me.twocoffee.entity.AuthToken;

public interface AuthTokenDao {
	/**
	 * 创建token
	 * 
	 * @param accountId
	 */
	AuthToken createToken(String accountId);

	/**
	 * 根据id查找
	 * 
	 * @param id
	 * @return
	 */
	AuthToken findById(String id);

	/**
	 * 根据accountId查找
	 * 
	 * @param accountId
	 * @return
	 */
	AuthToken findByAccountId(String accountId);
}
