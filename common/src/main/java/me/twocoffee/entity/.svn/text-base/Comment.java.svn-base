package me.twocoffee.entity;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Index;
import com.google.code.morphia.annotations.Indexes;

@Entity(value = "comment", noClassnameStored = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Comment {

	/**
	 * 评论者的id
	 */
	private String accountId;
	/**
	 * 被评论内容的id
	 */
	private String contentId;

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

	/**
	 * 评论id
	 */
	@Id
	private String id;
	/**
	 * 评分
	 */
	int score;
	/**
	 * 所作的评论
	 */
	String text;
	/**
	 * 最后一次评论的日期
	 */
	Date lastComment;

	/**
	 * 最后一次评分的日期
	 */
	Date lastScore;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	public Date getLastComment() {
		return lastComment;
	}
	public void setLastComment(Date lastComment) {
		this.lastComment = lastComment;
	}

	public Date getLastScore() {
		return lastScore;
	}

	public void setLastScore(Date lastScore) {
		this.lastScore = lastScore;
	}

}
