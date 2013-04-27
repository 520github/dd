package me.twocoffee.entity;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.google.code.morphia.annotations.Embedded;

/**
 * 视频信息
 * @author drizzt
 *
 */
@Embedded
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class VideoPayload {
	/**
	 * 视频类型
	 * @author drizzt
	 *
	 */
	public static enum VideoType {
		M3u8,
		Mp4
	}
	
	/**
	 * 预览图片
	 */
	private String imageUrl;
	
	/**
	 * web端播放器地址
	 */
	private String webPlayerUrl;
	
	/**
	 * phone端视频类型
	 */
	private VideoType videoType;
	
	/**
	 * phone端播放器url
	 */
	private String phonePlayerUrl;
	
	/**
	 * 如phone端为m3u8，则此实体包含m3u8信息
	 */
	private M3u8 m3u8Content;
	
	/**
	 * web端播放器详细信息
	 */
	private String webPlayerContent;

	public String getWebPlayerUrl() {
		return webPlayerUrl;
	}

	public void setWebPlayerUrl(String webPlayerUrl) {
		this.webPlayerUrl = webPlayerUrl;
	}

	public VideoType getVideoType() {
		return videoType;
	}

	public void setVideoType(VideoType videoType) {
		this.videoType = videoType;
	}

	public String getPhonePlayerUrl() {
		return phonePlayerUrl;
	}

	public void setPhonePlayerUrl(String phonePlayerUrl) {
		this.phonePlayerUrl = phonePlayerUrl;
	}

	public M3u8 getM3u8Content() {
		return m3u8Content;
	}

	public void setM3u8Content(M3u8 m3u8Content) {
		this.m3u8Content = m3u8Content;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getWebPlayerContent() {
		return webPlayerContent;
	}

	public void setWebPlayerContent(String webPlayerContent) {
		this.webPlayerContent = webPlayerContent;
	}
}
