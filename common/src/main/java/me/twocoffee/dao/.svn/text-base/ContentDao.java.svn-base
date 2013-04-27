package me.twocoffee.dao;

import me.twocoffee.entity.Content;
import me.twocoffee.entity.ContentAction;

public interface ContentDao {
	/**
	 * 根据id获得
	 * 
	 * @param id
	 * @return
	 */
	Content getById(String id);

	/**
	 * 根据url获得一个content实体
	 * 
	 * @param url
	 * @return
	 */
	Content getByUrl(String url);

	/**
	 * 根据url和类型获得一个content实体
	 * 
	 * @param url
	 * @return
	 */
	Content getByUrlAndContentType(String url, Content.ContentType type);

	/**
	 * 保存
	 * 
	 * @param content
	 */
	void save(Content content);

	void saveContentAction(ContentAction ca);

	void updateContent(Content content);
}
