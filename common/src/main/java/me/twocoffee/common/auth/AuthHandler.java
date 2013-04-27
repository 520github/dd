package me.twocoffee.common.auth;

import me.twocoffee.entity.LoginToken.DisplayType;
import net.oauth.OAuthConsumer;

/**
 * 用户授权处理
 * 
 * @author wenjian
 * 
 */
public interface AuthHandler {

	static final int CALLBACK_TYPE_NONE = 0;

	static final int CALLBACK_TYPE_SYSN = 1;

	static final int CALLBACK_TYPE_ASYSN = 2;

	String doHandler(OAuthConsumer consumer, String state, DisplayType type);

}
