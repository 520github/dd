package me.twocoffee.service.event;

import org.springframework.context.ApplicationEvent;

public class UnbindAccountEvent extends ApplicationEvent {
	private String authorization;
	
	public UnbindAccountEvent(Object source,String authorization) {
		super(source);
		this.authorization = authorization;
	}

	public String getAuthorization() {
		return authorization;
	}

	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}
	
}
