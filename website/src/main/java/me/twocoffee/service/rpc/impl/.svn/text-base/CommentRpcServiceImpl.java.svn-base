package me.twocoffee.service.rpc.impl;

import java.io.IOException;
import java.util.Date;

import javax.ws.rs.core.HttpHeaders;

import me.twocoffee.entity.Comment;
import me.twocoffee.entity.DateJsonValueProcessor;
import me.twocoffee.service.rpc.AbstractHttpRPCService;
import me.twocoffee.service.rpc.CommentRpcService;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

@Service
public class CommentRpcServiceImpl extends AbstractHttpRPCService implements CommentRpcService {

	@Override
	public boolean saveComment(String token, String contentId, Comment comment) {
		HttpPost http = new HttpPost("/service/comment/"+contentId+"/");
		http.setHeader("Authorization", "AuthToken " + token);
		
		http.setHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_JSON);

		HttpResponse httpResponse = null;
		try {
			JsonConfig jsonConfig = new JsonConfig();  
			jsonConfig.registerJsonValueProcessor(Date.class,  
			new DateJsonValueProcessor("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));  

			JSONObject jsonObject = JSONObject.fromObject(comment, jsonConfig);
			http.setEntity(new StringEntity(jsonObject.toString(),DEFAULT_CHARSET));
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



