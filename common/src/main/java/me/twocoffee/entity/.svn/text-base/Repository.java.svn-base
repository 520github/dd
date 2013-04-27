package me.twocoffee.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

/**
 * 用户的收藏关系
 * 
 * @author leon
 * 
 */
@Entity(value = "repository", noClassnameStored = true)
public class Repository {
    /**
     * 朋友分享信息
     * 
     * @author drizzt
     * 
     */
    @Embedded
    public static class FriendShare {
	/**
	 * 朋友ID
	 */
	private String friendId;

	/**
	 * 分享时间
	 */
	private Date shareTime;

	/**
	 * 评分
	 */
	private int score;

	/**
	 * 评论
	 */
	private String comment;

	/**
	 * 答谢状态 true or false
	 */
	private boolean acknowledgment;

	public boolean isAcknowledgment() {
	    return acknowledgment;
	}

	public void setAcknowledgment(boolean acknowledgment) {
	    this.acknowledgment = acknowledgment;
	}

	public String getComment() {
	    return comment;
	}

	public String getFriendId() {
	    return friendId;
	}

	public int getScore() {
	    return score;
	}

	public Date getShareTime() {
	    return shareTime;
	}

	public void setComment(String comment) {
	    this.comment = comment;
	}

	public void setFriendId(String friendId) {
	    this.friendId = friendId;
	}

	public void setScore(int score) {
	    this.score = score;
	}

	public void setShareTime(Date shareTime) {
	    this.shareTime = shareTime;
	}
    }

    /** 收藏者 */
    private String accountId;
    /** 被收藏的内容 */
    private String contentId;

    /** 收藏日期 */
    private Date date;

    /** 最近更改时间, 包括修改Tag */
    private Date lastModified;
    @Id
    private String id;

    /** 系统 tag 列表 */
    private List<String> tag;

    /** 用户 tag 列表 */
    private List<String> userTag;
    /**
     * 朋友分享来自，收藏为null
     */
    private List<FriendShare> fromFriends;
    /**
     * 分享链接
     */
    private String shareLink;

    public String getShareLink() {
	return shareLink;
    }

    public void setShareLink(String shareLink) {
	this.shareLink = shareLink;
    }

    public String getAccountId() {
	return accountId;
    }

    public String getContentId() {
	return contentId;
    }

    public Date getDate() {
	return date;
    }

    public List<FriendShare> getFromFriends() {
	return fromFriends;
    }

    public String getId() {
	return id;
    }

    public Date getLastModified() {
	return lastModified;
    }

    public List<String> getTag() {
	return tag;
    }

    public List<String> getUserTag() {
	return userTag;
    }

    public Repository setAccountId(String accountId) {
	this.accountId = accountId;
	return this;
    }

    public Repository setContentId(String contentId) {
	this.contentId = contentId;
	return this;
    }

    public Repository setDate(Date date) {
	this.date = date;
	return this;
    }

    public void setFromFriends(List<FriendShare> fromFriends) {
	this.fromFriends = fromFriends;
    }

    public void setId(String id) {
	this.id = id;
    }

    public void setLastModified(Date lastModified) {
	this.lastModified = lastModified;
    }

    public Repository setTag(List<String> tag) {
	this.tag = tag;
	return this;
    }

    public void setTag(List<String> tag, String contentType) {
	if (tag == null) {
	    tag = new ArrayList<String>();
	}
	// 内容类型默认为网页
	if (contentType == null || contentType.trim().length() < 1)
	    contentType = "Web";
	boolean isExistsContentType = false;

	for (int i = 0; i < tag.size(); i++) {
	    String tagValue = tag.get(i);
	    if (tagValue == null)
		continue;
	    if (tagValue.indexOf(contentType) > -1) {
		isExistsContentType = true;
		continue;
	    }
	}
	if (!isExistsContentType) {// 增加内容类型标签
	    if (!contentType.startsWith("Type"))
		contentType = "Type_" + contentType;
	    tag.add(contentType);
	}

	this.tag = tag;
    }

    public void setUserTag(List<String> userTag) {
	this.userTag = userTag;
    }

}
