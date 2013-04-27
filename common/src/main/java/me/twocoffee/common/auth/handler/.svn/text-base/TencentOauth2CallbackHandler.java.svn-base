/**
 * 
 */
package me.twocoffee.common.auth.handler;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import me.twocoffee.common.auth.AuthUtils;
import me.twocoffee.entity.ThirdPartyProfile;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;
import net.oauth.OAuthConsumer;
import net.sf.json.JSONObject;

import org.apache.commons.lang.time.DateUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Component;

/**
 * @author momo
 * 
 */
@Component("tencentOauth2CallbackHandler")
public class TencentOauth2CallbackHandler extends AbstractOauth2CallbackHandler {

    private static final String URL_UID = "https://graph.qq.com/oauth2.0/me";

    private static final String URL_USER = "https://graph.qq.com/user/get_user_info";

    @Override
    protected ThirdPartyProfile getProfile(JSONObject result,
	    OAuthConsumer consumer) {

	Map<String, String> uidParam = new HashMap<String, String>();
	uidParam.put("access_token", result.getString("access_token"));
	JSONObject uidObj = AuthUtils.invoke("Get", URL_UID,
		AuthUtils.GET_PARAM_TYPE_QUERYPARAM, uidParam);

	String uid = uidObj.getString("openid");
	Map<String, String> userParam = new HashMap<String, String>();
	userParam.put("access_token", result.getString("access_token"));
	userParam.put("oauth_consumer_key", consumer.consumerKey);
	userParam.put("openid", uid);
	JSONObject userObj = AuthUtils.invoke("Get", URL_USER,
		AuthUtils.GET_PARAM_TYPE_QUERYPARAM, userParam);

	ThirdPartyProfile profile = new ThirdPartyProfile();
	profile.setAccountType(ThirdPartyType.QQ);
	profile.setCreateDate(new Date());
	profile.setExpiredDate(DateUtils.addSeconds(new Date(),
		result.getInt("expires_in")));

	profile.setRefreshToken("");
	profile.setToken(result.getString("access_token"));
	profile.setTokenSecret("");
	profile.setUserId(uid);

	if (userObj.has("nickname")) {
	    profile.setNickName(userObj.getString("nickname"));
	}

	if (userObj.has("figureurl_1")) {
	    profile.setAvatar(userObj.getString("figureurl_1"));
	}
	ObjectMapper mapper = new ObjectMapper();

	try {
	    Map<String, String> profileMap = mapper.readValue(
		    userObj.toString(),
		    new TypeReference<Map<String, String>>() {
		    });

	    Iterator<Entry<String, String>> keys = profileMap.entrySet()
		    .iterator();

	    while (keys.hasNext()) {
		Entry<String, String> key = keys.next();

		if (key.getValue() == null || "".equals(key.getValue())) {
		    keys.remove();
		}
	    }
	    profile.setProfile(profileMap);

	} catch (JsonParseException e) {
	    e.printStackTrace();

	} catch (JsonMappingException e) {
	    e.printStackTrace();

	} catch (IOException e) {
	    e.printStackTrace();
	}
	return profile;
    }

    @Override
    protected boolean isError(Map<String, String> params) {

	if (params.containsKey("code") && params.get("code").equals("0")) {
	    return true;
	}
	return false;
    }

    @Override
    protected boolean isDenied(Map<String, String> params) {
	return false;
    }
}
