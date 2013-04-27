package me.twocoffee.common;

import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.htmlparser.Text;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.visitors.NodeVisitor;

public class AllTagContentVisitor extends NodeVisitor {
	private String content = "";
	private String clientSummary = "";
	private int len = 0;
	private int textLenLimit = 65535;
	private Tag maxTag = null;
	private CssSelectorNodeFilter brFilter = new CssSelectorNodeFilter("br");
	private CssSelectorNodeFilter wbrFilter = new CssSelectorNodeFilter("wbr");
	private CssSelectorNodeFilter aFilter = new CssSelectorNodeFilter("a");
	private CssSelectorNodeFilter imgFilter = new CssSelectorNodeFilter("img");

	public AllTagContentVisitor(int textLenLimit) {
		this.textLenLimit = textLenLimit;
	}

	public AllTagContentVisitor() {
		// TODO Auto-generated constructor stub
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public void visitTag(Tag tag) {
		// System.out.println(tag.toPlainTextString());
		// if ((len + tag.toPlainTextString().length()) < textLenLimit) {
		// len += tag.toPlainTextString().length();
		// System.out.println(tag.toHtml());
		// content += tag.toHtml();
		// }
		boolean isFlag = true;
		NodeList nodeList = tag.getChildren();
		if ((nodeList != null) && (nodeList.size() > 0)) {
			for (int i = 0; i < nodeList.size(); i++) {
				Node node = nodeList.elementAt(i);
				if ((!(node instanceof TextNode)) && (!brFilter.accept(node))
						&& (!wbrFilter.accept(node))
						&& (!aFilter.accept(node)) && (!imgFilter.accept(node))) {
					isFlag = false;
				}
			}
		}
		if (isFlag) {
			if (len < tag.toPlainTextString().length()) {
				len = tag.toPlainTextString().length();
				maxTag = tag;
			}
		}
	}

	public String getMaxText() {
		if ((maxTag != null)
				&& (maxTag.toPlainTextString().trim().length() > 0)) {
			if (maxTag.toPlainTextString().length() <= textLenLimit) {
				return "<p>" + maxTag.toPlainTextString() + "</p>";
			} else {
				return "<p>"
						+ maxTag.toPlainTextString().substring(0, textLenLimit)
						+ "......</p>";
			}
		} else {
			return "";
		}
	}

	@Override
	public void visitStringNode(Text string) {
		clientSummary += string.getText();
	}

	public void setClientSummary(String clientSummary) {
		this.clientSummary = clientSummary;
	}

	public String getClientSummary() {
		return clientSummary;
	}

}
