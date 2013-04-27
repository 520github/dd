/**
 * 
 */
package me.twocoffee.service.thirdparty.impl;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import me.twocoffee.common.auth.AuthClient;
import me.twocoffee.common.auth.AuthUtils;
import me.twocoffee.common.constant.SystemConstant;
import me.twocoffee.common.util.DateUtil;
import me.twocoffee.dao.ContactDao;
import me.twocoffee.dao.ThirdPartyContentDao;
import me.twocoffee.dao.ThirdPartyProfileDao;
import me.twocoffee.entity.Contact;
import me.twocoffee.entity.SystemTagEnum;
import me.twocoffee.entity.ThirdPartyContent;
import me.twocoffee.entity.ThirdPartyContent.MediaType;
import me.twocoffee.entity.ThirdPartyContent.ThirdPartyAttachment;
import me.twocoffee.entity.ThirdPartyContent.ThirdPartyContentUser;
import me.twocoffee.entity.ThirdPartyProfile;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;
import me.twocoffee.exception.ThirdPartyAccessException;
import me.twocoffee.exception.ThirdPartyAuthExpiredException;
import me.twocoffee.service.entity.ThirdPartyPostMessage;
import me.twocoffee.service.thirdparty.ThirdPartyClient;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author wenjian
 * 
 */
@Component
public class RenrenClient implements ThirdPartyClient {

    private static final Logger LOGGER = LoggerFactory
	    .getLogger(RenrenClient.class);

    @Autowired
    private AuthClient authClient;
    private static String url = "http://api.renren.com/restserver.do";
    private static String v = "1.0";
    private static String format = "JSON";
    // private static String sig = AuthClient.;//

    @Autowired
    private ThirdPartyContentDao thirdPartyContentDao;

    @Autowired
    private ThirdPartyProfileDao thirdProfileDao;

    @Autowired
    private ContactDao contactDao;

    private void findAndThrowException(JSONObject obj, String url)
	    throws ThirdPartyAuthExpiredException, ThirdPartyAccessException {

	if (obj.containsKey("error_code")) {
	    LOGGER.error("access thirdparty error! url {} errorcode {}",
		    new Object[] { url, obj.getString("error_code") });

	    try {

		if (obj.getInt("error_code") == 2002
			|| obj.getInt("error_code") == 2001) {

		    throw new ThirdPartyAuthExpiredException();
		}

	    } catch (Exception e) {

		if (e instanceof ThirdPartyAuthExpiredException) {
		    throw (ThirdPartyAuthExpiredException) e;
		}
	    }
	    throw new ThirdPartyAccessException(obj.getString("error_code"));
	}
    }

