package me.twocoffee.entity;

import me.twocoffee.rest.entity.AccountInfo;

public class ResponseUser {

	private String authToken;

	private AccountInfo account;

	public ResponseUser() {
		this.setAccount(new AccountInfo());
	}

	public ResponseUser(String authToken, AccountInfo account) {
		this.setAccount(account);
		this.setAuthToken(authToken);
	}

	public AccountInfo getAccount() {
		return account;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAccount(AccountInfo account) {
		this.account = account;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
}
