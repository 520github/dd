/**
 * 
 */
package me.twocoffee.service.rpc.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.ws.rs.core.HttpHeaders;

import me.twocoffee.common.search.PagedResult;
import me.twocoffee.entity.PrivateMessage;
import me.twocoffee.entity.PrivateMessageSession;
import me.twocoffee.rest.entity.PrivateMessageInfo;
import me.twocoffee.service.rpc.AbstractHttpRPCService;
import me.twocoffee.service.rpc.PrivateMessageRpcService;
import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

/**
 * @author momo
 * 
 */
@Service
public class PrivateMessageRpcServiceImpl extends AbstractHttpRPCService
		implements PrivateMessageRpcService {

	@Override
	public int deleteSession(String token, String contentId, String accountId) {
		HttpDelete httpDelete = new HttpDelete(
				"/service/chat/session;content_id="
						+ contentId + ";account_id=" + accountId);

		httpDelete.setHeader("Authorization", this.getWebTokenAuthorization(token));
		HttpResponse httpResponse = null;

		try {
			httpResponse = invoke(httpDelete);
			return httpResponse.getStatusLine().getStatusCode();

		} catch (ClientProtocolException e) {
			LOGGER.error(e.getMessage(), e);
			return 0;

		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			return 0;

		} finally {

			if (httpResponse != null && httpResponse.getEntity() != null) {

				try {
					EntityUtils.consume(httpResponse.getEntity());

				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
			httpDelete.abort();
		}
	}

	@Override
	public PagedResult<PrivateMessageSession> getPrivateMessageSessions(
			String token, String contentId, int limit, int offset) {

		// TODO Auto-generated method stub service/chat/session/index/
		HttpGet httpGet = new HttpGet(
				"/service/chat/session/index;content_id="
						+ contentId + "?limit=" + limit + "&offset=" + offset);

		httpGet.setHeader("Authorization", this.getWebTokenAuthorization(token));
		HttpResponse httpResponse = null;

		try {
			httpResponse = invoke(httpGet);
			int status = httpResponse.getStatusLine().getStatusCode();

			if (status == 200) {
				String responseStr = EntityUtils.toString(
						httpResponse.getEntity(),
						DEFAULT_CHARSET);

				LOGGER.debug("http get request to {} ok,response is {}",
						httpGet.getRequestLine().getUri().toString(),
						responseStr);

				ObjectMapper objectMapper = new ObjectMapper();
				PagedResult<PrivateMessageSession> r = objectMapper
						.readValue(
								responseStr,
								new TypeReference<PagedResult<PrivateMessageSession>>() {
								});
				return r;
			}

		} catch (ClientProtocolException e) {
			LOGGER.error(e.getMessage(), e);
			return null;

		} catch (IOException e) {
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
		return null;
	}

	@Override
	public PagedResult<PrivateMessage> getPrivateMessagesInSession(
			String token, String accountid, String contentId, String sinceId) {
		// TODO Auto-generated method stub service/chat/session/
		HttpGet httpGet = new HttpGet(
				"/service/chat/session;content_id="
						+ contentId + ";account_id=" + accountid + "?since_id="
						+ sinceId);

		httpGet.setHeader("Authorization", this.getWebTokenAuthorization(token));
		HttpResponse httpResponse = null;

		try {
			httpResponse = invoke(httpGet);
			int status = httpResponse.getStatusLine().getStatusCode();

			if (status == 200) {
				String responseStr = EntityUtils.toString(
						httpResponse.getEntity(),
						DEFAULT_CHARSET);

				LOGGER.debug("http get request to {} ok,response is {}",
						httpGet.getRequestLine().getUri().toString(),
						responseStr);

				ObjectMapper objectMapper = new ObjectMapper();
				PagedResult<PrivateMessage> r = objectMapper
						.readValue(
								responseStr,
								new TypeReference<PagedResult<PrivateMessage>>() {
								});

				return r;
			}

		} catch (ClientProtocolException e) {
			LOGGER.error(e.getMessage(), e);
			return null;

		} catch (IOException e) {
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
		return null;
	}

	@Override
	public int sendPrivateMessage(String token, String contentId,
			String accountId, String message, String uuid) {
		// TODO Auto-generated method stub service/chat/session/
		HttpPost httpPost = new HttpPost(
				"/service/chat/session/");

		httpPost.setHeader("Authorization", this.getWebTokenAuthorization(token));
		httpPost.setHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_JSON);
		PrivateMessageInfo info = new PrivateMessageInfo();
		info.setContentId(contentId);
		info.setMessage(message);
		info.setUuid(uuid);
		info.setAccountId(new String[] { accountId });
		JSONObject jsonObject = JSONObject.fromObject(info);
		try {
			httpPost
					.setEntity(new StringEntity(jsonObject.toString()));

		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage(), e);
			return 0;
		}
		HttpResponse httpResponse = null;

		try {
			httpResponse = invoke(httpPost);
			return httpResponse.getStatusLine().getStatusCode();

		} catch (ClientProtocolException e) {
			LOGGER.error(e.getMessage(), e);
			return 0;

		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			return 0;

		} finally {

			if (httpResponse != null && httpResponse.getEntity() != null) {

				try {
					EntityUtils.consume(httpResponse.getEntity());

				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
			httpPost.abort();
		}
	}

}
