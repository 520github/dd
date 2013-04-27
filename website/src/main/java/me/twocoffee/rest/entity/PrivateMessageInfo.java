/**
 * 
 */
package me.twocoffee.rest.entity;

/**
 * @author momo
 * 
 */
public class PrivateMessageInfo {

	private String[] accountId;

	private String contentId;

	private String message;

	private String uuid;

	public String[] getAccountId() {
		return accountId;
	}

	public String getContentId() {
		return contentId;
	}

	public String getMessage() {
		return message;
	}

	public String getUuid() {
		return uuid;
	}

	public void setAccountId(String[] accountId) {
		this.accountId = accountId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
