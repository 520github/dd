/**
 * 
 */
package me.twocoffee.rest.entity;

/**
 * @author momo
 * 
 */
public class CounterInfo {

	/**
	 * 公共分享数
	 */
	private int sharedInCommunity;

	private int friends;
	
	/**
	 * 好友答谢数
	 */
	private int acknowledgment;

	public CounterInfo() {
	}


	
	
	public CounterInfo(int sharedInCommunity, int friends, int acknowledgment) {
		super();
		this.sharedInCommunity = sharedInCommunity;
		this.friends = friends;
		this.acknowledgment = acknowledgment;
	}

	public int getFriends() {
		return friends;
	}

	public int getSharedInCommunity() {
		return sharedInCommunity;
	}

	public void setFriends(int friends) {
		this.friends = friends;
	}

	public void setSharedInCommunity(int sharedInCommunity) {
		this.sharedInCommunity = sharedInCommunity;
	}

	public int getAcknowledgment() {
		return acknowledgment;
	}

	public void setAcknowledgment(int acknowledgment) {
		this.acknowledgment = acknowledgment;
	}
	
}
