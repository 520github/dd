package me.twocoffee.entity;

import java.util.Date;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

@Entity(value = "inviteRecord", noClassnameStored = true)
public class InviteRecord {

	public static enum InviteesType {

		/** 邮箱 */
		Email,
		/**
		 * 手机号
		 */
		Mobile
	}

	@Id
	private String id;
	@Indexed
	private String invitor;
	@Indexed
	private String invitees;
	private InviteesType type;
	private Date inviteTime;
	private boolean result;
	@Indexed
	private String code;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setInvitor(String invitor) {
		this.invitor = invitor;
	}

	public String getInvitor() {
		return invitor;
	}

	public void setInvitees(String invitees) {
		this.invitees = invitees;
	}

	public String getInvitees() {
		return invitees;
	}

	public void setInviteTime(Date inviteTime) {
		this.inviteTime = inviteTime;
	}

	public Date getInviteTime() {
		return inviteTime;
	}

	public void setType(InviteesType type) {
		this.type = type;
	}

	public InviteesType getType() {
		return type;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public boolean isResult() {
		return result;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
