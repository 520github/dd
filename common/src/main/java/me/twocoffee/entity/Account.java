package me.twocoffee.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.twocoffee.common.constant.SystemConstant;
import me.twocoffee.common.util.Pinyin4jUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.utils.IndexDirection;

@Entity(value = "account", noClassnameStored = true)
public class Account {

    public static enum AccountType {
	/** 新浪微博 */
	Weibo,
	/** 邮箱 */
	TwoCoffeeEmail,
	/**
	 * 手机号
	 */
	TwoCoffeeMobile,
	/** 腾讯微博 */
	QQ,
	/** facebook */
	Facebook,
	/** twitter */
	Twitter,
	Renren,
	Gmail,
	Tencent
    }

    public static enum RoleType {
	/** 孔雀 */
	Seed,
	/** 新手 */
	NewBie,
	/** Shangmail */
	FromShangmail,
	/** 公共账号*/
	Public,
	/** 匿名用户(免注册)*/
	Guest,
    }

    public static enum Status {
	/** 邮箱已验证 */
	EmailVerified,
	/** 手机号已验证 */
	MobileVerified,
	/** 已经添加过多多助手 */
	KnowDuoduoAssistant,
	/** 已经完成了完善档案的任务，丌论是填写了还是跳过了 */
	ProfileResolved,
	KnowDuoduoPublic,
	ShowThirdpartyFriends
    }

    /**
     * 邮件地址
     */
    private String email;

    /**
     * 账户状态
     */
    private Set<Status> stat = new HashSet<Status>(0);

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 最后更新时间
     */
    private Date lastModified;

    /**
     * 个人描述
     */
    private String describe;

    /**
     * 登录名（邮箱或手机号）
     */
    @Indexed(value = IndexDirection.ASC, name = "loginNameIndex", unique = true)
    private String loginName;

    /**
     * 帐号类型
     */
    private AccountType accountType;

    @Id
    private String id;

    /**
     * 昵称
     */
    @Indexed(value = IndexDirection.ASC, name = "nameIndex", unique = true)
    private String name;

    /**
     * 密码的MD5
     */
    private String passwordHash;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 域名
     */
    // @Indexed(value=IndexDirection.ASC, name="domainIndex", unique=true)
    private String accountName;
    /**
     * 多多邮箱
     */
    @Indexed(value = IndexDirection.ASC, name = "duoduoEmailIndex", unique =
	    true,
	    sparse = true)
    private String duoduoEmail;
    /**
     * 职业
     */
    private String occupation;
    /**
     * 性别，male男，female女，unkown
     */
    private String gender;
    /**
     * 领域
     */
    private List<String> interest;
    /**
     * 角色
     */
    @Indexed
    private Set<RoleType> role = new HashSet<RoleType>(0);

    /**
     * 邀请者，上线
     */
    private String inviter;

    /**
     * 生日
     */
    private String birthday;

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

    /**
     * 下沉结果
     */
    private double score;

    /**
     * 头像
     */
    private Map<String, String> photos = new HashMap<String, String>(3);

    public static final String PHOTO_SIZE_SMALL = "35";

    public static final String PHOTO_SIZE_MIDDLE = "100";

    public static final String PHOTO_SIZE_BIG = "150";

    public static final String PHOTO_LARGE = "large";

    public static final String PHOTO_MEDIUM = "medium";

    public static final String PHOTO_SMALL = "small";

    private AccountMailConfig mailConfig;

    private String nameInPinyin;

    public String getNameInPinyin() {

	if (nameInPinyin == null) {
	    nameInPinyin = Pinyin4jUtils.getPinYin(name);
	}
	return nameInPinyin;
    }

    public void setNameInPinyin(String nameInPinyin) {
	this.nameInPinyin = nameInPinyin;
    }

    public void addRole(RoleType roleType) {

	if (role != null) {
	    role.add(roleType);
	}
    }

    public void addStat(Status status) {
	stat.add(status);
    }

    public String[] createAddressArray() {
	String[] address = new String[3];
	address[0] = country == null ? "" : country;
	address[1] = province == null ? "" : province;
	address[2] = city == null ? "" : city;
	return address;
    }

