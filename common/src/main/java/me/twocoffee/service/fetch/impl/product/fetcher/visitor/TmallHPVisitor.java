/**
 * 
 */
package me.twocoffee.service.fetch.impl.product.fetcher.visitor;

import java.util.Map;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.util.ParserException;

/**
 * @author momo
 * 
 */
public class TmallHPVisitor extends AbstractHPVisitor {

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

    class PriceTagFilter implements NodeFilter {

	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean accept(Node node) {

	    if (node instanceof Tag) {
		Tag t = (Tag) node;

		if ("script".equalsIgnoreCase(t.getTagName())
			&& t.toPlainTextString().indexOf("'defaultItemPrice':") > 0) {

		    return true;
		}
	    }
	    return false;
	}
    }

    private static void testUrl(String url)
	    throws ParserException {

	TmallResultHandler handler1 = new TmallResultHandler();
	TmallHPVisitor hpVisitor2 = new TmallHPVisitor();
	hpVisitor2.setResultHandler(handler1);
	Parser parser2 = new Parser(url);

	parser2.visitAllNodesWith(hpVisitor2);
	Map<String, String> results2 = hpVisitor2.getResult(url);

	for (String key : results2.keySet()) {
	    System.out.println(key);
	    System.out.println(results2.get(key));
	}
    }

    public static void main(String[] args) throws ParserException {
	testUrl("http://detail.tmall.com/item.htm?spm=1007.100361.1.7&id=15011055941");
	testUrl("http://detail.tmall.com/item.htm?spm=640.1000554.1000193.1&id=6063580226&cm_cat=50034017&source=dou");
	testUrl("http://detail.tmall.com/item.htm?spm=a220m.1000858.1000725.5&id=15414964154&is_b=1&cat_id=50030224&q=%C8%C8%CB%AE%C6%F7");
    }

    private final NameTagFilter nameTagFilter = new NameTagFilter();

    private final ImageTagFilter imageTagFilter = new ImageTagFilter();

    private final PriceTagFilter priceTagFilter = new PriceTagFilter();

    private void fetchImage(Tag tag) {

	if (imageTagFilter.accept(tag)) {
	    result.put("image", tag.getAttribute("src"));
	}
    }

    private void fetchName(Tag tag) {

	if (nameTagFilter.accept(tag)) {
	    result.put("name", tag.toPlainTextString());
	}
    }

    private void fetchPrice(Tag tag) {

	if (priceTagFilter.accept(tag)) {
	    String script = tag.toPlainTextString();
	    int start = script.indexOf("'defaultItemPrice':'");
	    int end = -1;
	    String price = "";

	    if (start >= 0) {
		end = script.indexOf("'", start + 20);

		if (end > 0) {
		    price = script.substring(start + 20, end);
		}
	    }
	    result.put("price", price);
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
}
