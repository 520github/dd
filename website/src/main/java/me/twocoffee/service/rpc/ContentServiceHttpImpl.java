package me.twocoffee.service.rpc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;

import me.twocoffee.common.util.TokenUtil;
import me.twocoffee.entity.Content.ContentType;
import me.twocoffee.entity.Content.FilePayload;
import me.twocoffee.entity.HtmlPayload;
import me.twocoffee.entity.ImagePayload;
import me.twocoffee.entity.ProductPayload;
import me.twocoffee.entity.ResponseContent;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;
import me.twocoffee.rest.entity.AccountId;
import me.twocoffee.rest.entity.FriendShareInfo;
import me.twocoffee.rest.entity.ThirdParty;
import me.twocoffee.rest.generic.GenericContent;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

@Service
public class ContentServiceHttpImpl extends AbstractHttpRPCService implements
	ContentRpcService {

    public static class PostContent {
	public static class ContentComment {
	    private int score;
	    private String text;

	    public int getScore() {
		return score;
	    }

	    public String getText() {
		return text;
	    }

	    public void setScore(int score) {
		this.score = score;
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
	private FilePayload filePayload;
	private ImagePayload imagePayload;
	private ProductPayload productPayload;

	public ProductPayload getProductPayload() {
	    return productPayload;
	}

	public void setProductPayload(ProductPayload productPayload) {
	    this.productPayload = productPayload;
	}

	public ImagePayload getImagePayload() {
	    return imagePayload;
	}

	public void setImagePayload(ImagePayload imagePayload) {
	    this.imagePayload = imagePayload;
	}

	public FilePayload getFilePayload() {
	    return filePayload;
	}

	public void setFilePayload(FilePayload filePayload) {
	    this.filePayload = filePayload;
	}

	public ContentComment getComment() {
	    return comment;
	}

	public ContentType getContentType() {
	    return contentType;
	}

	public HtmlPayload getHtmlPayload() {
	    return htmlPayload;
	}

	public String getId() {
	    return id;
	}

	public String getLanguage() {
	    return language;
	}

	public List<String> getShare() {
	    return share;
	}

	public List<String> getTag() {
	    return tag;
	}

	public String getTitle() {
	    return title;
	}

	public String getUrl() {
	    return url;
	}

	public void setComment(ContentComment comment) {
	    this.comment = comment;
	}

	public void setContentType(ContentType contentType) {
	    this.contentType = contentType;
	}

	public void setHtmlPayload(HtmlPayload htmlPayload) {
	    this.htmlPayload = htmlPayload;
	}

	public void setId(String id) {
	    this.id = id;
	}

	public void setLanguage(String language) {
	    this.language = language;
	}

	public void setShare(List<String> share) {
	    this.share = share;
	}

	public void setTag(List<String> tag) {
	    this.tag = tag;
	}

	public void setTitle(String title) {
	    this.title = title;
	}

	public void setUrl(String url) {
	    this.url = url;
	}
    }

    private String getShareJson(List<String> friends,
	    List<String> thirdPartyfriends, int score,
	    String comment) {
	if ((friends == null || friends.size() < 1)
		&& (thirdPartyfriends == null || thirdPartyfriends.size() < 1))
	    return null;

	FriendShareInfo fs = new FriendShareInfo();
	fs.setComment(comment);
	fs.setScore(score);
	if (friends != null && friends.size() > 0) {
	    int size = friends.size();
	    AccountId[] ids = new AccountId[size];
	    String[] receipt = new String[size];
	    for (int i = 0; i < size; i++) {
		AccountId id = new AccountId();
		id.setAccountId(friends.get(i));
		ids[i] = id;
		receipt[i] = friends.get(i);
	    }
	    fs.setReceipt(ids);
	}
	if (null != thirdPartyfriends && thirdPartyfriends.size() > 0) {
	    int thirdPartysize = thirdPartyfriends.size();
	    String[] thirdreceipt = new String[thirdPartysize];
	    for (int i = 0; i < thirdPartysize; i++) {
		thirdreceipt[i] = thirdPartyfriends.get(i);
	    }
	    ThirdParty[] types = new ThirdParty[1];
	    ThirdParty thirdParty = new ThirdParty();
	    thirdParty.setReceipt(thirdreceipt);
	    thirdParty.setType(ThirdPartyType.Weibo.toString());
	    types[0] = thirdParty;
	    fs.setThirdparty(types);
	}
	return JSONSerializer.toJSON(fs).toString();
    }

    @Override
    public Map getContentByUrl(String contentType, String url, String referer) {

	HttpGet httpGet = null;
	try {
	    String encodeUrl = URLEncoder.encode(
		    URLEncoder.encode(url, "utf-8"),
		    "utf-8");
	    httpGet = new HttpGet(
		    "/service/content/archive/"
			    + encodeUrl);
	} catch (UnsupportedEncodingException e) {
	    LOGGER.error(e.getMessage(), e);
	}

	httpGet.setHeader("Referer", referer);
	if (ContentType.Image.toString().equals(contentType)) {
	    httpGet
		    .setHeader(
			    "Accept",
			    "application/meta-image+json");
	} else {
	    httpGet
		    .setHeader(
			    "Accept",
			    "application/meta-product+json,application/meta-web+json;q=0.8");
	}

	HttpResponse httpResponse = null;
	try {

	    httpResponse = invoke(httpGet);

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

	    String httpResponseStr = EntityUtils.toString(httpResponse
		    .getEntity(), DEFAULT_CHARSET);

	    LOGGER.debug("http get request to {} ok,response is {}",
		    httpGet.getRequestLine().getUri().toString(),
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
	    httpGet.abort();
	}

    }

    @Override
    public ResponseContent getPublicContent(String accountId, String contentId) {

	try {

	    if (contentId == null || contentId.trim().length() < 1) {
		LOGGER.error("request contentId value is null");
		return null;
	    }
	    StringBuilder builder = new StringBuilder(
		    "/service/content/public/search/");

	    if (StringUtils.isNotBlank(accountId)) {
		builder.append(";accountId=").append(accountId);
	    }
	    builder.append(";contentId=").append(contentId);
	    HttpUriRequest request = new HttpGet(builder.toString());
	    Map<String, String> headerMap = new HashMap<String, String>();
	    return (ResponseContent) this.executeMethod(request, headerMap,
		    null, ResponseContent.class);

	} catch (Exception e) {

	}
	return null;
    }
    
    @Override
    public ResponseContent getResponseContentByRepositoryId(String repositoryId) {
	try {
	    if (repositoryId == null || repositoryId.trim().length() < 1) {
		LOGGER.error("request repositoryId value is null");
		return null;
	    }
	    HttpUriRequest request = new HttpGet(
		    "/service/content/public/" + repositoryId);
	    Map<String, String> headerMap = new HashMap<String, String>();
	    return (ResponseContent) this.executeMethod(request, headerMap,
		    null, ResponseContent.class);
	} catch (Exception e) {

	}
	return null;
    }

    @Override
    public ResponseContent getResponseContentById(String contentId,
	    String authToken, String userAgent) {
	try {
	    if (contentId == null || contentId.trim().length() < 1) {
		LOGGER.error("request contentId value is null");
		return null;
	    }
	    HttpUriRequest request = new HttpGet(
		    "/service/content/personal/item/" + contentId);
	    Map<String, String> headerMap = new HashMap<String, String>();
	    headerMap.put("Authorization",
		    TokenUtil.getWebTokenAuthorization(authToken));
	    if (userAgent != null && userAgent.trim().length() > 1) {
		headerMap.put("User-Agent", userAgent);
	    }
	    return (ResponseContent) this.executeMethod(request, headerMap,
		    null, ResponseContent.class);
	} catch (Exception e) {

	}
	return null;
    }

    @Override
    public Map postContent(String authToken, PostContent postContent)
	    throws ContentAlreadyCollectException {

	HttpPost httpPost = new HttpPost("/service/content/personal/item");

	httpPost.setHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_JSON);
	httpPost.setHeader("Authorization",
		TokenUtil.getWebTokenAuthorization(authToken));

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
		throw new ContentAlreadyCollectException(postContent.getId());
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

	} catch (ContentAlreadyCollectException e) {
	    throw e;
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

    @Override
    public boolean shareToFriend(String token, String contentId,
	    List<String> friendId, List<String> thirdPartyfriendId, int score,
	    String comment) {
	HttpPost http = new HttpPost("/service/content/personal/item/"
		+ contentId + "/share");
	http.setHeader("Authorization", this.getWebTokenAuthorization(token));

	http.setHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_JSON);

	HttpResponse httpResponse = null;
	try {
	    http.setEntity(new StringEntity(getShareJson(friendId,
		    thirdPartyfriendId, score,
		    comment), "UTF-8"));
	    httpResponse = invoke(http);

	    if (httpResponse.getStatusLine().getStatusCode() == 403) {
		LOGGER.debug("{} auth failed", token);
		return false;
	    }
	    if (httpResponse.getStatusLine().getStatusCode() != 204
		    && httpResponse.getStatusLine().getStatusCode() != 202) {
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
    public String updateRepository(String repositoryId, String authToken,
	    GenericContent genericContent) {
	try {
	    if (repositoryId == null || repositoryId.trim().length() < 1) {
		LOGGER.error("request repositoryId value is null");
		return null;
	    }
	    HttpUriRequest request = new HttpPut(
		    "/service/content/personal/item/" + repositoryId);
	    Map<String, String> headerMap = new HashMap<String, String>();
	    headerMap.put("Content-Type", CONTENT_TYPE_JSON);
	    this.setAuthorization(headerMap, authToken);
	    return (String) this.executeMethod(request, headerMap,
		    genericContent, String.class);
	} catch (Exception e) {

	}
	return null;
    }

}
