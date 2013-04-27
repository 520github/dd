package me.twocoffee.service.event;

import org.springframework.context.ApplicationEvent;

/**
 * 稍后读事件
 * 
 * @author chongf
 * 
 */
public class ReadLaterEvent extends ApplicationEvent {

	private static final long serialVersionUID = 7089838336637107183L;

	private String accountId;

	public ReadLaterEvent(Object source) {
		super(source);
	}

	public ReadLaterEvent(Object source, String accountId) {
		super(source);
		this.accountId = accountId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

}
