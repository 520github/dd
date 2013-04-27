/**
 * 
 */
package me.twocoffee.common.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import me.twocoffee.entity.LoginToken.DisplayType;
import me.twocoffee.entity.ThirdPartyProfile;
import net.oauth.ConsumerProperties;
import net.oauth.OAuthConsumer;

import org.apache.commons.lang.StringUtils;

/**
 * @author momo
 * 
 */
public class AuthClient {

	private ConsumerProperties consumerProperties;

	private Map<String, AuthHandler> authHandlers = new HashMap<String, AuthHandler>();

	public Map<String, AuthHandler> getAuthHandlers() {
		return authHandlers;
	}

	public void setAuthHandlers(Map<String, AuthHandler> authHandlers) {
		this.authHandlers = authHandlers;
	}

	private Map<String, CallbackHandler> callbackHandlers = new HashMap<String, CallbackHandler>();

	public Map<String, CallbackHandler> getCallbackHandlers() {
		return callbackHandlers;
	}

	public void setCallbackHandlers(
			Map<String, CallbackHandler> callbackHandlers) {

		this.callbackHandlers = callbackHandlers;
	}

	public AuthClient() {

		try {
			consumerProperties = new ConsumerProperties(
					"consumer.properties", AuthClient.class.getClassLoader());

		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	public String getAuthLocation(String consumerStr, String state,
			DisplayType type) {

		OAuthConsumer consumer = null;

		try {
			consumer = consumerProperties.getConsumer(consumerStr);
			AuthHandler authHandler = authHandlers.get(consumerStr);
			return authHandler.doHandler(consumer, state, type);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ConsumerProperties getConsumerProperties() {
		return consumerProperties;
	}

	public void setConsumerProperties(ConsumerProperties consumerProperties) {
		this.consumerProperties = consumerProperties;
	}

	public ThirdPartyProfile doCallback(String consumerStr, String callbackUrl) {

		if (StringUtils.isNotBlank(callbackUrl)) {
			Map<String, String> params = AuthUtils
					.getCallbackParams(callbackUrl);

			OAuthConsumer consumer = null;

			try {
				consumer = consumerProperties.getConsumer(consumerStr);
				CallbackHandler handler = callbackHandlers.get(consumerStr);
				return handler.doHandler(consumer, params);

			} catch (Exception e) {
				throw new RuntimeException(e);
			}

		}
		throw new RuntimeException(
				"oauth callback error! callbackUrl is empty!");

	}
}
