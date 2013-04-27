package me.twocoffee.entity;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.google.code.morphia.annotations.Embedded;

/**
 * 图片内容
 * 
 * @author leon
 * 
 */
@Embedded
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ImagePayload {
	private String id;
	private String name;
	private String mimeType;
	/** 图片的网址 */
	private String url;
	/** 图片文件的大小。单位是byte */
	private int length;
	/** 图片的宽度， 单位是像素 */
	private int width;
	/** 图片的高度， 单位是像素 */
	private int height;

	public ImagePayload setUrl(String url) {
		this.url = url;
		return this;
	}

	public ImagePayload setWidth(int width) {
		this.width = width;
		return this;
	}

	public ImagePayload setHeight(int height) {
		this.height = height;
		return this;
	}

	public String getUrl() {
		return this.url;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public ImagePayload setLength(int length) {
		this.length = length;
		return this;
	}

	public int getLength() {
		return length;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	
}