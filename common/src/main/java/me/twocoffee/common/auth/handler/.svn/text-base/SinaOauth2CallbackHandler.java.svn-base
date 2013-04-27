/**
 * 
 */
package me.twocoffee.common.auth.handler;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import me.twocoffee.common.BasicHttpClient;
import me.twocoffee.common.auth.AuthUtils;
import me.twocoffee.common.constant.SystemConstant;
import me.twocoffee.common.util.HttpClientUtils;
import me.twocoffee.entity.ThirdPartyProfile;
import net.oauth.OAuthConsumer;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author momo
 * 
 */
@Component("sinaOauth2CallbackHandler")
public class SinaOauth2CallbackHandler extends AbstractOauth2CallbackHandler {

    private static final Logger LOG = LoggerFactory
	    .getLogger(SinaOauth2CallbackHandler.class);

    private static final String URL_USER = "https://api.weibo.com/2/users/show.json";

    @Override
    protected ThirdPartyProfile getProfile(JSONObject result,
	    OAuthConsumer consumer) {

	if (!result.containsKey("uid")) {
	    LOG.error("callback error!{}", result.toString());
	    throw new RuntimeException("oauth callback error!");
	}
	String uid = result.getString("uid");
	ThirdPartyProfile profile = new ThirdPartyProfile();
	Map<String, String> params1 = new HashMap<String, String>();
	params1.put("access_token", result.getString("access_token"));
	params1.put("uid", uid);
	JSONObject profileObj = AuthUtils.invoke("Get", URL_USER,
		AuthUtils.GET_PARAM_TYPE_QUERYPARAM, params1);

	if (profileObj.containsKey("error_code")) {
	    LOG.error("oauth callback error! " + profileObj.toString());
	    throw new RuntimeException("oauth callback error!");
	}

	if (profileObj.containsKey("status")) {
	    profileObj.remove("status");
	}
	ObjectMapper mapper = new ObjectMapper();
	Map<String, String> profileMap = null;

	try {
	    profileMap = mapper.readValue(profileObj.toString(),
		    new TypeReference<Map<String, String>>() {
		    });

	} catch (JsonParseException e) {
	    throw new RuntimeException("oauth callback error!");

	} catch (JsonMappingException e) {
	    throw new RuntimeException("oauth callback error!");

	} catch (IOException e) {
	    throw new RuntimeException("oauth callback error!");
	}
	profile.setCreateDate(new Date());

	if (consumer.getProperty("expired") != null
		|| !"".equals(consumer.getProperty("expired"))) {

	    profile.setExpiredDate(DateUtils.addDays(new Date(), Integer
		    .parseInt(String.valueOf(consumer.getProperty("expired")))));

	}
	profile.setProfile(profileMap);
	profile.setUserId(uid);
	profile.setRefreshToken("");
	profile.setToken(result.getString("access_token"));
	profile.setTokenSecret("");

	if (StringUtils.isNotBlank(profileObj.getString("avatar_large"))) {
	    String url = HttpClientUtils.requestAndStoreResource(
		    BasicHttpClient.getHttpClient(),
		    profileObj.getString("avatar_large"), fileOperator);

	    if (url != null) {
		profile.setAvatar(url);

	    } else {
		profile.setAvatar(profileObj.getString("profile_image_url"));
	    }

	} else if (StringUtils.isNotBlank(profileObj
		.getString("profile_image_url"))) {
	    String url = HttpClientUtils.requestAndStoreResource(
		    BasicHttpClient.getHttpClient(),
		    profileObj.getString("profile_image_url"), fileOperator);

	    if (url != null) {
		profile.setAvatar(url);

	    } else {
		profile.setAvatar(profileObj.getString("profile_image_url"));
	    }

	} else {
	    String path = "http://" + SystemConstant.domainName + "/"
		    + LocaleContextHolder.getLocale().toString()
		    + "/images/";

	    profile.setAvatar(path + "avatar-35px.png");
	}
	profile.setNickName(profileObj.getString("screen_name"));
	return profile;
    }

    @Override
    protected boolean isDenied(Map<String, String> params) {

	if (params.containsKey("error_code")
		&& "21330".equals(params.get("error_code"))) {

	    return true;
	}
	return false;
    }

    @Override
    protected boolean isError(Map<String, String> params) {

	if (params.containsKey("error_code")
		&& !"21330".equals(params.get("error_code"))) {

	    return true;
	}
	return false;
    }

}
