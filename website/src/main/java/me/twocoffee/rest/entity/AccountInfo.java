package me.twocoffee.rest.entity;

import java.util.List;

import me.twocoffee.entity.Account.AccountType;
import me.twocoffee.entity.Account.RoleType;
import me.twocoffee.entity.Account.Status;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
// TODO:SNS add InvitationInfo
public class AccountInfo {

	public static class InvitationInfo {

		private String code;

		private String link;

		public String getCode() {
			return code;
		}

		public String getLink() {
			return link;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public void setLink(String link) {
			this.link = link;
		}

	}

	// m3 没用
	private String accountName;

	private AccountType accountType;

	private AvatarInfo avatar;

	private String birthday;

	private String email;

	private FriendInfo friend;

	private String gender;

	private String id;

	// m3 没用
	private String[] interest;

	private String introduction;

	private String loginName;

	private String mobile;

	private String name;
	// 昵称的拼音
	private String nameInPinyin;
	// m3 没用
	private String occupation;

	private PasswordInfo password;

	private RoleType[] role;

	private Status[] stat;

	private CounterInfo counter;

	private String coffeeMail;

	private List<String> emailWhiteList;

	private boolean friendRequestPending;

	private InvitationInfo invitation;

	public String getAccountName() {
		return accountName;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public AvatarInfo getAvatar() {
		return avatar;
	}

	public String getBirthday() {
		return birthday;
	}

	public String getCoffeeMail() {
		return coffeeMail;
	}

	public CounterInfo getCounter() {
		return counter;
	}

	public String getEmail() {
		return email;
	}

	public List<String> getEmailWhiteList() {
		return emailWhiteList;
	}

	public FriendInfo getFriend() {
		return friend;
	}

	public String getGender() {
		return gender;
	}

	public String getId() {
		return id;
	}

	public String[] getInterest() {
		return interest;
	}

	public String getIntroduction() {
		return introduction;
	}

	public InvitationInfo getInvitation() {
		return invitation;
	}

	public String getLoginName() {
		return loginName;
	}

	public String getMobile() {
		return mobile;
	}

	public String getName() {
		return name;
	}

	public String getNameInPinyin() {
		return nameInPinyin;
	}

	public String getOccupation() {
		return occupation;
	}

	public PasswordInfo getPassword() {
		return password;
	}

	public RoleType[] getRole() {
		return role;
	}

	public Status[] getStat() {
		return stat;
	}

	public boolean isFriendRequestPending() {
		return friendRequestPending;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public void setAvatar(AvatarInfo avatar) {
		this.avatar = avatar;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public void setCoffeeMail(String coffeeMail) {
		this.coffeeMail = coffeeMail;
	}

	public void setCounter(CounterInfo counter) {
		this.counter = counter;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setEmailWhiteList(List<String> emailWhiteList) {
		this.emailWhiteList = emailWhiteList;
	}

	public void setFriend(FriendInfo friend) {
		this.friend = friend;
	}

	public void setFriendRequestPending(boolean friendRequestPending) {
		this.friendRequestPending = friendRequestPending;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setInterest(String[] interest) {
		this.interest = interest;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public void setInvitation(InvitationInfo invitation) {
		this.invitation = invitation;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNameInPinyin(String nameInPinyin) {
		this.nameInPinyin = nameInPinyin;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public void setPassword(PasswordInfo password) {
		this.password = password;
	}

	public void setRole(RoleType[] role) {
		this.role = role;
	}

	public void setStat(Status[] stat) {
		this.stat = stat;
	}

}
