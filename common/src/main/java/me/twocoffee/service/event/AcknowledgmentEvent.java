package me.twocoffee.service.event;

import org.springframework.context.ApplicationEvent;

public class AcknowledgmentEvent extends ApplicationEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1793078874380800564L;

	private String accountId;
	
	private String targetAccountId;
	
	private String contentId;
	
	
	public AcknowledgmentEvent(Object source, String accountId,
			String targetAccountId, String contentId) {
		super(source);
		this.accountId = accountId;
		this.targetAccountId = targetAccountId;
		this.contentId = contentId;
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
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	
	

}
