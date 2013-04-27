package me.twocoffee.dao.impl;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import me.twocoffee.common.BaseDao;
import me.twocoffee.dao.WebTokenDao;
import me.twocoffee.entity.WebToken;
import me.twocoffee.entity.WebToken.UserType;

@Repository
public class WebTokenDaoImpl extends BaseDao<WebToken> implements WebTokenDao {
	@Override
	public void bindAccount(String id, String accountId) {
		this.bindAccount(id, accountId, UserType.LoginDuoduo);
	}
	
	public void bindAccount(String id,String accountId,UserType userType) {
		if(id == null || id.trim().length() < 1) {
			return;
		}
		if(accountId == null || accountId.trim().length() < 1) {
			return;
		}
		WebToken webToken = this.getById(id);
		if(webToken == null) {
			return;
		}
		webToken.setUserType(userType);
		webToken.setAccountId(accountId);
		webToken.setLastModified(new Date());
		this.save(webToken);
	}

	@Override
	public WebToken createWebToken() {
		return createWebToken("");
	}
	@Override
	public WebToken createWebToken(String userAgent) {
		return createWebToken(userAgent,"");
	}
	@Override
	public WebToken createWebToken(String userAgent,String url) {
		WebToken webToken = new WebToken();
		webToken.setUrl(url);
		webToken.setUserType(UserType.Guest);
		webToken.setDate(new Date());
		webToken.setLastModified(webToken.getDate());
		webToken.setId(new ObjectId().toString());
		webToken.setUserAgent(userAgent);
		this.save(webToken);
		return webToken;
	}

	@Override
	public WebToken findById(String id) {
		return this.createQuery().filter("id", id).get();
	}

	@Override
	public void unbingAccount(String id) {
		if(id == null || id.trim().length() < 1) {
			return;
		}
		WebToken webToken = this.findById(id);
		if(webToken == null) {
			return;
		}
		webToken.setAccountId("");
		webToken.setLastModified(new Date());
		this.save(webToken);
	}

}
