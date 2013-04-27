package me.twocoffee.entity;

import java.util.Date;

import me.twocoffee.entity.WebToken.UserType;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

@Entity(value = "device", noClassnameStored = true)
public class Device {
	@Indexed
	private String accountId;// 账户id
	private String clientVersion;// 客户端的版本号
	private Date date;// 创建时间
	private String deviceToken;// 推送的 deviceToken,如果这个客户端是 IOS
	@Id
	private String id;// 唯一性标识
	@Indexed
	private String mac;// 客户端网卡的 mac 地址
	private String os;// 操作系统名称
	private String osVersion;// 操作系统版本号”
	private boolean pushAlert;// 设置客户端是否接收 alert 通知
	private String pushURI;// Windows Phone 使用的 Push Notification URI
	private String userAgent;
	private UserType userType;
	private Date lastModified;
	
	
	public String getAccountId() {
		return accountId;
	}

	public String getClientVersion() {
		return clientVersion;
	}

	public Date getDate() {
		return date;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public String getId() {
		return id;
	}

	public String getMac() {
		return mac;
	}

	public String getOs() {
		return os;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public String getPushURI() {
		return pushURI;
	}

	public boolean isPushAlert() {
		return pushAlert;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public void setClientVersion(String clientVersion) {
		this.clientVersion = clientVersion;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public void setPushAlert(boolean pushAlert) {
		this.pushAlert = pushAlert;
	}
	
    public boolean getPushAlert(){
    	return pushAlert;
    }
	public void setPushURI(String pushURI) {
		this.pushURI = pushURI;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

}