    public String getAccountName() {
	return accountName;
    }

    public AccountType getAccountType() {
	return accountType;
    }

    public String getAvatar() {
	return getAvatar(PHOTO_SIZE_MIDDLE);
    }

    public String getAvatar(String photoSize) {
	if (photos == null || photos.size() < 1) {
	    this.getPhotos();
	}
	return photos.get(photoSize);
    }

    public String getBirthday() {
	return birthday;
    }

    public String getCity() {
	return city;
    }

    public String getCountry() {
	return country;
    }

    public Date getCreateDate() {
	return createDate;
    }

    public String getDescribe() {
	return describe;
    }

    public String getDuoduoEmail() {
	return duoduoEmail;
    }

    public String getEmail() {
	return email;
    }

    public String getGender() {
	return gender;
    }

    public String getId() {
	return id;
    }

    public List<String> getInterest() {
	return interest;
    }

    public String getInviter() {
	return inviter;
    }

    public Date getLastModified() {
	return lastModified;
    }

    public String getLoginName() {
	return loginName;
    }

    public AccountMailConfig getMailConfig() {
	return mailConfig;
    }

    public String getMobile() {
	return mobile;
    }

    public String getName() {
	return name;
    }

    public String getOccupation() {
	return occupation;
    }

    public String getPasswordHash() {
	return passwordHash;
    }

    public Map<String, String> getPhotos() {

	if (photos == null) {
	    photos = new HashMap<String, String>(3);
	}
	String path = "http://" + SystemConstant.domainName + "/"
		+ LocaleContextHolder.getLocale().toString()
		+ "/images/";

	if (StringUtils.isBlank(photos.get(PHOTO_SIZE_SMALL))) {
	    photos.put(PHOTO_SIZE_SMALL, path + "avatar-35px.png");
	}

	if (StringUtils.isBlank(photos.get(PHOTO_SIZE_MIDDLE))) {
	    photos.put(PHOTO_SIZE_MIDDLE, path + "avatar-100px.png");
	}

	if (StringUtils.isBlank(photos.get(PHOTO_SIZE_BIG))) {
	    photos.put(PHOTO_SIZE_BIG, path + "avatar-150px.png");
	}
	return photos;
    }

    public Map<String, String> getAvatarForMap() {
	if (photos == null) {
	    photos = new HashMap<String, String>(3);
	}
	String path = "http://" + SystemConstant.domainName + "/"
		+ LocaleContextHolder.getLocale().toString()
		+ "/images/";

	if (StringUtils.isBlank(photos.get(PHOTO_SIZE_SMALL))) {
	    photos.put(PHOTO_SMALL, path + "avatar-35px.png");
	} else {
	    photos.put(PHOTO_SMALL, photos.get(PHOTO_SIZE_SMALL));
	    photos.remove(PHOTO_SIZE_SMALL);
	}

	if (StringUtils.isBlank(photos.get(PHOTO_SIZE_MIDDLE))) {
	    photos.put(PHOTO_MEDIUM, path + "avatar-100px.png");
	} else {
	    photos.put(PHOTO_MEDIUM, photos.get(PHOTO_SIZE_MIDDLE));
	    photos.remove(PHOTO_SIZE_MIDDLE);
	}

	if (StringUtils.isBlank(photos.get(PHOTO_SIZE_BIG))) {
	    photos.put(PHOTO_LARGE, path + "avatar-150px.png");
	} else {
	    photos.put(PHOTO_LARGE, photos.get(PHOTO_SIZE_BIG));
	    photos.remove(PHOTO_SIZE_BIG);
	}
	return photos;

    }

    public String getProvince() {
	return province;
    }

    public Set<RoleType> getRole() {
	return role;
    }

    public Set<Status> getStat() {
	return stat;
    }

    public boolean hasRole(RoleType roleType) {

	if (roleType != null) {
	    return this.role.contains(roleType);
	}
	return false;
    }

