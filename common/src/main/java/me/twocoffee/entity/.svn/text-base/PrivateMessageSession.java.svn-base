package me.twocoffee.entity;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Index;
import com.google.code.morphia.annotations.Indexes;

/**
 * 悄悄话会话
 * 
 * @author drizzt
 * 
 */
@Entity(value = "privateMessageSession", noClassnameStored = true)
@JsonIgnoreProperties(value = { "owneraccountId" })
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Indexes(value = { @Index("owneraccountId, contentId, accountId"),
		@Index("owneraccountId, -lastUpdate.date"),
		@Index("owneraccountId, contentId, -lastUpdate.date") })
public class PrivateMessageSession {

	public static class LastUpdate {
		private String message; // 消息的内容
		private Date date; // 消息的时间
		private String accountId; // 发送者的account id
		private String id;

		public String getAccountId() {
			return accountId;
		}

		public Date getDate() {
			return date;
		}

		public String getId() {
			return id;
		}

		public String getMessage() {
			return message;
		}

		public void setAccountId(String accountId) {
			this.accountId = accountId;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		public void setId(String id) {
			this.id = id;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}

	@Id
	private String id;
	private String owneraccountId; // 所属帐号ID
	private String accountId; // 对方的帐户ID
	private String contentId; // 话题所对应的内容ID
	private LastUpdate lastUpdate; // 会话中的最后一句话
	private int unread;
	private int messageCount; // 会话中聊天记录的条数

	/**
	 * 返回互换了所属帐号和目标帐号的会话对象
	 * 
	 * @return
	 */
	public PrivateMessageSession changeDirection() {
		PrivateMessageSession newSession = new PrivateMessageSession();
		newSession.setAccountId(owneraccountId);
		newSession.setContentId(contentId);
		newSession.setMessageCount(messageCount);
		// newSession.setLastUpdate(lastUpdate);
		newSession.setOwneraccountId(accountId);
		return newSession;
	}

	public String getAccountId() {
		return accountId;
	}

	public String getContentId() {
		return contentId;
	}

	public String getId() {
		return id;
	}

	public LastUpdate getLastUpdate() {
		return lastUpdate;
	}

	public int getMessageCount() {
		return messageCount;
	}

	public String getOwneraccountId() {
		return owneraccountId;
	}

	public int getUnread() {
		return unread;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLastUpdate(LastUpdate lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public void setMessageCount(int messageConut) {
		this.messageCount = messageConut;
	}

	public void setOwneraccountId(String owneraccountId) {
		this.owneraccountId = owneraccountId;
	}

	public void setUnread(int unread) {
		this.unread = unread;
	}
}
