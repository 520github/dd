package me.twocoffee.common.auth;

import java.util.Map;

import me.twocoffee.entity.ThirdPartyProfile;
import net.oauth.OAuthConsumer;

/**
 * 回调处理
 * 
 * @author wenjian
 * 
 */
public interface CallbackHandler {

	public static final String KEY_CONSUMER = "consumer";

	public static final String KEY_OAUTH_TOKEN = "oauth_token";

	public static final String KEY_CLIENTOAUTH = "clientOauth";

	ThirdPartyProfile doHandler(OAuthConsumer consumer,
			Map<String, String> params);
}
