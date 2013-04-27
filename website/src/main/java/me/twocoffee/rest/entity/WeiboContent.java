/**
 * 
 */
package me.twocoffee.rest.entity;

/**
 * @author momo
 * 
 */
public class WeiboContent {
	
	private String uid;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	private String content;

	private String uuid;

	public String getContent() {
		return content;
	}

	public String getUuid() {
		return uuid;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}
