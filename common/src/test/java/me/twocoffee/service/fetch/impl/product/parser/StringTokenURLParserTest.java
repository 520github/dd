/**
 * 
 */
package me.twocoffee.service.fetch.impl.product.parser;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author momo
 * 
 */
public class StringTokenURLParserTest {

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
	 * Test method for
	 * {@link me.twocoffee.service.fetch.impl.product.parser.StringTokenURLParser#parse(java.lang.String)}
	 * .
	 */
	@Test
	public void testParse() {
		ProductWhitelistHolder holder = new MockWhitelistHolder();
		StringTokenURLParser parser = new StringTokenURLParser();
		parser.setProductWhitelistHoder(holder);
		String tao = " http://item.taobao.com/item.htm?id=13302264811&ali_refid=a3_420907_1007:1102416326:7:c4d895ce4bd1587df0a150eaaa84293d:77359e876218befdef4185fae22829ca&ali_trackid=1_77359e876218befdef4185fae22829ca";
		String tmail = "  http://detail.tmall.com/item.htm?id=5871859441&is_b=1&cat_id=50104440&q=";
		Assert.assertEquals("taobao", parser.parse(tao));
		Assert.assertEquals("tmail", parser.parse(tmail));
	}

	class MockWhitelistHolder implements ProductWhitelistHolder {

		@Override
		public List<ProductUrlInfo> getWithlist() {
			List<ProductUrlInfo> list = new ArrayList<ProductUrlInfo>();
			ProductUrlInfo taobao = new ProductUrlInfo("taobao",
					"http://item.taobao.com/item.htm?id");

			ProductUrlInfo tmail = new ProductUrlInfo("tmail",
					"http://detail.tmall.com/item.htm");

			list.add(tmail);
			list.add(taobao);
			return list;
		}
	}
}
