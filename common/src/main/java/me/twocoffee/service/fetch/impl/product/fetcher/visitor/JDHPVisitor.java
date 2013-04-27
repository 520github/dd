/**
 * 
 */
package me.twocoffee.service.fetch.impl.product.fetcher.visitor;

import java.util.Map;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.util.ParserException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author momo
 * 
 */
@Service("jdHPVisitor")
@Scope("prototype")
public class JDHPVisitor extends AbstractHPVisitor {
	//京东商品名称
	CssSelectorNodeFilter name = new CssSelectorNodeFilter(
			"div#product-intro>div#name>h1");
	//京东商品图片
	CssSelectorNodeFilter image = new CssSelectorNodeFilter(
			"div#preview>div#spec-n1>img");
	//京东商品价格
	CssSelectorNodeFilter price = new CssSelectorNodeFilter(
			"div#recommend>div>div.stabcon>div>div.p-price>input");
	private ScriptFilter scriptFilter = new ScriptFilter();

	class ScriptFilter implements NodeFilter {

		private static final long serialVersionUID = 1L;

		@Override
		public boolean accept(Node node) {

			if (node instanceof Tag) {
				Tag t = (Tag) node;

				if ("script".equalsIgnoreCase(t.getTagName())
						&& t.toPlainTextString().indexOf(
								"function jdPshowRecommend(") > 0) {

					return true;
				}
			}
			return false;
		}
	}

	@Override
	public void visitTag(Tag tag) {
		if(name.accept(tag)) {
			result.put("name", tag.toPlainTextString());
			result.put("title", result.get("name"));
		}
		else if(image.accept(tag)) {
			result.put("image", tag.getAttribute("src"));
		}
		else if(price.accept(tag)) {
			result.put("price", tag.getAttribute("wmeprice"));
		}
		else {
			//System.out.println("found not");
		}
	}

	private void fetch(Tag tag) {

		if (scriptFilter.accept(tag)) {
			String script = tag.toPlainTextString();
			int s = script.indexOf("var title = \"") + 13;
			int e = script.indexOf("\"", s);
			result.put("name", script.substring(s, e));
			s = script.indexOf("var img =\"") + 10;
			e = script.indexOf("\"", s);
			result.put("image", script.substring(s, e));
			s = script.indexOf("京东价：￥") + 5;
			e = script.indexOf("。", s);
			result.put("price", script.substring(s, e));
		}
	}

	// http://www.360buy.com/product/306305.html
	public static void main(String[] args) throws ParserException {
		JDHPVisitor hpVisitor = new JDHPVisitor();
		String url = "http://www.360buy.com/product/365310.html";
		Parser parser = new Parser(url);

		parser.visitAllNodesWith(hpVisitor);
		Map<String, String> results = hpVisitor.getResult(url);

		for (String key : results.keySet()) {
			System.out.println(key);
			System.out.println(results.get(key));
		}
		JDHPVisitor hpVisitor1 = new JDHPVisitor();
		url = "http://www.360buy.com/product/306305.html";
		Parser parser1 = new Parser(url);

		parser1.visitAllNodesWith(hpVisitor1);
		Map<String, String> results1 = hpVisitor1.getResult(url);

		for (String key : results1.keySet()) {
			System.out.println(key);
			System.out.println(results1.get(key));
		}

		JDHPVisitor hpVisitor2 = new JDHPVisitor();
		url = "http://www.360buy.com/product/338344.html";
		Parser parser2 = new Parser(url);

		parser2.visitAllNodesWith(hpVisitor2);
		Map<String, String> results2 = hpVisitor2.getResult(url);

		for (String key : results2.keySet()) {
			System.out.println(key);
			System.out.println(results2.get(key));
		}
	}

}
