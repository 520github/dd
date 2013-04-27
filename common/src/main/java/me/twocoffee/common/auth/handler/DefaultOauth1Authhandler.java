/**
 * 
 */
package me.twocoffee.common.auth.handler;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import me.twocoffee.common.auth.AuthHandler;
import me.twocoffee.common.auth.AuthUtils;
import me.twocoffee.common.auth.TokenManager;
import me.twocoffee.common.constant.SystemConstant;
import me.twocoffee.entity.LoginToken;
import me.twocoffee.entity.LoginToken.DisplayType;
import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthException;
import net.oauth.OAuthMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author momo
 * 
 */
@Component("defaultOauth1Authhandler")
public class DefaultOauth1Authhandler implements AuthHandler {

	@Autowired
	private TokenManager<LoginToken> tokenManager;

	public TokenManager<LoginToken> getTokenManager() {
		return tokenManager;
	}

	public void setTokenManager(TokenManager<LoginToken> tokenManager) {
		this.tokenManager = tokenManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * me.twocoffee.common.auth.AuthHandler#doHandler(net.oauth.OAuthConsumer,
	 * java.lang.String)
	 */
	@Override
	public String doHandler(OAuthConsumer consumer, String state,
			DisplayType type) {
		OAuthAccessor accessor = new OAuthAccessor(consumer);
		String callbackURL = consumer.callbackURL;
		callbackURL = getCallbackURL(callbackURL);
		
		List<OAuth.Parameter> parameters = OAuth.newList(
				OAuth.OAUTH_CALLBACK, callbackURL);

		String authorizationURL = consumer.serviceProvider.userAuthorizationURL;

		try {
			OAuthMessage response = AuthUtils.getRequestTokenResponse(
					accessor, parameters);

			String token = UUID.randomUUID().toString().replace("-", "");
			authorizationURL = OAuth.addParameters(authorizationURL,
					OAuth.OAUTH_TOKEN, accessor.requestToken, "token", token);

			if (response.getParameter(OAuth.OAUTH_CALLBACK_CONFIRMED) == null) {
				authorizationURL = OAuth.addParameters(authorizationURL,
						OAuth.OAUTH_CALLBACK, callbackURL);

			}
			LoginToken loginToken = new LoginToken();
			loginToken.setCreateDate(new Date());
			loginToken.setRequestToken(accessor.requestToken);
			loginToken.setToken(token);
			loginToken.setTokenSecret(accessor.tokenSecret);
			tokenManager.store(token, loginToken);

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);

		} catch (OAuthException e) {
			e.printStackTrace();
			throw new RuntimeException(e);

		} catch (URISyntaxException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return authorizationURL;
	}
	
	private String getCallbackURL(String callbackURL) {
		if(callbackURL == null)return callbackURL;
		if(callbackURL.indexOf("http:") >0)return callbackURL;
		return "http://" + SystemConstant.domainName + callbackURL;
	}

}
