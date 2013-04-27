package me.twocoffee.service.fetch;

import me.twocoffee.common.article.ArticleExtractor;

/**
 * 正文提取，从一个html网页中萃取出正文内容
 * 
 * @author chongf
 * 
 */
public interface HtmlArticleExtractionService {
	/**
	 * 获取正文
	 * 
	 * @param html
	 * @return
	 * @throws HtmlWithoutArticleException
	 *             无法从网页中提取出正文时抛出
	 */
	public ArticleExtractor.Article getArticle(String html)
			throws HtmlWithoutArticleException;
}
