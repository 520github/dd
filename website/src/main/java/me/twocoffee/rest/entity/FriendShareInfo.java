package me.twocoffee.rest.entity;

public class FriendShareInfo {

	private ThirdParty[] thirdparty;

	private int score;

	private String comment;

	private AccountId[] receipt;

	public String getComment() {
		return comment;
	}

	public AccountId[] getReceipt() {
		return receipt;
	}

	public int getScore() {
		return score;
	}

	public ThirdParty[] getThirdparty() {
		return thirdparty;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setReceipt(AccountId[] receipt) {
		this.receipt = receipt;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void setThirdparty(ThirdParty[] thirdparty) {
		this.thirdparty = thirdparty;
	}

}
