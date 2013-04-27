package me.twocoffee.entity;

import java.util.Date;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

@Entity(value = "authToken", noClassnameStored = true)
public class AuthToken {

	/* 账户id */
	@Indexed
	private String accountId;
	/* 创建时间 */
	private Date date;

	@Id
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

	public AuthToken setAccountId(String accountId) {
		this.accountId = accountId;
		return this;
	}

	public AuthToken setDate(Date date) {
		this.date = date;
		return this;
	}

	public void setId(String id) {
		this.id = id;
	}
}
