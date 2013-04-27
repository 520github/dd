package me.twocoffee.common;

import java.io.IOException;
import java.net.SocketException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicHttpClient {

	private static ThreadLocal<HttpClient> httpClientThreadLocal = new ThreadLocal<HttpClient>();

	private static HttpRequestRetryHandler httpRequestRetryHandler;

	private static final Logger logger = LoggerFactory
			.getLogger(BasicHttpClient.class);

	private static ThreadSafeClientConnManager threadSafeClientConnManager;

	static {
		httpRequestRetryHandler = new HttpRequestRetryHandler() {

			@Override
			public boolean retryRequest(
					IOException exception,
					int executionCount,
					HttpContext context) {

				HttpRequest httpRequest = (HttpRequest) context
						.getAttribute(ExecutionContext.HTTP_REQUEST);

				String uri = httpRequest.getRequestLine().getUri();
				String methodName = httpRequest.getRequestLine().getMethod();

				if (executionCount >= 5) {
					// Do not retry if over max retry count
					logger
							.error(
									"[{}] [{}] [{}] [{}] have been retry 5 times,stop retry.",
									new Object[] { uri, methodName,
											exception.getClass().getName(),
											exception.getMessage() });
					return false;
				}
				if (exception instanceof NoHttpResponseException) {
					// Retry if the server dropped connection on us
					logger.error("[{}] [{}] [{}] [{}] http retry",
							new Object[] { uri, methodName,
									exception.getClass().getName(),
									exception.getMessage() });
					return true;
				}
				if ((exception instanceof SocketException)
						&& exception.getMessage()
								.startsWith("Connection reset")) {
					logger.error("[{}] [{}] [{}] [{}] Connection reset retry",
							new Object[] { uri, methodName,
									exception.getClass().getName(),
									exception.getMessage() });
					return true;
				}

				boolean idempotent = !(httpRequest instanceof HttpEntityEnclosingRequest);
				if (idempotent) {
					// Retry if the request is considered idempotent
					logger.error("[{}] [{}] idempotent retry", new Object[] {
							uri, methodName });
					return true;
				}
				return false;
			}
		};
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(
				new Scheme("http", 80, PlainSocketFactory
						.getSocketFactory()));

		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {

				@Override
				public void checkClientTrusted(X509Certificate[] xcs,
						String string) {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] xcs,
						String string) {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx,
					SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			Scheme sch = new Scheme("https", 443, ssf);
			schemeRegistry.register(sch);

		} catch (KeyManagementException e) {
			logger.error("", e);

		} catch (NoSuchAlgorithmException e) {
			logger.error("", e);
		}
		threadSafeClientConnManager = new ThreadSafeClientConnManager(
				schemeRegistry);

		threadSafeClientConnManager.setMaxTotal(2000);
		threadSafeClientConnManager.setDefaultMaxPerRoute(2000);
	}

	private static void setParams(HttpClient httpClient) {
		httpClient
				.getParams()
				.setParameter(
						CoreProtocolPNames.USER_AGENT,
						"Mozilla/5.0 (X11; Linux i686) AppleWebKit/535.19 (KHTML, like Gecko) Ubuntu/11.04 Chromium/18.0.1025.151 Chrome/18.0.1025.151 Safari/535.19");

		httpClient.getParams()
				.setIntParameter("http.connection.timeout", 20000);

		httpClient.getParams().setIntParameter("http.socket.timeout", 600000);
	}

	/**
	 * 返回会话级的HttpClient对象
	 * 
	 * @return
	 */
	public static HttpClient getHttpClient() {
		DefaultHttpClient defaultHttpClient = new DefaultHttpClient(
				threadSafeClientConnManager);
		defaultHttpClient
				.setHttpRequestRetryHandler(httpRequestRetryHandler);
		setParams(defaultHttpClient);
		return defaultHttpClient;
	}

	/**
	 * 返回线程级的HttpClient对象
	 * 
	 * @return
	 */
	public static HttpClient getHttpClientThreadLocal() {
		if (httpClientThreadLocal.get() == null) {
			DefaultHttpClient defaultHttpClient = new DefaultHttpClient(
					threadSafeClientConnManager);
			defaultHttpClient
					.setHttpRequestRetryHandler(httpRequestRetryHandler);
			setParams(defaultHttpClient);
			httpClientThreadLocal.set(defaultHttpClient);
		}

		return httpClientThreadLocal.get();
	}
}
