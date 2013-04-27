package me.twocoffee.common;

import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.visitors.NodeVisitor;

public class TagContentVisitor extends NodeVisitor {
	private String content = "";
	private CssSelectorNodeFilter nodeFilter = null;
	private int len = 0;
	private int pNum = 0;
	private int textLenLimit = 300;
	private int pLimit = 20;
	private boolean goFlag = true;
	private String pHtml = null;
	private int limit = 0;

	public TagContentVisitor() {
	}

	public TagContentVisitor(CssSelectorNodeFilter nodeFilter) {
		this.nodeFilter = nodeFilter;
	}

	public int getLen() {
		return len;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public void visitTag(Tag tag) {
		if (goFlag && nodeFilter.accept(tag)) {
			pNum++;
			if (pNum > pLimit) {
				goFlag = false;
				return;
			}
			if ((len + tag.toPlainTextString().length()) > textLenLimit) {
				pHtml = tag.toHtml();
				limit = textLenLimit - len;
				goFlag = false;
				return;
			}
			if (tag.getAttribute("style") != null) {
				tag.setAttribute("style", "");
			}
			NodeList nodeList = tag.getChildren();
			if (nodeList != null) {
				for (int i = 0; i < nodeList.size(); i++) {
					Node node = nodeList.elementAt(i);
					if (node instanceof Tag) {
						Tag t = (Tag) node;
						if (t.getAttribute("style") != null) {
							t.setAttribute("style", "");
						}
					}
				}
			}
			len += tag.toPlainTextString().trim().length();
			content += tag.toHtml();
		}

	}

	public void setpHtml(String pHtml) {
		this.pHtml = pHtml;
	}

	public String getpHtml() {
		return pHtml;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getLimit() {
		return limit;
	}

}
