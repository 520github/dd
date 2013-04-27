/**
 * 
 */
package me.twocoffee.entity;

import java.util.Date;

import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Index;
import com.google.code.morphia.annotations.Indexes;

/**
 * @author wenjian
 *
 */
@Entity(value = "thirdPartyContentSynchronizeLog", noClassnameStored = true)
@Indexes(value = { @Index("thirdPartyType, uid, accountId") })
public class ThirdPartyContentSynchronizeLog {

	@Id
	private String id;
	
	private ThirdPartyType thirdPartyType;
	
	private String accountId;
	
	private Date sysnDate;
	
	private String uid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ThirdPartyType getThirdPartyType() {
		return thirdPartyType;
	}

	public void setThirdPartyType(ThirdPartyType thirdPartyType) {
		this.thirdPartyType = thirdPartyType;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Date getSysnDate() {
		return sysnDate;
	}

	public void setSysnDate(Date sysnDate) {
		this.sysnDate = sysnDate;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
	
}
