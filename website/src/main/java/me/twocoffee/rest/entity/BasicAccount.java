/**
 * 
 */
package me.twocoffee.rest.entity;

import java.util.Map;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author momo
 * 
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
// TODO:SNS new
public class BasicAccount {

	private String id;

	private String name;

	private Map<String, String> avatar;

	private String introduction;

	public Map<String, String> getAvatar() {
		return avatar;
	}

	public String getId() {
		return id;
	}

	public String getIntroduction() {
		return introduction;
	}

	public String getName() {
		return name;
	}

	public void setAvatar(Map<String, String> avatarInfo) {
		this.avatar = avatarInfo;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public void setName(String name) {
		this.name = name;
	}

}
