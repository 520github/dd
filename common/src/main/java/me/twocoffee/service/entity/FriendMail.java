/**
 * 
 */
package me.twocoffee.service.entity;

import java.util.List;

import me.twocoffee.entity.FriendMailConfig;

/**
 * 好友邮箱地址
 * 
 * @author xuehui.miao
 *
 */
public class FriendMail extends FriendMailConfig{
	/**
	 * 好友mail地址
	 */
	private List<String> friendMails;
	
	/**
	 * 昵称
	 */
	private String name;
	

	public List<String> getFriendMails() {
		return friendMails;
	}

	public void setFriendMails(List<String> friendMails) {
		this.friendMails = friendMails;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
