package me.twocoffee.entity;

import java.util.List;

import com.google.code.morphia.annotations.Embedded;

/**
 * 视频播放文件m3u8信息
 * @author drizzt
 *
 */
@Embedded
public class M3u8 {
	/**
	 * m3u8的地址
	 */
	private String url;
	
	/**
	 * 版本
	 */
	private String version;
	
	/**
	 * m3u8中每端元素
	 * @author drizzt
	 *
	 */
	@Embedded
	public static class Element {
		/**
		 * 长度，毫秒
		 */
		private int duration;
		
		/**
		 * ts播放文件的url
		 */
		private String url;

		public int getDuration() {
			return duration;
		}

		public void setDuration(int duration) {
			this.duration = duration;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
	}
	
	/**
	 * 元素列表
	 */
	private List<Element> elements;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<Element> getElements() {
		return elements;
	}

	public void setElements(List<Element> elements) {
		this.elements = elements;
	}
}
