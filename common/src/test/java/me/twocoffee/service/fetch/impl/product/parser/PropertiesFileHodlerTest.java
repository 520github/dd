package me.twocoffee.service.fetch.impl.product.parser;

import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PropertiesFileHodlerTest {

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
	public void testGetWithlist() {
		PropertiesFileHodler fileHodler = new PropertiesFileHodler();
		fileHodler.setPath("whitelist.properties");
		fileHodler.init();
		List<ProductUrlInfo> infos = fileHodler.getWithlist();
		Assert.assertEquals(2, infos.size());
	}

}
