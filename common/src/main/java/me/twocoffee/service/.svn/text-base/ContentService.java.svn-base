package me.twocoffee.service;

import me.twocoffee.entity.Content;
import me.twocoffee.entity.Content.ContentType;
import me.twocoffee.entity.ContentAction;

public interface ContentService {
	/**
	 * 根据id获取Content实体
	 * 
	 * @param id
	 * @return
	 */
	public Content getById(String id);

	/**
	 * 根据url获取content实体
	 * 
	 * @param url
	 * @return
	 */
	public Content getByUrl(String url);

	/**
	 * 根据url和type获取content实体
	 * 
	 * @param url
	 * @return
	 */
	public Content getByUrlAndType(String url, ContentType type);

	/**
	 * 内容存储
	 * 
	 * @param content
	 */
	public void save(Content content);

	public void saveContentAction(ContentAction ca);

}
