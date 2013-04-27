package me.twocoffee.entity;

import java.util.Date;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity(value = "feedback", noClassnameStored = true)
public class Feedback {

	@Id
	private String id;

	private String accountId;

	private String message;

	private Date createDate;

	public String getAccountId() {
		return accountId;
	}

	public Date getCreateDate() {
		return createDate;
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

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
