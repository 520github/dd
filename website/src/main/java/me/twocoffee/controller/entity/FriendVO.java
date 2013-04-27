package me.twocoffee.controller.entity;

import java.util.Date;

public class FriendVO {
	private AccountVO account;

	private Date created;

	private String favorite;

	private String remarkName;
	
	public String getRemarkName() {
		return remarkName;
	}

	public void setRemarkName(String remarkName) {
		this.remarkName = remarkName;
	}

	private boolean myFriend;

	private int sharedToMe;

	public AccountVO getAccount() {
		return account;
	}

	public Date getCreated() {
		return created;
	}

	public String getFavorite() {
		return favorite;
	}

	public int getSharedToMe() {
		return sharedToMe;
	}

	public boolean isMyFriend() {
		return myFriend;
	}

	public void setAccount(AccountVO account) {
		this.account = account;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public void setFavorite(String favorite) {
		this.favorite = favorite;
	}

	public void setMyFriend(boolean myFriend) {
		this.myFriend = myFriend;
	}

	public void setSharedToMe(int sharedToMe) {
		this.sharedToMe = sharedToMe;
	}

}
