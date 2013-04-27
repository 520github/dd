package me.twocoffee.controller.entity;

import me.twocoffee.entity.AccountMailConfig;

public class AccountVO {
	private String id;

	private String name;

	private int sharedNumber;

	private int friendNumber;

	private String occupation;

	private String largePhotoUrl;

	private String middlePhotoUrl;

	private String smallPhotoUrl;

	private String gender;

	private String duoduoEmail;

	private AccountMailConfig mailConfig;

	/**
	 * 个人介绍
	 */
	private String introduce;

	/**
	 * 国家
	 */
	private String country;

	/**
	 * 省
	 */
	private String province;

	/**
	 * 城市
	 */
	private String city;

	private String domain;

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public String getDomain() {
		return domain;
	}

	public String getDuoduoEmail() {
		return duoduoEmail;
	}

	public int getFriendNumber() {
		return friendNumber;
	}

	public String getGender() {
		return gender;
	}

	public String getId() {
		return id;
	}

	public String getIntroduce() {
		return introduce;
	}

	public String getLargePhotoUrl() {
		return largePhotoUrl;
	}

	public AccountMailConfig getMailConfig() {
		return mailConfig;
	}

	public String getMiddlePhotoUrl() {
		return middlePhotoUrl;
	}

	public String getName() {
		return name;
	}

	public String getOccupation() {
		return occupation;
	}

	public String getProvince() {
		return province;
	}

	public int getSharedNumber() {
		return sharedNumber;
	}

	public String getSmallPhotoUrl() {
		return smallPhotoUrl;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public void setDuoduoEmail(String duoduoEmail) {
		this.duoduoEmail = duoduoEmail;
	}

	public void setFriendNumber(int friendNumber) {
		this.friendNumber = friendNumber;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public void setLargePhotoUrl(String largePhotoUrl) {
		this.largePhotoUrl = largePhotoUrl;
	}

	public void setMailConfig(AccountMailConfig mailConfig) {
		this.mailConfig = mailConfig;
	}

	public void setMiddlePhotoUrl(String middlePhotoUrl) {
		this.middlePhotoUrl = middlePhotoUrl;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setSharedNumber(int sharedNumber) {
		this.sharedNumber = sharedNumber;
	}

	public void setSmallPhotoUrl(String smallPhotoUrl) {
		this.smallPhotoUrl = smallPhotoUrl;
	}
}
