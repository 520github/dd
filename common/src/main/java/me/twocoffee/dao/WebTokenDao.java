package me.twocoffee.dao;

import me.twocoffee.entity.WebToken;
import me.twocoffee.entity.WebToken.UserType;

public interface WebTokenDao {
	
	/**
	 * 创建webToken
	 */
	public WebToken createWebToken();
	
	
	/**
	 * 创建webToken
	 * @param userAgent
	 * @return
	 */
	public WebToken createWebToken(String userAgent);
	
	/**
	 * 创建webToken
	 * @param userAgent
	 * @param url
	 * @return
	 */
	public WebToken createWebToken(String userAgent,String url);
	
	/**
	 * 根据ID查找
	 * @param id
	 * @return
	 */
	public WebToken findById(String id);
	
	/**
	 * 绑定账号
	 * @param id  
	 * @param accountId 账号ID
	 */
	public void bindAccount(String id,String accountId);
	
	/**
	 * 绑定账号
	 * @param id  
	 * @param accountId 账号ID
	 * @param userType
	 */
	public void bindAccount(String id,String accountId,UserType userType);
	
	
	/**
	 * 解除绑定账号
	 * @param id
	 */
	public void unbingAccount(String id);
}
