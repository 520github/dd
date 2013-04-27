package me.twocoffee.common.auth.httpclient;

import java.net.URL;

import me.twocoffee.common.BasicHttpClient;
import net.oauth.client.httpclient4.HttpClientPool;

import org.apache.http.client.HttpClient;

/**
 * 连接池，支持http、https连接
 * 
 * @author wenjian
 * 
 */
public class SSLConnectionPool implements HttpClientPool {

	private static final HttpClient client = BasicHttpClient.getHttpClient();

	public HttpClient getHttpClient(URL server) {
		return client;
	}
}
