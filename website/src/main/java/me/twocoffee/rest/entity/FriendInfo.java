package me.twocoffee.rest.entity;

public class FriendInfo {

	private boolean isFriend;

	private int shared;

	private String alias;

	private String aliasInPinyin;

	private boolean frequently;

	public String getAlias() {
		return alias;
	}

	public String getAliasInPinyin() {
		return aliasInPinyin;
	}

	public boolean getIsFriend() {
		return isFriend;
	}

	public int getShared() {
		return shared;
	}

	public boolean isFrequently() {
		return frequently;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void setAliasInPinyin(String aliasInPinyin) {
		this.aliasInPinyin = aliasInPinyin;
	}

	public void setFrequently(boolean frequently) {
		this.frequently = frequently;
	}

	public void setFriend(boolean isFriend) {
		this.isFriend = isFriend;
	}

	public void setIsFriend(boolean isFriend) {
		this.isFriend = isFriend;
	}

	public void setShared(int shared) {
		this.shared = shared;
	}
}
