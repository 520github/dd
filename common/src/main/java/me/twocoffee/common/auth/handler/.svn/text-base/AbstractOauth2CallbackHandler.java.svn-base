/**
 * 
 */
package me.twocoffee.common.auth.handler;

import java.util.HashMap;
import java.util.Map;

import me.twocoffee.common.auth.AuthUtils;
import me.twocoffee.common.auth.CallbackHandler;
import me.twocoffee.common.constant.SystemConstant;
import me.twocoffee.common.dfs.FileOperator;
import me.twocoffee.entity.ThirdPartyProfile;
import net.oauth.OAuthConsumer;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author momo
 * 
 */
public abstract class AbstractOauth2CallbackHandler implements CallbackHandler {

	@Autowired
	protected FileOperator fileOperator;

	private String getCallbackURL(String callbackURL) {
		if (callbackURL == null)
			return callbackURL;
		if (callbackURL.indexOf("http:") >= 0)
			return callbackURL;
		return "http://" + SystemConstant.domainName + callbackURL;
	}

	protected void checkState(Map<String, String> params) {

		if (StringUtils.isBlank(params.get("state"))) {
			throw new RuntimeException("oauth callback error, state error!");
		}
	}

	protected JSONObject doCallback(OAuthConsumer consumer,
			Map<String, String> params) {

		Map<String, String> params1 = new HashMap<String, String>();
		params1.put("client_id", consumer.consumerKey);
		params1.put("client_secret", consumer.consumerSecret);
		params1.put("grant_type", "authorization_code");
		params1.put("code", params.get("code"));
		params1.put("redirect_uri",
				getCallbackURL(consumer.callbackURL));

		return AuthUtils.invoke("Post",
				consumer.serviceProvider.accessTokenURL,
				AuthUtils.POST_DATA_TYPE_FORM, params1);

	}

	protected abstract ThirdPartyProfile getProfile(JSONObject result,
			OAuthConsumer consumer);

	protected abstract boolean isDenied(Map<String, String> params);

	protected abstract boolean isError(Map<String, String> params);

	@Override
	public ThirdPartyProfile doHandler(OAuthConsumer consumer,
			Map<String, String> params) {

		if (isDenied(params)) {
			return null;
		}

		if (isError(params)) {
			throw new RuntimeException("oauth callback error!");
		}
		checkState(params);
		JSONObject result = doCallback(consumer, params);
		return getProfile(result, consumer);
	}

	public FileOperator getFileOperator() {
		return fileOperator;
	}

	public void setFileOperator(FileOperator fileOperator) {
		this.fileOperator = fileOperator;
	}
}
