/**
 * 
 */
package me.twocoffee.service.fetch.impl.product.fetcher;

import java.util.Map;

import me.twocoffee.service.fetch.FetchService;

import org.apache.commons.lang.StringUtils;
import org.htmlparser.Parser;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Page;
import org.htmlparser.visitors.NodeVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author momo
 * 
 */
@Service
public class HtmlParserExecutor implements ModelExecutor {

    @Autowired
    private FetchService fetchService;

    private String getHtml(String url) {
	String html = fetchService.getStringResource(url);

	if (StringUtils.isBlank(html)) {
	    throw new RuntimeException("can not fetch resource [" + url + "]");
	}
	return html;
    }

    @Override
    public Map<String, String> execute(String url, IProductVisitor visitor,
	    String html) {

	if (StringUtils.isBlank(html)) {
	    html = getHtml(url);
	}

	try {
	    Parser parser = new Parser();
	    // PrototypicalNodeFactory factory = new PrototypicalNodeFactory();
	    // factory.registerTag(new EMTag());
	    // parser.setNodeFactory(factory);
	    Page page = new Page(html);
	    Lexer lexer = new Lexer();
	    lexer.setPage(page);
	    parser.setLexer(lexer);
	    parser.visitAllNodesWith((NodeVisitor) visitor);
	    return visitor.getResult(url);

	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    public FetchService getFetchService() {
	return fetchService;
    }

    public void setFetchService(FetchService fetchService) {
	this.fetchService = fetchService;
    }

}
