package me.twocoffee.service.fetch.impl;

import me.twocoffee.common.article.ArticleExtractor;
import me.twocoffee.service.fetch.HtmlArticleExtractionService;
import me.twocoffee.service.fetch.HtmlWithoutArticleException;

import org.springframework.stereotype.Service;

@Service
public class HtmlArticleExtractionServiceImpl implements
		HtmlArticleExtractionService {

	@Override
	public ArticleExtractor.Article getArticle(String html)
			throws HtmlWithoutArticleException {
		ArticleExtractor articleExtractor = new ArticleExtractor();
		ArticleExtractor.Article article = articleExtractor.getArticle(html);
		return article;
	}

}
