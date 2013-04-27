/**
 * 
 */
package me.twocoffee.service.fetch.impl.product.fetcher.visitor;

import java.util.HashMap;
import java.util.Map;

import me.twocoffee.service.fetch.impl.product.fetcher.IProductVisitor;

import org.htmlparser.Tag;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.visitors.NodeVisitor;

/**
 * @author momo
 * 
 */
public abstract class AbstractHPVisitor extends NodeVisitor implements
		IProductVisitor {

	protected TagNameFilter titleFilter = new TagNameFilter("title");

	protected ResultHandler resultHandler;

	public ResultHandler getResultHandler() {
		return resultHandler;
	}

	@Override
	public void setResultHandler(ResultHandler resultHandler) {
		this.resultHandler = resultHandler;
	}

	protected Map<String, String> result = new HashMap<String, String>();

	@Override
	public Map<String, String> getResult(String url) {

		if (resultHandler != null) {
			result.put("url", url);
			return resultHandler.handle(result);
		}
		return result;
	}

	protected void fetchTitle(Tag tag) {

		if (titleFilter.accept(tag)) {
			result.put("title", tag.toPlainTextString());
		}
	}

}
