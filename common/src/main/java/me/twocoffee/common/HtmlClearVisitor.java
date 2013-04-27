/**
 * 
 */
package me.twocoffee.common;

import org.htmlparser.Tag;
import org.htmlparser.Text;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.visitors.NodeVisitor;

/**
 * @author wenjian
 * 
 */
public class HtmlClearVisitor extends NodeVisitor {

	private StringBuilder builder = new StringBuilder();

	private CssSelectorNodeFilter styleAttrfilter = new CssSelectorNodeFilter(
			"[style]");

	private CssSelectorNodeFilter classAttrfilter = new CssSelectorNodeFilter(
			"[class]");

	@Override
	public void visitTag(Tag tag) {

		if (styleAttrfilter.accept(tag)) {
			tag.removeAttribute("style");
		}

		if (classAttrfilter.accept(tag)) {
			tag.removeAttribute("class");
		}
		builder.append(tag.toTagHtml());
	}

	@Override
	public void visitStringNode(Text text) {
		builder.append(text.toHtml());
	}

	@Override
	public void visitEndTag(Tag tag) {
		builder.append(tag.toTagHtml());
	}

	public String getHtml() {
		return builder.toString();
	}

}
