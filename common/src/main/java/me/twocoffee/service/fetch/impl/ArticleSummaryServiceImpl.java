package me.twocoffee.service.fetch.impl;

import me.twocoffee.common.SummaryHtmlParser;
import me.twocoffee.service.fetch.ArticleSummaryService;

import org.htmlparser.util.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ArticleSummaryServiceImpl implements ArticleSummaryService {
	protected static final Logger LOGGER = LoggerFactory
			.getLogger(ArticleSummaryServiceImpl.class);

	@Override
	public String getSummary(String article) {
		if(article == null || article.trim().length()<1) {
			return "";
		}
		String summary = null;
		SummaryHtmlParser summaryHtmlParser = new SummaryHtmlParser();
		try {
			summary = summaryHtmlParser.getSummaryByP(article);
		} catch (ParserException e) {
			LOGGER.error(e.getMessage(), e);
			return null;
		}
		return summary;
	}
}
