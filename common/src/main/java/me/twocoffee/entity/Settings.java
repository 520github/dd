/**
 * 
 */
package me.twocoffee.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.NotSaved;

/**
 * @author momo
 * 
 */
@Entity(value = "Settings", noClassnameStored = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Settings {

	@Embedded
	public static class FileUploadSettings {

		private Limitation limitation;

		public Limitation getLimitation() {
			return limitation;
		}

		public void setLimitation(Limitation limitation) {
			this.limitation = limitation;
		}

	}

	public static enum FriendGroup {
		Me, All, FavoritesAndMe, Custom
	}

	@Embedded
	public static class Limitation {

		private Long maxFileLength;

		private int dailyQuota;

		@NotSaved
		private int dailyUpload;

		@JsonIgnore
		private Map<String, Integer> dailyUploadNumber = new HashMap<String, Integer>();

		/**
		 * @return the dailyUploadNumber
		 */
		public Map<String, Integer> getDailyUploadNumber() {
			return dailyUploadNumber;
		}

		/**
		 * @param dailyUploadNumber
		 *            the dailyUploadNumber to set
		 */
		public void setDailyUploadNumber(Map<String, Integer> dailyUploadNumber) {
			this.dailyUploadNumber = dailyUploadNumber;
		}

		@NotSaved
		private Collection<String> whitelist;

		public int getDailyQuota() {
			return dailyQuota;
		}

		public int getDailyUpload() {
			return dailyUpload;
		}

		public Long getMaxFileLength() {
			return maxFileLength;
		}

		public Collection<String> getWhitelist() {
			return whitelist;
		}

		public void setDailyQuota(int dailyQuota) {
			this.dailyQuota = dailyQuota;
		}

		public void setDailyUpload(int dailyUpload) {
			this.dailyUpload = dailyUpload;
		}

		public void setMaxFileLength(Long maxFileLength) {
			this.maxFileLength = maxFileLength;
		}

		public void setWhitelist(Collection<String> whitelist) {
			this.whitelist = whitelist;
		}
	}

	@Embedded
	public static class Sharing {

		private String defaultGroup;

		public String getDefaultGroup() {
			return defaultGroup;
		}

		public void setDefaultGroup(String defaultGroup) {
			this.defaultGroup = defaultGroup;
		}

	}

	@Id
	private String id;

	private Sharing sharing;

	private FileUploadSettings fileUpload;

	@Indexed
	private String account;

	public String getAccount() {
		return account;
	}

	public FileUploadSettings getFileUpload() {
		return fileUpload;
	}

	public String getId() {
		return id;
	}

	public Sharing getSharing() {
		return sharing;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public void setFileUpload(FileUploadSettings fileUpload) {
		this.fileUpload = fileUpload;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setSharing(Sharing sharing) {
		this.sharing = sharing;
	}

}
