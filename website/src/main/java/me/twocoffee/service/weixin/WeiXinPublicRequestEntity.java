package me.twocoffee.service.weixin;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class WeiXinPublicRequestEntity {
	public static enum MessageType {
		text,
		image,
		link,
		news,
		event,
	}
	
	public static enum EventKey {
		subscribe,
		unsubscribe,
		CLICK,
	}
	
	@XmlElement(name = "ToUserName")
	private String toUserName;
	@XmlElement(name = "FromUserName")
	private String fromUserName;
	@XmlElement(name = "CreateTime")
	private long createTime;
	@XmlElement(name = "MsgType")
	private String messageType;
	@XmlElement(name = "Location_X")
	private String locationX;
	@XmlElement(name = "Location_Y")
	private String locationY;
	@XmlElement(name = "Scale")
	private String scale;
	@XmlElement(name = "Label")
	private String label;
	@XmlElement(name = "MsgId")
	private String messageId;
	@XmlElement(name = "Description")
	private String descriptioni;
	@XmlElement(name = "Url")
	private String url;
	@XmlElement(name = "Title")
	private String title;
	@XmlElement(name = "PicUrl")
	private String imageUrl;
	@XmlElement(name = "Content")
	private String content;
	@XmlElement(name = "Event")
	private String event;
	@XmlElement(name = "EventKey")
	private String eventKey;
	
	public String getToUserName() {
		return toUserName;
	}

	public WeiXinPublicRequestEntity setToUserName(String toUserName) {
		this.toUserName = toUserName;
		return this;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public WeiXinPublicRequestEntity setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
		return this;
	}

	public long getCreateTime() {
		return createTime;
	}

	public WeiXinPublicRequestEntity setCreateTime(long createTime) {
		this.createTime = createTime;
		return this;
	}

	public String getMessageType() {
		return messageType;
	}

	public WeiXinPublicRequestEntity setMessageType(String messageType) {
		this.messageType = messageType;
		return this;
	}

	public String getLocationX() {
		return locationX;
	}

	public WeiXinPublicRequestEntity setLocationX(String locationX) {
		this.locationX = locationX;
		return this;
	}

	public String getLocationY() {
		return locationY;
	}

	public WeiXinPublicRequestEntity setLocationY(String locationY) {
		this.locationY = locationY;
		return this;
	}

	public String getScale() {
		return scale;
	}

	public WeiXinPublicRequestEntity setScale(String scale) {
		this.scale = scale;
		return this;
	}

	public String getLabel() {
		return label;
	}

	public WeiXinPublicRequestEntity setLabel(String label) {
		this.label = label;
		return this;
	}

	public String getMessageId() {
		return messageId;
	}

	public WeiXinPublicRequestEntity setMessageId(String messageId) {
		this.messageId = messageId;
		return this;
	}

	public String getDescriptioni() {
		return descriptioni;
	}

	public WeiXinPublicRequestEntity setDescriptioni(String descriptioni) {
		this.descriptioni = descriptioni;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public WeiXinPublicRequestEntity setUrl(String url) {
		this.url = url;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public WeiXinPublicRequestEntity setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public WeiXinPublicRequestEntity setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
		return this;
	}

	public String getContent() {
		return content;
	}

	public WeiXinPublicRequestEntity setContent(String content) {
		this.content = content;
		return this;
	}

	public String getEvent() {
		return event;
	}

	public WeiXinPublicRequestEntity setEvent(String event) {
		this.event = event;
		return this;
	}

	public String getEventKey() {
		return eventKey;
	}

	public WeiXinPublicRequestEntity setEventKey(String eventKey) {
		this.eventKey = eventKey;
		return this;
	}
	
}
