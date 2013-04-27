package me.twocoffee.service.event;

import org.springframework.context.ApplicationEvent;

public class SyncGuestRepositoryEvent extends ApplicationEvent {
	private String authorization;
	private String accountId;
	
	public SyncGuestRepositoryEvent(Object source,String authorization,String accountId) {
		super(source);
		this.authorization = authorization;
		this.accountId = accountId;
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
	
}
