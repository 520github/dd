package me.twocoffee.service.fetch;

import me.twocoffee.entity.Content;

/**
 * 视频抓取服务
 * @author drizzt
 *
 */
public interface VideoFetchService {
	/**
	 * 是否是视频
	 * @param url
	 * @return
	 */
	boolean isVideo(String url);
	
	/**
	 * 抓取并得到内容
	 * @param url
	 * @return
	 */
	Content getVideoContent(String url);
}
