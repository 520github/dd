/**
 * 
 */
package me.twocoffee.entity;

import java.util.Date;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

/**
 * @author momo
 * 
 */
@Entity(value = "userAgentLog", noClassnameStored = true)
public class UserAgentLog {

	@Id
	private String id;

	private String account;

	private String userAgent;

	private Date createDate;

	public String getAccount() {
		return account;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public String getId() {
		return id;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
}
