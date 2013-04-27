/**
 * 
 */
package me.twocoffee.common.auth.handler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import me.twocoffee.common.BasicHttpClient;
import me.twocoffee.common.auth.AuthUtils;
import me.twocoffee.common.constant.SystemConstant;
import me.twocoffee.common.util.HttpClientUtils;
import me.twocoffee.entity.ThirdPartyProfile;
import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;
import net.oauth.OAuthConsumer;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author momo
 * 
 */
@Component("tencentWeiboOauth2CallbackHandler")
public class TencentWeiboOauth2CallbackHandler extends
	AbstractOauth2CallbackHandler {

    private static final String URL_USER = "https://open.t.qq.com/api/user/info";

    private static final Logger LOG = LoggerFactory
	    .getLogger(TencentWeiboOauth2CallbackHandler.class);

    @Override
    protected ThirdPartyProfile getProfile(JSONObject result,
	    OAuthConsumer consumer) {

	if (result.containsKey("errorCode")) {
	    LOG.error("callback error!{}", result.toString());
	    throw new RuntimeException("callback error!");
	}
	String openId = result.getString("openid");
	Map<String, String> userParam = new HashMap<String, String>();
	userParam.put("format", "json");
	userParam.put("access_token", result.getString("access_token"));
	userParam.put("oauth_consumer_key", consumer.consumerKey);
	userParam.put("openid", openId);
	userParam.put("oauth_version", "2.a");
	JSONObject userObj = AuthUtils.invoke("Get", URL_USER,
		AuthUtils.GET_PARAM_TYPE_QUERYPARAM, userParam);

	if (userObj.containsKey("errorCode")) {
	    LOG.error("callback error!{}", userObj.toString());
	    throw new RuntimeException("callback error!");
	}
	ThirdPartyProfile profile = new ThirdPartyProfile();
	profile.setAccountType(ThirdPartyType.Tencent);
	profile.setCreateDate(new Date());
	profile.setExpiredDate(DateUtils.addSeconds(new Date(),
		result.getInt("expires_in")));

	profile.setRefreshToken(result.getString("refresh_token"));
	profile.setToken(result.getString("access_token"));
	profile.setUserId(openId);
	profile.setNickName(userObj.getJSONObject("data").getString("nick"));
	String userHead = userObj.getJSONObject("data").getString("head");

	if (!StringUtils.isBlank(userHead)) {
	    userHead = HttpClientUtils.requestAndStoreResource(
		    BasicHttpClient.getHttpClient(),
		    userObj.getJSONObject("data").getString("head") + "/180",
		    fileOperator);

	}
	if (StringUtils.isBlank(userHead)) {
	    userHead = "http://" + SystemConstant.domainName + "/"
		    + LocaleContextHolder.getLocale().toString()
		    + "/images/avatar-35px.png";
	}
	profile.setAvatar(userHead);

	// ObjectMapper mapper = new ObjectMapper(); try { Map<String, String>
	// profileMap = mapper.readValue( userObj.toString(), new
	// TypeReference<Map<String, String>>() { });
	//
	// Iterator<Entry<String, String>> keys = profileMap.entrySet()
	// .iterator();
	//
	// while (keys.hasNext()) { Entry<String, String> key = keys.next();
	//
	// if (key.getValue() == null || "".equals(key.getValue())) {
	// keys.remove(); } } profile.setProfile(profileMap);
	//
	// } catch (JsonParseException e) { e.printStackTrace();
	//
	// } catch (JsonMappingException e) { e.printStackTrace();
	//
	// } catch (IOException e) { e.printStackTrace(); }

	return profile;
    }

    @Override
    protected boolean isError(Map<String, String> params) {
	return false;
    }

    @Override
    protected boolean isDenied(Map<String, String> params) {
	return false;
    }
}
