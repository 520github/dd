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
@Entity(value = "loginToken", noClassnameStored = true)
public class LoginToken {

	@Id
	private String id;

	private Date createDate;

	private String requestToken;

	private String token;

	private String tokenSecret;

	public static enum DisplayType {
		Web,
		Mobile
	}

	public Date getCreateDate() {
		return createDate;
	}

	public String getRequestToken() {
		return requestToken;
	}

	public String getToken() {
		return token;
	}

	public String getTokenSecret() {
		return tokenSecret;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setRequestToken(String requestToken) {
		this.requestToken = requestToken;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
