package me.twocoffee.service.event;

import org.springframework.context.ApplicationEvent;

public class AcceptFriendEvent extends ApplicationEvent{

	private String accountId;

	private String targetAccountId;
	
	public AcceptFriendEvent(Object source,String accountId,String targetAccountId) {
		super(source);
		this.accountId = accountId;
		this.targetAccountId = targetAccountId;
		
		
		
		
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getTargetAccountId() {
		return targetAccountId;
	}

	public void setTargetAccountId(String targetAccountId) {
		this.targetAccountId = targetAccountId;
	}

	
	
}
