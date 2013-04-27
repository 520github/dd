package me.twocoffee.service.impl;

import me.twocoffee.dao.AuthTokenDao;
import me.twocoffee.entity.AuthToken;
import me.twocoffee.service.AuthTokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthTokenServiceImpl implements AuthTokenService {

	@Autowired
	private AuthTokenDao authTokenDao = null;

	@Override
	public AuthToken createToken(String accountId) {
		return authTokenDao.createToken(accountId);
	}

	@Override
	public AuthToken findById(String id) {
		return authTokenDao.findById(id);
	}

	@Override
	public AuthToken findByAccountId(String accountId) {
		return authTokenDao.findByAccountId(accountId);
	}

}
