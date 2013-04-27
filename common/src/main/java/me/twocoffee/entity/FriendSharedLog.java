package me.twocoffee.entity;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

/**
 * 好友分享日志
 * @author drizzt
 *
 */
@Entity(value = "friendSharedLog", noClassnameStored = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class FriendSharedLog {
	@Id
	private String id;
	
	/**
	 * 用户id
	 */
	@Indexed
	private String accountId;
	
	/**
	 * 好友ID列表
	 */
	private List<String> friendId;
	
	/**
	 * 分享时间
	 */
	private Date sharedTime;

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

	public List<String> getFriendId() {
		return friendId;
	}

	public void setFriendId(List<String> friendId) {
		this.friendId = friendId;
	}

	public Date getSharedTime() {
		return sharedTime;
	}

	public void setSharedTime(Date sharedTime) {
		this.sharedTime = sharedTime;
	}
}
