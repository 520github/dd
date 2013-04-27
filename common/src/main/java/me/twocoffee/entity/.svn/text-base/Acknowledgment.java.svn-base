package me.twocoffee.entity;

import java.util.Date;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Index;
import com.google.code.morphia.annotations.Indexes;

/**
 * 答谢实体
 * @author deqiang.gu
 *
 */
@Entity(value = "acknowledgment", noClassnameStored = true)
@Indexes(value = { @Index("accountId, content")})
public class Acknowledgment {
   
	
	@Id
	private String id;
	
	
	/**
	 * 答谢方id
	 */
	private String from;
	
	
	/**
	 * 被答谢ID
	 */
	private String accountId;
	
	
	/**
	 * 内容ID
	 */
	private String content;
	
	/**
	 * 答谢礼物
	 *
	 */
	public static enum acknowledgeType{
		
		
	}

	/**
	 * 答谢 文字内容
	 */
	private String message;
	
	/**
	 * 答谢标题
	 */
	private String subject;
	
	public Date date;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	
	
	
	
	
}
