/**
 * 
 */
package me.twocoffee.service.thirdparty.impl;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import me.twocoffee.common.auth.AuthUtils;
import me.twocoffee.common.constant.SystemConstant;
import me.twocoffee.entity.Contact;
import me.twocoffee.entity.SystemTagEnum;
import me.twocoffee.entity.ThirdPartyContent;
import me.twocoffee.entity.ThirdPartyContent.MediaType;
import me.twocoffee.entity.ThirdPartyContent.ThirdPartyAttachment;
import me.twocoffee.entity.ThirdPartyContent.ThirdPartyContentUser;
import me.twocoffee.entity.ThirdPartyProfile;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;
import me.twocoffee.exception.DuplicateThirdPartyMessageException;
import me.twocoffee.exception.ThirdPartyAccessException;
import me.twocoffee.exception.ThirdPartyAuthExpiredException;
import me.twocoffee.exception.ThirdPartyUnbindException;
import me.twocoffee.service.entity.ThirdPartyPostMessage;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component("tencentWeiboClient")
public class TencentWeiboClient extends AbstractThridpartyClient {

    private static final Logger LOGGER = LoggerFactory
	    .getLogger(TencentWeiboClient.class);

    private static String OAUTH_VERSION = "2.a";

    private static String FORMAT = "json";

    @Override
    public void broadcast(ThirdPartyProfile profile,
	    ThirdPartyPostMessage content) {

	doSend(profile, content);
    }

    @Override
    public List<ThirdPartyContent> getContents(ThirdPartyProfile profile) {

	if (new Date().after(profile.getExpiredDate())) {
	    throw new ThirdPartyAuthExpiredException();
	}
	List<ThirdPartyContent> contentList = new LinkedList<ThirdPartyContent>();
	Date latestDate = getLatestFavoriteDate(profile);
	String pageFlag = "0";// 第一页
	String pageTime = "0";
	String lastId = "0";
	String reqnum = "100";

	do {
	    JSONObject obj = this.getContentJSONObject(pageFlag, pageTime,
		    lastId, profile, reqnum);

	    JSONObject data = obj.getJSONObject("data");

	    if (getThirdPartyContent(data,
		    latestDate, profile, contentList)) {

		break;
	    }
	    // 向下翻页
	    pageFlag = "1";
	    // 当前页最后一条记录收藏日期
	    pageTime = data.getString("nexttime");
	    // 当前页最后的一条记录
	    lastId = data.getJSONArray("info")
		    .getJSONObject(data.getJSONArray("info").size() - 1)
		    .getString("id");

	} while (true);
	return contentList;
    }

    private boolean getThirdPartyContent(JSONObject obj,
	    Date latestDate, ThirdPartyProfile thirdPartyProfile,
	    List<ThirdPartyContent> list) {

	if (obj == null)
	    return true;

	if (!obj.containsKey("info") || obj.get("info") == null)
	    return true;

	for (int i = 0; i < obj.getJSONArray("info").size(); i++) {

	    try {
		JSONObject info = obj.getJSONArray("info").getJSONObject(i);
		// 日期转化好像有点不对
		Date storetime = new Date(1000L * Integer.parseInt(info
			.getString("storetime")));

		if (!storetime.after(latestDate)) {
		    return true;
		}
		ThirdPartyContent content = new ThirdPartyContent();
		content.setCreateDate(new Date());
		content.setId(new ObjectId().toString());
		content.setThirdPartyType(thirdPartyProfile.getAccountType());
		content.setUid(thirdPartyProfile.getUserId());
		content.setFavoriteTime(storetime);
		content.setContent(info.optString("text"));
		content.setRealurl("http://t.qq.com/favor");
		content.setTitle("From 腾讯微博");

		// 可能需要处理两种来源，一种是info.image,另一种是info.source.image,
		JSONArray images = info.optJSONArray("image");

		if (images != null &&
			images.size() > 0) {

		    for (int j = 0; j < images.size(); j++) {
			String image = images.getString(j) + "/2000";
			ThirdPartyAttachment attachment = new ThirdPartyAttachment();
			attachment.setMediaType(MediaType.Image);
			attachment.setNormalImage(image);
			content.addAttachment(attachment);
		    }
		}
		ThirdPartyContentUser contentUser = new ThirdPartyContentUser();
		contentUser.setAvatar(info.getString("head") + "/180");
		contentUser.setName(info.getString("nick"));
		contentUser.setUid(info.getString("openid"));
		content.setContentUser(contentUser);
		List<String> tags = new ArrayList<String>();
		tags.add(SystemTagEnum.Source_Tencent.toString());
		tags.add(SystemTagEnum.Unread.toString());
		content.setTags(tags);
		list.add(content);
		JSONObject replyContent = info
			.optJSONObject("source");

		if (replyContent != null) {
		    ThirdPartyContent reply = new ThirdPartyContent();
		    reply.setContent(replyContent.optString("text"));
		    reply.setCreateDate(new Date());
		    JSONArray reply_images = replyContent.optJSONArray("image");

		    if (reply_images != null &&
			    reply_images.size() > 0) {

			for (int j = 0; j < reply_images.size(); j++) {
			    String image = reply_images.getString(j) + "/2000";
			    ThirdPartyAttachment attachment = new ThirdPartyAttachment();
			    attachment.setMediaType(MediaType.Image);
			    attachment.setNormalImage(image);
			    reply.addAttachment(attachment);
			}
		    }
		    ThirdPartyContentUser user = new ThirdPartyContentUser();
		    user.setAvatar(replyContent.optString("head") + "/180");
		    user.setName(replyContent.optString("nick"));
		    user.setUid(replyContent.optString("openid"));
		    reply.setContentUser(user);
		    content.setReply(reply);
		}

	    } catch (Exception e) {
		LOGGER.error("find error when get qq content!", e);
	    }
	}
	String hasNext = obj.getString("hasnext");

	if ("1".equals(hasNext)) {// not content
	    return true;
	}
	return false;
    }

