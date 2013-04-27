package me.twocoffee.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;

import me.twocoffee.service.event.message.AcceptFriendRequestMessageActionEvent;
import me.twocoffee.service.event.message.BindActionEvent;
import me.twocoffee.service.event.message.IgnoreFriendRequestMessageActionEvent;
import me.twocoffee.service.event.message.SendFriendRequestMessageActionEvent;
import me.twocoffee.service.event.message.ShowThirdpartyFriendActionEvent;
import me.twocoffee.service.event.message.ViewAcknowledgmentActionEvent;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.context.ApplicationEvent;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

@Entity(value = "message", noClassnameStored = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Message {
	public static enum CatalogId {
		Friend, // 和朋友相关的消息，例如加好友申请、朋友加入的提醒
		System,// 系统消息，如欢迎消息等
		Acknowledgment, 
	    Chat;
	}

	public static class MessageAction {

		public static enum MessageActionValue {
			AcceptFriendRequest {// 接受好友请求
				@Override
				public ApplicationEvent getActionEvent(Object source,
						Message message) {

					return new AcceptFriendRequestMessageActionEvent(
							source, message);
				}

			},
			IgnoreFriendRequest {// 忽略好友请求
				@Override
				public ApplicationEvent getActionEvent(Object source,
						Message message) {
					return new IgnoreFriendRequestMessageActionEvent(source,
							message);
				}
			},
			ViewAcknowledgment {// 查看答谢
				@Override
				public ApplicationEvent getActionEvent(Object source,
						Message message) {
					return new ViewAcknowledgmentActionEvent(
							message);
				}
			},
			Bind {// 发送好友请求
				@Override
				public ApplicationEvent getActionEvent(Object source,
						Message message) {
					return new BindActionEvent(
							source, message);
				}
			},
			ShowThirdpartyFriend {// 发送好友请求
				@Override
				public ApplicationEvent getActionEvent(Object source,
						Message message) {
					return new ShowThirdpartyFriendActionEvent(source,
							message);
				}
			},
			SendFriendRequest {// 发送好友请求
				@Override
				public ApplicationEvent getActionEvent(Object source,
						Message message) {
					return new SendFriendRequestMessageActionEvent(source,
							message);
				}
			};
			/**
			 * 得到这个action对应的applicationEvent
			 * 
			 * @param source
			 * @param message
			 * @return
			 */
			public abstract ApplicationEvent getActionEvent(Object source,
					Message message);

		};

		private String href;

		private MessageActionValue action;

		private String name;

		public MessageAction() {
		}

		public MessageAction(String name, MessageActionValue action) {
			this.name = name;
			this.setAction(action);
		}

		public MessageActionValue getAction() {
			return action;
		}

		public String getHref() {
			return href;
		}

		public String getName() {
			return name;
		}

		public void setAction(MessageActionValue action) {
			this.action = action;
		}

		public void setHref(String href) {
			this.href = href;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	public static class MessageFrom {

		private String accountId;

		private Map<String, String> avatar;

		private String name;

		public String getAccountId() {
			return accountId;
		}

		public Map<String, String> getAvatar() {
			return avatar;
		}

		public String getName() {
			return name;
		}

		public void setAccountId(String accountId) {
			this.accountId = accountId;
		}

		public void setAvatar(Map<String, String> avatar) {
			this.avatar = avatar;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	public static enum MessageType {
		FriendRequest, // 收到的加好友申请(attribute有对方的账户信息)
		FriendSuggest, // 推荐加好友消息。例如好友加入多多（attribute有对方的账户信息）
		FriendRequestFeedback, // 用户收到同意反馈
		System, // 系统消息
		Acknowledgment,
		Binding,
		FriendInvitation,
		InviteeSuggest,
		ThirdpartyFriendSuggest
	}

	public static enum Status {
		deleted, // 删除
		normal, // 正常
	}
	
	@Indexed
	private String accountId;// 消息的所有者的账户id

	private List<MessageAction> action;// 消息支持的动作类型
	private Map<String, Object> attribute;// 账户信息，在存储时只存accountId
	private CatalogId catalogId;// 消息类型

	private Date date;// 消息创建的时间

	@Id
	private String id;

	private MessageType messageType;

	private Status status;// 状态

	private String subject;// 标题

	private MessageFrom from;

	public String getAccountId() {
		return accountId;
	}

	public List<MessageAction> getAction() {
		return action;
	}

	public Map<String, Object> getAttribute() {
		return attribute;
	}

	public CatalogId getCatalogId() {
		return catalogId;
	}

	public Date getDate() {
		return date;
	}

	public MessageFrom getFrom() {
		return from;
	}

	public String getId() {
		return id;
	}

	public MessageType getMessageType() {
		return messageType;
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

	public void setAction(List<MessageAction> action) {
		this.action = action;
	}

	public void setAttribute(Map<String, Object> attribute) {
		this.attribute = attribute;
	}

	public void setCatalogId(CatalogId catalogId) {
		this.catalogId = catalogId;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setFrom(MessageFrom from) {
		this.from = from;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
}
