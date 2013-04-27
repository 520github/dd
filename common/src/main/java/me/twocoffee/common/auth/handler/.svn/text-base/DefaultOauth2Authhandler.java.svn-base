/**
 * 
 */
package me.twocoffee.common.auth.handler;

import java.io.IOException;

import me.twocoffee.common.auth.AuthHandler;
import me.twocoffee.common.constant.SystemConstant;
import me.twocoffee.entity.LoginToken.DisplayType;
import net.oauth.OAuth;
import net.oauth.OAuthConsumer;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author momo
 * 
 */
@Component("defaultOauth2Authhandler")
public class DefaultOauth2Authhandler implements AuthHandler {

    private static final Logger LOG = LoggerFactory
	    .getLogger(DefaultOauth2Authhandler.class);

    private String getCallbackURL(String callbackURL) {
	if (callbackURL == null)
	    return callbackURL;
	if (callbackURL.indexOf("http:") >= 0)
	    return callbackURL;
	return "http://" + SystemConstant.domainName + callbackURL;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * me.twocoffee.common.auth.AuthHandler#doHandler(net.oauth.OAuthAccessor,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public String doHandler(OAuthConsumer consumer, String state,
	    DisplayType type) {

	String authorizationURL = consumer.serviceProvider.userAuthorizationURL;
	String display = (String) consumer.getProperty("display.key");
	String displayValue = "";
	String scope = (String) consumer.getProperty("scope");
	String with_offical_account = (String) consumer
		.getProperty("with_offical_account");

	String forcelogin = (String) consumer
		.getProperty("forcelogin.key");

	String forceloginValue = (String) consumer
		.getProperty("forcelogin.value");

	if (forceloginValue == null || "".equals(forceloginValue)) {
	    forceloginValue = "false";
	}

	switch (type) {
	case Mobile:
	    displayValue = (String) consumer.getProperty("display.mobile");
	    break;
	case Web:
	default:
	    break;
	}

	try {
	    authorizationURL = OAuth.addParameters(authorizationURL,
		    "client_id", consumer.consumerKey, "redirect_uri",
		    getCallbackURL(consumer.callbackURL), "state", state,
		    "response_type",
		    "code");

	    if (StringUtils.isNotBlank(displayValue)) {
		authorizationURL = OAuth.addParameters(authorizationURL,
			display, displayValue);

	    }

	    if (StringUtils.isNotBlank(scope)) {
		authorizationURL = OAuth.addParameters(authorizationURL,
			"scope", scope);

	    }

	    if (StringUtils.isNotBlank(with_offical_account)) {
		authorizationURL = OAuth.addParameters(authorizationURL,
			"with_offical_account", with_offical_account);

	    }

	    if (StringUtils.isNotBlank(forcelogin)) {
		authorizationURL = OAuth.addParameters(authorizationURL,
			forcelogin, forceloginValue);

	    }

	} catch (IOException e) {
	    LOG.error("error when get authrizationurl!", e);
	}
	return authorizationURL;
    }
}
