package me.twocoffee.service.rpc.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;

import me.twocoffee.common.search.PagedResult;
import me.twocoffee.common.util.TokenUtil;
import me.twocoffee.rest.entity.AccountInfo;
import me.twocoffee.rest.entity.FriendLogInfo;
import me.twocoffee.rest.entity.FriendLogResult;
import me.twocoffee.rest.entity.FriendResult;
import me.twocoffee.rest.entity.ListResult;
import me.twocoffee.rest.generic.JsonObject;
import me.twocoffee.service.rpc.AbstractHttpRPCService;
import me.twocoffee.service.rpc.FriendRpcService;
import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class FriendRpcServiceImpl extends AbstractHttpRPCService implements
		FriendRpcService {
	protected ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public boolean accept(String token, String friendId) {
		HttpPut http = new HttpPut("/service/relation/friend/" + friendId);
		http.setHeader("Authorization", this.getWebTokenAuthorization(token));

		HttpResponse httpResponse = null;
		try {
			httpResponse = invoke(http);

			if (httpResponse.getStatusLine().getStatusCode() == 403) {
				LOGGER.debug("{} auth failed", token);
				return false;
			}
			if (httpResponse.getStatusLine().getStatusCode() != 200) {
				LOGGER
						.error(
								"http get request to {} failed,failed status {},response is {}",
								new Object[] {
										http.getRequestLine().getUri()
												.toString(),
										httpResponse
												.getStatusLine()
												.getStatusCode(),
										EntityUtils.toString(httpResponse
												.getEntity(), DEFAULT_CHARSET) });
				return false;
			}

			return true;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return false;
		} finally {
			if (httpResponse != null && httpResponse.getEntity() != null) {
				try {
					EntityUtils.consume(httpResponse.getEntity());
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
			http.abort();
		}
	}

	@Override
	public boolean add(String token, String friendId) {
		HttpPost http = new HttpPost("/service/relation/friend-request/");
		http.setHeader("Authorization", "AuthToken " + token);

		http.setHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_JSON);

		HttpResponse httpResponse = null;
		try {
			http.setEntity(new StringEntity("{\"accountId\":\"" + friendId
					+ "\"}", "UTF-8"));
			httpResponse = invoke(http);

			if (httpResponse.getStatusLine().getStatusCode() == 403) {
				LOGGER.debug("{} auth failed", token);
				return false;
			}
			if (httpResponse.getStatusLine().getStatusCode() != 201) {
				LOGGER
						.error(
								"http get request to {} failed,failed status {},response is {}",
								new Object[] {
										http.getRequestLine().getUri()
												.toString(),
										httpResponse
												.getStatusLine()
												.getStatusCode(),
										EntityUtils.toString(httpResponse
												.getEntity(), DEFAULT_CHARSET) });
				return false;
			}

			return true;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return false;
		} finally {
			if (httpResponse != null && httpResponse.getEntity() != null) {
				try {
					EntityUtils.consume(httpResponse.getEntity());
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
			http.abort();
		}
	}

	@Override
	public boolean add(String token, String friendId, String alias,
			String message, String thirdType) {
		HttpPost http = new HttpPost("/service/relation/friend-request/");
		http.setHeader("Authorization", TokenUtil.getWebTokenAuthorization(token));

		http.setHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_JSON);

		HttpResponse httpResponse = null;
		try {
			http.setEntity(new StringEntity("{\"accountId\":\"" + friendId
					+ "\",\"alias\":\"" + alias + "\",\"message\":\""
					+ message.replace("\r", "").replace("\n", "<br />")
					+ "\",\"thirdpartyType\":\"" + thirdType + "\"}", "UTF-8"));
			httpResponse = invoke(http);

			if (httpResponse.getStatusLine().getStatusCode() == 403) {
				LOGGER.debug("{} auth failed", token);
				return false;
			}
			if (httpResponse.getStatusLine().getStatusCode() != 201) {
				LOGGER
						.error(
								"http get request to {} failed,failed status {},response is {}",
								new Object[] {
										http.getRequestLine().getUri()
												.toString(),
										httpResponse
												.getStatusLine()
												.getStatusCode(),
										EntityUtils.toString(httpResponse
												.getEntity(), DEFAULT_CHARSET) });
				return false;
			}

			return true;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return false;
		} finally {
			if (httpResponse != null && httpResponse.getEntity() != null) {
				try {
					EntityUtils.consume(httpResponse.getEntity());
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
			http.abort();
		}
	}

	@Override
	public List<AccountInfo> findFavoriteFriend(String token) {
		HttpGet httpGet = new HttpGet("/service/relation/friend/frequently");
		httpGet.setHeader("Authorization", this.getWebTokenAuthorization(token));

		HttpResponse httpResponse = null;
		try {
			httpResponse = invoke(httpGet);

			if (httpResponse.getStatusLine().getStatusCode() == 403) {
				LOGGER.debug("{} auth failed", token);
				return null;
			}
			if (httpResponse.getStatusLine().getStatusCode() != 200) {
				LOGGER
						.error(
								"http get request to {} failed,failed status {},response is {}",
								new Object[] {
										httpGet.getRequestLine().getUri()
												.toString(),
										httpResponse
												.getStatusLine()
												.getStatusCode(),
										EntityUtils.toString(httpResponse
												.getEntity(), DEFAULT_CHARSET) });
				return null;
			}

			String responseStr = EntityUtils.toString(httpResponse.getEntity(),
					DEFAULT_CHARSET);

			LOGGER.debug("http get request to {} ok,response is {}",
					httpGet.getRequestLine().getUri().toString(), responseStr);

			ObjectMapper objectMapper = new ObjectMapper();
			FriendResult r = objectMapper.readValue(responseStr,FriendResult.class);
			//ListResult r = result.getResult();//objectMapper.readValue(responseStr,ListResult.class);

			// JSONObject ja = JSONObject.fromObject(responseStr);
			// ListResult r = (ListResult) JSONObject.toBean(ja,
			// ListResult.class);
			if (r == null || r.getResult() == null)
				return null;
			return Arrays.asList(r.getResult());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return null;
		} finally {
			if (httpResponse != null && httpResponse.getEntity() != null) {
				try {
					EntityUtils.consume(httpResponse.getEntity());
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
			httpGet.abort();
		}
	}

	@Override
	public List<AccountInfo> findFriend(String token) {
		HttpGet httpGet = new HttpGet("/service/relation/friend");
		httpGet.setHeader("Authorization", this.getWebTokenAuthorization(token));

		HttpResponse httpResponse = null;
		try {
			httpResponse = invoke(httpGet);

			if (httpResponse.getStatusLine().getStatusCode() == 403) {
				LOGGER.debug("{} auth failed", token);
				return null;
			}
			if (httpResponse.getStatusLine().getStatusCode() != 200) {
				LOGGER
						.error(
								"http get request to {} failed,failed status {},response is {}",
								new Object[] {
										httpGet.getRequestLine().getUri()
												.toString(),
										httpResponse
												.getStatusLine()
												.getStatusCode(),
										EntityUtils.toString(httpResponse
												.getEntity(), DEFAULT_CHARSET) });
				return null;
			}

			String responseStr = EntityUtils.toString(httpResponse.getEntity(),
					DEFAULT_CHARSET);

			LOGGER.debug("http get request to {} ok,response is {}",
					httpGet.getRequestLine().getUri().toString(), responseStr);

			ObjectMapper objectMapper = new ObjectMapper();
			ListResult r = objectMapper.readValue(responseStr,
					ListResult.class);

			// JSONObject ja = JSONObject.fromObject(responseStr);
			// ListResult r = (ListResult) JSONObject.toBean(ja,
			// ListResult.class);
			if (r == null || r.getResult() == null)
				return null;
			return Arrays.asList(r.getResult());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return null;
		} finally {
			if (httpResponse != null && httpResponse.getEntity() != null) {
				try {
					EntityUtils.consume(httpResponse.getEntity());
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
			httpGet.abort();
		}
	}

	@Override
	public List<AccountInfo> findFriendLastTimeUsed(String token) {
		HttpGet httpGet = new HttpGet("/service/relation/friend/last-time-used");
		httpGet.setHeader("Authorization", TokenUtil.getWebTokenAuthorization(token));

		HttpResponse httpResponse = null;
		try {
			httpResponse = invoke(httpGet);

			if (httpResponse.getStatusLine().getStatusCode() == 403) {
				LOGGER.debug("{} auth failed", token);
				return null;
			}
			if (httpResponse.getStatusLine().getStatusCode() != 200) {
				LOGGER
						.error(
								"http get request to {} failed,failed status {},response is {}",
								new Object[] {
										httpGet.getRequestLine().getUri()
												.toString(),
										httpResponse
												.getStatusLine()
												.getStatusCode(),
										EntityUtils.toString(httpResponse
												.getEntity(), DEFAULT_CHARSET) });
				return null;
			}

			String responseStr = EntityUtils.toString(httpResponse.getEntity(),
					DEFAULT_CHARSET);

			LOGGER.debug("http get request to {} ok,response is {}",
					httpGet.getRequestLine().getUri().toString(), responseStr);

			ObjectMapper objectMapper = new ObjectMapper();
			ListResult r = objectMapper.readValue(responseStr,
					ListResult.class);

			// JSONObject ja = JSONObject.fromObject(responseStr);
			// ListResult r = (ListResult) JSONObject.toBean(ja,
			// ListResult.class);
			if (r == null || r.getResult() == null)
				return null;
			return Arrays.asList(r.getResult());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return null;
		} finally {
			if (httpResponse != null && httpResponse.getEntity() != null) {
				try {
					EntityUtils.consume(httpResponse.getEntity());
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
			httpGet.abort();
		}
	}

	@Override
	public List<FriendLogInfo> findFriendLog(String token) {
		HttpGet httpGet = new HttpGet("/service/relation/friend-request");
		httpGet.setHeader("Authorization", this.getWebTokenAuthorization(token));

		HttpResponse httpResponse = null;
		try {
			httpResponse = invoke(httpGet);

			if (httpResponse.getStatusLine().getStatusCode() == 403) {
				LOGGER.debug("{} auth failed", token);
				return null;
			}
			if (httpResponse.getStatusLine().getStatusCode() != 200) {
				LOGGER
						.error(
								"http get request to {} failed,failed status {},response is {}",
								new Object[] {
										httpGet.getRequestLine().getUri()
												.toString(),
										httpResponse
												.getStatusLine()
												.getStatusCode(),
										EntityUtils.toString(httpResponse
												.getEntity(), DEFAULT_CHARSET) });
				return null;
			}

			String responseStr = EntityUtils.toString(httpResponse.getEntity(),
					DEFAULT_CHARSET);

			LOGGER.debug("http get request to {} ok,response is {}",
					httpGet.getRequestLine().getUri().toString(), responseStr);

			JSONObject ja = JSONObject.fromObject(responseStr);
			FriendLogResult r = (FriendLogResult) JSONObject.toBean(ja,
					FriendLogResult.class);
			return Arrays.asList(r.getResult());
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return null;
		} finally {
			if (httpResponse != null && httpResponse.getEntity() != null) {
				try {
					EntityUtils.consume(httpResponse.getEntity());
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
			httpGet.abort();
		}
	}

	@Override
	public PagedResult<JsonObject> getFriendMessage(String authToken,
			String catalog,
			int limit, int offset) {

		HttpUriRequest request = new HttpGet(
					"/service/message/catalog/" + catalog + "?limit="
							+ String.valueOf(limit) + "&offset="
							+ String.valueOf(offset));
		HashMap<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("Authorization", TokenUtil.getWebTokenAuthorization(authToken));

		try {
			PagedResult<JsonObject> pagedResult = (PagedResult<JsonObject>) this
					.executeMethod(request,
							headerMap,
							null, PagedResult.class);
			return pagedResult;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return null;
		}

	}

	@Override
	public boolean reject(String token, String friendId) {
		HttpDelete http = new HttpDelete("/service/relation/friend-request/"
				+ friendId);
		http.setHeader("Authorization", this.getWebTokenAuthorization(token));

		HttpResponse httpResponse = null;
		try {
			httpResponse = invoke(http);

			if (httpResponse.getStatusLine().getStatusCode() == 403) {
				LOGGER.debug("{} auth failed", token);
				return false;
			}
			if (httpResponse.getStatusLine().getStatusCode() != 200) {
				LOGGER
						.error(
								"http get request to {} failed,failed status {},response is {}",
								new Object[] {
										http.getRequestLine().getUri()
												.toString(),
										httpResponse
												.getStatusLine()
												.getStatusCode(),
										EntityUtils.toString(httpResponse
												.getEntity(), DEFAULT_CHARSET) });
				return false;
			}

			return true;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return false;
		} finally {
			if (httpResponse != null && httpResponse.getEntity() != null) {
				try {
					EntityUtils.consume(httpResponse.getEntity());
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
			http.abort();
		}
	}

	@Override
	public boolean removeFriend(String token, String friendId) {
		HttpDelete http = new HttpDelete("/service/relation/friend/" + friendId);
		http.setHeader("Authorization", TokenUtil.getWebTokenAuthorization(token));

		HttpResponse httpResponse = null;
		try {
			httpResponse = invoke(http);

			if (httpResponse.getStatusLine().getStatusCode() == 403) {
				LOGGER.debug("{} auth failed", token);
				return false;
			}
			if (httpResponse.getStatusLine().getStatusCode() != 200) {
				LOGGER
						.error(
								"http get request to {} failed,failed status {},response is {}",
								new Object[] {
										http.getRequestLine().getUri()
												.toString(),
										httpResponse
												.getStatusLine()
												.getStatusCode(),
										EntityUtils.toString(httpResponse
												.getEntity(), DEFAULT_CHARSET) });
				return false;
			}

			return true;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return false;
		} finally {
			if (httpResponse != null && httpResponse.getEntity() != null) {
				try {
					EntityUtils.consume(httpResponse.getEntity());
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
			http.abort();
		}
	}
}
