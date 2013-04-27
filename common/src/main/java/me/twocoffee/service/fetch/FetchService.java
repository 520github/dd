package me.twocoffee.service.fetch;

import me.twocoffee.entity.Content;

public interface FetchService {
	/**
	 * 加入抓取队列，抓取Content中Attachment属性中的orgUrl，异步的
	 * 
	 * @param contentId
	 */
	public void addToFetch(Content content);

	/**
	 * 抓取资源非文本资源，同步的
	 * 
	 * @return 存放路径
	 */
	public String getResource(String url);
	
	/**
	 * 抓取资源非文本资源，同步的
	 * 
	 * @return 存放路径
	 */
	public String getResource(String url, String sourceUrl);

	/**
	 * 抓取资源文本资源,同步的
	 * 
	 * @return 资源
	 */
	public String getStringResource(String url);

	/**
	 * 停止
	 */
	public void shutdown();
}
