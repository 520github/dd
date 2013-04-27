package me.twocoffee.service.event;

import org.springframework.context.ApplicationEvent;

/**
 * 发送好友请求事件
 * 
 * @author chongf
 * 
 */
public class SendFriendRequestEvent extends ApplicationEvent {

	private static final long serialVersionUID = 7089838336637107183L;

	private String accountId;

	private String targetAccountId;

	private String thirdType;

	public SendFriendRequestEvent(Object source) {
		super(source);
	}

	public SendFriendRequestEvent(Object source, String accountId,
			String targetAccountId, String thirdType) {
		super(source);
		this.accountId = accountId;
		this.targetAccountId = targetAccountId;
		this.thirdType = thirdType;
	}

	public String getAccountId() {
		return accountId;
	}

	public String getTargetAccountId() {
		return targetAccountId;
	}

	public String getThirdType() {
		return thirdType;
	}

	public void setThirdType(String thirdType) {
		this.thirdType = thirdType;
	}

}
