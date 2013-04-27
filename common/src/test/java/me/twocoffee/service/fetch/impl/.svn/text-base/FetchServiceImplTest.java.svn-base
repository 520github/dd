package me.twocoffee.service.fetch.impl;

import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FetchServiceImplTest {

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
    public void testGetResource() throws URIException, NullPointerException {
	String url = "http://proxy.xgres.com/proxyImg.php?w=420&u=http%3A%2F%2Fproxy%2Exgres%2Ecom%2FdownloadPicture%2Ephp%3Fhttp%3A%2F%2Fi0%2Esinaimg%2Ecn%2Fent%2Fv%2Fm%2F2013-01-17%2FU8711P28T3D3838405F326DT20130117232538%2Ejpg";
	URI uri = new URI(url, true);
	Assert.assertEquals(url, uri.toString());
	String url1 = "http://proxy.xgres.com/proxyImg.php?w=420&u=http%3A%2F%2Fproxy%2Exgres%2Ecom%2FdownloadPicture%2Ephp%3Fhttp%3A%2F%2Fi0%2Esinaimg%2Ecn%2Fent%2Fv%2Fm%2F2013-01-17%2FU8711P28T3D3838405F326DT20130117232538%2Ejpg";
	URI uri1 = new URI(url1, false);
	Assert.assertNotSame(url1, uri1.toString());
	String url2 = "http://www.sina.com/sd?adf";
	URI uri2 = new URI(url2, true);
	Assert.assertEquals(url2, uri2.toString());
    }

}
