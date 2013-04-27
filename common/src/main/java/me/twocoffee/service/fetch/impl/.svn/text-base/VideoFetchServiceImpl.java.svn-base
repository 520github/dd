package me.twocoffee.service.fetch.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.twocoffee.entity.Content;
import me.twocoffee.entity.ProductPayload;
import me.twocoffee.entity.VideoPayload;
import me.twocoffee.service.fetch.FetchService;
import me.twocoffee.service.fetch.VideoFetchService;
import me.twocoffee.service.fetch.video.Crawler;
import me.twocoffee.service.fetch.video.YoukuCrawler;

@Service
public class VideoFetchServiceImpl implements VideoFetchService {
	private final Crawler[] crawlers = new Crawler[] {
			new YoukuCrawler()
	};
	
	@Override
	public boolean isVideo(String url) {
		for (Crawler c : crawlers) {
			if (c.isMine(url))
				return true;
		}
		return false;
	}

	@Override
	public Content getVideoContent(String url) {
		for (Crawler c : crawlers) {
			if (c.isMine(url))
				return crawlVideoContent(c, url);
		}
		return null;
	}

	private Content crawlVideoContent(Crawler c, String url) {
		return c.getContent(url);
	}
}
