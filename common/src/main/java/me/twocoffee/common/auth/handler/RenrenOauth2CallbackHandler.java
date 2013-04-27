/**
 * 
 */
package me.twocoffee.common.auth.handler;

import java.util.Date;
import java.util.Map;

import me.twocoffee.common.BasicHttpClient;
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
 * @author wenjian
 * 
 */
@Component
public class RenrenOauth2CallbackHandler extends AbstractOauth2CallbackHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(RenrenOauth2CallbackHandler.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.twocoffee.common.auth.handler.AbstractOauth2CallbackHandler#getProfile
	 * (net.sf.json.JSONObject, net.oauth.OAuthConsumer)
	 */
	@Override
	protected ThirdPartyProfile getProfile(JSONObject result,
			OAuthConsumer consumer) {

		try {
			ThirdPartyProfile profile = new ThirdPartyProfile();
			profile.setAccountType(ThirdPartyType.Renren);
			profile.setCreateDate(new Date());

			if (result.getJSONObject("user").containsKey("avatar")) {
				
				if (result.getJSONObject("user").getJSONArray("avatar").isArray()) {

					for (int i = 0; i < result.getJSONObject("user")
							.getJSONArray("avatar").size(); i++) {

						if (result.getJSONObject("user").getJSONArray("avatar")
								.getJSONObject(i).getString("type")
								.equals("avatar")) {

							if (StringUtils.isNotBlank(result
									.getJSONObject("user")
									.getJSONArray("avatar").getJSONObject(i)
									.getString("url"))) {

								String url = HttpClientUtils
										.requestAndStoreResource(
												BasicHttpClient.getHttpClient(),
												result.getJSONObject("user")
														.getJSONArray("avatar")
														.getJSONObject(i)
														.getString("url"),
												fileOperator);

								if (url != null) {
									profile.setAvatar(url);

								} else {
									profile.setAvatar(result
											.getJSONObject("user")
											.getJSONArray("avatar")
											.getJSONObject(i).getString("url"));
								}
							}
							break;
						}
					}
				}
			}
			
			if (StringUtils.isBlank(profile.getAvatar())) {
				String path = "http://"
						+ SystemConstant.domainName
						+ "/"
						+ LocaleContextHolder.getLocale()
								.toString() + "/images/";

				profile.setAvatar(path + "avatar-35px.png");
			}
			profile.setExpiredDate(DateUtils.addSeconds(new Date(), result.getInt("expires_in")));
			profile.setNickName(result.getJSONObject("user").getString("name"));
			profile.setRefreshToken(result.getString("refresh_token"));
			profile.setToken(result.getString("access_token"));
			profile.setUserId(result.getJSONObject("user").getString("id"));
			return profile;

		} catch (Exception e) {
			logger.error("renren user auth error! can not get profile!", e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.twocoffee.common.auth.handler.AbstractOauth2CallbackHandler#isDenied
	 * (java.util.Map)
	 */
	@Override
	protected boolean isDenied(Map<String, String> params) {
		//TODO:check error code
		if ("login_denied".equals(params.get("error"))) {
			logger.debug("renren user denied auth!");
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.twocoffee.common.auth.handler.AbstractOauth2CallbackHandler#isError
	 * (java.util.Map)
	 */
	@Override
	protected boolean isError(Map<String, String> params) {

		if (params.containsKey("error")) {
			logger.debug("renren user auth error!");
			return true;
		}
		return false;
	}

}