    private JSONObject getContentJSONObject(String pageFlag, String pageTime,
	    String lastId, ThirdPartyProfile profile, String reqnum) {

	String url = "https://open.t.qq.com/api/fav/list_t";
	Map<String, String> params = new LinkedHashMap<String, String>();
	params.put("pageflag", pageFlag);
	params.put("pagetime", pageTime);
	params.put("reqnum", reqnum);
	params.put("lastid", lastId);
	params.put("format", FORMAT);
	params.put("access_token", profile.getToken());
	params.put("oauth_consumer_key", getConsumerKey());
	params.put("openid", profile.getUserId());
	params.put("oauth_version", OAUTH_VERSION);
	JSONObject result = AuthUtils.invoke("get", url,
		AuthUtils.GET_PARAM_TYPE_QUERYPARAM,
		params);

	findAndThrowException(result, url);
	return result;
    }

    @Override
    public List<Contact> getFriends(ThirdPartyProfile profile) {
	List<Contact> contacts = new LinkedList<Contact>();
	String url = "https://open.t.qq.com/api/friends/mutual_list";
	int page = 1;
	int pageSize = 30;
	int reqnum = 0;

	do {
	    JSONObject obj = getFriend(page, url, pageSize, profile, reqnum);
	    contacts.addAll(getContact(obj.getJSONObject("data"), profile));

	    if ("1".equals(obj.getJSONObject("data").getString("hasnext"))) {
		break;
	    }
	    reqnum = pageSize;
	    page++;

	} while (true);
	return contacts;

    }

    private Collection<Contact> getContact(JSONObject jsonObject,
	    ThirdPartyProfile profile) {

	List<Contact> contacts = new LinkedList<Contact>();

	if (jsonObject.containsKey("info")) {

	    for (int i = 0; i < jsonObject.getJSONArray("info").size(); i++) {
		JSONObject o = (JSONObject) jsonObject.getJSONArray("info")
			.get(i);

		try {
		    Contact c = new Contact();
		    c.setName(o.optString("nick"));

		    if (StringUtils.isBlank(c.getName())) {
			continue;
		    }

		    if (StringUtils
			    .isNotBlank(o.getString("headurl"))) {

			c.setAvatar(o.getString("headurl") + "/180");

		    } else {
			String path = "http://" + SystemConstant.domainName
				+ "/"
				+ LocaleContextHolder.getLocale().toString()
				+ "/images/";

			c.setAvatar(path + "avatar-35px.png");
		    }
		    ObjectMapper mapper = new ObjectMapper();
		    Map<String, Object> profileMap = null;

		    try {
			profileMap = mapper.readValue(o.toString(),
				new TypeReference<Map<String, Object>>() {
				});

		    } catch (Exception e) {
			profileMap = new HashMap<String, Object>();
			profileMap.put("name", o.get("name"));
		    }
		    c.setProfile(profileMap);
		    c.setUid(o.getString("openid").toLowerCase());
		    contacts.add(c);

		} catch (Exception e) {
		    LOGGER.error("error get friend!" + o.toString(), e);
		}
	    }
	}
	return contacts;
    }

