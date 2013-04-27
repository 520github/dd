package me.twocoffee.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.twocoffee.dao.WebTokenDao;
import me.twocoffee.entity.WebToken;
import me.twocoffee.entity.WebToken.UserType;
import me.twocoffee.service.WebTokenService;

@Service
public class WebTokenServiceImpl implements WebTokenService {
	@Autowired
	private WebTokenDao webTokenDao;
	
	@Override
	public void bindAccount(String id, String accountId) {
		webTokenDao.bindAccount(id, accountId);
	}
	
	public void bindAccount(String id,String accountId,UserType userType) {
		webTokenDao.bindAccount(id, accountId, userType);
	}

	@Override
	public WebToken createWebToken() {
		return webTokenDao.createWebToken();
	}
	
	@Override
	public WebToken createWebToken(String userAgent) {
		return webTokenDao.createWebToken(userAgent);
	}
	
	@Override
	public WebToken createWebToken(String userAgent,String url) {
		return webTokenDao.createWebToken(userAgent,url);
	}

	@Override
	public WebToken findById(String id) {
		return webTokenDao.findById(id);
	}

	@Override
	public void unbingAccount(String id) {
		webTokenDao.unbingAccount(id);
	}

}
