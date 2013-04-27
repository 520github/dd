/**
 * 
 */
package me.twocoffee.common.auth.handler;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import me.twocoffee.common.auth.AuthUtils;
import me.twocoffee.entity.ThirdPartyProfile;
import net.oauth.OAuthConsumer;
import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Component;

/**
 * @author momo
 * 
 */
@Component("facebookOauth2CallbackHandler")
public class FacebookOauth2CallbackHandler extends
		AbstractOauth2CallbackHandler {

	private static final String url = " https://graph.facebook.com/me?access_token=";

	protected ThirdPartyProfile getProfile(JSONObject result,
			OAuthConsumer consumer) {

		JSONObject profileInfo = AuthUtils.invoke("Get",
				url + result.getString("access_token"),
				AuthUtils.GET_PARAM_TYPE_QUERYPARAM, null);

		ThirdPartyProfile profile = new ThirdPartyProfile();
		profile.setCreateDate(new Date());
		profile.setExpiredDate(null);
		profile.setRefreshToken("");
		profile.setToken(result.getString("token"));
		profile.setUserId(profileInfo.getString("userId"));
		ObjectMapper mapper = new ObjectMapper();

		try {
			Map<String, String> profileMap = mapper.readValue(
					profileInfo.toString(),

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
			throw new RuntimeException("get third party profile error", e);

		} catch (JsonMappingException e) {
			e.printStackTrace();
			throw new RuntimeException("get third party profile error", e);

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("get third party profile error", e);
		}
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
