package me.twocoffee.dao.impl;

import java.util.Date;

import me.twocoffee.common.BaseDao;
import me.twocoffee.dao.AuthTokenDao;
import me.twocoffee.entity.AuthToken;

import org.bson.types.ObjectId;

@org.springframework.stereotype.Repository
public class AuthTokenDaoImpl extends BaseDao<AuthToken> implements
	AuthTokenDao {

    @Override
    public AuthToken createToken(String accountId) {
	AuthToken authToken = new AuthToken();
	authToken.setAccountId(accountId);
	authToken.setDate(new Date());
	authToken.setId(new ObjectId().toString());

	this.save(authToken);
	return authToken;
    }

    @Override
    public AuthToken findByAccountId(String accountId) {
	return createQuery().filter("accountId =", accountId).get();
    }

    @Override
    public AuthToken findById(String id) {
	return this.getById(id);
    }

}
