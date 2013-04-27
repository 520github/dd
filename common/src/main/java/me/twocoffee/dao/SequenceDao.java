/**
 * 
 */
package me.twocoffee.dao;

/**
 * @author momo
 * 
 */
public interface SequenceDao {

	Long next();

	/**
	 * 获取指定类别的序列
	 * 
	 * @param name
	 * @return
	 */
	Long next(String name);
}
