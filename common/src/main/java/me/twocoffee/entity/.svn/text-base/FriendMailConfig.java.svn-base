package me.twocoffee.entity;

import java.util.List;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

/**
 * 邮箱白名单
 * @author drizzt
 *
 */
@Entity(value = "friendMailConfig", noClassnameStored = true)
public class FriendMailConfig {
	@Id
	private String id;
	
	/**
	 * 帐号id
	 */
	@Indexed
	private String accountId;
	
	/**
	 * 好友帐号id
	 */
	@Indexed
	private String friendId;
	
	/**
	 * 是否阻止
	 */
	private boolean isBlocked;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getFriendId() {
		return friendId;
	}

	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}

	public boolean isBlocked() {
		return isBlocked;
	}

	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}
	
	
}
