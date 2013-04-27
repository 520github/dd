/**
 * 
 */
package me.twocoffee.service.fetch.impl.product.fetcher.visitor;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Tag;

/**
 * @author momo
 * 
 */
public class DangdangHPVisitor extends AbstractHPVisitor {

	private ImageTagFilter imageTagFilter = new ImageTagFilter();

	private NameTagFilter nameTagFilter = new NameTagFilter();

	private PriceTagFilter priceTagFilter = new PriceTagFilter();

	class PriceTagFilter implements NodeFilter {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public boolean accept(Node node) {

			if (node instanceof Tag) {
				Tag t = (Tag) node;

				if ("span".equalsIgnoreCase(t.getTagName())
						&& "actualPriceValue".equalsIgnoreCase(t
								.getAttribute("id"))) {

					return true;
				}
			}
			return false;
		}

	}

	class NameTagFilter implements NodeFilter {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public boolean accept(Node node) {

			if (node instanceof Tag) {
				Tag t = (Tag) node;
				Tag p = (Tag) t.getParent();

				if ("h1".equalsIgnoreCase(t.getTagName())
						&& "h1_title book_head".equalsIgnoreCase(p
								.getAttribute("class"))) {

					return true;
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
						&& "largePic".equals(t.getAttribute("id"))) {

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

		if (!result.containsKey("price")) {
			fetchPrice(tag);
		}

		if (!result.containsKey("image")) {
			fetchImage(tag);
		}
	}

	private void fetchPrice(Tag tag) {

		if (priceTagFilter.accept(tag)) {
			result.put("price", tag.toPlainTextString());
		}
	}

	private void fetchName(Tag tag) {

		if (nameTagFilter.accept(tag)) {
			result.put("name", tag.toPlainTextString());
		}
	}

	private void fetchImage(Tag tag) {

		if (imageTagFilter.accept(tag)) {
			result.put("image", tag.getAttribute("src"));
		}
	}

}
