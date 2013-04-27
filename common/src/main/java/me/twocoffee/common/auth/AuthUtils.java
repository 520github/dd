/**
 * 
 */
package me.twocoffee.common.auth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.twocoffee.common.auth.httpclient.JsonResponseHandler;
import me.twocoffee.common.auth.httpclient.SSLConnectionPool;
import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthException;
import net.oauth.OAuthMessage;
import net.oauth.ParameterStyle;
import net.oauth.client.OAuthClient;
import net.oauth.client.httpclient4.HttpClient4;
import net.oauth.client.httpclient4.HttpClientPool;
import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author momo
 * 
 */
public class AuthUtils {

    public static final int POST_DATA_TYPE_FORM = 0;

    public static final int POST_DATA_TYPE_JSON = 1;

    public static final int GET_PARAM_TYPE_QUERYPARAM = 2;

    public static final int GET_DATA_TYPE_PATH = 3;

    private static HttpClientPool pool = new SSLConnectionPool();

    private static OAuthClient client = new OAuthClient(new HttpClient4(pool));

    private static final Logger LOGGER = LoggerFactory
	    .getLogger(AuthUtils.class);

    private static HttpUriRequest createGetReq(Map<String, String> params,
	    int contentType, String url) throws UnsupportedEncodingException {

	HttpUriRequest req = null;

	if (contentType == GET_PARAM_TYPE_QUERYPARAM) {
	    StringBuffer api = new StringBuffer(url);

	    if (params != null) {
		api.append("?");

		for (String key : params.keySet()) {
		    api.append(key)
			    .append("=")
			    .append(URLEncoder.encode(params.get(key), "utf-8"))
			    .append("&");

		}
		api.deleteCharAt(api.length() - 1);
	    }
	    req = new HttpGet(api.toString());
	    LOGGER.debug("Auth request GET uri {}", req.getURI().toString());

	} else if (contentType == GET_DATA_TYPE_PATH) {

	}
	return req;
    }

    private static HttpUriRequest createPostReq(Map<String, String> params,
	    int contentType, String url) throws UnsupportedEncodingException {

	HttpUriRequest req = null;

	if (contentType == POST_DATA_TYPE_FORM) {
	    List<NameValuePair> nvps = new ArrayList<NameValuePair>();

	    for (String key : params.keySet()) {
		nvps.add(new BasicNameValuePair(key, params.get(key)));
	    }
	    req = new HttpPost(url);
	    ((HttpPost) req).setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

	} else if (contentType == POST_DATA_TYPE_JSON) {
	    StringEntity reqEntity = new StringEntity(JSONObject.fromObject(
		    params).toString(), "UTF-8");

	    reqEntity.setContentType("application/json" + HTTP.CHARSET_PARAM
		    + "UTF-8");

	    req = new HttpPost(url);
	    ((HttpPost) req).setEntity(reqEntity);
	}
	LOGGER.debug("Auth request POST uri {}", url);
	return req;
    }

    public static OAuthMessage getAccessToken(OAuthAccessor accessor,
	    List<OAuth.Parameter> parameters) throws IOException,
	    OAuthException, URISyntaxException {

	return client.getAccessToken(accessor, null, parameters);

    }

    public static Map<String, String> getCallbackParams(String location) {
	Map<String, String> params = new HashMap<String, String>();

	if (location != null) {
	    int index = location.indexOf("?") + 1;
	    int total = location.length();

	    while (index != -1 && index < total) {
		int queryIndex = location.indexOf("=", index);
		String name = location.substring(index, queryIndex);
		index = location.indexOf("&", queryIndex);

		if (index == -1) {
		    index = location.length();
		}
		String value = location.substring(queryIndex + 1, index);
		params.put(name, value);
		index += 1;
	    }
	}
	return params;
    }

    public static OAuthMessage getRequestTokenResponse(OAuthAccessor accessor,
	    List<OAuth.Parameter> parameters) throws IOException,
	    OAuthException, URISyntaxException {

	return client.getRequestTokenResponse(accessor, null, parameters);
    }

    public static OAuthMessage invoke(OAuthMessage request1,
	    ParameterStyle style) {

	OAuthMessage response1 = null;

	try {
	    response1 = client.invoke(request1,
		    ParameterStyle.AUTHORIZATION_HEADER);

	    return response1;

	} catch (Exception e) {
	    throw new RuntimeException(request1.URL, e);
	}
    }

    public static JSONObject invoke(String method, String url, int contentType,
	    Map<String, String> params) {

	DefaultHttpClient client = (DefaultHttpClient) pool.getHttpClient(null);
	HttpUriRequest req = null;

	try {

	    if (net.oauth.http.HttpClient.GET.equalsIgnoreCase(method)) {
		req = createGetReq(params, contentType, url);

	    } else if (net.oauth.http.HttpClient.POST.equalsIgnoreCase(method)) {
		req = createPostReq(params, contentType, url);
	    }
	    return client.execute(req, new JsonResponseHandler());

	} catch (Exception e) {
	    throw new RuntimeException("invoke url [" + url + "] error!", e);
	}
    }

    public static String getMD5Sig(Map<String, String> params, String secret) {
	StringBuilder builder = new StringBuilder();

	for (String key : params.keySet()) {
	    builder.append(key).append("=")
		    .append(params.get(key) == null ? "" : params.get(key));
	}
	LOGGER.info("getMD5Sig:" + builder.toString());
	builder.append(secret);
	return md5(builder.toString());
    }

    private static String md5(String s) {
	return DigestUtils.md5Hex(s);
    }
}
