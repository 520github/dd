package me.twocoffee.service.rpc.impl;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;

import me.twocoffee.common.util.TokenUtil;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;
import me.twocoffee.rest.entity.ContactResult;
import me.twocoffee.rest.entity.DeviceSetting.Authority;
import me.twocoffee.service.rpc.AbstractHttpRPCService;
import me.twocoffee.service.rpc.ThirdPartyRpcService;
import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

@Service
public class ThirdPartyRpcServiceImpl extends AbstractHttpRPCService
		implements ThirdPartyRpcService {

	@Override
	public List<ContactResult> getSuggestThirdPartyFriends(String token,
			int limit, int offset) {

		HttpGet httpGet = new HttpGet(
				"/service/accounts/contact/suggest?limit=" + limit + "&offset="
						+ offset);

		httpGet.setHeader("Authorization", this.getWebTokenAuthorization(token));
		HttpResponse httpResponse = null;

		try {
			httpResponse = invoke(httpGet);
			int status = httpResponse.getStatusLine().getStatusCode();

			switch (status) {
			case 200:
				String responseStr = EntityUtils.toString(
						httpResponse.getEntity(),
						DEFAULT_CHARSET);

				LOGGER.debug("http get request to {} ok,response is {}",
						httpGet.getRequestLine().getUri().toString(),
						responseStr);

				ObjectMapper objectMapper = new ObjectMapper();
				List<ContactResult> r = objectMapper
						.readValue(
								responseStr,
								new TypeReference<List<ContactResult>>() {
								});

				return r;

			default:
				break;
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
	public List<ContactResult> getThirdPartyFriends(String token,
			ThirdPartyType weibo) {

		HttpGet httpGet = new HttpGet(
				"/service/accounts/contact/" + weibo.toString().toLowerCase());

		httpGet.setHeader("Authorization", TokenUtil.getWebTokenAuthorization(token));
		HttpResponse httpResponse = null;

		try {
			httpResponse = invoke(httpGet);
			int status = httpResponse.getStatusLine().getStatusCode();

			switch (status) {
			case 200:
				String responseStr = EntityUtils.toString(
						httpResponse.getEntity(),
						DEFAULT_CHARSET);

				LOGGER.debug("http get request to {} ok,response is {}",
						httpGet.getRequestLine().getUri().toString(),
						responseStr);

				ObjectMapper objectMapper = new ObjectMapper();
				List<ContactResult> r = objectMapper
						.readValue(
								responseStr,
								new TypeReference<List<ContactResult>>() {
								});

				return r;

			default:
				break;
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
	public int setSyncStatus(String token, String contentSynchronize) {

		HttpPut httpPut = new HttpPut(
				"/service/setting/authority/weibo/content_synchronize");

		httpPut.setHeader("Authorization", this.getWebTokenAuthorization(token));
		httpPut.setHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_JSON);
		Authority authority = new Authority();
		if ("true".equals(contentSynchronize))
			authority.setSynchronous(true);
		else
			authority.setSynchronous(false);

		JSONObject jsonObject = JSONObject.fromObject(authority);
		HttpResponse httpResponse = null;

		try {
			httpPut.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));
			httpResponse = invoke(httpPut);
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
			httpPut.abort();
		}

	}

	@Override
	public int unbind(String token) {

		HttpDelete httpDelete = new HttpDelete(
				"/service/setting/authority/weibo/");

		// Response me.twocoffee.rest.DeviceSettingResource.deleteSetting(String
		// token)

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

}
