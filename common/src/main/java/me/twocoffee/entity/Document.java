/**
 * 
 */
package me.twocoffee.entity;

import java.util.Date;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Index;
import com.google.code.morphia.annotations.Indexes;

/**
 * @author momo
 * 
 */
@Entity(value = "document", noClassnameStored = true)
@Indexes(value = { @Index("accountId, createDate") })
public class Document {

	@Id
	private String id;

	// 存储位置信息
	private String fileId;

	private String mimeType;

	private String name;

	private String accountId;

	private Date createDate;

	private Long length;

	private String url;

	public String getAccountId() {
		return accountId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public String getFileId() {
		return fileId;
	}

	public String getId() {
		return id;
	}

	public Long getLength() {
		return length;
	}

	public String getMimeType() {
		return mimeType;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLength(Long length) {
		this.length = length;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
