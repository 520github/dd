package me.twocoffee.service.event;

import me.twocoffee.entity.WebToken.UserType;

import org.springframework.context.ApplicationEvent;

public class BindAccountEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;
	private String authorization;
	private String accountId;
	private UserType userType;
	
	public BindAccountEvent(Object source,String authorization,String accountId,UserType userType) {
		super(source);
		this.accountId = accountId;
		this.authorization = authorization;
		this.userType = userType;
	}

	public String getAuthorization() {
		return authorization;
	}

	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}
	
}
