package me.twocoffee.service.fetch.impl.product.fetcher;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import junit.framework.Assert;
import me.twocoffee.service.fetch.impl.product.fetcher.visitor.JDHPVisitor;
import me.twocoffee.service.fetch.impl.product.fetcher.visitor.TaobaoHPVisitor;
import me.twocoffee.service.fetch.impl.product.fetcher.visitor.TaobaoResultHandler;
import me.twocoffee.service.fetch.impl.product.fetcher.visitor.TmallHPVisitor;
import me.twocoffee.service.fetch.impl.product.fetcher.visitor.TmallResultHandler;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class HtmlParserExecutorTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testExecuteTaobao() throws Throwable {
	String taobaoHtml = getTestStr("taobao.html");
	HtmlParserExecutor executor = new HtmlParserExecutor();
	TaobaoHPVisitor v = new TaobaoHPVisitor();
	TaobaoResultHandler handler = new MockTaobaoHandler();
	v.setResultHandler(handler);
	Map<String, String> result = executor.execute("", v, taobaoHtml);
	//Assert.assertEquals(4, result.size());
	Assert.assertEquals("title", result.get("title"));
	Assert.assertEquals("name", result.get("name"));
	Assert.assertEquals("29.00", result.get("price"));
	Assert.assertEquals("image.jpg", result.get("image"));
    }

    public void testExecuteTaobaoPromo() throws Throwable {
	String taobaoHtml = getTestStr("taobao_promo.html");
	HtmlParserExecutor executor = new HtmlParserExecutor();
	TaobaoHPVisitor v = new TaobaoHPVisitor();
	TaobaoResultHandler handler = new MockTaobaoPromoHandler();
	v.setResultHandler(handler);
	Map<String, String> result = executor.execute("", v, taobaoHtml);
	Assert.assertEquals(5, result.size());
	Assert.assertEquals("title", result.get("title"));
	Assert.assertEquals("name", result.get("name"));
	Assert.assertEquals("promotion", result.get("price"));
	Assert.assertEquals("image", result.get("image"));
    }

    @Test
    public void testExecuteTmall() throws Throwable {
	String taobaoHtml = getTestStr("tmail.html");
	HtmlParserExecutor executor = new HtmlParserExecutor();
	TmallHPVisitor v = new TmallHPVisitor();
	TmallResultHandler handler = new MockTmallHandler();
	v.setResultHandler(handler);
	Map<String, String> result = executor.execute("", v, taobaoHtml);
	//Assert.assertEquals(4, result.size());
	Assert.assertEquals("title", result.get("title"));
	Assert.assertEquals("name", result.get("name"));
	Assert.assertEquals("345.00", result.get("price"));
	Assert.assertEquals("image.jpg", result.get("image"));
    }

    public void testExecuteTmallPromo() throws Throwable {
	String taobaoHtml = getTestStr("tmail_promo.html");
	HtmlParserExecutor executor = new HtmlParserExecutor();
	TmallHPVisitor v = new TmallHPVisitor();
	TmallResultHandler handler = new MockTmallPromoHandler();
	v.setResultHandler(handler);
	Map<String, String> result = executor.execute("", v, taobaoHtml);
	Assert.assertEquals(5, result.size());
	Assert.assertEquals("title", result.get("title"));
	Assert.assertEquals("name", result.get("name"));
	Assert.assertEquals("promotion", result.get("price"));
	Assert.assertEquals("image", result.get("image"));
    }

    public void testExecuteJD() throws Throwable {
	String jdHtml = getTestStr("jingdong.html");
	HtmlParserExecutor executor = new HtmlParserExecutor();
	JDHPVisitor v = new JDHPVisitor();
	Map<String, String> result = executor.execute("", v, jdHtml);
	Assert.assertEquals(4, result.size());
	Assert.assertEquals("title", result.get("title"));
	Assert.assertEquals("name", result.get("name"));
	Assert.assertEquals("price", result.get("price"));
	Assert.assertEquals("image", result.get("image"));
    }

    private static String getTestStr(String string) throws Throwable {
	BufferedReader reader = null;
	StringBuilder builder = new StringBuilder();

	try {
	    reader = new BufferedReader(
		    new FileReader(
			    "src/test/resources/me/twocoffee/service/fetch/impl/product/fetcher/"
				    + string));

	    String tempString = null;

	    while ((tempString = reader.readLine()) != null) {
		builder.append(tempString);
	    }
	    reader.close();

	} catch (IOException e) {
	    e.printStackTrace();

	} finally {

	    if (reader != null) {

		try {
		    reader.close();

		} catch (IOException e1) {
		}
	    }
	}
	return builder.toString();
    }

    class MockTaobaoHandler extends TaobaoResultHandler {

	@Override
	protected String getRealPrice(String api, String url) {
	    return "";
	}

    }

    class MockTaobaoPromoHandler extends TaobaoResultHandler {

	@Override
	protected String getRealPrice(String api, String url) {
	    return "promotion";
	}
    }

    class MockTmallHandler extends TmallResultHandler {

	@Override
	protected String getRealPrice(String api, String url) {
	    return "";
	}

    }

    class MockTmallPromoHandler extends TmallResultHandler {

	@Override
	protected String getRealPrice(String api, String url) {
	    return "promotion";
	}
    }
}
