/**
 * 
 */
package me.twocoffee.service.thirdparty.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import me.twocoffee.common.auth.AuthUtils;
import me.twocoffee.common.constant.SystemConstant;
import me.twocoffee.common.util.DateUtil;
import me.twocoffee.entity.Contact;
import me.twocoffee.entity.SystemTagEnum;
import me.twocoffee.entity.ThirdPartyContent;
import me.twocoffee.entity.ThirdPartyContent.ThirdPartyAttachment;
import me.twocoffee.entity.ThirdPartyContent.ThirdPartyContentUser;
import me.twocoffee.entity.ThirdPartyProfile;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;
import me.twocoffee.exception.DuplicateThirdPartyMessageException;
import me.twocoffee.exception.ThirdPartyAccessException;
import me.twocoffee.exception.ThirdPartyAuthExpiredException;
import me.twocoffee.exception.ThirdPartyUnbindException;
import me.twocoffee.service.entity.ThirdPartyPostMessage;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author wenjian
 * 
 */
@Component
public class WeiboClient extends AbstractThridpartyClient {

    private static final Logger LOGGER = LoggerFactory
	    .getLogger(WeiboClient.class);

    @Override
    public List<Contact> getFriends(ThirdPartyProfile profile) {
	List<Contact> contacts = new LinkedList<Contact>();
	String url = "https://api.weibo.com/2/friendships/friends/bilateral.json";
	int page = 1;
	int pageSize = 200;
	boolean hasMore = false;

	do {
	    JSONObject obj = getFriend(page, url, pageSize, profile);
	    contacts.addAll(getContact(obj, profile));

	    if (obj.getInt("total_number") > pageSize * page) {
		hasMore = true;

	    } else {
		hasMore = false;
	    }
	    page++;

	} while (hasMore);
	return contacts;
    }

