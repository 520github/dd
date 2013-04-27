package me.twocoffee.service;

import java.util.List;

import me.twocoffee.entity.SystemTagEnum;
import me.twocoffee.entity.Tag;

public interface TagService {

	/**
	 * 根据id查找tag
	 * 
	 * @param tagId
	 * @return
	 */
	public Tag getById(Tag.TagId tagId);

	/**
	 * 根据账户查找所有tag
	 * 
	 * @param account
	 * @return
	 */
	public List<Tag> getTagsByAccount(String accountId);

	/**
	 * 存储tag
	 * 
	 * @param tag
	 */
	public void save(Tag tag);
	
	
	/**
	 * 删除tag
	 * 
	 * @param tag
	 */
	public void removeById(Tag.TagId tagId);
	
	
	/**
	 * 更新tag
	 * 
	 * @param tagId
	 * @param newTag
	 */
	public void updateTagById(Tag.TagId tagId, String newTag);
	
	
	/**
	 * 用户对内容打标签的时候,进行记录标签及打的次数
	 * 
	 * @param accountId
	 * @param oldTag
	 * @param newTag
	 */
	public void save(String accountId, List<String> oldTag,List<String> newTag);

	/**
	 * 得到tag的描述名
	 * @param tagEnum tag枚举
	 * @return tag描述名
	 */
	String getSystemTagName(SystemTagEnum tagEnum);
	
	
	/**
	 * 获取用户最常用的前几个标签列表
	 * 
	 * @param accountId
	 * @param top
	 * @return
	 */
	public List<String> getTopHotTagList(String accountId, int top);
	
	/**
	 * 获取用户最近使用的前几个标签列表
	 * 
	 * @param accountId
	 * @param top
	 * @return
	 */
	public List<String> getTopLasteUseTagList(String accountId, int top);
	
	/**
	 * 获取用户最常用的前几个标签字符串
	 * 
	 * @param accountId
	 * @param top
	 * @param joinStr
	 * @return
	 */
	public String getTopHotTagStr(String accountId, int top, String joinStr);
	
	/**
	 * 获取用户最近使用的前几个标签字符串
	 * 
	 * @param accountId
	 * @param top
	 * @param joinStr
	 * @return
	 */
	public String getTopLasteUseTagStr(String accountId, int top, String joinStr);
}
