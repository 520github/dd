package me.twocoffee.service.event;

import org.springframework.context.ApplicationEvent;

public class RecommendedByFriendEvent extends ApplicationEvent {
	private static final long serialVersionUID = 7089838323423107183L;

	private String accountId;

	private String targetAccountId;
	
	private String contentId;

	public RecommendedByFriendEvent(Object source) {
		super(source);
	}

	public RecommendedByFriendEvent(Object source, String accountId,
			String targetAccountId, String contentId) {
		super(source);
		this.accountId = accountId;
		this.targetAccountId = targetAccountId;
		this.contentId = contentId;
	}

	public String getAccountId() {
		return accountId;
	}

	public String getTargetAccountId() {
		return targetAccountId;
	}

	public String getContentId() {
		return contentId;
	}

}
