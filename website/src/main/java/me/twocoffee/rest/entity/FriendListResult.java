package me.twocoffee.rest.entity;

import java.util.List;

public class FriendListResult {
	public FriendListResult() {
		
	}
	
	private AccountInfo[] result;
	
	private FriendInfo[] friend;

	public AccountInfo[] getResult() {
		return result;
	}

	public void setResult(AccountInfo[] result) {
		this.result = result;
	}

	public FriendInfo[] getFriend() {
		return friend;
	}

	public void setFriend(FriendInfo[] friend) {
		this.friend = friend;
	}
}
