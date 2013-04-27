package me.twocoffee.entity;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

/**
 * 内容动作日志
 * @author drizzt
 *
 */
@Entity(value = "contentAction", noClassnameStored = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ContentAction {
	@Id
	private String id;
	
	/**
	 * 内容ID
	 */
	@Indexed
	private String contentId;
	
	/**
	 * 用户id
	 */
	@Indexed
	private String accountId;
	
	/**
	 * 目标用户ID
	 */
	private String toId;
	
	public static enum ActionType {
		/**
		 * 读
		 */
		Read,
		/**
		 * 分享
		 */
		Share,
		/**
		 * 评论
		 */
		Comment,
		/**
		 * 收藏
		 */
		Collect,
		/**
		 * 朋友分享
		 */
		Friend
	}
	
	/**
	 * 动作
	 */
	@Indexed
	private ActionType action;
	
	/**
	 * 参数
	 */
	private String params;
	
	/**
	 * 评分
	 */
	private int rating;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 动作时间
	 */
	private Date actionTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public ActionType getAction() {
		return action;
	}

	public void setAction(ActionType action) {
		this.action = action;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getActionTime() {
		return actionTime;
	}

	public void setActionTime(Date actionTime) {
		this.actionTime = actionTime;
	}

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}
}
