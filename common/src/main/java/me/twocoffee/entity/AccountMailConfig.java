package me.twocoffee.entity;

import java.util.Date;
import java.util.List;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

/**
 * 个人邮箱设置
 * @author drizzt
 *
 */
@Embedded
public class AccountMailConfig {
	/**
	 * 是否好友可见
	 */
	private boolean friendsVisible;
	
	/**
	 * 存入的tag，从SystemTagEnum中取得
	 */
	private List<String> inTags;
	
	/**
	 * mail地址
	 */
	private List<String> mails;
	
	/**
	 * 最后更新时间
	 */
	private Date lastUpated;

	public boolean isFriendsVisible() {
		return friendsVisible;
	}

	public void setFriendsVisible(boolean friendsVisible) {
		this.friendsVisible = friendsVisible;
	}

	public List<String> getInTags() {
		return inTags;
	}

	public void setInTags(List<String> inTags) {
		this.inTags = inTags;
	}

	public List<String> getMails() {
		return mails;
	}

	public void setMails(List<String> mails) {
		this.mails = mails;
	}

	public Date getLastUpated() {
		return lastUpated;
	}

	public void setLastUpated(Date lastUpated) {
		this.lastUpated = lastUpated;
	}
	
	
}