    private List<Contact> getContact(JSONObject obj, ThirdPartyProfile profile) {
	List<Contact> contacts = new LinkedList<Contact>();

	if (obj != null && obj.containsKey("users")) {

	    for (int i = 0; i < obj.getJSONArray("users").size(); i++) {
		JSONObject o = (JSONObject) obj.getJSONArray("users").get(i);
		try {
		    Contact c = new Contact();
		    c.setName(o.optString("name"));

		    if (StringUtils.isBlank(c.getName())) {
			c.setName(o.optString("screen_name"));
		    }

		    if (StringUtils.isBlank(c.getName())) {
			continue;
		    }

		    if (StringUtils
			    .isNotBlank(o.getString("profile_image_url"))) {
			c.setAvatar(o.getString("profile_image_url"));

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

	Map<String, String> params = new HashMap<String, String>();
	params.put("access_token", profile.getToken());
	params.put("uid", profile.getUserId());
	params.put("page", String.valueOf(page));
	params.put("count", String.valueOf(pageSize));
	JSONObject obj = AuthUtils.invoke("Get", url,
		AuthUtils.GET_PARAM_TYPE_QUERYPARAM, params);

	findAndThrowException(obj, url);
	return obj;
    }

    /**
     * @param obj
     * @param url
     * @throws ThirdPartyAuthExpiredException
     *             ThirdPartyAccessException
     */
    private void findAndThrowException(JSONObject obj, String url)
	    throws ThirdPartyAuthExpiredException, ThirdPartyAccessException {

	if (obj.containsKey("error_code")) {
	    LOGGER.error("access thirdparty error! url {} errorcode {}",
		    new Object[] { url, obj.getString("error_code") });

	    try {

		if (obj.getInt("error_code") > 21313
			&& obj.getInt("error_code") < 21320
			|| obj.getInt("error_code") == 21327) {

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

    /*
     * (non-Javadoc)
     * 
     * @see me.twocoffee.service.impl.ThirdPartyClient#getContents()
     */
    @Override
    public List<ThirdPartyContent> getContents(
	    ThirdPartyProfile thirdpartyProfile) {

	if (new Date().after(thirdpartyProfile.getExpiredDate())) {
	    throw new ThirdPartyAuthExpiredException();
	}
	List<ThirdPartyContent> contacts = new LinkedList<ThirdPartyContent>();
	Date latestDate = getLatestFavoriteDate(thirdpartyProfile);
	String url = "https://api.weibo.com/2/favorites.json";
	int page = 1;
	int pageSize = 200;
	boolean hasMore = false;

	do {
	    JSONObject obj = getContent(page, url, pageSize, thirdpartyProfile);

	    try {

		if (getContent(obj, contacts, latestDate, thirdpartyProfile)) {
		    break;
		}

	    } catch (ParseException e) {
		throw new ThirdPartyAccessException(e);
	    }

	    if (obj.getInt("total_number") > pageSize * page) {
		hasMore = true;

	    } else {
		hasMore = false;
	    }
	    page++;

	} while (hasMore);
	return contacts;
    }

    private boolean getContent(JSONObject obj,
	    List<ThirdPartyContent> contacts, Date latestDate,
	    ThirdPartyProfile thirdPartyProfile) throws ParseException {

	List<ThirdPartyContent> results = new LinkedList<ThirdPartyContent>();
	boolean stop = false;

	if (obj.containsKey("favorites")) {

	    for (int i = 0; i < obj.getJSONArray("favorites").size(); i++) {

		try {
		    ThirdPartyContent content = new ThirdPartyContent();
		    JSONObject o = obj.getJSONArray("favorites").getJSONObject(
			    i);
		    JSONObject c = o.getJSONObject("status");

		    if (c.containsKey("deleted")) {
			continue;
		    }
		    content.setContent(c.optString("text"));

		    if (c.containsKey("retweeted_status")) {
			JSONObject replyContent = c
				.getJSONObject("retweeted_status");

			ThirdPartyContent reply = new ThirdPartyContent();
			reply.setContent(replyContent.optString("text"));
			reply.setCreateDate(new Date());

			if (StringUtils.isNotBlank(replyContent
				.getString("created_at"))) {

			    reply.setFavoriteTime(DateUtil
				    .parseWeiboDate(replyContent
					    .getString("created_at")));
			}
			ThirdPartyAttachment attachment = new ThirdPartyAttachment();
			attachment.setMiddleImage(replyContent
				.optString("bmiddle_pic"));

			attachment.setNormalImage(replyContent
				.optString("original_pic"));

			reply.addAttachment(attachment);

			if (replyContent.containsKey("user")) {
			    JSONObject repu = replyContent
				    .getJSONObject("user");

			    ThirdPartyContentUser user = new ThirdPartyContentUser();
			    user.setAvatar(repu.optString("profile_image_url"));
			    user.setName(repu.optString("name"));
			    user.setUid(repu.optString("id"));
			    reply.setContentUser(user);
			    content.setReply(reply);
			}
		    }
		    Date d = DateUtil.parseWeiboDate(o
			    .getString("favorited_time"));

		    if (!d.after(latestDate)) {
			stop = true;
			break;
		    }
		    content.setCreateDate(new Date());
		    content.setId(new ObjectId().toString());
		    content.setThirdPartyType(ThirdPartyType.Weibo);
		    content.setUid(thirdPartyProfile.getUserId());
		    content.setFavoriteTime(d);
		    content.setTitle("From 新浪收藏");
		    content.setRealurl("http://weibo.com/fav");
		    ThirdPartyAttachment attachment = new ThirdPartyAttachment();
		    attachment.setMiddleImage(c.optString("bmiddle_pic"));
		    attachment.setNormalImage(c.optString("original_pic"));
		    content.addAttachment(attachment);

		    if (!c.containsKey("user")) {
			continue;
		    }
		    JSONObject u = c.getJSONObject("user");
		    ThirdPartyContentUser contentUser = new ThirdPartyContentUser();
		    contentUser.setAvatar(u.optString("profile_image_url"));
		    contentUser.setName(u.getString("name"));
		    contentUser.setUid(u.getString("id"));
		    content.setContentUser(contentUser);
		    List<String> tags = new ArrayList<String>();
		    tags.add("Source_Weibo");
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
	}
	return stop;
    }

    private JSONObject getContent(int page, String url, int pageSize,
	    ThirdPartyProfile profile) {

	Map<String, String> params = new HashMap<String, String>();
	params.put("access_token", profile.getToken());
	params.put("uid", profile.getUserId());
	params.put("page", String.valueOf(page));
	params.put("count", String.valueOf(pageSize));
	JSONObject obj = AuthUtils.invoke("Get", url,
		AuthUtils.GET_PARAM_TYPE_QUERYPARAM, params);

	findAndThrowException(obj, url);
	return obj;
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

	if (profile == null || profile.isBind() == false) {
	    throw new ThirdPartyUnbindException();
	}

	if (StringUtils.isNotBlank(message.getUuid())
		&& message.getUuid().equals(profile.getUuid())) {

	    throw new DuplicateThirdPartyMessageException();
	}
	String url = "https://api.weibo.com/2/statuses/update.json";
	Map<String, String> params = new HashMap<String, String>();
	params.put("access_token", profile.getToken());
	params.put("status", message.getContent());
	JSONObject obj = AuthUtils.invoke("POST", url,
		AuthUtils.POST_DATA_TYPE_FORM, params);

	findAndThrowException(obj, url);

	if (StringUtils.isNotBlank(message.getUuid())) {
	    profile.setUuid(message.getUuid());
	    thirdProfileDao.save(profile);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see me.twocoffee.service.impl.ThirdPartyClient#broadcast()
     */
    @Override
    public void broadcast(ThirdPartyProfile profile,
	    ThirdPartyPostMessage message) {
	invite(profile, message);
    }

    /*
     * (non-Javadoc)
     * 
     * @see me.twocoffee.service.impl.ThirdPartyClient#share()
     */
    @Override
    public void share(ThirdPartyProfile profile, ThirdPartyPostMessage message) {
	message.setContent(appandUser(profile, message));
	invite(profile, message);
    }

    @Override
    public int getFriendNumber(ThirdPartyProfile profile) {
	int num = 1;
	String numStr = profile.getProfile().get("friends_count");

	if (StringUtils.isNotBlank(numStr)) {

	    try {
		num = Integer.parseInt(numStr);

	    } catch (NumberFormatException e) {
		LOGGER.error("can not get friends num", e);
	    }
	}
	return num;
    }

}
