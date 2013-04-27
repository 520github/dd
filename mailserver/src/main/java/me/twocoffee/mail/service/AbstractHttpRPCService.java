package me.twocoffee.mail.service;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import me.twocoffee.common.SpringContext;
import me.twocoffee.common.constant.SystemConstant;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractHttpRPCService {
	private ThreadLocal<HttpClient> httpClientThreadLocal = new ThreadLocal<HttpClient>();
	protected static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";
	protected static final String CONTENT_TYPE_PLAIN = "text/plain;charset=UTF-8";
	protected static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractHttpRPCService.class);
	protected ObjectMapper objectMapper = new ObjectMapper();
	protected static final String DEFAULT_CHARSET = "utf-8";
	private String request_charset = "utf-8";
	private String response_charset = "utf-8";
	private HttpRequestRetryHandler httpRequestRetryHandler;

	public AbstractHttpRPCService() {

		httpRequestRetryHandler = new HttpRequestRetryHandler() {

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
					LOGGER
							.error(
									"[{}] [{}] [{}] [{}] have been retry 5 times,stop retry.",
									new Object[] { uri, methodName,
											exception.getClass().getName(),
											exception.getMessage() });
					return false;
				}
				if (exception instanceof NoHttpResponseException) {
					// Retry if the server dropped connection on us
					LOGGER.error("[{}] [{}] [{}] [{}] http retry",
							new Object[] { uri, methodName,
									exception.getClass().getName(),
									exception.getMessage() });
					return true;
				}
				if ((exception instanceof SocketException)
						&& exception.getMessage()
								.startsWith("Connection reset")) {
					LOGGER.error("[{}] [{}] [{}] [{}] Connection reset retry",
							new Object[] { uri, methodName,
									exception.getClass().getName(),
									exception.getMessage() });
					return true;
				}

				boolean idempotent = !(httpRequest instanceof HttpEntityEnclosingRequest);
				if (idempotent) {
					// Retry if the request is considered idempotent
					LOGGER.error("[{}] [{}] idempotent retry", new Object[] {
							uri, methodName });
					return true;
				}
				return false;
			}

		};

	}

	private HttpClient getHttpClient() {
		// TODO 需要优化
		if (httpClientThreadLocal.get() == null) {

			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(
					new Scheme("http", 80, PlainSocketFactory
							.getSocketFactory()));

			ThreadSafeClientConnManager threadSafeClientConnManager = new ThreadSafeClientConnManager(
					schemeRegistry);
			threadSafeClientConnManager.setMaxTotal(100);
			threadSafeClientConnManager.setDefaultMaxPerRoute(100);

			DefaultHttpClient defaultHttpClient = new DefaultHttpClient(
					threadSafeClientConnManager);

			defaultHttpClient
					.setHttpRequestRetryHandler(httpRequestRetryHandler);

			httpClientThreadLocal.set(defaultHttpClient);
		}

		return httpClientThreadLocal.get();
	}

	private String host = null;
	
	protected String getHost() {
		return SystemConstant.domainName;
	}

	public HttpResponse invoke(HttpUriRequest httpUriRequest)
			throws ClientProtocolException, IOException {
		return getHttpClient().execute(new HttpHost(getHost()), httpUriRequest);
	}

	public HttpResponse invoke(HttpUriRequest httpUriRequest, String host)
			throws ClientProtocolException, IOException {
		return getHttpClient().execute(new HttpHost(host), httpUriRequest);
	}
	
	/**
	 * 执行操作方法
	 * 
	 * @param request
	 *             请求url类型
	 * @param headerMap
	 *             头部设置
	 * @param entity
	 *             参数对象设置
	 * @param resultEntity
	 *             返回结果类型
	 * @return
	 * @throws Exception
	 */
	public Object executeMethod(HttpUriRequest request,
			Map<String,String> headerMap,
			Object entity,Class<?> resultEntity) throws Exception {
		Object result = null;
		try {
			if(request == null) {
				LOGGER.error("found HttpUriRequest is null");
				throw new NullPointerException("found HttpUriRequest is null");
			}
			
			this.setHeader(request, headerMap);
			this.setEntity(request, entity);
			//执行请求操作
			HttpResponse response =  this.invoke(request);
			//处理返回结果
			result = this.getResponseEntity(response, request, resultEntity);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		}finally {
			if(request != null) {
				request.abort();
			}
		}
		return result;
	}
	
	/**
	 * 设置头部请求信息
	 * 
	 * @param request
	 * @param headerMap
	 */
	public void setHeader(HttpUriRequest request,Map<String,String> headerMap) {
		if(headerMap == null || headerMap.size() < 1)return ;
		Set<Entry<String,String>> headerSet = headerMap.entrySet();
		for(Entry<String,String> header:headerSet) {
			request.setHeader(header.getKey(), header.getValue());
		}
	}
	
	/**
	 * 设置参数对象
	 * 
	 * @param request
	 * @param entity
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public void setEntity(HttpUriRequest request, Object entity) 
	throws UnsupportedEncodingException,IOException {
		if(entity == null) {
			return;
		}
		if(entity instanceof java.lang.String 
				&& entity.toString().trim().length() < 1) {
			return ;
		}
		if(request instanceof HttpPost) {
			((HttpPost)request).setEntity(this.getHttpEntity(request, entity));
		}
		else if(request instanceof HttpPut) {
			((HttpPut)request).setEntity(this.getHttpEntity(request, entity));
		}
	}
	
	/**
	 * 获取HttpEntity参数实体
	 * 
	 * @param request
	 * @param entity
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public HttpEntity getHttpEntity(HttpUriRequest request, Object entity) 
	    throws UnsupportedEncodingException,IOException {
		HttpEntity httpEntity = null;
		
		if(entity == null) {
			httpEntity = null;
		}
		else if(entity instanceof java.lang.String) {
			httpEntity = new StringEntity(entity.toString(), request_charset);
		}
		else if(entity instanceof JSONObject){
			httpEntity = new StringEntity(entity.toString(), request_charset);
		}
		else {
			StringWriter writer = new StringWriter();
			JsonGenerator gen = new JsonFactory().createJsonGenerator(writer);
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(gen, entity);
			httpEntity = new StringEntity(writer.toString(), request_charset);
		}
		
		return httpEntity;
	}
	
	/**
	 * 获取返回结果实体
	 * 
	 * @param response
	 * @param request
	 * @param resultEntity
	 * @return
	 */
	public Object getResponseEntity(HttpResponse response, 
			HttpUriRequest request, Class<?> resultEntity) throws Exception {
		Object responseEntity = null;
		try {
			if(response == null) {
				return responseEntity;
			}

			if(response.getStatusLine().getStatusCode() == 403) {
				String token = null;
				if(request.getFirstHeader("Authorization") != null) {
					token =  request.getFirstHeader("Authorization").getValue();
				}
				LOGGER.error("{} auth failed", token);
				return responseEntity;
			}
			
			String responseEntityStr = null;
			int status = response.getStatusLine().getStatusCode();
			
			if(response.getEntity() == null) {
				if(status == 200)return "ok";
				if(status == 204)return "notContent";
			}
			else {
				responseEntityStr = EntityUtils.toString(response.getEntity(), response_charset);
			}
			
			if(status != 200) {
				LOGGER.error(
						"http get request to {} failed,failed status {},response is {}",
						new Object[] {
								request.getRequestLine().getUri().toString(),
										response
										.getStatusLine()
										.getStatusCode(),
										responseEntityStr});
				return null;
			}
			
			if(resultEntity == null) {
				responseEntity = responseEntityStr;
			}
			else if(resultEntity.newInstance() instanceof String) {
				responseEntity = responseEntityStr;
			}
			else {
				ObjectMapper objectMapper = new ObjectMapper();
				responseEntity = objectMapper.readValue(responseEntityStr, resultEntity);
			}
		} catch (Exception e) {
			throw e;
		}finally {
			if(response != null 
					&& response.getEntity() != null) {
				try {
					EntityUtils.consume(response.getEntity());
					//response.getEntity().consumeContent();
				} catch (Exception e2) {
				}
			}
		}
		return responseEntity;
	}
	
	protected void setAuthorization(Map<String,String> headerMap, String authToken) {
		if(headerMap == null)return ;
		headerMap.put("Authorization", "AuthToken " + authToken);
	}
	
	public String getRequest_charset() {
		return request_charset;
	}

	public void setRequest_charset(String requestCharset) {
		request_charset = requestCharset;
	}

	public String getResponse_charset() {
		return response_charset;
	}

	public void setResponse_charset(String responseCharset) {
		response_charset = responseCharset;
	}

	public void destroy() {
		if (httpClientThreadLocal.get() != null) {
			httpClientThreadLocal.get().getConnectionManager().shutdown();
			LOGGER.info("connection manager have been shutdown.");
		}
	}

}
