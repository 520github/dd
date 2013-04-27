package me.twocoffee.entity;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.google.code.morphia.annotations.Embedded;

/**
 * 网页的正文，或者网页剪辑的内容
 * 
 * @author leon
 * 
 */
@Embedded
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class HtmlPayload {

    public static class Attachment {
	private String archiveUrl;// 保存在本地服务器的url
	private String orgUrl;// 原url
	private long size;
	private String contentType;

	public String getArchiveUrl() {
	    return archiveUrl;
	}

	public String getOrgUrl() {
	    return orgUrl;
	}

	public void setArchiveUrl(String archiveUrl) {
	    this.archiveUrl = archiveUrl;
	}

	public void setOrgUrl(String orgUrl) {
	    this.orgUrl = orgUrl;
	}

	public void setSize(long size) {
	    this.size = size;
	}

	public long getSize() {
	    return size;
	}

	public void setContentType(String contentType) {
	    this.contentType = contentType;
	}

	public String getContentType() {
	    return contentType;
	}
    }

    private List<Attachment> attachment;// 附件

    /** 内容 */
    private String content;

    public List<Attachment> getAttachment() {
	return attachment;
    }

    public String getContent() {
	return content;
    }

    public void setAttachment(List<Attachment> attachment) {
	this.attachment = attachment;
    }

    public HtmlPayload setContent(String content) {
	this.content = content;
	return this;
    }

    public HtmlPayload setAttachment(List<String> images, String firstImage) {
	if (images == null || images.size() < 1) {
	    return this;
	}
	List<Attachment> list = new ArrayList<Attachment>();
	for (int i = 0; i < images.size(); i++) {// 存储图片原url
	    String orgUrl = images.get(i);
	    if (orgUrl == null || orgUrl.trim().length() < 1)
		continue;
	    Attachment attach = new Attachment();
	    attach.setOrgUrl(orgUrl);
	    attach.setArchiveUrl(orgUrl);
	    list.add(attach);
	}
	// if (list.size() > 0) {// 存储第一张图片的本地服务器的url
	// Attachment attach = list.get(0);
	// if (firstImage == null || firstImage.trim().length() < 1) {
	// firstImage = attach.getOrgUrl();
	// }
	// attach.setArchiveUrl(firstImage);
	// }
	this.attachment = list;
	return this;
    }

}
