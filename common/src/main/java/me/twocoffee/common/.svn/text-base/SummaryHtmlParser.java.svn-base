package me.twocoffee.common;

import org.htmlparser.Parser;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Page;
import org.htmlparser.util.DefaultParserFeedback;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.ParserFeedback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SummaryHtmlParser {

	Logger logger = LoggerFactory.getLogger(SummaryHtmlParser.class);

	public SummaryHtmlParser() {

	}

	/**
	 * 解析字符串
	 * 
	 * @param inputHTML
	 *            String
	 * @return Parser
	 */
	public Parser createParser(String inputHTML) {
		Lexer mLexer = new Lexer(new Page(inputHTML));
		return new Parser(mLexer, (ParserFeedback) new DefaultParserFeedback(
				DefaultParserFeedback.QUIET));

	}

	public String getSummaryByP(String document)
			throws ParserException {
		String summary = null;
		Parser parser = createParser(document);
		parser.reset();
		// parser.setResource(document);

		CssSelectorNodeFilter nodeFilter = new CssSelectorNodeFilter("p");
		TagContentVisitor visitor = new TagContentVisitor(nodeFilter);
		parser.visitAllNodesWith(visitor);
		if ((visitor.getContent().equalsIgnoreCase(""))
				|| (visitor.getLen() <= 0)) {
			summary = getSummary(document, 500);
		} else {
			if (visitor.getLimit() > 0) {
				summary = visitor.getContent()
						+ getSummary(visitor.getpHtml(), visitor.getLimit());
			} else {
				summary = visitor.getContent();
			}
		}
		if (summary == null || summary.equalsIgnoreCase("")) {
			summary = document.substring(0, document.length() > 300 ? 300
					: document.length()) + "......";
		}
		return summary.replaceAll("<img[^>]*>", "")
				.replaceAll("<IMG[^>]*>", "");
	}

	public String getSummary(String document, int limit)
			throws ParserException {
		String summary = "";
		Parser parser = createParser(document);
		parser.reset();
		//parser.setResource(document);
		AllTagContentVisitor visitor = new AllTagContentVisitor(limit);
		parser.visitAllNodesWith(visitor);
		summary = visitor.getMaxText();
		if (summary == null || summary.equals("")) {
			String doc = visitor.getClientSummary();
			summary = "<p>" + doc.substring(0,
					doc.length() > 300 ? 300
							: doc.length()).trim()
					+ "......</p>";
		}
		return summary;

	}

}
