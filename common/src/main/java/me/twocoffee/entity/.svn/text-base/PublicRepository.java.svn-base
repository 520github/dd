package me.twocoffee.entity;

import java.util.Date;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

/**
 * 公共分享
 * @author drizzt
 *
 */
@Entity(value = "publicRepository", noClassnameStored = true)
public class PublicRepository {
	@Id
	private String id;
	/** 分享者 */
	@Indexed
	private String accountId;
	/** 被分享的内容 */
	private String contentId;
	/** 分享日期 */
	@Indexed
	private Date date;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
}