    public boolean hasRole(String role) {

	if (this.role != null) {

	    try {
		return this.role.contains(RoleType.valueOf(role));

	    } catch (IllegalArgumentException e) {
		// ignore
	    }
	}
	return false;
    }

    public boolean hasStatus(Status status) {

	if (stat != null && stat.contains(status)) {
	    return true;
	}
	return false;
    }

    // public void removeStat(Status status) {
    // stat.remove(status);
    // }

    public boolean hasStatus(String status) {

	if (this.stat != null) {

	    try {
		return this.stat.contains(Status.valueOf(status));

	    } catch (IllegalArgumentException e) {
		// ignore
	    }
	}
	return false;
    }

    public void removeRole(RoleType roleType) {

	if (role != null) {
	    role.remove(roleType);
	}
    }

    public Account setAccountName(String accountName) {
	this.accountName = accountName;
	return this;
    }

    public void setAccountType(AccountType accountType) {
	this.accountType = accountType;
    }

    public void setAddress(String[] address) {

	if (address != null && address.length > 0) {

	    for (int i = 0; i < address.length; i++) {
		switch (i) {
		case 0:
		    this.setCountry(address[i]);
		    break;
		case 1:
		    this.setProvince(address[i]);
		    break;
		case 2:
		    this.setCity(address[i]);
		    break;
		default:
		    break;
		}
	    }
	}
    }

    /**
     * 设置各个size的头像为指定的头像
     * 
     * @param avatar
     */
    public void setAvatar(String avatar) {

	if (StringUtils.isBlank(avatar)) {
	    return;
	}
	photos.put(PHOTO_SIZE_SMALL, avatar);
	photos.put(PHOTO_SIZE_MIDDLE, avatar);
	photos.put(PHOTO_SIZE_BIG, avatar);
    }

    public void setAvatar(String photoSize, String avatar) {
	photos.put(photoSize, StringUtils.isBlank(avatar) ? ""
		: avatar);

    }

    public void setBirthday(String birthday) {
	this.birthday = birthday;
    }

    public void setCity(String city) {
	this.city = city;
    }

    public void setCountry(String country) {
	this.country = country;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    public void setDescribe(String describe) {
	this.describe = describe;
    }

    public Account setDuoduoEmail(String duoduoEmail) {
	this.duoduoEmail = duoduoEmail;
	return this;
    }

    public Account setEmail(String email) {
	this.email = email;
	return this;
    }

    public Account setGender(String gender) {
	this.gender = gender;
	return this;
    }

    public void setId(String id) {
	this.id = id;
    }

    public void setInterest(List<String> interest) {
	this.interest = interest;
    }

    public void setInviter(String inviter) {
	this.inviter = inviter;
    }

    public void setLastModified(Date lastModified) {
	this.lastModified = lastModified;
    }

    public void setLoginName(String loginName) {
	this.loginName = loginName;
    }

    public void setMailConfig(AccountMailConfig mailConfig) {
	this.mailConfig = mailConfig;
    }

    public void setMobile(String mobile) {
	this.mobile = mobile;
    }

    public Account setName(String name) {
	this.name = name;
	return this;
    }

    public Account setOccupation(String occupation) {
	this.occupation = occupation;
	return this;
    }

    public Account setPasswordHash(String passwordHash) {
	this.passwordHash = passwordHash;
	return this;
    }

    public void setPhotos(Map<String, String> photos) {
	this.photos = photos;
    }

    public void setProvince(String province) {
	this.province = province;
    }

    public void setRole(Set<RoleType> role) {
	this.role = role;
    }

    public double getScore() {
	return score;
    }

    public void setScore(double score) {
	this.score = score;
    }

    public void setRoleByArray(RoleType[] roles) {

	if (roles != null) {

	    for (RoleType r : roles) {
		this.role.add(r);
	    }
	}
    }

    public void setStat(Set<Status> stat) {
	this.stat = stat;
    }

    public void setStatByArray(Status[] stats) {

	if (stats != null) {

	    for (Status r : stats) {
		this.stat.add(r);
	    }
	}
    }

    public void removeStatus(Status status) {

	if (this.stat != null) {
	    stat.remove(status);
	}
    }

}
