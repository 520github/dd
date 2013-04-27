/**
 * 
 */
package me.twocoffee.common.auth;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import net.sf.json.JSONObject;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author momo
 * 
 */
public class AuthUtilsTest {

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
     * {@link me.twocoffee.common.auth.AuthUtils#getCallbackParams(java.lang.String)}
     * .
     */
    @Test
    public void testGetCallbackParams() {
	String url = "http://127.0.0.1/account/weibo/oauth/callback?error_uri=%2Foauth2%2Fauthorize&error=access_denied&error_description=user%20denied%20your%20request.&error_code=21330";
	Map<String, String> params = AuthUtils.getCallbackParams(url);
	Assert.assertEquals(4, params.size());
	Assert.assertEquals("21330", params.get("error_code"));
	Assert.assertEquals("%2Foauth2%2Fauthorize", params.get("error_uri"));
	Assert.assertEquals("access_denied", params.get("error"));
	Assert.assertEquals("user%20denied%20your%20request.",
		params.get("error_description"));

	String url1 = "error_uri=%2Foauth2%2Fauthorize&error=access_denied&error_description=user%20denied%20your%20request.&error_code=21330";
	Map<String, String> params1 = AuthUtils.getCallbackParams(url1);
	Assert.assertEquals(4, params1.size());
	Assert.assertEquals("21330", params1.get("error_code"));
	Assert.assertEquals("%2Foauth2%2Fauthorize", params1.get("error_uri"));
	Assert.assertEquals("access_denied", params1.get("error"));
	Assert.assertEquals("user%20denied%20your%20request.",
		params1.get("error_description"));

    }

    public void testGetFriends() {
	String url = "https://open.t.qq.com/api/friends/mutual_list";
	Map<String, String> params = new HashMap<String, String>();
	params.put("oauth_consumer_key", "100360112");
	params.put("access_token", "DA0CD37CDC2F37EEE37274F322FDE6C7");
	params.put("openid", "C46B03BDC283ED415196A6F5C0C1CD79");
	params.put("clientip", "127.0.0.1");
	params.put("oauth_version", "2.a");
	params.put("format", "json");
	params.put("startindex", "0");
	params.put("reqnum", "10");
	// params.put("scope", "");
	JSONObject obj = AuthUtils.invoke("GET", url,
		AuthUtils.GET_PARAM_TYPE_QUERYPARAM, params);

	System.out.println(obj.toString());
    }

}
