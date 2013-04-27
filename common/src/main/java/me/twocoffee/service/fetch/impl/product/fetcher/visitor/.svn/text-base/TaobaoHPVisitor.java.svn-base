/**
 * 
 */
package me.twocoffee.service.fetch.impl.product.fetcher.visitor;

import java.util.Map;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.util.ParserException;

/**
 * @author momo
 * 
 */
public class TaobaoHPVisitor extends AbstractHPVisitor {

    private NameTagFilter nameTagFilter = new NameTagFilter();

    private ImageTagFilter imageTagFilter = new ImageTagFilter();

    private PriceTagFilter priceTagFilter = new PriceTagFilter();

    class NameTagFilter implements NodeFilter {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean accept(Node node) {

	    if (node instanceof Tag) {
		Tag t = (Tag) node;
		Node p = t.getParent();

		if ("h3".equalsIgnoreCase(t.getTagName())) {

		    if (p instanceof Tag
			    && "tb-detail-hd".equals(((Tag) p)
				    .getAttribute("class"))
			    && "div".equalsIgnoreCase(((Tag) p).getTagName())) {

			return true;
		    }
		}
	    }
	    return false;
	}
    }

    class ImageTagFilter implements NodeFilter {

	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean accept(Node node) {

	    if (node instanceof Tag) {
		Tag t = (Tag) node;

		if ("img".equalsIgnoreCase(t.getTagName())
			&& "J_ImgBooth".equals(t.getAttribute("id"))) {

		    return true;
		}
	    }
	    return false;
	}
    }

    class PriceTagFilter implements NodeFilter {

	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean accept(Node node) {

	    if (node instanceof Tag) {
		Tag t = (Tag) node;

		if ("em".equalsIgnoreCase(t.getTagName())
			&& "em".equalsIgnoreCase(t.getTagName())
			&& "tb-rmb-num".equals(t
				.getAttribute("class"))) {

		    return true;
		}
	    }
	    return false;
	}
    }

    @Override
    public void visitTag(Tag tag) {

	if (!result.containsKey("title")) {
	    fetchTitle(tag);
	}

	if (!result.containsKey("name")) {
	    fetchName(tag);
	}

	if (!result.containsKey("price") || !result.containsKey("priceapi")) {
	    fetchPrice(tag);
	}

	if (!result.containsKey("image")) {
	    fetchImage(tag);
	}
    }

    private void fetchImage(Tag tag) {

	if (imageTagFilter.accept(tag)) {
	    result.put("image", tag.getAttribute("src"));
	}
    }

    private void fetchPrice(Tag tag) {

	if (priceTagFilter.accept(tag)) {
	    // NodeList children = tag.getChildren();
	    // String price = "";
	    //
	    // if (children == null || children.size() == 0) {
	    // return;
	    // }
	    //
	    // for (int i = 0; i < children.size(); i++) {
	    // Tag node = (Tag) children.elementAt(i);
	    //
	    // if (node.getAttribute("class").equals("tb-rmb-num")
	    // && node.getTagName().equals("em")) {
	    //
	    // price = node.toPlainTextString();
	    // }
	    // }
	    Node next = tag.getNextSibling();

	    if (next instanceof TextNode && next.toPlainTextString() != null) {
		result.put("price", next.toPlainTextString().trim());
	    }
	}
    }

    private void fetchName(Tag tag) {

	if (nameTagFilter.accept(tag)) {
	    result.put("name", tag.toPlainTextString());
	}
    }

    public static void main(String[] args) throws ParserException {
	TaobaoResultHandler handler = new TaobaoResultHandler();
	TaobaoHPVisitor hpVisitor = new TaobaoHPVisitor();
	hpVisitor.setResultHandler(handler);
	String url = "http://item.taobao.com/item.htm?spm=1001.1000439.11.2.9OS0DU&id=9845002860";
	Parser parser = new Parser(url);
	parser.visitAllNodesWith(hpVisitor);
	Map<String, String> results = hpVisitor.getResult(url);

	for (String key : results.keySet()) {
	    System.out.println(key);
	    System.out.println(results.get(key));
	}
	TaobaoHPVisitor hpVisitor1 = new TaobaoHPVisitor();
	hpVisitor1.setResultHandler(handler);
	url = "http://item.taobao.com/item.htm?spm=a2106.m965.1000384.d131.dmQCpc&id=8634500455&scm=1029.newlist-0.bts7.50034536&ppath=";
	Parser parser1 = new Parser(url);

	parser1.visitAllNodesWith(hpVisitor1);
	Map<String, String> results1 = hpVisitor1.getResult(url);

	for (String key : results1.keySet()) {
	    System.out.println(key);
	    System.out.println(results1.get(key));
	}
    }
}
