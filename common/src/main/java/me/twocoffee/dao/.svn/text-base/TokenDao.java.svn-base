package me.twocoffee.dao;

import java.util.Date;
import java.util.List;

import me.twocoffee.entity.Token;

public interface TokenDao {

	/**
	 * 创建token
	 * 
	 * @param accountId
	 */
	public void createToken(Token token);

	/**
	 * 根据id查找
	 * 
	 * @param id
	 * @return
	 */
	public Token findById(String id);

	/**
	 * 查找最后一个创建的token
	 * 
	 * @param refer
	 * @return
	 */
	public Token findLastTokenByRefer(String refer);

	/**
	 * 根据referh和createTime查找
	 * 
	 * @param refer
	 * @param minCreateTime最小创建时间
	 *            ，创建时间大于该参数的将被查询出来
	 * @return
	 */
	public List<Token> listByReferAndCreateTime(String refer, Date minCreateTime);

}