    private JSONObject getFriend(int page, String url, int pageSize,
	    ThirdPartyProfile profile, int reqnum) {

	Map<String, String> params = new LinkedHashMap<String, String>();
	params.put("fopenid", profile.getUserId());
	params.put("startindex", String.valueOf(pageSize * (page - 1)));
	params.put("reqnum", String.valueOf(reqnum));
	params.put("install", "0");
	params.put("format", "json");
	params.put("access_token", profile.getToken());
	params.put("oauth_consumer_key", getConsumerKey());
	params.put("openid", profile.getUserId());
	params.put("oauth_version", "2.a");
	JSONObject result = AuthUtils.invoke("get", url,
		AuthUtils.GET_PARAM_TYPE_QUERYPARAM,
		params);

	findAndThrowException(result, url);
	return result;
    }

    @Override
    public void invite(ThirdPartyProfile profile, ThirdPartyPostMessage content) {
	doSend(profile, content);
    }

    @Override
    public void share(ThirdPartyProfile profile, ThirdPartyPostMessage content) {
	content.setContent(appandUser(profile, content));
	doSend(profile, content);
    }

    @Override
    protected String appandUser(ThirdPartyProfile profile,
	    ThirdPartyPostMessage message) {

	StringBuffer buffer = new StringBuffer(message.getContent());

	if (message.getUserId() != null && message.getUserId().size() > 0) {

	    for (String t : message.getUserId()) {
		Contact c = getThirdPartyFriend(profile.getAccountId(),
			profile.getAccountType(), t);

		if (c != null && c.getProfile() != null
			&& c.getProfile().containsKey("name")) {

		    String accountName = (String) c.getProfile().get("name");
		    buffer.insert(0, "@").insert(1, accountName + " ");
		}
	    }
	}
	return buffer.toString();
    }

    private void doSend(ThirdPartyProfile profile, ThirdPartyPostMessage content) {

	if (profile == null || profile.isBind() == false) {
	    throw new ThirdPartyUnbindException();
	}

	if (StringUtils.isNotBlank(content.getUuid())
		&& content.getUuid().equals(profile.getUuid())) {

	    throw new DuplicateThirdPartyMessageException();
	}
	String url = "https://open.t.qq.com/api/t/add";
	Map<String, String> params = new LinkedHashMap<String, String>();
	params.put("content", content.getContent());
	params.put("clientip", "");
	params.put("longitude", "");
	params.put("latitude", "");
	params.put("syncflag", "1");
	params.put("compatibleflag", "0");
	params.put("format", "json");
	params.put("access_token", profile.getToken());
	params.put("oauth_consumer_key", getConsumerKey());
	params.put("openid", profile.getUserId());
	params.put("oauth_version", "2.a");
	JSONObject result = AuthUtils.invoke("post", url,
		AuthUtils.POST_DATA_TYPE_FORM,
		params);

	findAndThrowException(result, url);
    }

    private void findAndThrowException(JSONObject obj, String url)
	    throws ThirdPartyAuthExpiredException, ThirdPartyAccessException {

	if (obj.containsKey("errcode") && obj.getInt("errcode") != 0) {
	    LOGGER.error("access thirdparty error! url {} errorcode {}",
		    new Object[] { url, obj.getString("errcode") });

	    try {

		if (obj.getInt("errcode") > 10016
			&& obj.getInt("errcode") < 10022
			|| obj.getInt("errcode") > 35
			&& obj.getInt("errcode") < 41) {

		    throw new ThirdPartyAuthExpiredException();
		}

	    } catch (Exception e) {

		if (e instanceof ThirdPartyAuthExpiredException) {
		    throw (ThirdPartyAuthExpiredException) e;
		}
	    }
	    throw new ThirdPartyAccessException(obj.getString("errcode"));
	}
    }

    private String getConsumerKey() {

	try {
	    return authClient.getConsumerProperties().getConsumer(
		    ThirdPartyType.Tencent.toString()).consumerKey;

	} catch (MalformedURLException e) {
	    throw new RuntimeException("can not get tencent consumerkey", e);
	}
    }

    private Date getLatestFavoriteDate(ThirdPartyProfile thirdpartyProfile) {
	ThirdPartyContent content = thirdPartyContentDao.getLatestContent(
		thirdpartyProfile.getAccountType(),
		thirdpartyProfile.getUserId());

	if (content != null && content.getFavoriteTime() != null) {
	    return content.getFavoriteTime();
	}
	return new Date(1000);
    }

}
