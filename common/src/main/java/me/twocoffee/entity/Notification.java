package me.twocoffee.entity;

import java.util.Date;

import me.twocoffee.entity.Badge.BadgeName;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity(value = "notification", noClassnameStored = true)
public class Notification {
	public static enum CollapseKey {
		friend { // 朋友相关信息  客户端进入朋友列表
			@Override
			public String getAction() {
				return "iicoffee://view/relation/friend/";
			}

			@Override
			public BadgeName getBadgeName() {
				return Badge.BadgeName.FriendMessage;
			}

			@Override
			public boolean pushAlert() {
				return true;
			}
		},
		message {// 消息(系统消息)
			@Override
			public String getAction() {
				return "iicoffee://view/message/";
			}

			@Override
			public BadgeName getBadgeName() {
				return Badge.BadgeName.message;
			}

			@Override
			public boolean pushAlert() {
				return false;
			}
		},
		readLater {// 稍后读
			@Override
			public String getAction() {
				return "iicoffee://view/tag/readLater/";
			}

			@Override
			public BadgeName getBadgeName() {
				return Badge.BadgeName.readLater;
			}

			@Override
			public boolean pushAlert() {
				return false;
			}
		},
		RecommendByFriend {// 朋友推荐内容
			@Override
			public String getAction() {
			//	return "iicoffee://view/tag/recommendedByFriend/";
			  return "iicoffee://view/content/";
			
			}

			@Override
			public BadgeName getBadgeName() {
				return Badge.BadgeName.RecommendByFriend;
			}

			@Override
			public boolean pushAlert() {
				return true;
			}
		},
		
		Chat {//悄悄话
			@Override
			public String getAction() {
				return "iicoffee://view/chat/";
			}

			@Override
			public BadgeName getBadgeName() {
				return Badge.BadgeName.Chat;
			}

			@Override
			public boolean pushAlert() {
				return true;
			}
		},		
		
		FriendMessage {//添加或邀请朋友消息
			@Override
			public String getAction() {
				return "iicoffee://view/relation/friend/message/";
			}

			@Override
			public BadgeName getBadgeName() {
				return Badge.BadgeName.FriendMessage;
			}

			@Override
			public boolean pushAlert() {
				return true;
			}
			
		},
		
		Acknowledgment {//答谢消息
			@Override
			public String getAction() {
				return "iicoffee://view/acknowledgment/";
			}

			@Override
			public BadgeName getBadgeName() {
				return Badge.BadgeName.Acknowledgment;
			}

			@Override
			public boolean pushAlert() {
				return true;
			}
			
		};
		
		public abstract String getAction();

		public abstract BadgeName getBadgeName();

		public abstract boolean pushAlert();
	}

	public static enum Status {
		deleted, // 已删除
		normal;// 常态
	}

	private String accountId;// 通知的所有者的账户id
	private String action;// 用户点击通知时发生的动作
	private Date date;// 通知创建的时间
	@Id
	private String id;
	private CollapseKey key;// 区别通知类型，用于覆盖旧通知

	private Status status;// 通知的状态

	private String subject;// 通知的标题

	public String getAccountId() {
		return accountId;
	}

	public String getAction() {
		return action;
	}

	public Date getDate() {
		return date;
	}

	public String getId() {
		return id;
	}

	public CollapseKey getKey() {
		return key;
	}

	public Status getStatus() {
		return status;
	}

	public String getSubject() {
		return subject;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setKey(CollapseKey key) {
		this.key = key;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
}
