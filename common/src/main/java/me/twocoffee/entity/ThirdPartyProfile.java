/**
 * 
 */
package me.twocoffee.entity;

import java.util.Date;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Index;
import com.google.code.morphia.annotations.Indexes;

/**
 * 第三方认证用户个人信息
 * 
 * @author momo
 * 
 */
@Indexes(value = { @Index("userId, accountType"),
	@Index("accountId, accountType"), @Index("accountType, expiredDate") })
@Entity(value = "thirdpartyprofile", noClassnameStored = true)
public class ThirdPartyProfile {

    public static enum ErrorType {
	Authexpired,
	Unbind,
	AccessError
    }

    public static enum ThirdPartyType {

	/** 新浪微博 */
	Weibo,
	Phone,
	/** 腾讯微博 */
	QQ,
	/** facebook */
	Facebook,
	/** twitter */
	Twitter,
	Renren,
	Gmail,
	Public,
	Tencent,
	Unknown;
	public static ThirdPartyType forName(String name) {

	    for (ThirdPartyType t : ThirdPartyType.values()) {

		if (t.name().equalsIgnoreCase(name)) {
		    return t;
		}
	    }
	    return Unknown;
	}
    }

    /**
     * 帐号id
     */
    private String accountId;

    private Date createDate;

    /**
     * 第三方认证token 过期时间
     */
    private Date expiredDate;

    @Id
    private String id;

    /**
     * 用户在第三方的个人信息
     */
    private Map<String, String> profile;

    /**
     * 第三方 refresh token 用于token过期后获得新token
     */
    private String refreshToken;

    /**
     * 第三方认证token
     */
    private String token;

    private String tokenSecret;

    private String avatar;

    private String nickName;

    /**
     * 第三方用户标识
     */
    private String userId;

    private ThirdPartyType accountType;

    // 是否绑定
    private boolean bind = true;

    // 是否同步收藏内容
    private boolean syncContent = true;

    private ErrorType contentErrorType;

    private ErrorType contactErrorType;

    private String uuid;

    private Date syncDate = new Date(1000);

    private boolean login;

    public String getAccountId() {
	return accountId;
    }

    public ThirdPartyType getAccountType() {
	return accountType;
    }

    public String getAvatar() {
	return avatar;
    }

    public ErrorType getContactErrorType() {
	return contactErrorType;
    }

    public ErrorType getContentErrorType() {
	return contentErrorType;
    }

    public Date getCreateDate() {
	return createDate;
    }

    public Date getExpiredDate() {
	return expiredDate;
    }

    public String getId() {
	return id;
    }

    public String getNickName() {
	return nickName;
    }

    public Map<String, String> getProfile() {
	return profile;
    }

    public String getRefreshToken() {
	return refreshToken;
    }

    public Date getSyncDate() {
	return syncDate;
    }

    public String getToken() {
	return token;
    }

    public String getTokenSecret() {
	return tokenSecret;
    }

    public String getUserId() {
	return userId;
    }

    public String getUuid() {
	return uuid;
    }

    public boolean isBind() {
	return bind;
    }

    public boolean isLogin() {
	return login;
    }

    public boolean isSyncContent() {
	return syncContent;
    }

    public void setAccountId(String accountId) {
	this.accountId = accountId;
    }

    public void setAccountType(ThirdPartyType accountType) {
	this.accountType = accountType;
    }

    public void setAvatar(String avatar) {
	this.avatar = avatar;
    }

    public void setBind(boolean bind) {
	this.bind = bind;
    }

    public void setContactErrorType(ErrorType contactErrorType) {
	this.contactErrorType = contactErrorType;
    }

    public void setContentErrorType(ErrorType contentErrorType) {
	this.contentErrorType = contentErrorType;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    public void setExpiredDate(Date expiredDate) {
	this.expiredDate = expiredDate;
    }

    public void setId(String id) {
	this.id = id;
    }

    public void setLogin(boolean login) {
	this.login = login;
    }

    public void setNickName(String nickName) {
	this.nickName = nickName;
    }

    public void setProfile(Map<String, String> profile) {
	this.profile = profile;
    }

    public void setRefreshToken(String refreshToken) {
	this.refreshToken = refreshToken;
    }

    public void setSyncContent(boolean syncContent) {
	this.syncContent = syncContent;
    }

    public void setSyncDate(Date syncDate) {
	this.syncDate = syncDate;
    }

    public void setToken(String token) {
	this.token = token;
    }

    public void setTokenSecret(String tokenSecret) {
	this.tokenSecret = tokenSecret;
    }

    public void setUserId(String userId) {
	this.userId = userId;
    }

    public void setUuid(String uuid) {
	this.uuid = uuid;
    }

}
