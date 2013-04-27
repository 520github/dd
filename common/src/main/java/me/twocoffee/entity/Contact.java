/**
 * 
 */
package me.twocoffee.entity;

import java.util.Date;
import java.util.Map;

import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Index;
import com.google.code.morphia.annotations.Indexes;
import com.google.code.morphia.annotations.Transient;

/**
 * @author momo
 * 
 */
@Entity(value = "contact", noClassnameStored = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(value = { "id", "friend", "friendId", "accountId",
	"matchedAccount", "createDate" })
@Indexes(value = { @Index("accountId, thirdPartyType, uid"),
	@Index("accountId, thirdPartyType, relation") })
public class Contact {

    public static enum RelationType {
	DuoduoFriend,
	DuoduoAccount,
	NotDuoduoAccount
    }

    @Id
    private String id;

    private String uid;

    private String name;

    private String avatar;

    private RelationType relation;

    private String[] mobile;

    private String accountId;

    private String friendId;

    @Transient
    private Account matchedAccount;

    private ThirdPartyType thirdPartyType;

    private Date createDate;

    private Map<String, Object> profile;

    public Map<String, Object> getProfile() {
	return profile;
    }

    public void setProfile(Map<String, Object> profile) {
	this.profile = profile;
    }

    public String getAccountId() {
	return accountId;
    }

    public String getAvatar() {
	return avatar;
    }

    public Date getCreateDate() {
	return createDate;
    }

    public String getFriendId() {
	return friendId;
    }

    public String getId() {
	return id;
    }

    public Account getMatchedAccount() {
	return matchedAccount;
    }

    public String[] getMobile() {
	return mobile;
    }

    public String getName() {
	return name;
    }

    public RelationType getRelation() {
	return relation;
    }

    public ThirdPartyType getThirdPartyType() {
	return thirdPartyType;
    }

    public String getUid() {
	return uid;
    }

    public void setAccountId(String accountId) {
	this.accountId = accountId;
    }

    public void setAvatar(String avatar) {
	this.avatar = avatar;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    public void setFriendId(String friendId) {
	this.friendId = friendId;
    }

    public void setId(String id) {
	this.id = id;
    }

    public void setMatchedAccount(Account matchedAccount) {
	this.matchedAccount = matchedAccount;
    }

    public void setMobile(String[] mobile) {
	this.mobile = mobile;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setRelation(RelationType relation) {
	this.relation = relation;
    }

    public void setThirdPartyType(ThirdPartyType thirdPartyType) {
	this.thirdPartyType = thirdPartyType;
    }

    public void setUid(String uid) {
	this.uid = uid;
    }
}
