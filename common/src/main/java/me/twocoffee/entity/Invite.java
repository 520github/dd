package me.twocoffee.entity;

import java.util.Date;
import java.util.Set;

import me.twocoffee.entity.Account.RoleType;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

@Entity(value = "invite", noClassnameStored = true)
public class Invite {
	@Id
	private String id;
	private String producer;
	@Indexed
	private String ownerId;
	private Set<RoleType> roles;
	private Date createTime;
	private Date expiredTime;

	public Date getCreateTime() {
		return createTime;
	}

	public Date getExpiredTime() {
		return expiredTime;
	}

	public String getId() {
		return id;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public String getProducer() {
		return producer;
	}

	public Set<RoleType> getRoles() {
		return roles;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setExpiredTime(Date expiredTime) {
		this.expiredTime = expiredTime;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public void setProducer(String producer) {
		this.producer = producer;
	}

	public void setRoles(Set<RoleType> roles) {
		this.roles = roles;
	}

}