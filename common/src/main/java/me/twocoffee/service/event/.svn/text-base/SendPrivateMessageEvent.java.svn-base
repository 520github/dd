package me.twocoffee.service.event;

import me.twocoffee.entity.PrivateMessageSession;

import org.springframework.context.ApplicationEvent;

public class SendPrivateMessageEvent extends ApplicationEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String accountId;

	private String targetAccountId;

	private PrivateMessageSession sourceSession;

	private PrivateMessageSession targetSession;

	private boolean sendCommentNotify;

	public SendPrivateMessageEvent(Object source) {
		super(source);
	}

	public SendPrivateMessageEvent(Object source, String accountId,
			String targetAccountId, PrivateMessageSession sourceSession,
			PrivateMessageSession targetSession, boolean sendCommentNotify) {

		super(source);
		this.accountId = accountId;
		this.targetAccountId = targetAccountId;
		this.sourceSession = sourceSession;
		this.targetSession = targetSession;
		this.sendCommentNotify = sendCommentNotify;
	}

	public String getAccountId() {
		return accountId;
	}

	public PrivateMessageSession getSourceSession() {
		return sourceSession;
	}

	public String getTargetAccountId() {
		return targetAccountId;
	}

	public PrivateMessageSession getTargetSession() {
		return targetSession;
	}

	public boolean isSendCommentNotify() {
		return sendCommentNotify;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public void setSendCommentNotify(boolean sendCommentNotify) {
		this.sendCommentNotify = sendCommentNotify;
	}

	public void setSourceSession(PrivateMessageSession sourceSession) {
		this.sourceSession = sourceSession;
	}

	public void setTargetAccountId(String targetAccountId) {
		this.targetAccountId = targetAccountId;
	}

	public void setTargetSession(PrivateMessageSession targetSession) {
		this.targetSession = targetSession;
	}

}