    private String getSig() {

	try {
	    return authClient.getConsumerProperties().getConsumer(
		    ThirdPartyType.Renren.toString()).consumerSecret;

	} catch (MalformedURLException e) {
	    throw new RuntimeException("get renren consumerSecret error!", e);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see me.twocoffee.service.impl.ThirdPartyClient#getContents()
     */
    @Override
    public List<ThirdPartyContent> getContents(
	    ThirdPartyProfile thirdpartyProfile) {

	List<ThirdPartyContent> contacts = new LinkedList<ThirdPartyContent>();
	Date latestDate = getLatestFavoriteDate(thirdpartyProfile);

	int page = 1;
	int pageSize = 50;

	do {
	    JSONObject obj = getContent(page, url, pageSize, thirdpartyProfile);

	    try {

		if (getContent(obj, contacts, latestDate, thirdpartyProfile)) {
		    break;
		}

	    } catch (ParseException e) {
		throw new ThirdPartyAccessException(e);
	    }
	    page++;

	} while (true);
	return contacts;
    }

    private boolean getContent(JSONObject obj,
	    List<ThirdPartyContent> contacts, Date latestDate,
	    ThirdPartyProfile thirdPartyProfile) throws ParseException {

	List<ThirdPartyContent> results = new LinkedList<ThirdPartyContent>();
	boolean stop = false;

	// get content found error,break out cycle call
	if (!obj.containsKey("result")) {
	    return true;
	}

	// not found content data,break out cycle call
	if (obj.getJSONArray("result").size() < 1) {
	    return true;
	}

	for (int i = 0; i < obj.getJSONArray("result").size(); i++) {
	    try {
		ThirdPartyContent content = new ThirdPartyContent();
		JSONObject jsonContentItem = obj.getJSONArray("result")
			.getJSONObject(i);

		JSONObject likes = jsonContentItem.getJSONObject("likes");
		String userLike = likes.getString("user_like");

		if (!"1".equalsIgnoreCase(userLike)) {// 非用户喜欢的
		    continue;
		}
		content.setContent(jsonContentItem.getString("description"));
		Date d = DateUtil.parse("yyyy-MM-dd HH:mm:ss",
			jsonContentItem.getString("update_time"));

		if (!d.after(latestDate)) {
		    stop = true;
		    break;
		}
		content.setCreateDate(new Date());
		content.setId(new ObjectId().toString());
		content.setThirdPartyType(thirdPartyProfile.getAccountType());

		content.setUid(thirdPartyProfile.getUserId());
		content.setFavoriteTime(d);
		content.setMessage(jsonContentItem.optString("message"));
		content.setRealurl(jsonContentItem.optString("href"));
		content.setTitle(jsonContentItem.optString("title"));

		if (jsonContentItem.containsKey("attachment")) {
		    JSONArray attachments = jsonContentItem
			    .getJSONArray("attachment");

		    for (int index = 0; index < attachments.size(); index++) {
			JSONObject attachmentJson = attachments
				.getJSONObject(index);

			if ("photo".equals(attachmentJson
				.getString("media_type"))) {
			    ThirdPartyAttachment attachment = new ThirdPartyAttachment();
			    attachment.setMediaType(MediaType.Image);
			    attachment.setMessage(attachmentJson
				    .optString("content"));
			    attachment.setNormalImage(attachmentJson
				    .optString("raw_src"));
			    content.addAttachment(attachment);
			}
		    }
		}
		ThirdPartyContentUser contentUser = new ThirdPartyContentUser();
		contentUser.setAvatar(jsonContentItem.getString("headurl"));
		contentUser.setName(jsonContentItem.getString("name"));
		contentUser.setUid(jsonContentItem.getString("actor_id"));
		content.setContentUser(contentUser);
		List<String> tags = new ArrayList<String>();
		tags.add("Source_Renren");
		tags.add(SystemTagEnum.Unread.toString());
		content.setTags(tags);
		results.add(content);

	    } catch (Exception e) {
		LOGGER.error("find error when get weibo content!", e);
	    }
	}

	if (results.size() > 0) {
	    contacts.addAll(results);
	}
	return stop;
    }

    private JSONObject getContent(int page, String url, int pageSize,
	    ThirdPartyProfile profile) {

	Map<String, String> cparams = new LinkedHashMap<String, String>();
	cparams.put("access_token", profile.getToken());
	cparams.put("count", String.valueOf(pageSize));
	Map<String, String> other = new LinkedHashMap<String, String>();
	other.put("method", "feed.get");
	other.put("page", String.valueOf(page));
	other.put("count", String.valueOf(pageSize));
	other.put("type", "20,21,30,32,33,50,51,52");
	cparams = this.getCommonPara(cparams, other);
	JSONObject result = AuthUtils.invoke("POST", url,
		AuthUtils.POST_DATA_TYPE_FORM,
		cparams);

	findAndThrowException(result, url);
	return result;
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

    /*
     * (non-Javadoc)
     * 
     * @see me.twocoffee.service.impl.ThirdPartyClient#invite()
     */
    @Override
    public void invite(ThirdPartyProfile profile, ThirdPartyPostMessage message) {
	StringBuilder builder = new StringBuilder();

	for (String toUid : message.getUserId()) {
	    builder.append(toUid).append(",");
	}

	if (builder.length() > 0) {
	    builder.deleteCharAt(builder.length() - 1);
	}
	Map<String, String> params = new LinkedHashMap<String, String>();
	params.put("access_token", profile.getToken());
	Map<String, String> other = new LinkedHashMap<String, String>();
	other.put("method", "notifications.send");
	other.put("notification", message.getContent());
	other.put("to_ids", builder.toString());
	params = this.getCommonPara(params, other);
	JSONObject result = AuthUtils.invoke("POST", url,
		AuthUtils.POST_DATA_TYPE_FORM, params);

	findAndThrowException(result, url);
    }

    /*
     * (non-Javadoc)
     * 
     * @see me.twocoffee.service.impl.ThirdPartyClient#broadcast()
     */
    @Override
    public void broadcast(ThirdPartyProfile profile,
	    ThirdPartyPostMessage message) {

	Map<String, String> other = new LinkedHashMap<String, String>();
	other.put("access_token", profile.getToken());
	other.put("description", message.getContent());
	other.put("format", format);
	other.put("message", message.getMessage());
	other.put("method", "feed.publishFeed");
	other.put("name", message.getTitle());
	other.put("url", message.getUrl());
	other.put("v", v);
	other.put("sig", AuthUtils.getMD5Sig(other, getSig()));
	JSONObject result = AuthUtils.invoke("POST", url,
		AuthUtils.POST_DATA_TYPE_FORM, other);

	findAndThrowException(result, url);
    }

    /*
     * (non-Javadoc)
     * 
     * @see me.twocoffee.service.impl.ThirdPartyClient#share()
     */
    @Override
    public void share(ThirdPartyProfile profile, ThirdPartyPostMessage message) {
	String addFriends = this.getAddfriends(profile, message);

	Map<String, String> paraMap = new LinkedHashMap<String, String>();
	paraMap.put("access_token", profile.getToken());
	paraMap.put("content", addFriends + message.getContent());
	paraMap.put("format", format);
	paraMap.put("method", "blog.addBlog");
	paraMap.put("title", message.getTitle());
	paraMap.put("v", v);
	paraMap.put("visable", "1");
	paraMap.put("sig", AuthUtils.getMD5Sig(paraMap, getSig()));

	JSONObject result = AuthUtils.invoke("POST", url,
		AuthUtils.POST_DATA_TYPE_FORM, paraMap);

	findAndThrowException(result, url);
    }

    private String getAddfriends(ThirdPartyProfile profile,
	    ThirdPartyPostMessage message) {

	StringBuffer addFriends = new StringBuffer();

	if (message.getUserId() == null || message.getUserId().size() == 0) {
	    return "";
	}
	List<Contact> contactList = contactDao.find(profile.getAccountId(),
		profile.getAccountType(), message.getUserId());
	if (contactList == null || contactList.size() < 1)
	    return "";

	for (Contact contact : contactList) {
	    if (contact == null)
		continue;
	    if (contact.getName() == null
		    || contact.getName().trim().length() < 1)
		continue;
	    addFriends.append("@" + contact.getName() + "(" + contact.getUid()
		    + ")  ");
	}

	return addFriends.toString();
    }

    @Override
    public List<Contact> getFriends(ThirdPartyProfile profile) {
	List<Contact> contacts = new LinkedList<Contact>();
	int page = 1;
	int pageSize = 500;
	boolean hasMore = true;

	do {
	    JSONObject obj = getFriend(page, url, pageSize, profile);

	    if (!obj.containsKey("result")) {
		hasMore = false;
	    }

	    // not found content data,break out cycle call
	    if (obj.getJSONArray("result").size() < 1) {
		hasMore = false;
	    }
	    contacts.addAll(getContact(obj, profile));
	    page++;
	} while (hasMore);
	return contacts;
    }

    private Map<String, String> getCommonPara(Map<String, String> params,
	    Map<String, String> other) {

	if (params == null) {
	    return null;
	}
	params.put("format", format);
	params.put("method", other.get("method"));
	other.remove("method");
	params.putAll(other);
	params.put("v", v);
	params.put("sig", AuthUtils.getMD5Sig(params, getSig()));
	return params;
    }

    private List<Contact> getContact(JSONObject obj, ThirdPartyProfile profile) {
	List<Contact> contacts = new LinkedList<Contact>();

	if (obj != null && obj.containsKey("result")) {

	    for (int i = 0; i < obj.getJSONArray("result").size(); i++) {
		JSONObject o = (JSONObject) obj.getJSONArray("result").get(i);
		try {
		    Contact c = new Contact();
		    c.setName(o.optString("name"));

		    if (StringUtils.isNotBlank(o.getString("headurl"))) {

			c.setAvatar(o.getString("headurl"));

		    } else {
			String path = "http://" + SystemConstant.domainName
				+ "/"
				+ LocaleContextHolder.getLocale().toString()
				+ "/images/";

			c.setAvatar(path + "avatar-35px.png");
		    }
		    c.setUid(o.getString("id"));
		    contacts.add(c);

		} catch (Exception e) {
		    LOGGER.error("error get friend!" + o.toString(), e);
		}
	    }
	}
	return contacts;
    }

    private JSONObject getFriend(int page, String url, int pageSize,
	    ThirdPartyProfile profile) {

	Map<String, String> params = new LinkedHashMap<String, String>();
	params.put("access_token", profile.getToken());
	Map<String, String> other = new LinkedHashMap<String, String>();
	other.put("method", "friends.getFriends");
	other.put("page", String.valueOf(page));
	params = this.getCommonPara(params, other);
	JSONObject result = AuthUtils.invoke("POST", url,
		AuthUtils.POST_DATA_TYPE_FORM,
		params);

	findAndThrowException(result, url);
	return result;
    }

    @Override
    public String getContentDisplay(ThirdPartyContent content) {
	// String vm =
	// getTemplate("src/test/resources/thirdparty_content/weibo/favorite.vm");
	String vm = "<div><img src=\"$avatar\">$nickName </div><div><p>$text</p></div>";
	vm = vm.replace("$avatar", content.getContentUser().getAvatar());
	vm = vm.replace("$nickName", content.getContentUser().getName());
	vm = vm.replace("$text", content.getContent());
	StringBuilder builder = new StringBuilder(vm);

	if (content.getAttachments() != null
		&& content.getAttachments().size() > 0) {

	    for (int index = 0; index < content.getAttachments().size(); index++) {

		if (StringUtils.isNotBlank(content.getAttachments().get(index)
			.getNormalImage())) {

		    builder.append("<div><img src=\"")
			    .append(content.getAttachments().get(index)
				    .getNormalImage()).append("\"></div>");

		}
	    }
	}
	return builder.toString();
    }

    @Override
    public String getSummaryDisplay(ThirdPartyContent content) {
	// String vm =
	// getTemplate("src/test/resources/thirdparty_content/weibo/favorite.vm");
	String vm = "<div>$nickName </div><div><p>$text</p></div>";
	vm = vm.replace("$nickName", content.getContentUser().getName());
	vm = vm.replace("$text", content.getContent());
	String replyvm = "<div>$reply_nickName</div> <div><p>$reply_text</p></div>";

	if (content.getReply() != null) {
	    replyvm = replyvm.replace("$reply_nickName", content.getReply()
		    .getContentUser().getName());

	    replyvm = replyvm.replace("$reply_text", content.getReply()
		    .getContent());

	    vm += replyvm;
	}
	return vm;
    }

    @Override
    public int getFriendNumber(ThirdPartyProfile profile) {
	return 0;
    }

    public static void main(String[] args) {
	RenrenClient client = new RenrenClient();
	ThirdPartyProfile profile = new ThirdPartyProfile();
	profile.setToken("221296|6.c4a335ef0035b5a2955c93445aeae564.2592000.1357992000-500420982");
	ThirdPartyPostMessage m = new ThirdPartyPostMessage();
	m.setContent("welcome!http://www.mduoduo.com/welcome!http://www.mduoduo.com/welcome!http://www.mduoduo.com/");
	List<String> uids = new ArrayList<String>();
	uids.add("500477037");
	m.setUserId(uids);
	client.invite(profile, m);
    }
}
