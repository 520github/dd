package me.twocoffee.mail.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;

import me.twocoffee.entity.Content.ContentType;
import me.twocoffee.entity.HtmlPayload;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

public class ContentServiceHttpImpl extends AbstractHttpRPCService {
	public static class PostContent {
		public static class ContentComment {
			private int score;
			private String text;

			public int getScore() {
				return score;
			}

			public void setScore(int score) {
				this.score = score;
			}

			public String getText() {
				return text;
			}

			public void setText(String text) {
				this.text = text;
			}
		}

		private String id;
		private ContentType contentType;
		private String language;
		private List<String> tag;
		private ContentComment comment;
		private List<String> share;
		private String url;
		private String title;
		private HtmlPayload htmlPayload;
		
		
		public List<String> getTag() {
			return tag;
		}

		public void setTag(List<String> tag) {
			this.tag = tag;
		}

		public List<String> getShare() {
			return share;
		}

		public void setShare(List<String> share) {
			this.share = share;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public ContentType getContentType() {
			return contentType;
		}

		public void setContentType(ContentType contentType) {
			this.contentType = contentType;
		}

		public String getLanguage() {
			return language;
		}

		public void setLanguage(String language) {
			this.language = language;
		}

		public ContentComment getComment() {
			return comment;
		}

		public void setComment(ContentComment comment) {
			this.comment = comment;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public HtmlPayload getHtmlPayload() {
			return htmlPayload;
		}

		public void setHtmlPayload(HtmlPayload htmlPayload) {
			this.htmlPayload = htmlPayload;
		}
	}

	public Map postContent(String authToken,PostContent postContent) {

		HttpPost httpPost = new HttpPost("/service/content/personal/item");

		httpPost.setHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_JSON);
		httpPost.setHeader("Authorization", "AuthToken " + authToken);

		try {
			JSONObject jsonObject = JSONObject.fromObject(postContent);

			httpPost
					.setEntity(new StringEntity(jsonObject.toString(),
							DEFAULT_CHARSET));
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(e.getMessage(), e);
		}

		HttpResponse httpResponse = null;
		try {

			httpResponse = invoke(httpPost);

			if (httpResponse.getStatusLine().getStatusCode() == 403) {
				LOGGER.debug("{} auth failed", authToken);
				return null;
			}
			if (httpResponse.getStatusLine().getStatusCode() == 409) {
				return null;
			}
			if (httpResponse.getStatusLine().getStatusCode() != 200) {
				LOGGER
						.error(
								"http get request to {} failed,failed status {},response is {}",
								new Object[] {
										httpPost.getRequestLine().getUri()
												.toString(),
										httpResponse
												.getStatusLine()
												.getStatusCode(),
										EntityUtils.toString(httpResponse
												.getEntity(), DEFAULT_CHARSET) });
				return null;
			}

			String httpResponseStr = EntityUtils.toString(httpResponse
					.getEntity(), DEFAULT_CHARSET);

			LOGGER.debug("http get request to {} ok,response is {}",
					httpPost.getRequestLine().getUri().toString(),
					httpResponseStr);

			return objectMapper.readValue(httpResponseStr, Map.class);

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
			httpPost.abort();
		}

	}
}
