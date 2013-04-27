package me.twocoffee.entity;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Index;
import com.google.code.morphia.annotations.Indexes;

/**
 * 悄悄话
 * 
 * @author drizzt
 * 
 */
@Entity(value = "privateMessage", noClassnameStored = true)
@JsonIgnoreProperties(value = { "sessionId", "toAccountId" })
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Indexes(value = { @Index("sessionId, uuid"),
		@Index("sessionId, id") })
public class PrivateMessage {
	@Id
	private String id;

	/**
	 * 会话id
	 */
	private String sessionId;

	/**
	 * 发消息帐号id
	 */
	private String accountId;

	/**
	 * 接消息帐号id
	 */
	private String toAccountId;

	/**
	 * 发送时间
	 */
	private Date date;

	/**
	 * 消息
	 */
	private String message;

	/**
	 * 客户端生成的唯一标识，表示一个会话内的消息。用于判断客户端是否重复发送
	 */
	private String uuid;

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

	public String getSessionId() {
		return sessionId;
	}

	public String getToAccountId() {
		return toAccountId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setAccountId(String fromAccountId) {
		this.accountId = fromAccountId;
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

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public void setToAccountId(String toAccountId) {
		this.toAccountId = toAccountId;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
