/**
 * 
 */
package me.twocoffee.common;

import static org.junit.Assert.assertEquals;

import org.htmlparser.Parser;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Page;
import org.htmlparser.util.ParserException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author wenjian
 *
 */
public class HtmlClearVisitorTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link me.twocoffee.common.HtmlClearVisitor#getHtml()}.
	 * @throws ParserException 
	 */
	@Test
	public void testGetHtml() throws ParserException {
		String html = "aaaaa<span style=\"color: rgb(26, 26, 26); font-family: XinGothic-SC-W4; font-size: 19px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: 31px; orphans: 2; text-align: -webkit-auto; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; -webkit-tap-highlight-color: rgba(26, 26, 26, 0.300781); -webkit-composition-fill-color: rgba(175, 192, 227, 0.234375); -webkit-composition-frame-color: rgba(77, 128, 180, 0.234375); -webkit-text-size-adjust: 120%; -webkit-text-stroke-width: 0px; background-color: rgb(247, 247, 247); display: inline !important; float: none; \">这一年，刚刚卸任世界银行副行长的林毅夫抛出“中国经济有潜力继续高速增长20年”的观点，立即招来一阵抨击。</span>";
		assertEquals("aaaaa<span >这一年，刚刚卸任世界银行副行长的林毅夫抛出“中国经济有潜力继续高速增长20年”的观点，立即招来一阵抨击。</span>", doClear(html));
		String html7 = "a 4 <a";
		assertEquals("a 4 <a></a>", doClear(html7));
		String html4 = "<4 456";
		assertEquals("<4 456", doClear(html4));
		String html2 = "";
		assertEquals("", doClear(html2));
		String html5 = "aaaaa";
		assertEquals("aaaaa", doClear(html5));
		String html6 = "<a href=\"#\"><img src=\"#\"></img></a>";
		assertEquals("<a href=\"#\"><img src=\"#\"></img></a>", doClear(html6));
		String html1 = "<span style=\"color: rgb(26, 26, 26); font-family: XinGothic-SC-W4; font-size: 19px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: 31px; orphans: 2; text-align: -webkit-auto; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; -webkit-tap-highlight-color: rgba(26, 26, 26, 0.300781); -webkit-composition-fill-color: rgba(175, 192, 227, 0.234375); -webkit-composition-frame-color: rgba(77, 128, 180, 0.234375); -webkit-text-size-adjust: 120%; -webkit-text-stroke-width: 0px; background-color: rgb(247, 247, 247); display: inline !important; float: none; \">这一年，刚刚卸任世界银行副行长的林毅夫抛出“中国经济有潜力继续高速增长20年”的观点，立即招来一阵抨击。</span>";
		assertEquals("<span >这一年，刚刚卸任世界银行副行长的林毅夫抛出“中国经济有潜力继续高速增长20年”的观点，立即招来一阵抨击。</span>", doClear(html1));
		String html3 = "<span style=\"color: rgb(26, 26, 26); font-family: XinGothic-SC-W4; font-size: 19px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: 31px; orphans: 2; text-align: -webkit-auto; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; -webkit-tap-highlight-color: rgba(26, 26, 26, 0.300781); -webkit-composition-fill-color: rgba(175, 192, 227, 0.234375); -webkit-composition-frame-color: rgba(77, 128, 180, 0.234375); -webkit-text-size-adjust: 120%; -webkit-text-stroke-width: 0px; background-color: rgb(247, 247, 247); display: inline !important; float: none; \">这一年，刚刚卸任世界银行副行长的林毅夫抛出“中国经济有潜力继续高速增长20年”的观点，立即招来一阵抨击。</span><span style=\"color: rgb(26, 26, 26); font-family: XinGothic-SC-W4; font-size: 19px; font-style: normal; font-variant: normal; font-weight: normal; letter-spacing: normal; line-height: 31px; orphans: 2; text-align: -webkit-auto; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; -webkit-tap-highlight-color: rgba(26, 26, 26, 0.300781); -webkit-composition-fill-color: rgba(175, 192, 227, 0.234375); -webkit-composition-frame-color: rgba(77, 128, 180, 0.234375); -webkit-text-size-adjust: 120%; -webkit-text-stroke-width: 0px; background-color: rgb(247, 247, 247); display: inline !important; float: none; \">这一</span>";
		assertEquals("<span >这一年，刚刚卸任世界银行副行长的林毅夫抛出“中国经济有潜力继续高速增长20年”的观点，立即招来一阵抨击。</span><span >这一</span>", doClear(html3));
		
	}

	private String doClear(String html)
			throws ParserException {
		
		try {
			HtmlClearVisitor visitor = new HtmlClearVisitor();
			Parser parser = new Parser();
		Page page = new Page(html);
		Lexer lexer = new Lexer();
		lexer.setPage(page);
		parser.setLexer(lexer);
			parser.visitAllNodesWith(visitor);
			return visitor.getHtml();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return html;
		}
	}

}
