package me.twocoffee.service.rpc.impl;

import java.io.IOException;

import me.twocoffee.common.constant.SystemConstant;
import me.twocoffee.service.rpc.AbstractHttpRPCService;
import me.twocoffee.service.rpc.TopicRpcService;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

@Service
public class TopicRpcServiceImpl extends AbstractHttpRPCService implements
		TopicRpcService {

	@Override
	public int isList() {
		HttpGet httpGet = new HttpGet("http://" + SystemConstant.domainName
				+ "/minisite/web/banner_list.html");
		HttpResponse httpResponse = null;
		int status = 0;
		try {
			DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
			httpResponse = defaultHttpClient.execute(httpGet);
			status = httpResponse.getStatusLine().getStatusCode();
			return status;
		} catch (ClientProtocolException e) {
			LOGGER.error(e.getMessage(), e);

		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);

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
		return status;

	}

}
