/**
 * 
 */
package me.twocoffee.dao.impl;

import me.twocoffee.common.BaseDao;
import me.twocoffee.common.auth.TokenManager;
import me.twocoffee.entity.LoginToken;

/**
 * @author momo
 * 
 */
@org.springframework.stereotype.Repository
public class LoginTokenDaoImpl extends BaseDao<LoginToken> implements
		TokenManager<LoginToken> {

	@Override
	public void store(String key, LoginToken obj) {

	}

	@Override
	public LoginToken getToken(String key) {
		return null;
	}

}
