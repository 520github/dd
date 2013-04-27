package me.twocoffee.entity;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

/**
 * 好友操作日志
 * @author drizzt
 *
 */
@Entity(value = "friendLog", noClassnameStored = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class FriendLog {
	@Id
	private String id;
	
	/**
	 * 用户ID
	 */
	private String accountId;
	
	/**
	 * 好友ID
	 */
	private String friendId;
	
	public static enum LogType {
		/** 添加申请 */
		Adding,
		/** 添加成功 */
		AddFinish,
		/** 添加被拒绝 */
		AddRefuse,
		/** 删除好友 */
		Removed
	}
	
	/**
	 * 操作类型
	 */
	private LogType logType;
	
	/**
	 * 操作时间
	 */
	private Date createTime;
	
	/**
	 * 完成操作时间
	 */
	private Date finishTime;
	
	/**
	 * 备注名
	 */
	private String remarkName;
	
	/**
	 * 附言
	 */
	private String postscript;

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

	public String getFriendId() {
		return friendId;
	}

	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}

	public LogType getLogType() {
		return logType;
	}

	public void setLogType(LogType logType) {
		this.logType = logType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public String getRemarkName() {
		return remarkName;
	}

	public void setRemarkName(String remarkName) {
		this.remarkName = remarkName;
	}

	public String getPostscript() {
		return postscript;
	}

	public void setPostscript(String postscript) {
		this.postscript = postscript;
	}
	
	
}
