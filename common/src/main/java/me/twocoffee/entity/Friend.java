package me.twocoffee.entity;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

/**
 * 朋友关系表
 * @author drizzt
 *
 */
@Entity(value = "friend", noClassnameStored = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Friend {
	@Id
	private String id;
	
	/**
	 * 帐号ID，此帐号的好友
	 */
	@Indexed
	private String accountId;
	
	/**
	 * 朋友ID
	 */
	@Indexed
	private String friendId;
	
	/**
	 * 创建时间
	 */
	@Indexed
	private Date created;
	
	/**
	 * 热度
	 */
	@Indexed
	private int hot;
	
	/**
	 * 备注名
	 */
	private String remarkName;
	
	/**
	 * 备注名，拼音
	 */
	private String remarkNameInPinyin;
	
	
	/**
	 * 最后推荐时间
	 */
	private Date lastModify;
	
	/**
	 * 分享次数
	 */
	private int shareCount;
	
	/**
	 * 重力下沉系数
	 */
	private double score;
	
	/**
	 * 好友类型
	 * @author drizzt
	 *
	 */
	public static enum FriendType {
		/**
		 * 常用好友
		 */
		Favorite,
		
		/**
		 * 一般好友
		 */
		Normal,
	}
	
	public static enum ContactType{
		/**
		 * 最近联系人
		 */
	   Latest_Frequently,
	   
	   /**
	    * 常用联系人
	    */
	   Frequently
	}
	
	public static enum Direction{
		/**
		 * 正序排列
		 */
		Asc,
		/**
		 * 倒序排列
		 */
		Desc
	}
	@Indexed
	private FriendType friendType;

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

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public int getHot() {
		return hot;
	}

	public void setHot(int hot) {
		this.hot = hot;
	}

	public String getRemarkName() {
		return remarkName;
	}

	public void setRemarkName(String remarkName) {
		this.remarkName = remarkName;
	}

	public FriendType getFriendType() {
		return friendType;
	}

	public void setFriendType(FriendType friendType) {
		this.friendType = friendType;
	}

	public String getRemarkNameInPinyin() {
		return remarkNameInPinyin;
	}

	public void setRemarkNameInPinyin(String remarkNameInPinyin) {
		this.remarkNameInPinyin = remarkNameInPinyin;
	}

	public Date getLastModify() {
		return lastModify;
	}

	public void setLastModify(Date lastModify) {
		this.lastModify = lastModify;
	}

	public int getShareCount() {
		return shareCount;
	}

	public void setShareCount(int shareCount) {
		this.shareCount = shareCount;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
	
}
