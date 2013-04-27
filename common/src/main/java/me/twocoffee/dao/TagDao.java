package me.twocoffee.dao;

import java.util.List;

import me.twocoffee.entity.StatisticTag;
import me.twocoffee.entity.Tag;

public interface TagDao {

	/**
	 * 根据id查找Tag
	 * 
	 * @param id
	 * @return
	 */
	Tag getById(Tag.TagId tagId);

	/**
	 * 根据账户查找所有tag列表
	 * 
	 * @param accountId
	 * @return
	 */
	List<Tag> getTagsByAccount(String accountId);

	/**
	 * 保存/修改
	 * 
	 * @param tag
	 */
	void save(Tag tag);
	
	/**
	 * 删除Tag
	 * 
	 * @param tag
	 */
	void removeById(Tag.TagId tagId);
	
	
	/**
	 * 取得前N个tag列表
	 * 
	 * @param accountId
	 * @param order
	 * @param limit
	 * @return
	 */
	List<Tag> getTopTags(String accountId, String order, int limit);
}
