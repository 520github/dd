package me.twocoffee.service.weixin;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class WeiXinPublicResponseEntity {
	@XmlElement(name = "ToUserName")
	private String toUserName;
	@XmlElement(name = "FromUserName")
	private String fromUserName;
	@XmlElement(name = "CreateTime")
	private long createTime;
	@XmlElement(name = "MsgType")
	private String messageType;
	@XmlElement(name = "Content")
	private String content;
	@XmlElement(name = "ArticleCount")
	private Integer articleCount;
	@XmlElement(name = "item")
	@XmlElementWrapper(name = "Articles")
	private List<WeiXinPublicArticle> articleList;
	@XmlElement(name = "FuncFlag")
	private int functionFlag;

	public List<WeiXinPublicArticle> getWeiXinPublicArticleList() {
		if(articleList == null)return null;
		return Collections.unmodifiableList(articleList);
	}

	public WeiXinPublicResponseEntity setWeiXinPublicArticleList(List<WeiXinPublicArticle> articleList) {
		this.articleList = articleList;
		updateWeiXinPublicArticleCount();
		return this;
	}

	private void updateWeiXinPublicArticleCount() {
		if (this.articleList != null) {
			this.articleCount = this.articleList.size();
		} else {
			this.articleCount = 0;
		}
	}

	public String getToUserName() {
		return toUserName;
	}

	public WeiXinPublicResponseEntity setToUserName(String toUserName) {
		this.toUserName = toUserName;
		return this;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public WeiXinPublicResponseEntity setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
		return this;
	}

	public long getCreateTime() {
		return createTime;
	}

	public WeiXinPublicResponseEntity setCreateTime(long createTime) {
		this.createTime = createTime;
		return this;
	}

	public String getMessageType() {
		return messageType;
	}

	public WeiXinPublicResponseEntity setMessageType(String messageType) {
		this.messageType = messageType;
		return this;
	}

	public String getContent() {
		return content;
	}

	public WeiXinPublicResponseEntity setContent(String content) {
		this.content = content;
		return this;
	}

	public Integer getFunctionFlag() {
		return functionFlag;
	}

	public WeiXinPublicResponseEntity setFunctionFlag(int functionFlag) {
		this.functionFlag = functionFlag;
		return this;
	}
}
