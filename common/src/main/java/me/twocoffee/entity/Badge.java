package me.twocoffee.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

/**
 * 统计badge数字的对象
 * 
 * @author chongf
 * 
 */
@Entity(value = "badge", noClassnameStored = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Badge {
	public static class BadgeModel {
		private List<BadgeModel> children;// 子类
		private boolean leaf = true;// 叶子节点(没有子类)
		private final Badge.BadgeName name;// badge的代表名称
		private int number;// 数量
		private BadgeModel parent;// 父类

		public BadgeModel(Badge.BadgeName name) {
			this.name = name;
		}

		public BadgeModel addChild(BadgeModel child) {
			if (children == null) {
				this.children = new ArrayList<BadgeModel>();
			}
			child.setParent(this);
			this.leaf = false;
			this.children.add(child);
			return this;
		}

		public List<BadgeModel> getChildren() {
			return children;
		}

		public Badge.BadgeName getName() {
			return name;
		}

		public int getNumber() {
			return number;
		}

		public BadgeModel getParent() {
			return parent;
		}

		public boolean isLeaf() {
			return leaf;
		}

		public BadgeModel setChildren(List<BadgeModel> children) {
			this.children = children;
			return this;
		}

		public void setLeaf(boolean leaf) {
			this.leaf = leaf;
		}

		public BadgeModel setNumber(int number) {
			this.number = number;
			return this;
		}

		public BadgeModel setParent(BadgeModel parent) {
			this.parent = parent;
			return this;
		}
	}

	public static enum BadgeName {
		folder, // 所有folder相关badge的总数
		friend, // 所有朋友相关badge的总数
		FriendMessage, // 加好友请求badge数
		message, // 所有未读消息的总数
		readLater, // 稍后读folder的badge数
		RecommendByFriend, // 朋友推荐folder的badge数
		systemMessage, // 所有Badge的总数
		note, // 纸条
		total, // 系统消息
		Chat, // 悄悄话
		ChatSession, // 评论会话
		MyCoffee,	// 我的多多
		Acknowledgment//答谢
	}

	public class Folder {
		private int friend;

		public int getFriend() {
			return friend;
		}

		public void setFriend(int friend) {
			this.friend = friend;
		}

	}

	public class Message {
		private int friendMessage;

		public int getFriendMessage() {
			return friendMessage;
		}

		public void setFriendMessage(int friendMessage) {
			this.friendMessage = friendMessage;
		}

	}

	public final static Map<String, BadgeModel> badgeModelMap;// 用来初始化所有badge，并建立关系

	static {

		/**
		 * 新加纸条节点，
		 */

		badgeModelMap = new HashMap<String, BadgeModel>();
		//BadgeModel note = new BadgeModel(BadgeName.note);
		BadgeModel folder = new BadgeModel(BadgeName.folder);
		BadgeModel friend = new BadgeModel(BadgeName.friend);
		BadgeModel friendRequest = new BadgeModel(BadgeName.FriendMessage);
		BadgeModel message = new BadgeModel(BadgeName.message);
		BadgeModel systemMessage = new BadgeModel(BadgeName.systemMessage);
		BadgeModel readLater = new BadgeModel(BadgeName.readLater);
		BadgeModel recommendedByFriend = new BadgeModel(
				BadgeName.RecommendByFriend);
		BadgeModel total = new BadgeModel(BadgeName.total);
		BadgeModel chat = new BadgeModel(BadgeName.Chat);

		BadgeModel acknowledgment = new BadgeModel(BadgeName.Acknowledgment);
		folder.addChild(readLater).addChild(recommendedByFriend);
		friend.addChild(friendRequest);
		// note.addChild(chat).addChild(message);
		// total.addChild(chat).addChild(folder).addChild(friend).addChild(message);
		total.addChild(chat).addChild(recommendedByFriend).addChild(friend).addChild(acknowledgment).addChild(message);
		badgeModelMap.put(chat.getName().name(), chat);
		badgeModelMap.put(folder.getName().name(), folder);
		badgeModelMap.put(friend.getName().name(), friend);
		badgeModelMap.put(friendRequest.getName().name(), friendRequest);
		badgeModelMap.put(message.getName().name(), message);
		badgeModelMap.put(readLater.getName().name(), readLater);
		badgeModelMap.put(recommendedByFriend.getName().name(),
				recommendedByFriend);
		badgeModelMap.put(total.getName().name(), total);
		badgeModelMap.put(systemMessage.getName().name(), systemMessage);
		badgeModelMap.put(acknowledgment.getName().name(),acknowledgment);
	}

	public static void findParentName(BadgeModel bm, List<String> names) {
		if (bm == null) {
			return;
		}
		names.add(bm.getName().name());
		if (bm.getParent() != null) {
			findParentName(bm.getParent(), names);
		}
	}

	public static void main(String[] args) {
		BadgeModel bm = badgeModelMap.get(BadgeName.RecommendByFriend.name());
		List<String> list = new ArrayList<String>();
		findParentName(bm, list);
		for (String name : list) {
			System.out.println(name);
		}
	}

	@Indexed
	private String accountId;// 账户id
	private int folder;// 所有folder相关badge的总数
	private int friend;// 所有朋友相关badge的总数
	private int FriendMessage;// 加好友请求badge数
	@Id
	private String id;
	private int message;// 所有未读消息的总数
	private int readLater;// 稍后读folder的badge数
	private int RecommendByFriend;// 朋友推荐folder的badge数
	private int Chat;
	private int note;
	private int systemMessage;// 系统消息
	private int total;// 所有Badge的总数
	private Message msg;
    private Folder fld;
    private int Acknowledgment;
    private Map<String,Object> flag;
     
	public String getAccountId() {
		return accountId;
	}

	public int getChat() {
		return Chat;
	}

	public Folder getFld() {
		fld = new Folder();
		fld.setFriend(getRecommendByFriend());
		return fld;
	}

	public int getfolder() {
		return folder;
	}

	public int getFolder() {
		return folder;
	}

	public int getFriend() {
		return friend;
	}

	public int getFriendMessage() {
		return FriendMessage;
	}

	public String getId() {
		return id;
	}

	public int getMessage() {
		return message;
	}

	public Message getMsg() {
		msg = new Message();
		msg.setFriendMessage(getFriendMessage());
		return msg;
	}

	public int getNote() {
		return note;
	}

	public int getReadLater() {
		return readLater;
	}

	public int getRecommendByFriend() {
		return RecommendByFriend;
	}

	public int getSystemMessage() {
		return systemMessage;
	}

	public int getTotal() {
		return total;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public void setChat(int Chat) {
		this.Chat = Chat;
	}

	public void setFld(Folder fld) {
		this.fld = fld;
	}

	public void setfolder(int folder) {
		this.folder = folder;
	}

	public void setFolder(int folder) {
		this.folder = folder;
	}

	public void setFriend(int friend) {
		this.friend = friend;
	}

	public void setFriendMessage(int FriendMessage) {
		this.FriendMessage = FriendMessage;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setMessage(int message) {
		this.message = message;
	}

	public void setMsg(Message msg) {
		this.msg = msg;
	}

	public void setNote(int note) {
		this.note = note;
	}

	public void setReadLater(int readLater) {
		this.readLater = readLater;
	}

	public void setRecommendByFriend(int RecommendByFriend) {
		this.RecommendByFriend = RecommendByFriend;
	}

	public void setSystemMessage(int systemMessage) {
		this.systemMessage = systemMessage;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	public int getAcknowledgment() {
		return Acknowledgment;
	}

	public void setAcknowledgment(int acknowledgment) {
		Acknowledgment = acknowledgment;
	}

	public Map<String, Object> getFlag() {
		return flag;
	}

	public void setFlag(Map<String, Object> flag) {
		this.flag = flag;
	}
	
}