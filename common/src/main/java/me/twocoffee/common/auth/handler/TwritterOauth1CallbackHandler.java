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
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthMessage;
import net.oauth.ParameterStyle;
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
@Component("twritterOauth1CallbackHandler")
public class TwritterOauth1CallbackHandler extends
		AbstractOauth1CallbackHandler {

	private static final String URL = "https://api.twitter.com/1/account/verify_credentials.json";

	@Override
	protected ThirdPartyProfile getProfile(JSONObject result,
			OAuthConsumer consumer) {

		OAuthMessage request1 = null;
		String responseBody = null;
		OAuthAccessor accessor = new OAuthAccessor(consumer);

		try {
			request1 = accessor.newRequestMessage("get", URL, null);
			OAuthMessage response1 = null;
			response1 = AuthUtils.invoke(request1,
					ParameterStyle.AUTHORIZATION_HEADER);

			responseBody = response1.readBodyAsString();

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		JSONObject jsonObj = JSONObject.fromObject(responseBody);
		String uid = jsonObj.getString("id");
		ThirdPartyProfile profile = new ThirdPartyProfile();
		profile.setCreateDate(new Date());
		profile.setExpiredDate(null);
		profile.setRefreshToken("");
		profile.setToken(result.getString("token"));
		profile.setTokenSecret(result.getString("secret"));
		profile.setUserId(uid);
		ObjectMapper mapper = new ObjectMapper();

		try {
			Map<String, String> profileMap = mapper.readValue(responseBody,
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

}
