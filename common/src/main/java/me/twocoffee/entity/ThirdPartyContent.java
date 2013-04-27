/**
 * 
 */
package me.twocoffee.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import me.twocoffee.entity.ThirdPartyProfile.ThirdPartyType;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Index;
import com.google.code.morphia.annotations.Indexes;

/**
 * @author momo
 * 
 */
@Entity(value = "thirdPartyContent", noClassnameStored = true)
@Indexes(value = { @Index("thirdPartyType, uid, -favoriteTime") })
public class ThirdPartyContent {

	public static enum MediaType {
		Image, Text, Vedio
	}

	@Embedded
	public static class ThirdPartyAttachment {

		private MediaType mediaType;

		private String href;

		private String message;

		private String smallImage;

		private String middleImage;

		private String normalImage;

		public MediaType getMediaType() {
			return mediaType;
		}

		public void setMediaType(MediaType mediaType) {
			this.mediaType = mediaType;
		}

		public String getHref() {
			return href;
		}

		public void setHref(String href) {
			this.href = href;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getSmallImage() {
			return smallImage;
		}

		public void setSmallImage(String smallImage) {
			this.smallImage = smallImage;
		}

		public String getMiddleImage() {
			return middleImage;
		}

		public void setMiddleImage(String middleImage) {
			this.middleImage = middleImage;
		}

		public String getNormalImage() {
			return normalImage;
		}

		public void setNormalImage(String normalImage) {
			this.normalImage = normalImage;
		}

	}

	@Embedded
	public static class ThirdPartyContentUser {

		String uid;

		String name;

		String avatar;

		public String getAvatar() {
			return avatar;
		}

		public String getName() {
			return name;
		}

		public String getUid() {
			return uid;
		}

		public void setAvatar(String avatar) {
			this.avatar = avatar;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}
	}

	@Id
	private String id;

	private Date favoriteTime;

	private String content;

	private String message;

	private String title;

	private String realurl;

	private List<ThirdPartyAttachment> attachments;
	
	private List<String> tags = new ArrayList<String>(1);

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRealurl() {
		return realurl;
	}

	public void setRealurl(String realurl) {
		this.realurl = realurl;
	}

	public List<ThirdPartyAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<ThirdPartyAttachment> attachments) {
		this.attachments = attachments;
	}

	private ThirdPartyContentUser contentUser;

	private Date createDate;

	private ThirdPartyType thirdPartyType;

	private String uid;

	private ThirdPartyContent reply;

	public void addAccount(String account) {

	}

	public String getContent() {
		return content;
	}

	public ThirdPartyContentUser getContentUser() {
		return contentUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public Date getFavoriteTime() {
		return favoriteTime;
	}

	public String getId() {
		return id;
	}

	public ThirdPartyContent getReply() {
		return reply;
	}

	public ThirdPartyType getThirdPartyType() {
		return thirdPartyType;
	}

	public String getUid() {
		return uid;
	}

	public boolean hasImage() {
		return false;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setContentUser(ThirdPartyContentUser contentUser) {
		this.contentUser = contentUser;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setFavoriteTime(Date favoriteTime) {
		this.favoriteTime = favoriteTime;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setReply(ThirdPartyContent reply) {
		this.reply = reply;
	}

	public void setThirdPartyType(ThirdPartyType thirdPartyType) {
		this.thirdPartyType = thirdPartyType;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public void addAttachment(ThirdPartyAttachment attachment) {
		
		if (this.attachments == null) {
			this.attachments = new LinkedList<ThirdPartyContent.ThirdPartyAttachment>();
		}
		this.attachments.add(attachment);
	}
}
