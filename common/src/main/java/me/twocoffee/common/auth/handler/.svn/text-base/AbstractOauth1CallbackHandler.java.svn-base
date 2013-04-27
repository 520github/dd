/**
 * 
 */
package me.twocoffee.common.auth.handler;

import java.util.List;
import java.util.Map;

import me.twocoffee.common.auth.AuthUtils;
import me.twocoffee.common.auth.CallbackHandler;
import me.twocoffee.common.auth.TokenManager;
import me.twocoffee.entity.LoginToken;
import me.twocoffee.entity.ThirdPartyProfile;
import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

/**
 * @author momo
 * 
 */
public abstract class AbstractOauth1CallbackHandler implements CallbackHandler {

	private TokenManager<LoginToken> tokenManager;

	public TokenManager<LoginToken> getTokenManager() {
		return tokenManager;
	}

	public void setTokenManager(TokenManager<LoginToken> tokenManager) {
		this.tokenManager = tokenManager;
	}

	@Override
	public ThirdPartyProfile doHandler(OAuthConsumer consumer,
			Map<String, String> params) {

		JSONObject result = doCallback(consumer, params);
		return getProfile(result, consumer);
	}

	protected JSONObject doCallback(OAuthConsumer consumer,
			Map<String, String> params) {

		String qtoken = params.get("oauth_token");
		String token = params.get("token");
		JSONObject obj = new JSONObject();

		if (StringUtils.isBlank(qtoken) || StringUtils.isBlank(token)) {
			new RuntimeException("oauth callback exeception!");
		}
		OAuthAccessor accessor = new OAuthAccessor(consumer);
		LoginToken loginToken = tokenManager.getToken(token);
		accessor.requestToken = loginToken.getRequestToken();
		accessor.tokenSecret = loginToken.getTokenSecret();
		String a = params.get("clientOauth");
		List<OAuth.Parameter> parameters = null;
		String verifier = a.substring(a.indexOf("=") + 1);

		if (verifier != null) {
			parameters = OAuth.newList(OAuth.OAUTH_VERIFIER, verifier);
		}

		try {
			AuthUtils.getAccessToken(accessor,
						parameters);

			if (StringUtils.isBlank(accessor.accessToken)
					|| StringUtils.isBlank(accessor.tokenSecret)) {

				throw new RuntimeException(
						"oauth callback error!no access token");

			}
			accessor.requestToken = null;
			obj.put("token", accessor.accessToken);
			obj.put("secret", accessor.tokenSecret);

		} catch (Exception t) {
			new RuntimeException(t);
		}
		return obj;
	}

	protected abstract ThirdPartyProfile getProfile(JSONObject result,
			OAuthConsumer consumer);
}
