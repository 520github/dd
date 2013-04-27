package me.twocoffee.service;

import java.util.Map;

import me.twocoffee.common.search.PagedResult;
import me.twocoffee.entity.Content.ContentType;
import me.twocoffee.service.entity.ContentDetail;

public interface ContentSearcher {
	/**
	 * 添加索引，收藏内容ID
	 * 
	 * @param repositoryId
	 */
	void addIndex(String repositoryId);

	/**
	 * 添加索引
	 * 
	 * @param map
	 */
	void addIndex(Map<String, Object> map);

	/**
	 * 添加索引
	 * 
	 * @param detail
	 */
	void addIndex(ContentDetail detail);

	/**
	 * 更新索引
	 * 
	 * @param repositoryId
	 */
	void updateIndex(String repositoryId);

	/**
	 * 更新索引
	 * 
	 * @param detail
	 */
	void updateIndex(ContentDetail detail);

	/**
	 * 删除索引
	 * 
	 * @param repositoryId
	 */
	void removeIndex(String repositoryId);

	/**
	 * 搜索收藏内容
	 * 
	 * @param accountId
	 *            用户帐号ID
	 * @param tag
	 *            系统标签
	 * @param key
	 *            搜索关键字
	 * @param source
	 *            搜索来源，0全部，1来自插件，2分享，3广场，4来自我关注的人
	 * @param contentType
	 *            内容类型，0全部，1链接，2文本，3图文，4商品
	 * @param userTag
	 *            标签，null或空表示不限，否则为标签字符串
	 * @param sortType
	 *            排序类型，0时间倒序，1评论倒序，2阅读倒序，3分享倒序，4收藏倒序
	 * @param language
	 *            语言，双字母，cn or en
	 * @param offset
	 *            从第几条开始
	 * @param limit
	 *            返回多少条
	 * @return 结果
	 */
	PagedResult search(String accountId, String tag, String key,
			String userTag, int sortType,
			String language, int offset, int limit);

	/**
	 * 搜索收藏内容
	 * 
	 * @param accountId
	 * @param tag
	 * @param key
	 * @param userTag
	 * @param friend
	 * @param sortType
	 * @param language
	 * @param offset
	 * @param limit
	 * @return
	 */
	PagedResult search(String accountId, String tag, String key,
			String userTag, String friend, int sortType,
			String language, int offset, int limit);

	/**
	 * 搜索收藏内容
	 * 
	 * @param accountId
	 * @param orTags
	 *            或操作tags
	 * @param tag
	 * @param key
	 * @param userTag
	 * @param friend
	 * @param sortType
	 * @param language
	 * @param offset
	 * @param limit
	 * @return
	 */
	PagedResult search(String accountId, String orTags, String tag,
			String key, String userTag, String friend, int sortType,
			String language, int offset, int limit);
	
	/**
	 * 搜索收藏内容
	 * 
	 * @param publicCond 公共内容搜索条件
	 * @param accountId
	 * @param orTags
	 *            或操作tags
	 * @param tag
	 * @param key
	 * @param userTag
	 * @param friend
	 * @param sortType
	 * @param language
	 * @param offset
	 * @param limit
	 * @return
	 */
	PagedResult search(String publicCond, String accountId, String orTags, String tag,
			String key, String userTag, String friend, int sortType,
			String language, int offset, int limit);

	/**
	 * 列出收藏内容
	 * 
	 * @param accountId
	 *            用户帐号ID
	 * @param tag
	 *            系统标签
	 * @param key
	 *            搜索关键字
	 * @param source
	 *            搜索来源，0全部，1来自插件，2分享，3广场，4来自我关注的人
	 * @param contentType
	 *            内容类型，0全部，1链接，2文本，3图文，4商品
	 * @param userTag
	 *            标签，null或空表示不限，否则为标签字符串
	 * @param sortType
	 *            排序类型，0时间倒序，1评论倒序，2阅读倒序，3分享倒序，4收藏倒序,5修改时间
	 * @param language
	 *            语言，双字母，cn or en
	 * @param offset
	 *            从第几条开始
	 * @param limit
	 *            返回多少条
	 * @return 结果
	 */
	PagedResult list(String accountId, String tag, String key, String userTag,
			int sortType,
			String language, int offset, int limit);

	/**
	 * 列出收藏内容
	 * 
	 * @param accountId
	 * @param tag
	 * @param key
	 * @param userTag
	 * @param friend
	 * @param sortType
	 * @param language
	 * @param offset
	 * @param limit
	 * @return
	 */
	PagedResult list(String accountId, String tag, String key, String userTag,
			String friend, int sortType,
			String language, int offset, int limit);

	/**
	 * 列出收藏内容
	 * 
	 * @param accountId
	 * @param orTags
	 *            或操作tag
	 * @param tag
	 * @param key
	 * @param userTag
	 * @param friend
	 * @param sortType
	 * @param language
	 * @param offset
	 * @param limit
	 * @return
	 */
	PagedResult list(String accountId, String orTags, String tag, String key,
			String userTag, String friend, int sortType,
			String language, int offset, int limit);
	
	/**
	 * 列出收藏内容
	 * 
	 * @param publicCond 公共内容搜索条件
	 * @param accountId
	 * @param orTags
	 *            或操作tag
	 * @param tag
	 * @param key
	 * @param userTag
	 * @param friend
	 * @param sortType
	 * @param language
	 * @param offset
	 * @param limit
	 * @return
	 */
	PagedResult list(String publicCond,String accountId, String orTags, String tag, String key,
			String userTag, String friend, int sortType,
			String language, int offset, int limit);

	/**
	 * 从entity的contentType转成索引用的值
	 * 
	 * @param contentType
	 * @return
	 */
	int getContentType(ContentType contentType);

	/**
	 * 获取排序枚举值转为索引用的数值
	 * 
	 * @param order
	 *            排序枚举
	 * @return
	 */
	int getSortType(String order);

	/**
	 * 批量添加内容索引
	 * 
	 * @param ids
	 */
	void addIndexs(String[] ids);
}
