package me.twocoffee.service.event;

import org.springframework.context.ApplicationEvent;

public class AcceptInviteEvent extends ApplicationEvent {

	private static final long serialVersionUID = -4196248601471087812L;

	private String accountId;

	private String targetAccountId;

	public AcceptInviteEvent(Object source) {
		super(source);
	}

	public AcceptInviteEvent(Object source, String accountId,
			String targetAccountId) {
		super(source);
		this.accountId = accountId;
		this.targetAccountId = targetAccountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setTargetAccountId(String targetAccountId) {
		this.targetAccountId = targetAccountId;
	}

	public String getTargetAccountId() {
		return targetAccountId;
	}

}
