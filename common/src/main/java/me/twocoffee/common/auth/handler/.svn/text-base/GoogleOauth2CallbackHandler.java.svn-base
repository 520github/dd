package me.twocoffee.common.auth.handler;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import me.twocoffee.common.BasicHttpClient;
import me.twocoffee.common.auth.AuthUtils;
import me.twocoffee.common.util.HttpClientUtils;
import me.twocoffee.entity.ThirdPartyProfile;
import net.oauth.OAuthConsumer;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author momo
 * 
 */
@Component("googleOauth2CallbackHandler")
public class GoogleOauth2CallbackHandler extends AbstractOauth2CallbackHandler {

	private static final Logger LOG = LoggerFactory
			.getLogger(SinaOauth2CallbackHandler.class);

	private static final String URL_USER = "https://www.googleapis.com/oauth2/v1/userinfo";

	@Override
	protected boolean isError(Map<String, String> params) {

		if (params.containsKey("error")) {
			return true;
		}
		return false;
	}

	@Override
	protected boolean isDenied(Map<String, String> params) {

		if (params.containsKey("error")) {
			return true;
		}
		return false;
	}

	@Override
	protected ThirdPartyProfile getProfile(JSONObject result,
			OAuthConsumer consumer) {

		if (!result.containsKey("access_token")) {
			throw new RuntimeException("oauth callback error!");
		}
		ThirdPartyProfile profile = new ThirdPartyProfile();
		Map<String, String> params1 = new HashMap<String, String>();
		params1.put("access_token", result.getString("access_token"));
		JSONObject profileObj = AuthUtils.invoke("Get", URL_USER,
					AuthUtils.GET_PARAM_TYPE_QUERYPARAM, params1);

		if (!profileObj.containsKey("id")) {
			LOG.error("oauth callback error! " + profileObj.toString());
			throw new RuntimeException("oauth callback error!");
		}
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> profileMap = null;

		try {
			profileMap = mapper.readValue(profileObj.toString(),
					new TypeReference<Map<String, String>>() {
					});

		} catch (JsonParseException e) {
			e.printStackTrace();
			throw new RuntimeException("oauth callback error!");

		} catch (JsonMappingException e) {
			e.printStackTrace();
			throw new RuntimeException("oauth callback error!");

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("oauth callback error!");
		}
		profile.setCreateDate(new Date());
		profile.setExpiredDate(null);
		profile.setProfile(profileMap);
		profile.setUserId(profileObj.getString("id"));
		profile.setRefreshToken("");
		profile.setToken(result.getString("access_token"));
		profile.setTokenSecret("");

		if (profileObj.containsKey("picture")) {

			if (StringUtils.isNotBlank(profileObj.getString("picture"))) {
				String url = HttpClientUtils.requestAndStoreResource(
						BasicHttpClient.getHttpClient(),
						profileObj.getString("picture"), fileOperator);

				if (url != null) {
					profile.setAvatar(url);

				} else {
					profile.setAvatar(profileObj.getString("picture"));
				}

			} else {
				profile.setAvatar(profileObj.getString("picture"));
			}
		}
		profile.setNickName(profileObj.getString("name"));
		return profile;
	}

}
